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
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.WXAPI;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class UserSafeActivity extends AppCompatActivity implements DialogInterface.OnCancelListener,View.OnClickListener{
    private static String TAG = UserSafeActivity.class.getSimpleName();
    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private ProgressHUD mProgressDialog;
    private TextView txt_safe_weixin, txt_safe_qq, txt_safe_phone, btn_safe_qq, btn_safe_phone, btn_safe_weixin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safe);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) UserSafeActivity.this.getParent();
        if (APIManager.mTencent == null)
            APIManager.mTencent = Tencent.createInstance(APIManager.QQ_APP_ID, TabHostActivity.TabHostStack);
        CommonUtils.customActionBar(mContext, this, true, "账号安全");

        txt_safe_phone =(TextView) findViewById(R.id.txt_safe_phone);
        String mobile = CommonUtils.userInfo.getUserJoinMobile();
        txt_safe_phone.setText(mobile);

        btn_safe_phone = (TextView) findViewById(R.id.btn_safe_phone);
        btn_safe_phone.setOnClickListener(this);
        if(mobile.equals(""))
            btn_safe_phone.setText("绑定");
        else
            btn_safe_phone.setText("换绑");

        btn_safe_qq = (TextView) findViewById(R.id.btn_safe_qq);
        btn_safe_qq.setOnClickListener(this);
        txt_safe_qq =(TextView) findViewById(R.id.txt_safe_qq);
        String qq = CommonUtils.userInfo.getUserQQ();
        txt_safe_qq.setText(qq);
        if(qq.equals(""))
            btn_safe_qq.setText("绑定");
        else
            btn_safe_qq.setText("解绑");

        btn_safe_weixin = (TextView) findViewById(R.id.btn_safe_weixin);
        btn_safe_weixin.setOnClickListener(this);
        txt_safe_weixin =(TextView) findViewById(R.id.txt_safe_weixin);
        String weixin = CommonUtils.userInfo.getUserWeixin();
        txt_safe_weixin.setText(weixin);
        if(weixin.equals(""))
            btn_safe_weixin.setText("绑定");
        else
            btn_safe_weixin.setText("解绑");

        TextView btn_safe_pass = (TextView) findViewById(R.id.btn_safe_pass);
        btn_safe_pass.setOnClickListener(this);
        RelativeLayout lytLogout = (RelativeLayout) findViewById(R.id.rlt_my_logout);
        lytLogout.setOnClickListener(this);
    }
    IUiListener qq_loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            try {
                int ret=values.getInt("ret");
                if(ret==0) {
                    String openid = values.getString("openid");
                    String access_token = values.getString("access_token");
                    String expires_in = values.getString("expires_in");
                    bindUserProperty("qq", "bind", openid, access_token);
                    //APIManager.mTencent.setOpenId(openid);
                    //APIManager.mTencent.setAccessToken(access_token, expires_in);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(View view) {
        Dialog dialogBasic = new Dialog(mContext);
        final Dialog dialog = dialogBasic;
        LayoutInflater inflater = getLayoutInflater();
        View logout_view = inflater.inflate(R.layout.alert_exit, null);

        TextView txt_alert_dlg_content = (TextView) logout_view.findViewById(R.id.txt_alert_dialog_content);



        int id = view.getId();
        if (id == R.id.btn_safe_phone){
            Intent intent = new Intent(UserSafeActivity.this, VerifyPhoneActivity.class);
            pActivity.startChildActivity("me", intent);
        }
        else if (id == R.id.btn_safe_qq) {
            /*Intent intent = new Intent(UserSafeActivity.this, VerifyQQActivity.class);
            pActivity.startChildActivity("me", intent);*/
            //qq_login;
            if (CommonUtils.userInfo.getUserQQ().equals(""))
            {
                if(!this.isFinishing()){
                    APIManager.mTencent.login(UserSafeActivity.this, "all", qq_loginListener);
                }
            }
            else {
                txt_alert_dlg_content.setText("您确定要解绑QQ吗？");
                TextView btnLogout_qq = (TextView) logout_view.findViewById(R.id.btn_ok);
                TextView btnExit = (TextView) logout_view.findViewById(R.id.btn_cancel);
                btnLogout_qq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View p_view) {
                        dialog.dismiss();
                        bindUserProperty("qq", "untie", "null", "null");
                    }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View p_view) {
                        dialog.dismiss();
                        //TabHostActivity.TabHostStack.finish();
                    }
                });
                CommonUtils.showAlertDialog(mContext, dialog, logout_view, 216);
//                new AlertDialog.Builder(mContext)
//                        .setTitle("提示")
//                        .setMessage("您确定要解绑QQ吗？")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                bindUserProperty("qq", "untie", "null", "null");
//                            }
//                        })
//                        .create().show();
            }
        }
        else if (id == R.id.btn_safe_weixin) {
            /*Intent intent = new Intent(UserSafeActivity.this, VerifyWeixinActivity.class);
            pActivity.startChildActivity("me", intent);*/
            if (CommonUtils.userInfo.getUserWeixin().equals("")) {
                WXAPI.callBackHandler = new Handler() {//微信登录后回调模块
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle b = msg.getData();
                        int res = b.getInt("loginRes");
                        String token = b.getString("loginToken");
                        if (res == 0 && token != null)
                            bindUserProperty("wechat", "bind", token, "null");
                    }
                };
                WXAPI.Init(UserSafeActivity.this);
                WXAPI.Login();
            } else {
                txt_alert_dlg_content.setText("您确定要解绑微信吗？");
                TextView btnLogout_weixin = (TextView) logout_view.findViewById(R.id.btn_ok);
                TextView btnExit = (TextView) logout_view.findViewById(R.id.btn_cancel);
                btnLogout_weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View p_view) {
                        dialog.dismiss();
                        bindUserProperty("wechat", "untie", "null", "null");
                    }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View p_view) {
                        dialog.dismiss();
                        //TabHostActivity.TabHostStack.finish();
                    }
                });
                CommonUtils.showAlertDialog(mContext, dialog, logout_view, 216);
//                CommonUtils.showAlertDialog(mContext, "您确定要解绑微信吗？", );
//                new AlertDialog.Builder(mContext)
//                        .setTitle("提示")
//                        .setMessage("您确定要解绑微信吗？")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                bindUserProperty("wechat", "untie", "null", "null");
//                            }
//                        })
//                        .create().show();
            }
        }
        else if (id == R.id.btn_safe_pass){
            Intent intent = new Intent(UserSafeActivity.this, VerifyPasswordActivity.class);
            pActivity.startChildActivity("me", intent);
        }
        else if (id == R.id.rlt_my_logout){
            //注销
//            final Dialog dialog = new Dialog(mContext);
//            LayoutInflater inflater = getLayoutInflater();
//            View logout_view = inflater.inflate(R.layout.alert_exit, null);
            TextView btnLogout = (TextView) logout_view.findViewById(R.id.btn_ok);
            TextView btnExit = (TextView) logout_view.findViewById(R.id.btn_cancel);
            btnLogout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p_view) {
                    dialog.dismiss();
                    CommonUtils.userInfo.setUserID(0);
                    CommonUtils.userInfo.setUserName("");
                    CommonUtils.userInfo.setUserGender(0);
                    CommonUtils.userInfo.setUserBirthday("");
                    CommonUtils.userInfo.setUserPhone("");
                    CommonUtils.userInfo.setUserPhoto("");
                    CommonUtils.userInfo.setToken("");
                    CommonUtils.userInfo.setUserIdentify("");
                    CommonUtils.userInfo.setUserPassword("");
                    CommonUtils.userInfo.setBaixingbi("0");
                    CommonUtils.userInfo.setLevel("");
                    CommonUtils.userInfo.setLoginType("normal");
                    CommonUtils.userInfo.setLoginUsername("");
                    CommonUtils.userInfo.setLoginPassword("");
                    CommonUtils.isLogin = false;

                    SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                    editor.putLong("userID", CommonUtils.userInfo.getUserID());
                    editor.putString("userName", CommonUtils.userInfo.getUserName());
                    editor.putInt("userGender", CommonUtils.userInfo.getUserGender());
                    editor.putString("userBirthday", CommonUtils.userInfo.getUserBirthday());
                    editor.putString("userPhone", CommonUtils.userInfo.getUserPhone());
                    editor.putString("userPhoto", CommonUtils.userInfo.getUserPhoto());
                    editor.putString("token", CommonUtils.userInfo.getToken());
                    editor.putString("identify", CommonUtils.userInfo.getUserIdentify());
                    editor.putString("password", CommonUtils.userInfo.getUserPassword());
                    editor.putString("baixingbi", CommonUtils.userInfo.getBaixingbi());
                    editor.putString("level", CommonUtils.userInfo.getLevel());
                    editor.putString("login_type", CommonUtils.userInfo.getLoginType());
                    editor.putString("login_username", CommonUtils.userInfo.getLoginUsername());
                    editor.putString("login_password", CommonUtils.userInfo.getLoginPassword());
                    editor.apply();

                    TabHostActivity.TabHostStack.gotoLogin();
                }
            });
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View p_view) {
                    dialog.dismiss();
                    //TabHostActivity.TabHostStack.finish();
                }
            });
            CommonUtils.showAlertDialog(mContext, dialog, logout_view, 216);
        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {}
    private void bindUserProperty(final String property_type, final String bind_type, final  String username, final  String password) {
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        RequestParams params = new RequestParams();
        params.put("act", "bind");
        params.put("uid", CommonUtils.userInfo.getUid());
        params.put("property_type", property_type);
        params.put("bind_type", bind_type);
        params.put("username", username);
        params.put("password", password);
        String url = APIManager.Ucenter_URL;
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Integer code = response.getInt("code");
                    String res = response.getString("res");
                    mProgressDialog.dismiss();
                    if (code==1) {
                        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        if (property_type.equals("wechat")){
                            if (bind_type.equals("bind"))
                                CommonUtils.userInfo.setUserWeixin(res);
                            else
                                CommonUtils.userInfo.setUserWeixin("");
                            String weixin = CommonUtils.userInfo.getUserWeixin();
                            editor.putString("wechat", weixin);
                            txt_safe_weixin.setText(weixin);
                            if(weixin.equals(""))
                                btn_safe_weixin.setText("绑定");
                            else
                                btn_safe_weixin.setText("解绑");
                        }
                        else if (property_type.equals("qq")){
                            if (bind_type.equals("bind"))
                                CommonUtils.userInfo.setUserQQ(res);
                            else
                                CommonUtils.userInfo.setUserQQ("");
                            String qq = CommonUtils.userInfo.getUserQQ();
                            editor.putString("qq", qq);
                            txt_safe_qq.setText(qq);
                            if(qq.equals(""))
                                btn_safe_qq.setText("绑定");
                            else
                                btn_safe_qq.setText("解绑");
                        }
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(UserSafeActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProgressDialog.dismiss();
                Toast.makeText(UserSafeActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,qq_loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        pActivity.finishChildActivity();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pActivity.finishChildActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
