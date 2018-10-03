package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.api.WXAPI;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class RegisterPatientInfoActivity extends ConfirmDialogActivity implements DialogInterface.OnCancelListener, View.OnClickListener {

    private Context mContext;
    private Resources res;
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private RelativeLayout mLytMale;
    private TextView mBtnMale;
    private RelativeLayout mLytFemale;
    private TextView mBtnFemale;
    private RelativeLayout mLytBirthday;
    private TextView mTxtBirthday;
    private EditText mEditName;
    private EditText mEditCertification;
    private EditText mEditMobile;
    private EditText mEditVerifyCode;
    private EditText mEditPassword;
    private TextView mBtnSendVerifyCode;
    private String strPhone;

    private int mGender = 0;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mTmpYear;
    private int mTmpMonth;
    private int mTmpDay;

    private ProgressHUD mProgressDialog;

    private boolean isModify;
    private boolean isVerified = false;
    private int mIntLeftTime = 180;
    private String mStrVerifyCode;
    private String mRegType = "normal";
    private String mOpenId = "";
    private String mAccessToken = "";
    private RelativeLayout mSocialLoginPanel;

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient_info);

        mContext = RegisterPatientInfoActivity.this;
        res = mContext.getResources();

        Intent intent = getIntent();
        isModify = intent.getBooleanExtra("modify", false);
        String title = res.getString(R.string.member_register);
        if (isModify) title = res.getString(R.string.member_register);
        CommonUtils.customActionBar(mContext, this, true, title);

        mEditMobile = (EditText) findViewById(R.id.edit_mobile);
        mEditVerifyCode = (EditText) findViewById(R.id.edit_verify_code);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        mBtnSendVerifyCode = (TextView)findViewById(R.id.btn_send_verify_code);
        mSocialLoginPanel = findViewById(R.id.login_social);

        findViewById(R.id.btn_send_verify_code).setOnClickListener(this);
        findViewById(R.id.btn_policy).setOnClickListener(this);
        findViewById(R.id.img_weixin_icon).setOnClickListener(this);
        findViewById(R.id.img_qq_icon).setOnClickListener(this);

        mEditVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (mStrVerifyCode != null && !mStrVerifyCode.equals("") && mEditVerifyCode.getText().toString().equals(mStrVerifyCode)) {
                    isVerified = true;
                    stopTimerTask();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(final View view) {
        view.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(false);
            }
        }, 500);

        int id = view.getId();
        if (id == R.id.btn_ok) {
            if (!isVerified) {
                Toast.makeText(mContext, res.getString(R.string.verify_code_incorrect), Toast.LENGTH_LONG).show();
                return;
            }
            register();
        } else if (id == R.id.btn_send_verify_code){
            sendVerifyCode();
        } else if (id == R.id.btn_policy){
            Intent intent = new Intent(RegisterPatientInfoActivity.this, PrivacyActivity.class);
            intent.putExtra("register_activity", "privacy_activity");
            RegisterPatientInfoActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
        } else if (id == R.id.img_weixin_icon){
            WXAPI.callBackHandler = new Handler(){//微信登录后回调模块
                @Override
                public void handleMessage(Message msg) {
                    Bundle b = msg.getData();
                    int res = b.getInt("loginRes");
                    String token = b.getString("loginToken");
                    if (res == 0 && token != null){
                        mSocialLoginPanel.setVisibility(GONE);
                        mRegType = "wechat";
                        mOpenId = token;
                        register();
                    }
                }
            };
            WXAPI.Init(RegisterPatientInfoActivity.this);
            WXAPI.Login();
        } else if (id == R.id.img_qq_icon) {
            //qq_login;
            APIManager.mTencent.login(RegisterPatientInfoActivity.this, "all", qq_loginListener);
        }
    }
    IUiListener qq_loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("qq_login", values.toString());
            try {
                int ret=values.getInt("ret");
                if(ret==0) {
                    String openid = values.getString("openid");
                    String access_token = values.getString("access_token");
                    String expires_in = values.getString("expires_in");
                    mRegType = "qq";
                    mOpenId = openid;
                    mAccessToken = access_token;
                    mSocialLoginPanel.setVisibility(GONE);
                    register();
                    //APIManager.mTencent.setOpenId(openid);
                    //APIManager.mTencent.setAccessToken(access_token, expires_in);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,qq_loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendVerifyCode() {
        if (mEditMobile.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_mobile), Toast.LENGTH_LONG).show();
            return;
        } else if (mEditMobile.getText().toString().length() != 11) {
            Toast.makeText(mContext, res.getString(R.string.mobile_error), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, RegisterPatientInfoActivity.this);
        RequestParams params = new RequestParams();
        strPhone = mEditMobile.getText().toString();
        params.put("phone", strPhone);
        params.put("type","verify");
        System.out.println("phone:"+strPhone);
        String url = "sms/send";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1") && response.optJSONObject("ret").optInt("result") >= 0) {
                        mStrVerifyCode = response.optJSONObject("ret").optString("retkey");
                        isVerified = false;
                        startTimer();
                    } else if (code.equals("0")) {
                        Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
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

    public void selectDate() {
        final Dialog dialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.datepicker_dialog, null);

        Calendar calendar = Calendar.getInstance();
        long curTime = calendar.getTimeInMillis() + 1000;
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        mTmpYear = mYear;
        mTmpMonth = mMonth;
        mTmpDay = mDay;

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        datePicker.setMaxDate(curTime);
        datePicker.updateDate(mYear, mMonth, mDay);
        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                mTmpYear = year;
                mTmpMonth = month;
                mTmpDay = dayOfMonth;
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int y = getFieldValue(datePicker, "year");
                if (y > 0 && y != mTmpYear) {
                    mTmpYear = y;
                }
                int m = getFieldValue(datePicker, "month") - 1;
                if (m > -1 && m != mTmpMonth) {
                    mTmpMonth = m;
                }
                int d = getFieldValue(datePicker, "day");
                if (d > 0 && d != mTmpDay) {
                    mTmpDay = d;
                }
                if ((mYear != mTmpYear || mMonth != mTmpMonth || mDay != mTmpDay) && checkValidDate(mTmpYear, mTmpMonth, mTmpDay)) {
                    mYear = mTmpYear;
                    mMonth = mTmpMonth;
                    mDay = mTmpDay;
                }
                String birthday = Integer.toString(mYear) + "/" + Integer.toString(mMonth+1) + "/" + Integer.toString(mDay);
                mTxtBirthday.setText(birthday);
                dialog.dismiss();
            }
        });
        CommonUtils.showAlertDialog(mContext, dialog, view, 308);
    }

    private int getFieldValue(ViewGroup dp, String field) {
        int fieldValue = 0;
        for (int x = 0, n = dp.getChildCount(); x < n; x++) {
            View v = dp.getChildAt(x);
            if (v instanceof TextView) {
                try {
                    String id = dp.getResources().getResourceName(dp.getId());
                    if (id.equals("android:id/" + field)) {
                        String value = ((TextView) v).getText().toString();
                        if (!value.equals("")) {
                            if (CommonUtils.isValidInteger(value)) {
                                if (field.equals("year") && value.length()<4) {
                                    fieldValue = 0;
                                } else {
                                    fieldValue = Integer.valueOf(value);
                                }
                            } else {
                                if (field.equals("year") || field.equals("day")) {
                                    fieldValue = Integer.valueOf(value.substring(0, value.length() - 1).trim());
                                } else {
                                    if (value.length() == 3) {
                                        fieldValue = CommonUtils.getMonthFromString(value);
                                        if (fieldValue == 0) {
                                            fieldValue = Integer.valueOf(value.substring(0, value.length() - 1).trim());
                                        }
                                    } else {
                                        fieldValue = Integer.valueOf(value.substring(0, value.length() - 1).trim());
                                    }
                                }
                            }
                        }
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            } else if (v instanceof ViewGroup) {
                fieldValue = getFieldValue((ViewGroup)v, field);
                if (fieldValue > 0) {
                    break;
                }
            }
        }
        return fieldValue;
    }

    private boolean checkValidDate(int y, int m, int d) {
        if (CommonUtils.isDateValid(y, m, d)) {
            Calendar calendar = Calendar.getInstance();
            long curTime = calendar.getTimeInMillis();
            calendar.set(Calendar.YEAR, y);
            calendar.set(Calendar.MONTH, m);
            calendar.set(Calendar.DAY_OF_MONTH, d);
            long time = calendar.getTimeInMillis();
            return time <= curTime;
        } else {
            return false;
        }
    }

    private void register() {
        if(!validateInput()){
            CommonUtils.showAlertDialog(mContext, getString(R.string.register_input),
                    null);
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", mEditMobile.getText().toString());
        params.put("username", mOpenId);
        params.put("password", mEditPassword.getText().toString());
        params.put("register_type", mRegType);
        params.put("access_token", mAccessToken);
        params.put("act","register");
        String url = APIManager.Ucenter_URL;
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                mProgressDialog.dismiss();
                JSONObject response = resp.body();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        String msg = res.getString(R.string.success_register);
                        if (isModify) {
                            msg = res.getString(R.string.success_update);
                            CommonUtils.userInfo.setLoginType(mRegType);
                            CommonUtils.userInfo.setLoginUsername(mOpenId);
                            CommonUtils.userInfo.setLoginPassword(mEditPassword.getText().toString());
                            SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                            editor.putString("login_type", CommonUtils.userInfo.getLoginType());
                            editor.putString("login_username", CommonUtils.userInfo.getLoginUsername());
                            editor.putString("login_password", CommonUtils.userInfo.getLoginPassword());
                            editor.apply();
                        }
                        CommonUtils.showAlertDialog(mContext, msg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RegisterPatientInfoActivity.this.finish();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean validateInput() {
        if (mEditMobile.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_mobile), Toast.LENGTH_LONG).show();
            return false;
        } else if (mEditMobile.getText().toString().length() != 11) {
            Toast.makeText(mContext, res.getString(R.string.mobile_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isModify) {
            if (mEditVerifyCode.getText().toString().equals("")) {
                Toast.makeText(mContext, res.getString(R.string.please_input_verify_code), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (mEditPassword.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (!(((EditText)findViewById(R.id.edit_compassword)).getText().toString().equals(mEditPassword.getText().toString()))) {
            Toast.makeText(mContext,"密码不一致，请再确认密码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mEditPassword.getText().toString().length() < 6) {
            Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }

        if (!(hasUppercase.matcher(mEditPassword.getText().toString()).find() || hasLowercase.matcher(mEditPassword.getText().toString()).find())) {
            Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }

        if (!hasNumber.matcher(mEditPassword.getText().toString()).find()) {
            Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        return true;
    }

    public void startTimer() {
        timer = new Timer();
        mBtnSendVerifyCode.setEnabled(false);
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 1000ms
        timer.schedule(timerTask, 0, 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            mBtnSendVerifyCode.setEnabled(true);
            mBtnSendVerifyCode.setText(res.getString(R.string.send_verify_code));
        }
    }

    public void initializeTimerTask() {
        mIntLeftTime = 180;
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        mIntLeftTime--;
                        mBtnSendVerifyCode.setText(CommonUtils.getLeftTime(mContext, mIntLeftTime));
                        if (mIntLeftTime == 0) {
                            stopTimerTask();
                        }
                    }
                });
            }
        };
    }

    @Override
    public void callBack() {
        if (isModify) {
            Intent intent = new Intent();
            /*
            FamilyMemberItem familyMemberItem = new FamilyMemberItem(CommonUtils.userInfo.getUserID(), CommonUtils.userInfo.getUserName(),
                    1, CommonUtils.userInfo.getUserGender(), "me", CommonUtils.userInfo.getUserIdentify(), false);
            intent.putExtra("FamilyItem", familyMemberItem);
            setResult(CommonUtils.RESULT_CODE_MODIFY, intent);
            */
        }
        finish();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RegisterPatientInfoActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCancel(DialogInterface dialog) {
    }
    //QQ callback
    private class BaseUiListener implements IUiListener{
        @Override
        public void onComplete(Object response) {
            if (null == response)
                return;
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0)
                return;
            doComplete((JSONObject)response);
        }
        protected void doComplete(JSONObject values) {}
        @Override
        public void onError(UiError e) {}
        @Override
        public void onCancel() {}
    }
}
