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
import android.widget.TextView;
import android.widget.Toast;

import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, View.OnClickListener {

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
                    if(CommonUtils.userInfo != null){
                        CommonUtils.userInfo.setUserPhone(strPhone);
                    }
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
                if(CommonUtils.userInfo != null){
                    CommonUtils.userInfo.setUserJoinMobile(strPhone);
                }
                stopTimerTask();
                signPhone(strPhone);
            }
        } else if (id == R.id.txt_left_time) {
            strPhone = mEditPhone.getText().toString();
            if (strPhone.equals("")) {
                Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG).show();
                return;
            }
            getVerifyCode(strPhone);
        }
    }

    private void getVerifyCode(String strPhone) {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        HashMap<String, Object> params = new HashMap<>();
        String type = (String) getIntent().getSerializableExtra("type");
        if (type == null) {
            params.put("type", "link");
        } else {
            params.put("type", type);
        }
        params.put("phone", strPhone);
        System.out.println("phone:" + strPhone);
        String url = "api/sms/send";
        mTxtLeftTime.setClickable(false);
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                try {
                    mTxtLeftTime.setClickable(true);
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
                    Toast.makeText(mContext, getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mTxtLeftTime.setClickable(true);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        }, 0);
    }

    private void signPhone(final String strPhone) {
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);

        String act = (String) getIntent().getSerializableExtra("act");
        if (act == null) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("act", "bind");
            params.put("uid", CommonUtils.userInfo.getUid());
            params.put("property_type", "mobile");
            params.put("bind_type", "bind");
            params.put("username", strPhone);
            params.put("password", "null");
            String url = APIManager.Ucenter_URL;
            NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                    try {
                        CommonUtils.dismissProgress(mProgressDialog);
                        JSONObject response = res.body();
                        if (response != null && response.getInt("code") == 1) {
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
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    CommonUtils.dismissProgress(mProgressDialog);
                    Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            });
        } else if(act.equals("login")) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("phone", strPhone);
            String url = "api/user/profilephone";
            NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                    try {
                        CommonUtils.dismissProgress(mProgressDialog);
                        JSONObject response = res.body();
                        if (response != null && response.getInt("code") == 1) {
                            JSONObject userObj = response.optJSONObject("ret");
                            CommonUtils.isLogin = true;
                            System.out.println(userObj);
                            Integer gender = 0;
                            if(!userObj.optString("gender").isEmpty()){
                                gender = Integer.parseInt(userObj.optString("gender"));
                            }
                            CommonUtils.userInfo = new UserInfo(Long.valueOf(userObj.optString("uid")), userObj.optString("username"),
                                    gender, userObj.optString("birthyear")+ "/"+ userObj.optString("birthmonth")+"/"+userObj.optString("userday"), strPhone,
                                    userObj.optString("avatar"), userObj.optString("credits"), userObj.optString("grouptitle"), userObj.optString("realname"),userObj.optString("uid"), "", "", strPhone, userObj.optString("credits"), userObj.optString("grouptitle"), "phone", userObj.optString("username"), "",userObj.optString("changeid") ,userObj.optString("dateline"));

                            SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                            editor.putLong("userID", CommonUtils.userInfo.getUserID());
                            editor.putString("userName", CommonUtils.userInfo.getUserName());
                            editor.putInt("userGender", CommonUtils.userInfo.getUserGender());
                            editor.putString("userBirthday", CommonUtils.userInfo.getUserBirthday());
                            editor.putString("userPhone", CommonUtils.userInfo.getUserPhone());
                            editor.putString("userPhoto", CommonUtils.userInfo.getUserPhoto());
                            editor.putString("credits", CommonUtils.userInfo.getToken());
                            editor.putString("identify", CommonUtils.userInfo.getUserIdentify());
                            editor.putString("realname", CommonUtils.userInfo.getUserPassword());
                            editor.putString("uid", CommonUtils.userInfo.getUid());
                            editor.putString("qq", CommonUtils.userInfo.getUserQQ());
                            editor.putString("wechat", CommonUtils.userInfo.getUserWeixin());
                            editor.putString("userJoinMobile", CommonUtils.userInfo.getUserJoinMobile());
                            editor.putString("baixingbi", CommonUtils.userInfo.getBaixingbi());
                            editor.putString("level", CommonUtils.userInfo.getLevel());
                            editor.putString("login_type", CommonUtils.userInfo.getLoginType());
                            editor.putString("login_username", CommonUtils.userInfo.getLoginUsername());
                            editor.putString("login_password", CommonUtils.userInfo.getLoginPassword());
                            editor.putString("changeid", CommonUtils.userInfo.getChangeid());
                            editor.putString("dateline", CommonUtils.userInfo.getDateline());
                            editor.apply();

                            if(!CommonUtils.channel_id.isEmpty()){
                                CommonUtils.registerChannelId(mContext);
                            }                        } else {
                            Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    CommonUtils.dismissProgress(mProgressDialog);
                    Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            });
        }

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
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(VerifyPhoneActivity.this, UserSafeActivity.class);
            if(pActivity != null) {
                pActivity.startChildActivity("user_safe", intent);
            }
            else{
                finish();
            }
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
