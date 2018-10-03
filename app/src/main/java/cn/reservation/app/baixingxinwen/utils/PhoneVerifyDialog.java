package cn.reservation.app.baixingxinwen.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneVerifyDialog extends Dialog implements View.OnClickListener{
    private String mStrVerifyCode;
    private boolean isVerified;
    private int mIntLeftTime;
    private String mStrLeftTime;

    public PhoneVerifyDialog(@NonNull Context context, String phoneNumber) {
        super(context);
        mContext = context;
        mPhoneNumber = phoneNumber;
    }

    private String mPhoneNumber;
    private Context mContext;
    private Button mBtnOK;
    private Button mBtnCancel;
    private TextView mTxtSendRequest;
    private View.OnClickListener mOkListener;
    private View.OnClickListener mCancelListener;
    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_phone_verify);
        mBtnOK = (Button) findViewById(R.id.btn_ok);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        TextView phoneNumber = findViewById(R.id.edit_phone_number);
        phoneNumber.setText(mPhoneNumber);
        mTxtSendRequest = findViewById(R.id.txt_left_time);
        mTxtSendRequest.setOnClickListener(this);
        mBtnOK.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    public void setOkListener(View.OnClickListener okListener){
        mOkListener = okListener;
    }

    public void setCancelListener(View.OnClickListener cancelListener){
        mCancelListener = cancelListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                EditText txtCode = findViewById(R.id.edit_verify_code);
                String strCode = txtCode.getText().toString();
                if(strCode.isEmpty()){
                    Toast.makeText(mContext, mContext.getString(R.string.please_input_verify_code), Toast.LENGTH_LONG).show();
                }
                else if(!strCode.equals(mStrVerifyCode)){
                    Toast.makeText(mContext, mContext.getString(R.string.verify_code_incorrect), Toast.LENGTH_LONG).show();
                }
                else{
                    if(mOkListener != null){
                        mOkListener.onClick(view);
                    }
                    dismiss();
                }
                break;
            case R.id.btn_cancel:
                if(mCancelListener != null){
                    mCancelListener.onClick(view);
                }
                dismiss();
                break;
            case R.id.txt_left_time:
                this.getVerifyCode(mPhoneNumber);
                break;
            default:
                break;
        }
    }

    private void initializeTimerTask() {
        mIntLeftTime = 180;
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        mIntLeftTime--;
                        mStrLeftTime = "[" + mContext.getString(R.string.left_time) + " " + CommonUtils.getLeftTime(mContext, mIntLeftTime) + "]";
                        mTxtSendRequest.setText(mStrLeftTime);
                        if (mIntLeftTime == 0) {
                            stopTimerTask();
                        }
                    }
                });
            }
        };
    }

    private void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 1000ms
        timer.schedule(timerTask, 0, 1000); //
    }

    private void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            mTxtSendRequest.setText("");
        }
    }

    private void getVerifyCode(String strPhone) {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", "change");
        params.put("phone", strPhone);
        System.out.println("phone:" + strPhone);
        if(CommonUtils.isLogin){
            params.put("uid", CommonUtils.userInfo.getUserID());
        }

        String url = "api/sms/send";
        mTxtSendRequest.setClickable(false);
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                try {
                    mTxtSendRequest.setClickable(true);
                    JSONObject response = res.body();
                    if (response != null && response.getInt("code") == 1) {
                        int ret = response.optJSONObject("ret").optInt("result");
                        if(ret > 0) {
                            mStrVerifyCode = response.optJSONObject("ret").optString("retkey");
                            isVerified = false;
                            startTimer();
                        }
                        else{
                            Toast.makeText(mContext, response.optJSONObject("ret").getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, mContext.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mTxtSendRequest.setClickable(true);
                Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }, 0);
    }

}
