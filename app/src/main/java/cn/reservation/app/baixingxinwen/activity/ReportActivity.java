package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.alipay.PayResult;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class ReportActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = ReportActivity.class.getSimpleName();
    private IWXAPI api;
    private PayTask alipay;
    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private int report_type;
    private String userID;
    private String pay_method;
    private double base_price;
    private int cid;
    private int price;
    private static final int SDK_PAY_FLAG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) ReportActivity.this.getParent();
        report_type = 0;
        CommonUtils.customActionBar(mContext, this, true, "举报");
        CheckBox chk_report_adver = (CheckBox) findViewById(R.id.chk_report_adver);
        chk_report_adver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(report_type==0){
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }else{
                    report_type=0;
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }
            }
        });
        CheckBox chk_report_norule = (CheckBox) findViewById(R.id.chk_report_norule);
        chk_report_norule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(report_type==1){
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }else{
                    report_type=1;
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }
            }
        });
        CheckBox chk_report_ugly = (CheckBox) findViewById(R.id.chk_report_ugly);
        chk_report_ugly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(report_type==2){
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }else{
                    report_type=2;
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(false);
                }
            }
        });
        CheckBox chk_report_repeat = (CheckBox) findViewById(R.id.chk_report_repeat);
        chk_report_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(report_type==3){
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(true);
                }else{
                    report_type=3;
                    ((CheckBox) findViewById(R.id.chk_report_adver)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_norule)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_ugly)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_report_repeat)).setChecked(true);
                }
            }
        });
        RelativeLayout rlt_report_data = (RelativeLayout) findViewById(R.id.rlt_report_data);
        rlt_report_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void createOrder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ReportActivity.this);
                RequestParams params = new RequestParams();
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                params.put("gateway", pay_method);
                params.put("price", base_price);
                String url = "group/order";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.optInt("code") == 1) {
                                System.out.println(response);

                                price = response.getJSONObject("ret").optInt("price");
                                cid = response.getJSONObject("ret").optInt("cid");
                            }
                            CommonUtils.dismissProgress(progressDialog);
                        } catch (JSONException e) {
                            CommonUtils.dismissProgress(progressDialog);
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
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);

    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public void onBackPressed() {
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
