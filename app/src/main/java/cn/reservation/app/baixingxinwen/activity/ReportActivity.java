package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        final View activityRootView = findViewById(R.id.activity_report);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > CommonUtils.getPixelValue(mContext, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_report);
                    scrollView.scrollTo(0, scrollView.getBottom() + 150);
                }
            }
        });

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
                HashMap<String, Object> params = new HashMap<String, Object>();
                Intent intent = getIntent();
                String fId = (String) intent.getSerializableExtra("fid");
                String tid = (String) intent.getSerializableExtra("tid");
                params.put("uid", CommonUtils.userInfo.getUserID());
                params.put("tid", tid);
                params.put("fid", fId);

                //edit_report_content
                EditText txt_report_message = (EditText)findViewById(R.id.edit_report_content);
                txt_report_message.getText();
                String message = txt_report_message.getText().toString();
                if(message.isEmpty()){
                    CommonUtils.showAlertDialog(ReportActivity.this, res.getString(R.string.please_input_info), null);
                    return;
                }
                params.put("message", txt_report_message.getText());

                final String url = "api/news/dispute";
                NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>(){

                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        JSONObject responseBody = response.body();
                        assert responseBody != null;
                        try {
                            if (responseBody.getInt("code") == 1) {
                                if(responseBody.getInt("ret") == 1){
                                    CommonUtils.showAlertDialog(ReportActivity.this,
                                            res.getString(R.string.report_received_message), new View.OnClickListener(){

                                                @Override
                                                public void onClick(View view) {
                                                    finish();
                                                }
                                            });
                                }
                                else{
                                    CommonUtils.showAlertDialog(ReportActivity.this, res.getString(R.string.error_message), null);
                                }
                            }
                            else{
                                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
//    private void createOrder() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ReportActivity.this);
//                RequestParams params = new RequestParams();
//                if(userID=="" || userID=="0"){
//                    progressDialog.dismiss();
//                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                params.put("uid", userID);
//                params.put("gateway", pay_method);
//                params.put("price", base_price);
//                String url = "group/order";
//                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            if (response.optInt("code") == 1) {
//                                System.out.println(response);
//
//                                price = response.getJSONObject("ret").optInt("price");
//                                cid = response.getJSONObject("ret").optInt("cid");
//                            }
//                            CommonUtils.dismissProgress(progressDialog);
//                        } catch (JSONException e) {
//                            CommonUtils.dismissProgress(progressDialog);
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        progressDialog.dismiss();
//                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        progressDialog.dismiss();
//                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }, 500);
//
//    }
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
