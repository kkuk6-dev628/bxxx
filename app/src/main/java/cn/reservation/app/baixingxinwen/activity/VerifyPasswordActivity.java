package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class VerifyPasswordActivity extends AppCompatActivity implements DialogInterface.OnCancelListener,View.OnClickListener{
    private static String TAG = VerifyPasswordActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String newPass;
    private final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private final Pattern hasLowercase = Pattern.compile("[a-z]");
    private final Pattern hasNumber = Pattern.compile("\\d");
    private ProgressHUD mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) VerifyPasswordActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "修改密码");
        TextView btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_ok) {
            newPass = ((EditText)findViewById(R.id.edit_password)).getText().toString();
            String oldPass = ((EditText)findViewById(R.id.edit_oldpass)).getText().toString();
            String comPass = ((EditText)findViewById(R.id.edit_compassword)).getText().toString();
            String oriPas = CommonUtils.userInfo.getUserPassword();
            if(oldPass.trim().equals("") && oriPas!=null && oriPas.equals("") == false){
                Toast.makeText(mContext, "请输入原密码", Toast.LENGTH_LONG).show();
                findViewById(R.id.edit_oldpass).setFocusableInTouchMode(true);
                findViewById(R.id.edit_oldpass).requestFocus();
                return;
            }
            if(newPass.trim().equals("")){
                Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_LONG).show();
                findViewById(R.id.edit_password).setFocusableInTouchMode(true);
                findViewById(R.id.edit_password).requestFocus();
                return;
            }
            if(!newPass.equals(comPass)){
                Toast.makeText(mContext, "请输入正确确认密码", Toast.LENGTH_LONG).show();
                findViewById(R.id.edit_compassword).setFocusableInTouchMode(true);
                findViewById(R.id.edit_compassword).requestFocus();
                return;
            }
            if (newPass.length() < 6) {
                Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                toast.show();
                return;
            }

            if (!(hasUppercase.matcher(newPass).find() || hasLowercase.matcher(newPass).find())) {
                Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                toast.show();
                return;
            }

            if (!hasNumber.matcher(newPass).find()) {
                Toast toast = Toast.makeText(mContext, "密码必须至少6个字符，而且同时包含字母和数字", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                toast.show();
                return;
            }
            changePassword(newPass, oldPass);
        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {}

    private void changePassword(final String newPassword, final String oldPasword) {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_LONG).show();
            return;
        }
        //mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        RequestParams params = new RequestParams();
        params.put("act", "bind");
        params.put("uid", CommonUtils.userInfo.getUid());
        params.put("property_type", "normal");
        params.put("bind_type", "bind");
        params.put("username", newPassword);
        params.put("password", oldPasword);
        String url = APIManager.Ucenter_URL;
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //mProgressDialog.dismiss();
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                        CommonUtils.userInfo.setUserPassword(newPassword);
                        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        editor.putString("realname", newPassword);
                        Intent intent = new Intent(VerifyPasswordActivity.this, UserSafeActivity.class);
                        pActivity.startChildActivity("user_safe", intent);
                    } else {
                        Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VerifyPasswordActivity.this, UserSafeActivity.class);
        pActivity.startChildActivity("user_safe", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(VerifyPasswordActivity.this, UserSafeActivity.class);
            pActivity.startChildActivity("user_safe", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
