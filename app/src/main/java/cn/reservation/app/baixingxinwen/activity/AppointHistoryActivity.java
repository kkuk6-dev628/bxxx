package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.AppointItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cz.msebera.android.httpclient.Header;

public class AppointHistoryActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    public Resources res;
    private Context mContext;
    public AnimatedActivity pActivity;

    private ListView lstAlarm;
    AppointItemListAdapter appointItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public AppointItem mAppointItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_history);

        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) AppointHistoryActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.appoint_history));

        lstAlarm = (ListView) findViewById(R.id.lst_appoint_history);
        appointItemListAdapter = new AppointItemListAdapter(mContext);
        lstAlarm.setAdapter(appointItemListAdapter);
        lstAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAppointItem = (AppointItem) appointItemListAdapter.getItem(i);
                gotoAppointAlarmView();
            }
        });
        lstAlarm.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getAlarmItems();
                        isLoadMore = false;
                    }
                }
            }
        });
        getAlarmItems();
    }

    private void getAlarmItems() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointHistoryActivity.this);
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
                                progressDialog.dismiss();
                                Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                            } else if (response.getInt("code") == 2) {
                                progressDialog.dismiss();
                            } else {
                                isLoadMore = response.getBoolean("hasmore");
                                JSONArray listRooms = response.getJSONArray("list");
                                for(int i=0; i<listRooms.length(); i++) {
                                    JSONObject book = listRooms.getJSONObject(i);
                                    AppointItem appointItem = new AppointItem((long)book.getInt("ReserveNo"), book.getString("ReserveDay"), book.getString("ReserveTime"),
                                            book.getString("ReserveMin"),
                                            new DoctorItem((long)book.getInt("CourseNo"), (long)book.getInt("WHospNo"), (long)book.getInt("HosNo"), "Picture",
                                                    book.getString("CourseName"), book.getString("HospName"), book.getString("HosName"), "",
                                                    book.getString("Price"), book.getString("HospTel"), 5, true),
                                            book.getString("PatientNo"), book.getString("PatientName"), book.getInt("Cancel")==1);
                                    appointItemListAdapter.addItem(appointItem);
                                }
                                appointItemListAdapter.notifyDataSetChanged();
                                mIntPage++;
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
        }, 500);
    }

    private void gotoAppointAlarmView() {
        Intent intent = new Intent(AppointHistoryActivity.this, AppointAlarmViewActivity.class);
        intent.putExtra("Appoint", mAppointItem);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pActivity.startChildActivity("appoint_alarm_view", intent);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            pActivity.finishChildActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                //super.onBackPressed();
                pActivity.finishChildActivity();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonUtils.RESULT_CODE_APPOINT) {
            if (data.getBooleanExtra("canceled", false)) {
                mAppointItem.setCanceled(true);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
