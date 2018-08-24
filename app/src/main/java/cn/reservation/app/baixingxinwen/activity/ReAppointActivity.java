package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.AppointHistoryItemListAdapter;
import cn.reservation.app.baixingxinwen.adapter.PatientItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cz.msebera.android.httpclient.Header;

public class ReAppointActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    public Resources res;
    private Context mContext;

    public List<FamilyMemberItem> mPatientItems = new ArrayList<FamilyMemberItem>();
    public PatientItemListAdapter mPatientItemListAdapter;
    private RecyclerView mHlvPatients;
    private ListView lstAppointHistory;
    AppointHistoryItemListAdapter appointHistoryItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public ProgressHUD mProgressDialog;

    public ImageView mBtnPatientLeft;
    public ImageView mBtnPatientRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_appoint);

        mContext = ReAppointActivity.this;
        res = mContext.getResources();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.reappoint));

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvPatients = (RecyclerView) findViewById(R.id.hlv_patient);
        mHlvPatients.setLayoutManager(layoutManager2);
        mPatientItems.add(new FamilyMemberItem("1", mContext.getString(R.string.self), 0, 0, "", "", false));
        mPatientItemListAdapter = new PatientItemListAdapter(mPatientItems, mContext, ReAppointActivity.this);
        mHlvPatients.setAdapter(mPatientItemListAdapter);

        mBtnPatientLeft = (ImageView) findViewById(R.id.btn_left_patient);
        mBtnPatientLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();
                layoutManager2.scrollToPositionWithOffset(firstVisibleItem + 1, 0);
            }
        });
        mBtnPatientRight = (ImageView) findViewById(R.id.btn_right_patient);
        mBtnPatientRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstVisibleItem = layoutManager2.findFirstVisibleItemPosition();
                if (firstVisibleItem > 0) {
                    layoutManager2.scrollToPositionWithOffset(firstVisibleItem - 1, 0);
                }
            }
        });

        lstAppointHistory = (ListView) findViewById(R.id.lst_appoint_history);
        appointHistoryItemListAdapter = new AppointHistoryItemListAdapter(mContext, ReAppointActivity.this);
        lstAppointHistory.setAdapter(appointHistoryItemListAdapter);
        lstAppointHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        lstAppointHistory.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getAppointItems();
                        isLoadMore = false;
                    }
                }
            }
        });
        loadFirstPatients();
    }

    public void loadPatients() {
        mPatientItemListAdapter.setClear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ReAppointActivity.this);
                RequestParams params = new RequestParams();
                params.put("userid", CommonUtils.userInfo.getUserID());
                params.put("token", CommonUtils.userInfo.getToken());

                String url = "get_family";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray list = response.getJSONArray("family");
                                for(int i=0; i < list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String name = item.getString("name");
                                    if (item.getString("relation").equals("me")) {
                                        name = res.getString(R.string.self);
                                    }
                                    mPatientItemListAdapter.addItem(new FamilyMemberItem(item.getString("id"), name,
                                            item.getInt("man_type")-1, item.getInt("sex"), item.getString("relation"), item.getString("identify"), false));
                                }
                                mPatientItems.add(new FamilyMemberItem("0", mContext.getString(R.string.add), 3, 0, "", "", false));
                                mPatientItemListAdapter.notifyDataSetChanged();
                                CommonUtils.dismissProgress(progressDialog);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 300);
    }

    public void loadFirstPatients() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ReAppointActivity.this);
                mPatientItemListAdapter.setClear();
                RequestParams params = new RequestParams();
                params.put("userid", CommonUtils.userInfo.getUserID());
                params.put("token", CommonUtils.userInfo.getToken());

                String url = "get_family";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 0) {
                                mProgressDialog.dismiss();
                                Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray list = response.getJSONArray("family");
                                for(int i=0; i < list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String name = item.getString("name");
                                    if (item.getString("relation").equals("me")) {
                                        name = res.getString(R.string.self);
                                    }
                                    boolean isSelected = false;
                                    mPatientItemListAdapter.addItem(new FamilyMemberItem(item.getString("id"), name,
                                            item.getInt("man_type")-1, item.getInt("sex"), item.getString("relation"), item.getString("identify"), isSelected));
                                }
                                mPatientItems.add(new FamilyMemberItem("0", mContext.getString(R.string.add), 3, 0, "", "", false));
                                mPatientItemListAdapter.notifyDataSetChanged();
                                getAppointItems();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }

    public void setCheckPatient(int position) {
        mPatientItemListAdapter.setChecked(position);
        mPatientItemListAdapter.notifyDataSetChanged();
    }

    public void addPatient() {
        Intent intent = new Intent(ReAppointActivity.this, InputFamilyInfoActivity.class);
        this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_FAMILY);
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    private void getAppointItems() {
        RequestParams params = new RequestParams();
        params.put("userid", CommonUtils.userInfo.getUserID());
        params.put("token", CommonUtils.userInfo.getToken());
        params.put("page", mIntPage);
        params.put("lang", CommonUtils.mIntLang);

        String url = "bookhistory";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                    } else if (response.getInt("code") == 2) {
                        mProgressDialog.dismiss();
                    } else {
                        isLoadMore = response.getBoolean("hasmore");
                        JSONArray listRooms = response.getJSONArray("list");
                        for(int i=0; i<listRooms.length(); i++) {
                            JSONObject book = listRooms.getJSONObject(i);
                            AppointItem appointItem = new AppointItem((long)book.getInt("ReserveNo"), book.getString("ReserveDay"), book.getString("ReserveTime"),
                                    book.getString("ReserveMin"),
                                    new DoctorItem((long)book.getInt("CourseNo"), (long)book.getInt("WHospNo"), (long)book.getInt("HosNo"), book.getString("Picture"),
                                            book.getString("CourseName"), book.getString("HospName"), book.getString("HosName"), book.getString("Title"),
                                            book.getString("Price"), book.getString("HospTel"), 5, true),
                                    book.getString("PatientNo"), book.getString("PatientName"), book.getInt("Cancel")==1);
                            appointHistoryItemListAdapter.addItem(appointItem);
                        }
                        appointHistoryItemListAdapter.notifyDataSetChanged();
                        mIntPage++;
                        CommonUtils.dismissProgress(mProgressDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAppoint(AppointItem appointItem) {
        FamilyMemberItem patientItem = mPatientItemListAdapter.getCheckedItem();
        if (patientItem == null) {
            Toast.makeText(mContext, res.getString(R.string.please_choose_patient_to_reappoint), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(ReAppointActivity.this, ChooseAppointDoctorActivity.class);
        DoctorItem doctorItem = appointItem.getmDoctor();
        intent.putExtra("Doctor", doctorItem);
        intent.putExtra("PatientID", patientItem.getmMemberID());
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.REQUEST_CODE_FAMILY && resultCode == CommonUtils.RESULT_CODE_FAMILY) {
            loadPatients();
        }
    }
}
