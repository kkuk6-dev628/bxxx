package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.EnterpriseItemListAdapter;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.EnterpriseItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class EnterpriseListActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {
    private static String TAG = EnterpriseListActivity.class.getSimpleName();

    private Context mContext;
    AnimatedActivity pActivity;
    private Resources res;
    private ListView lstEnterprise;
    private String userID;
    public int mRegionID;
    private String mCatid;
    private boolean allsel;
    public ArrayList<EnterpriseItem> enterpriseItems = new ArrayList<EnterpriseItem>();
    public EnterpriseItemListAdapter enterpriseItemListAdapter;

    private ProgressHUD mProgressDialog;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    private String[] arrPhone;
    private String mStrKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        mContext = TabHostActivity.TabHostStack;
        pActivity = (AnimatedActivity) EnterpriseListActivity.this.getParent();
        res = mContext.getResources();

        Intent intent = getIntent();
        mRegionID = intent.getIntExtra("RegionID", 0);
        mCatid = intent.getStringExtra("order");
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        String enterprise = (String) intent.getSerializableExtra("enterprise");
        CommonUtils.customActionBar(mContext, this, true, enterprise);

        lstEnterprise = (ListView) findViewById(R.id.lst_enterprise);
        enterpriseItemListAdapter = new EnterpriseItemListAdapter(this);
        enterpriseItemListAdapter.setListItems(enterpriseItems);
        lstEnterprise.setAdapter(enterpriseItemListAdapter);
//        getEnterprise();
        lstEnterprise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("tel:" + arrPhone[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + arrPhone[position]));
                startActivity(intent);
                /*
                RelativeLayout rlt_enterprise_call = (RelativeLayout) findViewById(R.id.rlt_enterprise_call);
                rlt_enterprise_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                });
                */
            }
        });


        lstEnterprise.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lstEnterprise.invalidateViews();
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getEnterprise();
                        isLoadMore = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEnterprise();
    }

    private void getEnterprise() {
        final ProgressHUD progressDialog = ProgressHUD.show(pActivity, res.getString(R.string.processing), true, false, EnterpriseListActivity.this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", mIntPage);
        params.put("catid", mCatid);
        String url = "api/business/list";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                try {
                    CommonUtils.dismissProgress(progressDialog);
                    JSONObject response = res.body();
                    if (response != null && response.getInt("code") == 1) {
                        System.out.println(response);

                        JSONArray list = response.getJSONArray("ret");
                        if (list == null) {
                            return;
                        } else {
                            isLoadMore = list.length() >= 10;
                        }
                        arrPhone = new String[list.length()];
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String img_url = item.optString("avatar");
                            String name = item.optString("bname");
                            String desc = item.optString("description");
                            String phone = item.optString("phone");
                            String tid = item.optString("bid");
                            arrPhone[i] = phone;
                            enterpriseItemListAdapter.addItem(new EnterpriseItem(
                                    Long.parseLong(tid), img_url, name, desc, phone));
                        }
                        mIntPage++;
                    } else {
                        if (mIntPage == 1) {
                            enterpriseItemListAdapter.clearItems();
                        }
                        mIntPage = 1;
                    }
                    //if(enterpriseItemListAdapter!=null) {
                    enterpriseItemListAdapter.notifyDataSetChanged();
                    //}
                    lstEnterprise.invalidateViews();

                } catch (JSONException e) {
                    CommonUtils.dismissProgress(progressDialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onBackPressed() {
//        Intent intent;
//        intent = new Intent(EnterpriseListActivity.this, HomeActivity.class);
//        pActivity.startChildActivity("home_activity", intent);
        HomeGroupActivity.HomeGroupStack.finishChildActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent;
//            intent = new Intent(EnterpriseListActivity.this, HomeActivity.class);
//            pActivity.startChildActivity("home_activity", intent);
            HomeGroupActivity.HomeGroupStack.finishChildActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
