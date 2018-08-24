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
import android.widget.TextView;
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

import java.lang.reflect.Member;
import java.util.Map;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.alipay.PayResult;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HistoryItem;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class MemberActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = MemberActivity.class.getSimpleName();
    private IWXAPI api;
    private PayTask alipay;
    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private int member_level;
    private int month_time;
    private String userID;
    private String pay_method;
    private double base_price;
    private int cid;
    private int price;
    private static final int SDK_PAY_FLAG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP_ID);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) MemberActivity.this.getParent();
        member_level = 26;
        month_time = 6;
        pay_method = "zhifubao";
        cid = 0;
        price = 0;
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        CommonUtils.customActionBar(mContext, this, false, "");
        final TextView txt_diamond_member = (TextView) findViewById(R.id.txt_diamond_member);
        final TextView txt_vip_member = (TextView) findViewById(R.id.txt_vip_member);
        final TextView txt_member_desc = (TextView) findViewById(R.id.txt_member_desc);
        txt_diamond_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member_level=26;
                ImageView img_mark =(ImageView) findViewById(R.id.img_member_mark);
                img_mark.setImageResource(R.drawable.ic_diamond);
                txt_diamond_member.setTextColor(getResources().getColor(R.color.colorHomeBackground));
                txt_vip_member.setTextColor(getResources().getColor(R.color.colorLineLight));
                txt_member_desc.setText("钻石会员 享受20条房源信息发布总条数，全程系统自动刷新，无需手动值守");
                getPrice();
            }
        });

        txt_vip_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member_level=61;
                ImageView img_mark =(ImageView) findViewById(R.id.img_member_mark);
                img_mark.setImageResource(R.drawable.ic_vip);
                txt_diamond_member.setTextColor(getResources().getColor(R.color.colorLineLight));
                txt_vip_member.setTextColor(getResources().getColor(R.color.colorHomeBackground));
                txt_member_desc.setText("VIP会员  享受20条房源信息发布总条数");
                getPrice();
            }
        });
        CheckBox chk_member_term1 = (CheckBox) findViewById(R.id.chk_member_term1);
        chk_member_term1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month_time==1){
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(false);
                }else{
                    month_time=1;
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(false);
                    getPrice();
                }
            }
        });
        CheckBox chk_member_term2 = (CheckBox) findViewById(R.id.chk_member_term2);
        chk_member_term2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month_time==3){
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(false);
                }else{
                    month_time=3;
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(true);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(false);
                    getPrice();
                }
            }
        });
        CheckBox chk_member_term3 = (CheckBox) findViewById(R.id.chk_member_term3);
        chk_member_term3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month_time==6){
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(true);
                }else{
                    month_time=6;
                    ((CheckBox) findViewById(R.id.chk_member_term1)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term2)).setChecked(false);
                    ((CheckBox) findViewById(R.id.chk_member_term3)).setChecked(true);
                    getPrice();
                }
            }
        });
        CheckBox weixin_chk = (CheckBox) findViewById(R.id.weixin_chk);
        weixin_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()){
                    ((CheckBox) findViewById(R.id.zhifubao_chk)).setChecked(false);
                    pay_method = "weixin";
                    //getPrice();
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
                    pay_method = "zhifubao";
                    ((CheckBox) findViewById(R.id.weixin_chk)).setChecked(false);
                    //getPrice();
                }else{
                    ((CheckBox) findViewById(R.id.weixin_chk)).setChecked(true);
                }
            }
        });
        getPrice();
        RelativeLayout rlt_charge_next = (RelativeLayout) findViewById(R.id.rlt_charge_next);
        rlt_charge_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.zhifubao_chk)).isChecked()){
                    createAlipayOrder();
                }
                else if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()){
                    createWeixinOrder();
                }
            }
        });
        ImageView imgBack = (ImageView) findViewById(R.id.layout_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pActivity.finishChildActivity();
                /*
                Intent intent;
                intent = new Intent(RoomDetailActivity.this, HomeActivity.class);
                HomeGroupActivity.HomeGroupStack.startChildActivity("home", intent);
                HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                */
                /*
                Intent intent;
                intent = new Intent(RoomDetailActivity.this, SearchActivity.class);
                intent.putExtra("fid", fId);
                intent.putExtra("sortid", Integer.parseInt(sortId));
                pActivity.startChildActivity("activity_search", intent);
                */
            }
        });
    }
    private void getPrice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MemberActivity.this);
                RequestParams params = new RequestParams();
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("gid", member_level);
                params.put("months", month_time);
                String url = "group/price";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response.optInt("code") == 1) {
                            System.out.println(response);

                            base_price = response.optDouble("ret");
                            ((TextView)findViewById(R.id.txt_pay_price)).setText("￥"+base_price);//*month_time);
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
    private void createOrder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MemberActivity.this);
                RequestParams params = new RequestParams();
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                params.put("gid", member_level);
                params.put("months", month_time);
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
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MemberActivity.this);
                params.put("type","level");
                params.put("uid", CommonUtils.userInfo.getUid());
                params.put("gid", member_level);
                params.put("months", month_time);
                String url = "payment/alipay";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            CommonUtils.dismissProgress(progressDialog);
                            String tradeNo		= response.getString("tradeno");
                            CommonUtils.tradeNo = tradeNo;
                            CommonUtils.orderString = response.optString("orderstring");
                            CommonUtils.dismissProgress(progressDialog);
                            Runnable payRunnable = new Runnable() {
                                @Override    public void run() {
                                    alipay = new PayTask(MemberActivity.this);
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
        }, 50);
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
                        Toast.makeText(MemberActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MemberActivity.this, MeActivity.class);
                        pActivity.startChildActivity("me", intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知
                        Toast.makeText(MemberActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MemberActivity.this, MeActivity.class);
                        pActivity.startChildActivity("me", intent);
                        //MemberActivity.this.finish();
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
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MemberActivity.this);
                params.put("gid", member_level);
                params.put("months", month_time);
                params.put("type","level");
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
                            CommonUtils.pay_type = 2;
                            CommonUtils.tradeNo = tradeNo;
                            api.sendReq(req);
                            Intent intent = new Intent(MemberActivity.this, MeActivity.class);
                            pActivity.startChildActivity("me", intent);
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
    private void completeOrder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MemberActivity.this);
                RequestParams params = new RequestParams();
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                params.put("cid", cid);
                String url = "group/complete";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //try {
                            if (response.optInt("code") == 1) {
                                System.out.println(response);

                            }
                            CommonUtils.dismissProgress(progressDialog);
                        //} catch (JSONException e) {
                        //    CommonUtils.dismissProgress(progressDialog);
                        //    e.printStackTrace();
                        //}
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
        Intent intent = new Intent(MemberActivity.this, MeActivity.class);
        pActivity.startChildActivity("me", intent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MemberActivity.this, MeActivity.class);
            pActivity.startChildActivity("me", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
