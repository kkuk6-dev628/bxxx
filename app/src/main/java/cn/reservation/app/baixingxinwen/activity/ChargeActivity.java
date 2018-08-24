package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.Map;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.alipay.PayResult;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class ChargeActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = ChargeActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    private String coins = "1000";
    public AnimatedActivity pActivity;
    private IWXAPI api;
    private PayTask alipay;
    private static final int SDK_PAY_FLAG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP_ID);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) ChargeActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "百姓币充值");
        CheckBox weixin_chk = (CheckBox) findViewById(R.id.weixin_chk);
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
        RelativeLayout rlt_charge_next = (RelativeLayout) findViewById(R.id.rlt_charge_next);
        rlt_charge_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coins = ((EditText)findViewById(R.id.edit_charge_val)).getText().toString();
                if(!coins.equals("") && (Float.parseFloat(coins))>0.0f ) {
                    //setCharge();
                    if(((CheckBox) findViewById(R.id.zhifubao_chk)).isChecked()){
                        createAlipayOrder();
                    }else if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()) {
                        createWeixinOrder();
                    }
                }else{
                    Toast.makeText(mContext, "请输入充值金额", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCharge() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivity.this);
                RequestParams params = new RequestParams();
                if(!CommonUtils.isLogin){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", CommonUtils.userInfo.getUid());
                coins = ((EditText)findViewById(R.id.edit_charge_val)).getText().toString();
                params.put("coins", coins);
                String url = "credit/charge";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response.optInt("code") == 1) {
                            System.out.println(response);


                        } else {

                        }
                        CommonUtils.dismissProgress(progressDialog);
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
    private void createAlipayOrder(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {

                }else{
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivity.this);
                params.put("type","coin");
                coins = ((EditText)findViewById(R.id.edit_charge_val)).getText().toString();
                params.put("coins", coins);
                params.put("uid", CommonUtils.userInfo.getUid());
                //params.put("amount",1);
                System.out.println("coins++"+CommonUtils.userInfo.getUserID());
                String url = "payment/alipay";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println("alipay response"+response);
                            //isLoadMore = response.getBoolean("hasmore");

                            //req.appId = "wxf8b4f85f3a794e77";  // ²âÊÔÓÃappId

                            String tradeNo		= response.getString("tradeno");
                            CommonUtils.tradeNo = tradeNo;
                            CommonUtils.orderString = response.optString("orderstring");

                            CommonUtils.dismissProgress(progressDialog);
                            Runnable payRunnable = new Runnable() {
                                @Override    public void run() {
                                    alipay = new PayTask(ChargeActivity.this);
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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知
                        Toast.makeText(ChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知
                        Toast.makeText(ChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                    }
                    break;
                }
                /*
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayDemoActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayDemoActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivity.this);
                params.put("type","coin");
                coins = ((EditText)findViewById(R.id.edit_charge_val)).getText().toString();
                params.put("coins", coins);
                params.put("uid", CommonUtils.userInfo.getUid());
                //params.put("amount",1);
                System.out.println("ccc++"+CommonUtils.userInfo.getUserID());
                String url = "payment/wechat";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        CommonUtils.dismissProgress(progressDialog);
                        try {
                            System.out.println("weixin response"+response);
                            //isLoadMore = response.getBoolean("hasmore");

                            PayReq req = new PayReq();

                            req.appId			= response.getString("appid");
                            req.partnerId		= response.getString("partnerid");
                            req.prepayId		= response.getString("prepayid");
                            req.nonceStr		= response.getString("noncestr");
                            req.timeStamp		= response.getString("timestamp");
                            req.packageValue	= response.getString("package");
                            req.sign			= response.getString("sign");
                            req.extData			= "app data"; // optional
                            String tradeNo		= response.getString("tradeno");
                            CommonUtils.pay_type = 4;
                            CommonUtils.tradeNo = tradeNo;
                            ChargeActivity.this.finish();
                            Intent intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
                            HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
                            HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            TabHostActivity.tabWidget.setCurrentTab(3);
                            TabHostActivity.tabs.setCurrentTab(3);
                            //Toast.makeText(mContext, req.appId	+ tradeNo, Toast.LENGTH_SHORT).show();
                            api.sendReq(req);
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
    @Override
    public void onBackPressed() {
        //this.getParent().getParent().onBackPressed();
        //Intent intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
        //HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
        //HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        ChargeActivity.this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChargeActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
