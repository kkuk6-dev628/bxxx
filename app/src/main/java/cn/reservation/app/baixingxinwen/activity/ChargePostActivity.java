package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.alipay.PayResult;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.NVP;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

@SuppressWarnings("deprecation")
public class ChargePostActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = ChargePostActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String fId;
    private String tId;
    private String sortId;
    private String userID;
    private String price;
    private String oid;
    private Dictionary data1;
    private File[] mFiles;
    private IWXAPI api;
    private PayTask alipay;
    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_post);
        api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP_ID);
        this.data1 = new Hashtable();
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) ChargePostActivity.this.getParent();
        Intent intent = getIntent();
        fId = (String) intent.getSerializableExtra("fid");
        sortId = (String) intent.getSerializableExtra("sortid");
        tId = (String) intent.getSerializableExtra("tid");
        userID = (String) intent.getSerializableExtra("userID");
        CommonUtils.customActionBar(mContext, this, true, "??????????????????");
        CheckBox weixin_chk = (CheckBox) findViewById(R.id.weixin_chk);
        getPostPrice();
        weixin_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()){
                    ((CheckBox) findViewById(R.id.zhifubao_chk)).setChecked(false);
                }else{
                    ((CheckBox) findViewById(R.id.zhifubao_chk)).setChecked(true);
                }
            }
        });
        CheckBox zhifubao_chk = (CheckBox) findViewById(R.id.zhifubao_chk);
        zhifubao_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.zhifubao_chk)).isChecked()){
                    ((CheckBox) findViewById(R.id.weixin_chk)).setChecked(false);
                }else{
                    ((CheckBox) findViewById(R.id.weixin_chk)).setChecked(true);
                }
            }
        });

        RelativeLayout rlt_charge_term_next = (RelativeLayout)findViewById(R.id.rlt_charge_term_next);
        rlt_charge_term_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()) {
                    createWeixinOrder();
                }else if(((CheckBox) findViewById(R.id.zhifubao_chk)).isChecked()) {
                    createAlipayOrder();
                }
            }
        });              
    }    
    private void getPostPrice(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "price/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("alipay response"+response);
                        //isLoadMore = response.getBoolean("hasmore");
                        String price = response.optString("price");
                        ((TextView)findViewById(R.id.txt_pay_price)).setText("???"+price);
                        CommonUtils.dismissProgress(progressDialog);
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
        }, 500);
    }

    private void createAlipayOrder(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {

                }else{
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                params.put("type","post");
                params.put("uid", CommonUtils.userInfo.getUid());
                //params.put("amount",1);
                System.out.println("ccc++"+CommonUtils.userInfo.getUserID());
                String url = "payment/alipay";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("alipay response"+response);
                            //isLoadMore = response.getBoolean("hasmore");

                            //req.appId = "wxf8b4f85f3a794e77";  // ????????????appId
                            String tradeNo		= response.getString("tradeno");
                            CommonUtils.tradeNo = tradeNo;
                            CommonUtils.orderString = response.optString("orderstring");
                            CommonUtils.dismissProgress(progressDialog);
                            Runnable payRunnable = new Runnable() {
                                @Override    public void run() {
                                    alipay = new PayTask(ChargePostActivity.this);
                                    Map<String, String> result = alipay.payV2(CommonUtils.orderString, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();

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
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(ChargePostActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        switch(fId){
                            case "2":
                                postData1();
                                break;
                            case "93":
                                postData3();
                                break;
                            case "38": //?????????????????????
                                postData4();
                                break;
                            case "42": //????????????????????????
                                postData5();
                                break;
                            case "39": //????????????????????????
                                postData6();
                                break;
                            case "40": //????????????????????????
                                postData7();
                                break;
                            case "107": //????????????????????????
                                postData8();
                                break;
                            case "44":
                                postData8();
                                break;
                            case "48": //????????????????????????
                                postData8();
                                break;
                            case "74": //????????????????????????
                                postData8();
                                break;
                            case "94":
                                postData8();
                                break;
                            case "83": //????????????????????????
                                postData8();
                                break;
                            case "92": //????????????????????????
                                postData8();
                                break;
                            case "50":
                                postData8();
                                break;
                        }
                    } else {
                        // ????????????????????????????????????????????????????????????????????????
                        Toast.makeText(ChargePostActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                /*
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // ??????resultStatus ??????9000??????result_code
                    // ??????200?????????????????????????????????????????????????????????????????????????????????
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // ??????alipay_open_id???????????????????????????extern_token ???value
                        // ??????????????????????????????????????????
                        Toast.makeText(PayDemoActivity.this,
                                "????????????\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // ?????????????????????????????????
                        Toast.makeText(PayDemoActivity.this,
                                "????????????" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                */
                default:
                    break;
            }

        };
    };
    private void createWeixinOrder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {

                }else{
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                params.put("type","post");
                params.put("uid", CommonUtils.userInfo.getUid());
                String url = "payment/wechat";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("weixin response"+response);
                            PayReq req = new PayReq();
                            CommonUtils.dismissProgress(progressDialog);
                            req.appId			= response.getString("appid");
                            req.partnerId		= response.getString("partnerid");
                            req.prepayId		= response.getString("prepayid");
                            req.nonceStr		= response.getString("noncestr");
                            req.timeStamp		= response.getString("timestamp");
                            req.packageValue	= response.getString("package");
                            req.sign			= response.getString("sign");
                            req.extData			= "app data"; // optional
                            String tradeNo		= response.getString("tradeno");
                            CommonUtils.pay_type = 1;
                            CommonUtils.tradeNo = tradeNo;
                            api.sendReq(req);
                            finish();
                            //Toast.makeText(mContext, req.appId	+ tradeNo, Toast.LENGTH_SHORT).show();

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
        }, 500);
    }
    public void postData1(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("source", CommonUtils.data1.get("source"));
                params.put("house_number", CommonUtils.data1.get("house_number"));
                params.put("floors", CommonUtils.data1.get("floors"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("house_type", CommonUtils.data1.get("house_type"));
                params.put("house_level", CommonUtils.data1.get("house_level"));
                params.put("havesun", CommonUtils.data1.get("havesun"));
                params.put("villiage", CommonUtils.data1.get("villiage"));
                params.put("award_method", CommonUtils.data1.get("award_method"));
                params.put("square", CommonUtils.data1.get("square"));
                params.put("square_range", CommonUtils.data1.get("square_range"));
                params.put("price", CommonUtils.data1.get("price"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                System.out.print(tid+"++++tttiiiddd111");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }, 500);
    }
    public void postData3(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("type", CommonUtils.data1.get("type"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("price", CommonUtils.data1.get("price"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }, 500);
    }
    public void postData4(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("numbers", CommonUtils.data1.get("numbers"));
                params.put("level", CommonUtils.data1.get("level"));
                params.put("salary_range", CommonUtils.data1.get("salary_range"));
                params.put("award_period", CommonUtils.data1.get("award_period"));
                params.put("sex_demand", CommonUtils.data1.get("sex_demand"));
                params.put("salary", CommonUtils.data1.get("salary"));
                params.put("experience", CommonUtils.data1.get("experience"));
                params.put("education", CommonUtils.data1.get("education"));
                params.put("nation", CommonUtils.data1.get("nation"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                System.out.print(tid+"++++tttiiiddd");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        // Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData5(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("type", CommonUtils.data1.get("type"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData6(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("abaility_estimate", CommonUtils.data1.get("abaility_estimate"));
                params.put("years_estimate", CommonUtils.data1.get("years_estimate"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("car_type", CommonUtils.data1.get("car_type"));
                params.put("car_brand", CommonUtils.data1.get("car_brand"));
                params.put("car_speed", CommonUtils.data1.get("car_speed"));
                params.put("price", CommonUtils.data1.get("price"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData7(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("level", CommonUtils.data1.get("level"));
                params.put("price", CommonUtils.data1.get("price"));
                params.put("type", CommonUtils.data1.get("type"));
                params.put("contact", CommonUtils.data1.get("contact"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData8(){
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid",CommonUtils.data1.get("userID"));
                params.put("fid", CommonUtils.data1.get("fId"));
                params.put("sortid", CommonUtils.data1.get("sortId"));
                params.put("region", CommonUtils.data1.get("region"));
                params.put("qq", CommonUtils.data1.get("qq"));
                params.put("telephone", CommonUtils.data1.get("telephone"));
                params.put("phone", CommonUtils.data1.get("phone"));
                params.put("title", CommonUtils.data1.get("title"));
                params.put("message", CommonUtils.data1.get("message"));
                params.put("tradeno", CommonUtils.tradeNo);
                switch(CommonUtils.data1.get("fId").toString()){
                    case "107":
                        params.put("group", CommonUtils.data1.get("group"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "44":
                        params.put("group", CommonUtils.data1.get("group"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("price", CommonUtils.data1.get("price"));
                        params.put("contact", CommonUtils.data1.get("contact"));
                        break;
                    case "48":
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("age", CommonUtils.data1.get("age"));
                        params.put("sex", CommonUtils.data1.get("sex"));
                        params.put("name", CommonUtils.data1.get("name"));
                        break;
                    case "74":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("period", CommonUtils.data1.get("period"));
                        params.put("price", CommonUtils.data1.get("price"));
                        params.put("group", CommonUtils.data1.get("group"));
                        break;
                    case "92":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("source", CommonUtils.data1.get("source"));
                        params.put("award_method", CommonUtils.data1.get("award_method"));
                        break;
                    case "94":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("company", CommonUtils.data1.get("company"));
                        params.put("price", CommonUtils.data1.get("price"));
                        break;
                    case "83":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("period", CommonUtils.data1.get("period"));
                        params.put("group", CommonUtils.data1.get("group"));
                        break;
                    case "50":
                        params.put("contact", CommonUtils.data1.get("contact"));
                        params.put("type", CommonUtils.data1.get("type"));
                        params.put("order", CommonUtils.data1.get("order"));
                        break;
                }
                String url = "news/post";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        CommonUtils.dismissProgress(progressDialog);
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    private void uploadUserPhoto(long _tid) {
        if (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0] == null) {
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChargePostActivity.this, RoomDetailActivity.class);
            String fid = CommonUtils.data1.get("fId").toString();
            String sortid = CommonUtils.data1.get("sortId").toString();
            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
            /*
            if(CommonUtils.share_bmp.getByteCount()>23000) {
                if (CommonUtils.share_bmp != null && !CommonUtils.share_bmp.isRecycled()) {
                    CommonUtils.share_bmp = Bitmap.createScaledBitmap(CommonUtils.share_bmp, 32, 32, true);
                }
            }
            */
            String url = APIManager.User_URL+"news/paper/"+Long.toString(_tid);
            intent.putExtra("fid", fid);
            intent.putExtra("sortid", sortid);
            intent.putExtra("newsId", Long.toString(_tid));
            intent.putExtra("title", CommonUtils.data1.get("title").toString());
            intent.putExtra("desc", CommonUtils.data1.get("message").toString());
            intent.putExtra("url", url);
            finish();
            ChargePostActivity.this.startActivity(intent);
            return;
        }
        RequestParams params = new RequestParams();
        try {
            params.put("tid", String.valueOf(_tid));
            params.put("act", "upload");
            System.out.println(CommonUtils.mFiles.length);
            //params.put("liangyao_user_photo", new File(selectedImagePath), "image/png");
            for (int i = 0; i < CommonUtils.mFiles.length; i++) {
                params.put("file["+i+"]", CommonUtils.mFiles[i]);
                //params.put("file[]", CommonUtils.mFiles[i], "image/png");
                System.out.println(i + "::" + CommonUtils.mFiles[i].getAbsolutePath());
            }

            //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
            String url = APIManager.Ucenter_URL;
            System.out.println(url + "iimmgg:" + _tid);
            APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChargePostActivity.this, RoomDetailActivity.class);
                    String fid = CommonUtils.data1.get("fId").toString();
                    String sortid = CommonUtils.data1.get("sortId").toString();
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    if(!CommonUtils.mFiles[0].getAbsolutePath().equals(""))
                        CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(CommonUtils.mFiles[0].getAbsolutePath());
                    if(CommonUtils.share_bmp==null)
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    String url = APIManager.User_URL+"news/paper/"+CommonUtils.data1.get("tid").toString();
                    intent.putExtra("fid", fid);
                    intent.putExtra("sortid", sortid);
                    intent.putExtra("newsId", CommonUtils.data1.get("tid").toString());
                    intent.putExtra("title", CommonUtils.data1.get("title").toString());
                    intent.putExtra("desc", CommonUtils.data1.get("message").toString());
                    intent.putExtra("url", url);
                    for(int i=0;i<CommonUtils.mFiles.length;i++){
                        if(!(CommonUtils.mFiles[i].equals(null) || CommonUtils.mFiles[i].equals("")) && CommonUtils.mFiles[i].exists()){
                            CommonUtils.mFiles[i].delete();
                        }
                    }
                    finish();
                    ChargePostActivity.this.startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println("img01+" + errorResponse);
                    //CommonUtils.dismissProgress(progressDialog);
                    //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Intent intent = new Intent(ChargePostActivity.this, RoomDetailActivity.class);
                    String fid = CommonUtils.data1.get("fId").toString();
                    String sortid = CommonUtils.data1.get("sortId").toString();
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    if(!CommonUtils.mFiles[0].getAbsolutePath().equals(""))
                        CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(CommonUtils.mFiles[0].getAbsolutePath());
                    if(CommonUtils.share_bmp==null)
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    String url = APIManager.User_URL+"news/paper/"+CommonUtils.data1.get("tid").toString();
                    intent.putExtra("fid", fid);
                    intent.putExtra("sortid", sortid);
                    intent.putExtra("newsId", CommonUtils.data1.get("tid").toString());
                    intent.putExtra("title", CommonUtils.data1.get("title").toString());
                    intent.putExtra("desc", CommonUtils.data1.get("message").toString());
                    intent.putExtra("url", url);
                    for(int i=0;i<CommonUtils.mFiles.length;i++){
                        if(!(CommonUtils.mFiles[i].equals(null) || CommonUtils.mFiles[i].equals("")) && CommonUtils.mFiles[i].exists()){
                            CommonUtils.mFiles[i].delete();
                        }
                    }
                    finish();
                    ChargePostActivity.this.startActivity(intent);
                }
            });
        }catch (Exception e){

        }
        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}
    }
    public void gotoUpdate() {
        Intent intent = new Intent(ChargePostActivity.this, UpdateActivity.class);
        intent.putExtra("fid", fId);
        intent.putExtra("tid", tId);
        intent.putExtra("sortid", sortId);
        pActivity.startChildActivity("update", intent);
    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public void onBackPressed() {
        //this.getParent().getParent().onBackPressed();
        //Intent intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
        //HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
        //HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        //Intent intent = new Intent(ChargePostActivity.this, UserNewsActivity.class);
        //pActivity.startChildActivity("news", intent);
        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChargePostActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}