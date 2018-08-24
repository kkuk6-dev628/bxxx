package cn.reservation.app.baixingxinwen.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

public class NewPasswordActivity extends KeypadActivity {

    private Context mContext;
    private Resources res;

    private TextView mBtnOK;
    private EditText mEditNewPassword;
    private String strPhone;

    private ProgressHUD mProgressDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        mContext = NewPasswordActivity.this;
        res = mContext.getResources();
        Intent intent = getIntent();
        strPhone = intent.getStringExtra("phone");

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.set_password));

        setKeypad();

        mEditNewPassword = (EditText) findViewById(R.id.edit_new_password);
        mEditNewPassword.setShowSoftInputOnFocus(false);
        mEditNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mCurEditText = mEditNewPassword;
                } else {
                    mCurEditText = null;
                }
            }
        });

        mBtnOK = (TextView) findViewById(R.id.btn_ok);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPassword();
            }
        });
    }

    private void setNewPassword() {
        if (mEditNewPassword.getText().toString().equals("")) {
            Toast.makeText(mContext, res.getString(R.string.please_input_new_password), Toast.LENGTH_LONG).show();
            return;
        }else if (mEditNewPassword.getText().toString().length() < 6) {
            Toast.makeText(mContext, res.getString(R.string.password_error), Toast.LENGTH_LONG).show();
            return;
        }
        mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        RequestParams params = new RequestParams();
        params.put("phone", strPhone);
        params.put("password", mEditNewPassword.getText().toString());

        String url = "resetpwd";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    mProgressDialog.dismiss();
                    String code = response.getString("code");
                    if (code.equals("0")) {
                        Toast.makeText(mContext, res.getString(R.string.password_update_failed), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, res.getString(R.string.password_updated_successfully), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(NewPasswordActivity.this, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProgressDialog.dismiss();
                Toast.makeText(NewPasswordActivity.this, responseString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }
}
