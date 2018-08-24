package cn.reservation.app.baixingxinwen.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;


public class RecoverPasswordActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private EditText mEditPhone;
    private TextView mBtnSendVerifyCode;
    private EditText mEditVerifyCode;
    private EditText mEditPassword;
    private boolean isModify;
    private boolean isVerified = false;
    private int mIntLeftTime = 180;
    private String mStrVerifyCode;
    private String strPhone;

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();

    private ProgressHUD mProgressDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        mContext = RecoverPasswordActivity.this;
        res = mContext.getResources();

        CommonUtils.customActionBar(mContext, this, true, "重置密码");

        mEditPhone = (EditText) findViewById(R.id.edit_phone_number);
        mEditVerifyCode = (EditText) findViewById(R.id.edit_verify_code);
        mEditPassword = (EditText) findViewById(R.id.edit_password);

        TextView mBtnOK = (TextView) findViewById(R.id.btn_recover_pass);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });
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

        mBtnSendVerifyCode = (TextView) findViewById(R.id.btn_send_verify_code);
        mBtnSendVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerifyCode();
            }
        });
    }

    private void checkPhone() {
        final String strPhone = mEditPhone.getText().toString();
        if (strPhone.equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_mobile), Toast.LENGTH_LONG).show();
            return;
        } else if (strPhone.length() != 11) {
            Toast.makeText(mContext, res.getString(R.string.mobile_error), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        RequestParams params = new RequestParams();
        params.put("phone", strPhone);

        String url = "havephone";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    progressDialog.dismiss();
                    String code = response.getString("code");
                    if (code.equals("0")) {
                        Toast.makeText(mContext, res.getString(R.string.phone_error), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(RecoverPasswordActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("phone", strPhone);
                        RecoverPasswordActivity.this.startActivity(intent);
                        RecoverPasswordActivity.this.finish();
                        RecoverPasswordActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(RecoverPasswordActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(RecoverPasswordActivity.this, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendVerifyCode() {
        if (mEditPhone.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_mobile), Toast.LENGTH_LONG).show();
            return;
        } else if (mEditPhone.getText().toString().length() != 11) {
            Toast.makeText(mContext, res.getString(R.string.mobile_error), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, RecoverPasswordActivity.this);
        RequestParams params = new RequestParams();
        strPhone = mEditPhone.getText().toString();
        params.put("phone", strPhone);
        params.put("type","change");
        System.out.println("phone:"+strPhone);
        String url = "sms/send";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        mStrVerifyCode = response.optJSONObject("ret").optString("retkey");
                        isVerified = false;
                        startTimer();
                    } else if (code.equals("101")) {
                        Toast.makeText(mContext, res.getString(R.string.registered_phone), Toast.LENGTH_SHORT).show();
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
    private void recoverPassword() {
        if (!validateInput()) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("password",mEditPassword.getText().toString());
        params.put("username", mEditPhone.getText().toString());
        String url = "user/change";
        if (!isVerified) {
            Toast.makeText(mContext, res.getString(R.string.verify_code_incorrect), Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, RecoverPasswordActivity.this);
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                        RecoverPasswordActivity.this.finish();
                        //Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                        //RecoverPasswordActivity.this.startActivity(intent);
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
                System.out.println("verify pass"+responseString);
                progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validateInput() {
        if (mEditPhone.getText().toString().equals("")) {
            Toast toast = Toast.makeText(mContext, res.getString(R.string.please_input_mobile), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        } else if (mEditPhone.getText().toString().length() != 11) {
            Toast toast = Toast.makeText(mContext, res.getString(R.string.mobile_error), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if (mEditVerifyCode.getText().toString().equals("")) {
            Toast toast = Toast.makeText(mContext, res.getString(R.string.please_input_verify_code), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }
        if (mEditPassword.getText().toString().equals("")) {
            Toast toast = Toast.makeText(mContext, res.getString(R.string.please_input_password), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
            toast.show();
            return false;
        }/*else if (!(((EditText)findViewById(R.id.edit_compassword)).getText().toString().equals(mEditPassword.getText().toString()))) {
            Toast.makeText(mContext,"密码不一致。请再确认密码。", Toast.LENGTH_LONG).show();
            return false;
        }*/
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
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RecoverPasswordActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
