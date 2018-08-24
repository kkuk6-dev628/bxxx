package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class VerifyQQActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = VerifyQQActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private TextView mBtnConfirmCode;
    private EditText mEditQQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_qq);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) VerifyQQActivity.this.getParent();
        mEditQQ = (EditText) findViewById(R.id.edit_qq_number);
        CommonUtils.customActionBar(mContext, this, true, "QQ绑定");
        mBtnConfirmCode = (TextView) findViewById(R.id.btn_confirm_code);
        mBtnConfirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String strQQ = mEditQQ.getText().toString();
                    signQQ(strQQ);
            }
        });
    }
    private void signQQ(String strQQ) {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_LONG).show();
            return;
        }
        if(strQQ.trim().equals("")){
            Toast.makeText(mContext, "请输入QQ号码吧", Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("type","qq");
        params.put("lvalue", strQQ);
        params.put("uid", CommonUtils.userInfo.getUid());
        System.out.println("qq:"+strQQ);
        CommonUtils.userInfo.setUserQQ(strQQ);
        String url = "user/link";
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyQQActivity.this);
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                CommonUtils.dismissProgress(progressDialog);
                try {
                    String code = response.getString("code");
                    System.out.println(response);
                    if (code.equals("1")) {
                        Intent intent = new Intent(VerifyQQActivity.this, UserSafeActivity.class);
                        pActivity.startChildActivity("user_safe", intent);
                    } else {
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VerifyQQActivity.this, UserSafeActivity.class);
        pActivity.startChildActivity("user_safe", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(VerifyQQActivity.this, UserSafeActivity.class);
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
