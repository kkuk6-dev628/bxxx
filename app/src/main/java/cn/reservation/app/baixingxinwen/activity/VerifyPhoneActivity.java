package cn.reservation.app.baixingxinwen.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

public class VerifyPhoneActivity extends AppCompatActivity  implements DialogInterface.OnCancelListener, View.OnClickListener{

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    private TextView mBtnNext;
    private EditText mEditPhone;
    private EditText mEditVerifyCode;
    private TextView mBtnConfirmCode;
    private TextView mTxtLeftTime;
    private int mIntLeftTime = 180;
    private String mStrLeftTime;
    private String strPhone;
    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    private String mStrVerifyCode;
    private boolean isVerified = false;
    private ProgressHUD mProgressDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mContext = VerifyPhoneActivity.this;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) VerifyPhoneActivity.this.getParent();
        CommonUtils.customActionBar(mContext, this, true, "手机绑定");
        mStrVerifyCode = "";
        mEditPhone = (EditText) findViewById(R.id.edit_phone_number);
        mEditPhone.setText(strPhone);

        mEditVerifyCode = (EditText) findViewById(R.id.edit_verify_code);
        mEditVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!mStrVerifyCode.equals("") && mEditVerifyCode.getText().toString().equals(mStrVerifyCode)) {
                    isVerified = true;
                    stopTimerTask();
                    CommonUtils.userInfo.setUserPhone(strPhone);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mTxtLeftTime = (TextView) findViewById(R.id.txt_left_time);
        mBtnConfirmCode = (TextView) findViewById(R.id.btn_confirm_code);
        mBtnConfirmCode.setOnClickListener(this);
        mTxtLeftTime.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_confirm_code) {
            String strVerifyCode = mEditVerifyCode.getText().toString();
            if (strVerifyCode.equals("")) {
                Toast.makeText(mContext, res.getString(R.string.please_input_verify_code), Toast.LENGTH_LONG).show();
                return;
            } else if (!strVerifyCode.equals(mStrVerifyCode)) {
                Toast.makeText(mContext, res.getString(R.string.verify_code_incorrect), Toast.LENGTH_LONG).show();
                return;
            }
            if (mStrVerifyCode.equals(strVerifyCode)) {
                isVerified = true;
                mBtnConfirmCode.setText(mContext.getString(R.string.success));
                CommonUtils.userInfo.setUserJoinMobile(strPhone);
                stopTimerTask();
                signPhone(strPhone);
            }
        }
        else if (id == R.id.txt_left_time){
            strPhone = mEditPhone.getText().toString();
            if(strPhone.equals("")){
                Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG).show();
                return;
            }
            getVerifyCode(strPhone);
        }
    }

    private void getVerifyCode(String strPhone) {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        RequestParams params = new RequestParams();
        params.put("type","link");
        params.put("phone", strPhone);
        System.out.println("phone:"+strPhone);
        String url = "sms/send";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //progressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        mStrVerifyCode = response.optJSONObject("ret").optString("retkey");
                        isVerified = false;
                        startTimer();
                    } else {
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void signPhone(final String strPhone) {
        //mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        RequestParams params = new RequestParams();
        params.put("act", "bind");
        params.put("uid", CommonUtils.userInfo.getUid());
        params.put("property_type", "mobile");
        params.put("bind_type", "bind");
        params.put("username", strPhone);
        params.put("password", "null");
        String url = APIManager.Ucenter_URL;
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //mProgressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    if (code.equals("1")) {
                        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        CommonUtils.userInfo.setUserJoinMobile(strPhone);
                        editor.putString("userJoinMobile", strPhone);
                        Intent intent = new Intent(VerifyPhoneActivity.this, UserSafeActivity.class);
                        pActivity.startChildActivity("user_safe", intent);
                    } else {
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 1000ms
        timer.schedule(timerTask, 0, 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            mTxtLeftTime.setText("");
        }
    }

    public void initializeTimerTask() {
        mIntLeftTime = 180;
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        mIntLeftTime--;
                        mStrLeftTime = "[" + res.getString(R.string.left_time) + " " + CommonUtils.getLeftTime(mContext, mIntLeftTime) + "]";
                        mTxtLeftTime.setText(mStrLeftTime);
                        if (mIntLeftTime == 0) {
                            stopTimerTask();
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VerifyPhoneActivity.this, UserSafeActivity.class);
        pActivity.startChildActivity("user_safe", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(VerifyPhoneActivity.this, UserSafeActivity.class);
            pActivity.startChildActivity("user_safe", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
