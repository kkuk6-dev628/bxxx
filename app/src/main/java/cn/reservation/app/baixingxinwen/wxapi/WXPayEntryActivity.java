package cn.reservation.app.baixingxinwen.wxapi;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.HomeActivity;
import cn.reservation.app.baixingxinwen.activity.HomeGroupActivity;
import cn.reservation.app.baixingxinwen.activity.MeActivity;
import cn.reservation.app.baixingxinwen.activity.MemberActivity;
import cn.reservation.app.baixingxinwen.activity.MoreActivity;
import cn.reservation.app.baixingxinwen.activity.RoomDetailActivity;
import cn.reservation.app.baixingxinwen.activity.TabHostActivity;
import cn.reservation.app.baixingxinwen.activity.UserNewsActivity;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AppConstant;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.theartofdev.edmodo.cropper.CropImage;
import com.walnutlabs.android.ProgressHUD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

public class WXPayEntryActivity  extends Activity implements IWXAPIEventHandler{

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    private ProgressHUD progressDg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP_ID);

        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        //Toast.makeText(WXPayEntryActivity.this, "onPayFinish, errCode = " + resp.errCode, Toast.LENGTH_SHORT).show();
        //        WXSuccess           = 0,    /**< 成功    */
        //        WXErrCodeCommon     = -1,   /**< 普通错误类型    */
        //        WXErrCodeUserCancel = -2,   /**< 用户点击取消并返回    */
        //        WXErrCodeSentFail   = -3,   /**< 发送失败    */
        //        WXErrCodeAuthDeny   = -4,   /**< 授权失败    */
        //        WXErrCodeUnsupport  = -5,   /**< 微信不支持    */

        int type = resp.getType();
        //Toast.makeText(WXPayEntryActivity.this, type+"---onPayFinish, errCode = " + resp.errCode+":"+CommonUtils.pay_type, Toast.LENGTH_SHORT).show();
        if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                switch (CommonUtils.pay_type) {
                    case 1: //post
                        switch(CommonUtils.data1.get("fId").toString()){
                            case "2":
                                postData1();
                                break;
                            case "93":
                                postData3();
                                break;
                            case "38": //招兼职信息发布
                                postData4();
                                break;
                            case "42": //便民服务信息发布
                                postData5();
                                break;
                            case "39": //车辆出租信息发布
                                postData6();
                                break;
                            case "40": //物品出售信息发布
                                postData7();
                                break;
                            case "107": //打折促销信息发布
                                postData8();
                                break;
                            case "44":
                                postData8();
                                break;
                            case "48": //婚姻交友信息发布
                                postData8();
                                break;
                            case "74": //教育培训信息发布
                                postData8();
                                break;
                            case "94":
                                postData8();
                                break;
                            case "83": //出国资讯信息发布
                                postData8();
                                break;
                            case "92": //宠物天地信息发布
                                postData8();
                                break;
                            case "50":
                                postData8();
                                break;
                        }

                        break;

                    case 2:
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HomeActivity.homeActivity, MeActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("me", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                        break;
                    case 3:
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                        intent = new Intent(HomeActivity.homeActivity, UserNewsActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("user_news", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                        break;
                    case 4:
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                        intent = new Intent(HomeActivity.homeActivity, MoreActivity.class);
                        HomeGroupActivity.HomeGroupStack.startChildActivity("more", intent);
                        HomeGroupActivity.HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        TabHostActivity.tabWidget.setCurrentTab(3);
                        TabHostActivity.tabs.setCurrentTab(3);
                        break;
                }

            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                CommonUtils.pay_type = -1;
                Toast.makeText(WXPayEntryActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                finish();
            } else{
                CommonUtils.pay_type = -1;
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void goNextActivity(String id) {
        /*
        Intent intent = new Intent(WXPayEntryActivity.this, PrintPaymentSuccessActivity.class);

        intent.putExtra("order_id", id);
        switch (CommonUtils.pay_type) {
            case 1:
                intent.putExtra("pay_type", 1);
                break;

            case 2:
                intent.putExtra("pay_type", 2);
                break;

            case 3:
                intent.putExtra("pay_type", 3);
                break;
        }

        WXPayEntryActivity.this.startActivity(intent);
        WXPayEntryActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        */
    }
    public void postData1(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }, 500);
    }
    public void postData3(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
                RequestParams params = new RequestParams();
                params.put("uid", CommonUtils.data1.get("userID"));
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }, 500);
    }
    public void postData4(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                System.out.print(tid+"++++tttiiiddd");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                       // Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData5(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData6(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData7(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
                String url = "news/post";
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    public void postData8(){
        if(!CommonUtils.isLogin){
            Toast.makeText(WXPayEntryActivity.this, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChargePostActivity.this);
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
                if (!CommonUtils.data1.get("tid").equals("")) {
                    params.put("action", "update");
                    params.put("tid", CommonUtils.data1.get("tid"));
                }
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
                        params.put("education_price", CommonUtils.data1.get("education_price"));
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
                APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            //CommonUtils.dismissProgress(progressDialog);
                            System.out.println(response);
                            if (response.getInt("code") == 1) {
                                long tid = response.getLong("tid");
                                //JSONObject list = response.getJSONObject("option");
                                CommonUtils.data1.put("tid", Long.toString(tid));
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                                uploadUserPhoto(tid);
                            }else{
                                Toast.makeText(WXPayEntryActivity.this, response.optString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        //Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 500);
    }
    private void uploadUserPhoto(long _tid) {
        if (CommonUtils.mFiles == null || CommonUtils.mFiles.length<1 || CommonUtils.mFiles[0]==null) {
            //Toast.makeText(WXPayEntryActivity.this, "发布成功。", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(WXPayEntryActivity.this, RoomDetailActivity.class);
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
            WXPayEntryActivity.this.startActivity(intent);
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
            APIManager.post(WXPayEntryActivity.this, url, params, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //Toast.makeText(WXPayEntryActivity.this, "发布成功。", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WXPayEntryActivity.this, RoomDetailActivity.class);
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
                    WXPayEntryActivity.this.startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    System.out.println("img01+" + errorResponse);
                    //CommonUtils.dismissProgress(progressDialog);
                    //Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Intent intent = new Intent(WXPayEntryActivity.this, RoomDetailActivity.class);
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
                    WXPayEntryActivity.this.startActivity(intent);
                }
            });
        }catch (Exception e){

        }
        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}
    }
}
