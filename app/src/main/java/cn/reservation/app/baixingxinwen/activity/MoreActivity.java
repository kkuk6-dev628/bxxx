package cn.reservation.app.baixingxinwen.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.ChargeDetailItemListAdapter;
import cn.reservation.app.baixingxinwen.utils.ChargeDetailItem;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class MoreActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    TextView txtNewsBadge;
    private int mIntPage=1;
    private BroadcastReceiver receiver;
    private ListView lstChargeDetail;

    ChargeDetailItemListAdapter chargeDetailItemListAdapter;
    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) MoreActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, false, "");

        lstChargeDetail = (ListView) findViewById(R.id.lst_credit_log);
        chargeDetailItemListAdapter = new ChargeDetailItemListAdapter(this);
        lstChargeDetail.setAdapter(chargeDetailItemListAdapter);

        RelativeLayout lytBack = (RelativeLayout) findViewById(R.id.layout_back);
        lytBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, MeActivity.class);
                pActivity.startChildActivity("charge", intent);
            }
        });
        RelativeLayout rltCharge = (RelativeLayout) findViewById(R.id.rlt_baixing_charge);
        rltCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MoreActivity.this, ChargeActivity.class);
                MoreActivity.this.startActivity(mainIntent);
                MoreActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        //getCreditLog();
    }

    private void getCreditLog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                if(!CommonUtils.isLogin){
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MoreActivity.this);

                params.put("uid", CommonUtils.userInfo.getUid());
                params.put("page", 1);
                String url = "credit/log";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.optInt("code") == 1) {
                                System.out.println(response);

                                JSONArray list = response.getJSONArray("ret");
                                if(list==null){
                                    CommonUtils.dismissProgress(progressDialog);
                                    return;
                                }
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String date = item.optString("dateline");
                                    String desc = item.optString("operation");
                                    String coin = item.optString("coin");
                                    String lid = item.optString("logid");
                                    chargeDetailItemListAdapter.addItem(new ChargeDetailItem(
                                            Long.parseLong(lid), desc, date, coin));
                                }
                                mIntPage++;
                            } else {
                                if (mIntPage == 1) {
                                    chargeDetailItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            if(chargeDetailItemListAdapter!=null) {
                                chargeDetailItemListAdapter.notifyDataSetChanged();
                            }
                            lstChargeDetail.invalidateViews();
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

    @Override
    public void onBackPressed() {
        pActivity.finishChildActivity();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pActivity.finishChildActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getCreditLog();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

}
