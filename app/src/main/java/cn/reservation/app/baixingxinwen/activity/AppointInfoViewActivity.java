package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.PatientItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cz.msebera.android.httpclient.Header;

public class AppointInfoViewActivity extends ConfirmDialogActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    public int mYear;
    public int mMonth;
    public int mDay;
    public int mWeekday;
    public String mTime;
    public DoctorItem mDoctorItem;
    public List<FamilyMemberItem> mPatientItems = new ArrayList<FamilyMemberItem>();
    public PatientItemListAdapter mPatientItemListAdapter;
    private RecyclerView mHlvPatients;
    private TextView mBtnOK;
    public ScrollView scrollView;

    public String mReserveDay;
    public FamilyMemberItem mPatient;
    public AppointItem appointItem;

    public Drawable mImgPlaceholder;

    public String mPatientID;

    public ImageView mBtnPatientLeft;
    public ImageView mBtnPatientRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_info_view);

        mYear = CommonUtils.appointYear;
        mMonth = CommonUtils.appointMonth;
        mDay = CommonUtils.appointDay;
        mWeekday = CommonUtils.appointWeekday;
        mTime = CommonUtils.appointTime;
        mDoctorItem = CommonUtils.appointDoctorItem;

        mContext = AppointInfoViewActivity.this;
        res = mContext.getResources();

        Intent intent = getIntent();
        mPatientID = intent.getStringExtra("PatientID");

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.appoint_info));

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHlvPatients = (RecyclerView) findViewById(R.id.hlv_patient);
        mHlvPatients.setLayoutManager(layoutManager2);

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

        ImageView imgDoctorPhoto = (ImageView) findViewById(R.id.img_doctor_photo);
        mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_doctor);
        Picasso
                .with(mContext)
                .load(mDoctorItem.getmPhoto())
                .placeholder(mImgPlaceholder)
                .transform(CommonUtils.getTransformation(mContext))
                .into(imgDoctorPhoto);

        TextView txtDoctorName = (TextView) findViewById(R.id.txt_doctor_name);
        txtDoctorName.setText(mDoctorItem.getmName());

        TextView txtHospitalTitle = (TextView) findViewById(R.id.txt_hospital_title);
        txtHospitalTitle.setText(mDoctorItem.getmHospital());

        TextView txtRoomTitle = (TextView) findViewById(R.id.txt_room_title);
        txtRoomTitle.setText(mDoctorItem.getmRoom());

        TextView txtDoctorFee = (TextView) findViewById(R.id.txt_doctor_fee);
        txtDoctorFee.setText(mDoctorItem.getmFee());

        TextView txtDateTime = (TextView) findViewById(R.id.txt_date_time);
        txtDateTime.setText(CommonUtils.getFormattedDateTime(mContext, mYear, mMonth+1, mDay, mTime));

        mPatientItems.add(new FamilyMemberItem("1", mContext.getString(R.string.self), 0, 0, "", "", false));
        mPatientItemListAdapter = new PatientItemListAdapter(mPatientItems, mContext, AppointInfoViewActivity.this);
        mHlvPatients.setAdapter(mPatientItemListAdapter);
        loadPatients();

        mBtnOK = (TextView) findViewById(R.id.btn_ok);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAppointment();
            }
        });
    }

    public void loadPatients() {
        mPatientItemListAdapter.setClear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointInfoViewActivity.this);
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
                                    boolean select = false;
                                    if (mPatientID != null && mPatientID.equals(item.getString("id"))) {
                                        select = true;
                                    } else if (i == 0) {
                                        select = true;
                                    }
                                    mPatientItemListAdapter.addItem(new FamilyMemberItem(item.getString("id"), name,
                                            item.getInt("man_type")-1, item.getInt("sex"), item.getString("relation"), item.getString("identify"), select));
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
                        Toast.makeText(mContext, responseString, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }

    private void saveAppointment() {
        mPatient = mPatientItemListAdapter.getCheckedItem();
        if (mPatient == null) {
            Toast.makeText(mContext, res.getString(R.string.please_choose_patient), Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointInfoViewActivity.this);
        mReserveDay = Integer.toString(mYear)+"-"+Integer.toString(mMonth+1)+"-"+Integer.toString(mDay);
        RequestParams params = new RequestParams();
        params.put("userid", CommonUtils.userInfo.getUserID());
        params.put("token", CommonUtils.userInfo.getToken());
        params.put("hospital", mDoctorItem.getmHospital());
        params.put("work", mDoctorItem.getmRoom());
        params.put("whospno", mDoctorItem.getmHospitalID());
        params.put("hosno", mDoctorItem.getmRoomID());
        params.put("courseno", mDoctorItem.getmDoctorID());
        params.put("patientno", mPatient.getmMemberID());
        params.put("reserveday", mReserveDay);
        params.put("reservetime", getTime(mTime));
        params.put("fee", mDoctorItem.getmFee());

        String url = "setbook";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressDialog.dismiss();
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject book = response.getJSONObject("bookInfo");
                        appointItem = new AppointItem((long)book.getInt("ReserveNo"), book.getString("ReserveDay"), book.getString("ReserveTime"),
                                book.getString("ReserveMin"),
                                new DoctorItem((long)book.getInt("CourseNo"), (long)book.getInt("WHospNo"), (long)book.getInt("HosNo"), "Picture",
                                        "", book.getString("Hospital"), book.getString("Work"), "",
                                        book.getString("Fee"), "", 5, true),
                                book.getString("PatientNo"), book.getString("PatientName"), false);
                        CommonUtils.showConfirmDialog(mContext, AppointInfoViewActivity.this, 0, res.getString(R.string.success_appoint));
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

    public void setCheckPatient(int position) {
        mPatientItemListAdapter.setChecked(position);
        mPatientItemListAdapter.notifyDataSetChanged();
    }

    public void addPatient() {
        Intent intent = new Intent(AppointInfoViewActivity.this, InputFamilyInfoActivity.class);
        this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_FAMILY);
        this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public void callBack(){
        Intent intent = new Intent(AppointInfoViewActivity.this, AppointHistoryViewActivity.class);
        intent.putExtra("Appoint", appointItem);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(intent);
        this.finish();
        this.overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadPatients();
    }

    private String getTime(String t) {
        int h = Integer.parseInt(t.substring(0, 2));
        int m = Integer.parseInt(t.substring(3));
        return Integer.toString(h) + "." + Integer.toString(m);
    }

}
