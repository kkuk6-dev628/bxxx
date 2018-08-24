package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppointItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

public class AppointAlarmViewActivity extends ConfirmDialogActivity implements DialogInterface.OnCancelListener {

    public Resources res;
    private Context mContext;
    public AnimatedActivity pActivity;

    private AppointItem appointItem;

    private TextView mTxtPatient;
    private TextView mTxtHostpital;
    private TextView mTxtJob;
    private TextView mTxtDateTime;
    private TextView mTxtFee;
    private TextView mTxtCanceled;
    TextView btnCancelAppoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_alarm_view);

        Intent intent = getIntent();
        appointItem = (AppointItem) intent.getSerializableExtra("Appoint");

        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) AppointAlarmViewActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.appoint_history));

        mTxtPatient = (TextView) findViewById(R.id.txt_patient);
        mTxtHostpital = (TextView) findViewById(R.id.txt_hospital_title);
        mTxtJob = (TextView) findViewById(R.id.txt_job);
        mTxtDateTime = (TextView) findViewById(R.id.txt_date_time);
        mTxtFee = (TextView) findViewById(R.id.txt_fee);
        mTxtCanceled = (TextView) findViewById(R.id.txt_alert_canceled);
        btnCancelAppoint = (TextView) findViewById(R.id.btn_cancel_appoint);

        mTxtPatient.setText(appointItem.getmPatient());
        mTxtHostpital.setText(appointItem.getmDoctor().getmHospital());
        mTxtJob.setText(appointItem.getmDoctor().getmRoom());
        mTxtDateTime.setText(CommonUtils.getFormattedDateTime(mContext, appointItem.getmDate(),
                appointItem.getmHour(), appointItem.getmMinute()));
        mTxtFee.setText(appointItem.getmDoctor().getmFee() + res.getString(R.string.fee_unit));

        if (appointItem.isCanceled()) {
            //mTxtCanceled.setVisibility(View.VISIBLE);
        } else {
            btnCancelAppoint.setVisibility(View.VISIBLE);
        }

        btnCancelAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtils.showConfirmDialog(mContext, AppointAlarmViewActivity.this, 1, res.getString(R.string.confirm_cancel_appoint));
            }
        });
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
    public void callBack() {
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AppointAlarmViewActivity.this);
        RequestParams params = new RequestParams();
        params.put("userid", CommonUtils.userInfo.getUserID());
        params.put("token", CommonUtils.userInfo.getToken());
        params.put("whospno", appointItem.getmDoctor().getmHospitalID());
        params.put("hosno", appointItem.getmDoctor().getmRoomID());
        params.put("courseno", appointItem.getmDoctor().getmDoctorID());
        params.put("reserveno", appointItem.getmID());
        params.put("reserveday", appointItem.getmDate());
        params.put("patientno", appointItem.getmPatientID());

        String url = "cancelbook";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    progressDialog.dismiss();
                    if (response.getInt("code") == 0) {
                        Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                    } else {
                        appointItem.setCanceled(true);
                        mTxtCanceled.setVisibility(View.VISIBLE);
                        btnCancelAppoint.setVisibility(View.GONE);
                        Intent intent = new Intent();
                        intent.putExtra("canceled", appointItem.isCanceled());
                        setResult(CommonUtils.RESULT_CODE_APPOINT, intent);
                        pActivity.finishChildActivity();
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

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
