package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.alipay.PayResult;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.NewsItem;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class ChargeActivitySet extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = ChargeActivitySet.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String fId;
    private String tId;
    private String sortId;
    private String days;
    private String price;
    private IWXAPI api;
    private PayTask alipay;
    private String oid;
    private static final int SDK_PAY_FLAG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_set);
        api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP_ID);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) ChargeActivitySet.this.getParent();
        Intent intent = getIntent();
        fId = (String) intent.getSerializableExtra("fid");
        sortId = (String) intent.getSerializableExtra("sortid");
        tId = (String) intent.getSerializableExtra("tid");
        CommonUtils.customActionBar(mContext, this, true, "置顶");
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
        getPricesTable();
        days = "1";
        getPrices();
        RelativeLayout rlt_charge_plus = (RelativeLayout)findViewById(R.id.rlt_charge_plus);
        rlt_charge_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = Integer.parseInt(days);
                cnt++;
                days = String.valueOf(cnt);
                ((TextView)findViewById(R.id.txt_date_val)).setText(days);
                getPrices();
            }
        });
        RelativeLayout rlt_charge_minus = (RelativeLayout)findViewById(R.id.rlt_charge_minus);
        rlt_charge_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = Integer.parseInt(days);
                cnt--;
                if(cnt<1){
                    Toast.makeText(mContext, "置顶天数的最小值是1", Toast.LENGTH_SHORT).show();
                    return;
                }
                days = String.valueOf(cnt);
                ((TextView)findViewById(R.id.txt_date_val)).setText(days);
                getPrices();
            }
        });
        RelativeLayout rlt_charge_term_next = (RelativeLayout)findViewById(R.id.rlt_charge_term_next);
        rlt_charge_term_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) findViewById(R.id.zhifubao_chk)).isChecked()) {
                    createAlipayOrder();
                }else if(((CheckBox) findViewById(R.id.weixin_chk)).isChecked()){
                    createWeixinOrder();
                }
            }
        });
    }
    private void getPricesTable() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivitySet.this);
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {
                    //params.put("uid", CommonUtils.userInfo.getUserID());
                    params.put("days", 0);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("ccc++"+CommonUtils.userInfo.getUserID());
                String url = "news/movetop/price";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                System.out.println("pay1:"+response);
                                JSONArray list = response.getJSONArray("ret");
                                if (list == null) {
                                    CommonUtils.dismissProgress(progressDialog);
                                    return;
                                }
                                initPriceTable(list);
                            } else {

                            }
                            CommonUtils.dismissProgress(progressDialog);

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
    private void getPrices() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivitySet.this);
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {
                    //params.put("uid", CommonUtils.userInfo.getUserID());
                    params.put("days", days);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("ccc++"+CommonUtils.userInfo.getUserID());
                String url = "news/movetop/price";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                System.out.println("pay:"+response);
                                if(days=="0") {
                                    JSONArray list = response.getJSONArray("ret");
                                    if (list == null) {
                                        CommonUtils.dismissProgress(progressDialog);
                                        return;
                                    }
                                    initPriceTable(list);
                                }else{
                                    price = response.optString("ret");
                                    ((TextView)findViewById(R.id.txt_pay_price)).setText("￥"+price);
                                }

                            } else {

                            }
                            CommonUtils.dismissProgress(progressDialog);

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
    private void createOrder() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivitySet.this);
                RequestParams params = new RequestParams();
                if(CommonUtils.isLogin) {
                    params.put("uid", CommonUtils.userInfo.getUserID());
                    params.put("tid", tId);
                    params.put("days",days);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "news/movetop/order";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                System.out.println("pay:"+response);
                                oid = response.optJSONObject("ret").optString("oid");
                            } else {

                            }
                            CommonUtils.dismissProgress(progressDialog);
                            Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                            //gotoUpdate();
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
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivitySet.this);
                params.put("days",days);
                params.put("type","move");
                params.put("tid", tId);
                params.put("uid", CommonUtils.userInfo.getUid());
                //params.put("amount",1);
                System.out.println("ccc++"+CommonUtils.userInfo.getUserID());
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
                                    alipay = new PayTask(ChargeActivitySet.this);
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
                        Toast.makeText(ChargeActivitySet.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HomeActivity.homeActivity, UserNewsActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("user_news", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知
                        Toast.makeText(ChargeActivitySet.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HomeActivity.homeActivity, UserNewsActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("user_news", intent);
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
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargeActivitySet.this);
                params.put("days",days);
                params.put("type","move");
                params.put("tid", tId);
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
                            CommonUtils.pay_type = 3;
                            CommonUtils.tradeNo = tradeNo;
                            ChargeActivitySet.this.finish();
                            Intent intent = new Intent(HomeActivity.homeActivity, UserNewsActivity.class);
                            HomeGroupActivity.HomeGroupStack.startChildActivity("user_news", intent);
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
    private void initPriceTable(JSONArray list)
    {
        LinearLayout lyt_charge_parent = (LinearLayout) findViewById(R.id.lyt_charge_parent);
        lyt_charge_parent.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list.length(); i++)
        {
            //R.layout.search_item;
            try {
                JSONObject item = list.getJSONObject(i);
                View rowView1 = inflater.inflate(R.layout.charge_term_item, null);
                ((TextView) rowView1.findViewById(R.id.txt_charge_term_value)).setText(item.optString("text"));
                if(item.optString("unit").equals("0"))
                    ((TextView)rowView1.findViewById(R.id.txt_charge_price_value)).setText(item.optString("price")+"元/天");
                else
                    ((TextView)rowView1.findViewById(R.id.txt_charge_price_value)).setText(item.optString("price")+"元");
                RelativeLayout rlt_charge_price_vip = (RelativeLayout)rowView1.findViewById((R.id.rlt_charge_price_vip));
                if(item.optString("discount").equals("100")){
                    rlt_charge_price_vip.setVisibility(View.INVISIBLE);
                }else {
                    rlt_charge_price_vip.setVisibility(View.VISIBLE);
                    ((TextView) rowView1.findViewById(R.id.txt_charge_price_vip)).setText(item.optString("discount") + "折");
                }
                CheckBox term_chk = (CheckBox) rowView1.findViewById(R.id.term_chk);
                term_chk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout lyt_charge_parent = (LinearLayout) findViewById(R.id.lyt_charge_parent);
                        for(int j=0;j<lyt_charge_parent.getChildCount();j++) {
                            ((CheckBox) lyt_charge_parent.getChildAt(j).findViewById(R.id.term_chk)).setChecked(false);
                        }
                        ((CheckBox) view).setChecked(true);
                        int m = 0;
                        for(int j=0;j<lyt_charge_parent.getChildCount();j++) {
                            if(((CheckBox)lyt_charge_parent.getChildAt(j).findViewById(R.id.term_chk)).isChecked()){
                                m = j;
                            }
                        }
                        switch(m){
                            case 0 :
                                days = "1";
                                break;
                            case 1 :
                                days = "4";
                                break;
                            case 2 :
                                days = "8";
                                break;
                            case 3 :
                                days = "16";
                                break;
                            case 4 :
                                days = "90";
                                break;
                            case 5 :
                                days = "180";
                                break;
                            case 6 :
                                days = "365";
                                break;
                        }
                        ((TextView)findViewById(R.id.txt_date_val)).setText(days);
                        getPrices();
                    }
                });
                if(i==0) term_chk.setChecked(true);
                lyt_charge_parent.addView(rowView1, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void gotoUpdate() {
        Intent intent = new Intent(ChargeActivitySet.this, UpdateActivity.class);
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
        /*
        Intent intent = new Intent(HomeActivity.homeActivity, UserNewsActivity.class);
        HomeGroupActivity.HomeGroupStack.startChildActivity("user_news", intent);
        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        TabHostActivity.tabWidget.setCurrentTab(3);
        TabHostActivity.tabs.setCurrentTab(3);
        */
        this.finish();
        //Intent intent = new Intent(ChargeActivitySet.this, UserNewsActivity.class);
        //pActivity.startChildActivity("news", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChargeActivitySet.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
