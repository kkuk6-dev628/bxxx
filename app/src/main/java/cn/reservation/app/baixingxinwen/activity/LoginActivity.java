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
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.api.WXAPI;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, View.OnClickListener{
    private Context mContext;
    private Resources res;
    private EditText mEditPhone;
    private EditText mEditPassword;
    private String uid ="";
    private String login_type;
    private String login_username;
    private String login_password;
    private String qq;
    private String wechat;
    private String mobile;

    private ProgressHUD mProgressDialog;

//    private String fromActivity;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        if (APIManager.mTencent == null)
            APIManager.mTencent = Tencent.createInstance(APIManager.QQ_APP_ID, TabHostActivity.TabHostStack);
        res = mContext.getResources();
        CommonUtils.customActionBarWithRightButton(mContext, this, true, res.getString(R.string.login_member),
                getString(R.string.new_register), new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        view.setClickable(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setClickable(false);
                            }
                        }, 500);

                        Intent intent = new Intent(LoginActivity.this, RegisterPatientInfoActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    }
                });
        Intent intent = getIntent();
//        fromActivity = intent.getStringExtra("from_activity");
        mEditPhone = (EditText) findViewById(R.id.edit_phone_number);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        findViewById(R.id.btn_forgot_password).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.img_weixin_icon).setOnClickListener(this);
        findViewById(R.id.img_qq_icon).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CommonUtils.isLogin){
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(final View view) {
        view.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 500);

        int id = view.getId();
        if (id == R.id.btn_forgot_password) {
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        } else if (id == R.id.btn_register) {
            Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("type", "login");
            intent.putExtra("act", "login");
            LoginActivity.this.startActivity(intent);
//            LoginActivity.this.finish();
            LoginActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        } else if (id == R.id.btn_ok) {
            if (!validateInput()){
                return;
            }
            loginDirect("normal", mEditPhone.getText().toString(), mEditPassword.getText().toString());
        } else if (id == R.id.img_weixin_icon) {
            WXAPI.callBackHandler = new Handler(){//微信登录后回调模块
                @Override
                public void handleMessage(Message msg) {
                    Bundle b = msg.getData();
                    int res = b.getInt("loginRes");
                    String token = b.getString("loginToken");
                    if (res == 0 && token != null)
                        loginDirect("wechat", token, "null");
                }
            };
            WXAPI.Init(LoginActivity.this);
            WXAPI.Login();
        } else if (id == R.id.img_qq_icon) {
            //qq_login;
            APIManager.mTencent.login(LoginActivity.this, "all", qq_loginListener);
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
                    loginDirect("qq", openid, access_token);
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
    /**
     * @param loginType: null 用户名或者手机号码登录。 "qq":qq登录, "wechat":微信登录
     * @param userName: 账号， qq的open_id, wechat的code
     * @param passWord: 密码， qq的access_token, wechat的os
     */
    public void user_login(final String loginType, final String userName, final String passWord) {
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("act","login");
        params.put("login_type", loginType);
        params.put("username", userName);
        params.put("password", passWord);
        String url = APIManager.Ucenter_URL;
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                mProgressDialog.dismiss();
                JSONObject response = resp.body();
                assert response != null;
                try {
                    if (response.getInt("code") == 1) {
                        login_type = loginType;
                        login_username = userName;
                        login_password = passWord;
                        uid = response.getString("uid");
                        qq = response.optString("qq");
                        wechat = response.optString("wechat");
                        mobile = response.optString("mobile");
                        if(qq.equals("null")) qq="";
                        if(mobile.equals("null")) mobile="";
                        if(wechat.equals("null")) wechat="";
                        getUserInfo(uid);
                        gotoForward();
                    } else{
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void loginDirect(final String loginType, final String userName, final String passWord) {
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("act","login");
        params.put("login_type", loginType);
        params.put("username", userName);
        params.put("password", passWord);
        String url = "api/user/login";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                mProgressDialog.dismiss();
                JSONObject response = resp.body();
                assert response != null;
                try {
                    if (response.getInt("code") == 1) {
                        JSONObject userObj = response.optJSONObject("ret");
                        CommonUtils.isLogin = true;
                        System.out.println(userObj);
                        Integer gender = 0;
                        if(!userObj.optString("gender").isEmpty()){
                            gender = Integer.parseInt(userObj.optString("gender"));
                        }
                        CommonUtils.userInfo = new UserInfo(Long.valueOf(userObj.optString("uid")), userObj.optString("username"),
                                gender, userObj.optString("birthyear")+ "/"+ userObj.optString("birthmonth")+"/"+userObj.optString("userday"), userObj.optString("mobile"),
                                userObj.optString("avatar"), userObj.optString("credits"), userObj.optString("grouptitle"), userObj.optString("realname"),userObj.optString("uid"), userObj.optString("qq"), userObj.optString("wechat"), userObj.optString("mobile"), userObj.optString("credits"), userObj.optString("grouptitle"), login_type, login_username, login_password,userObj.optString("changeid") ,userObj.optString("dateline"));

                        CommonUtils.userInfo.save(mContext);
                        if(!CommonUtils.channel_id.isEmpty()){
                            CommonUtils.registerChannelId(mContext);
                        }

                        gotoForward();
                    } else{
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getUserInfo(String _uid) {
        //mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", _uid);
        String url = "api/user/profile/";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                JSONObject response = resp.body();
                assert response != null;
                try {
                    if (response.getInt("code") == 1) {
                        JSONObject userObj = response.optJSONObject("ret");
                        CommonUtils.isLogin = true;
                        System.out.println(userObj);
                        Integer gender = 0;
                        if(!userObj.optString("gender").isEmpty()){
                            gender = Integer.parseInt(userObj.optString("gender"));
                        }
                        CommonUtils.userInfo = new UserInfo(Long.valueOf(userObj.optString("uid")), userObj.optString("username"),
                                gender, userObj.optString("birthyear")+ "/"+ userObj.optString("birthmonth")+"/"+userObj.optString("userday"), userObj.optString("mobile"),
                                userObj.optString("avatar"), userObj.optString("credits"), userObj.optString("grouptitle"), userObj.optString("realname"),userObj.optString("uid"), qq, wechat, mobile, userObj.optString("credits"), userObj.optString("grouptitle"), login_type, login_username, login_password,userObj.optString("changeid") ,userObj.optString("dateline"));

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
                        }

//                        gotoForward();
                    } else{
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean validateInput() {
        if (mEditPhone.getText().toString().equals("")) {
            Toast.makeText(mContext, "请输入用户名或者手机号", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mEditPassword.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_password), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void gotoForward() {
        //if (fromActivity==null){
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCancel(DialogInterface dialog) {}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
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
