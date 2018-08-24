package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.FamilyItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.FamilyMemberItem;
import cz.msebera.android.httpclient.Header;
@SuppressWarnings("deprecation")
public class AddFamilyActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    public Resources res;
    private Context mContext;
    public AnimatedActivity pActivity;

    private ListView lstFamily;
    private FamilyItemListAdapter mFamilyItemListAdapter;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);

        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) AddFamilyActivity.this.getParent();
        final TabActivity tabActivity = (TabActivity) AddFamilyActivity.this.getParent().getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.add_family));

        lstFamily = (ListView) findViewById(R.id.lst_family);
        lstFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FamilyMemberItem familyItem = (FamilyMemberItem) mFamilyItemListAdapter.getItem(i);
                Intent intent;
                if (familyItem.getmMemberID().equals("0")) {
                    intent = new Intent(tabActivity, InputFamilyInfoActivity.class);
                    tabActivity.startActivity(intent);
                    tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                } else {
                    intent = new Intent(AddFamilyActivity.this, FamilyInfoViewActivity.class);
                    intent.putExtra("FamilyItem", familyItem);
                    pActivity.startChildActivity("family_info_view", intent);
                }
            }
        });
        mFamilyItemListAdapter = new FamilyItemListAdapter(mContext);
        lstFamily.setAdapter(mFamilyItemListAdapter);
        loadFamily();

        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.reservation.app.baixingxinwen.add_family");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadFamily();
            }
        };
        registerReceiver(receiver, filter);
    }

    public void loadFamily() {

        mFamilyItemListAdapter.clearItems();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, AddFamilyActivity.this);
                RequestParams params = new RequestParams();
                params.put("userid", CommonUtils.userInfo.getUserID());
                params.put("token", CommonUtils.userInfo.getToken());

                String url = "get_family";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, res.getString(R.string.token_error), Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray list = response.getJSONArray("family");
                                for(int i=0; i < list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String name = item.getString("name");
                                    mFamilyItemListAdapter.addItem(new FamilyMemberItem(item.getString("id"), name,
                                            item.getInt("man_type")-1, item.getInt("sex"), item.getString("relation"), item.getString("identify"), false));
                                }
                                mFamilyItemListAdapter.addItem(new FamilyMemberItem("0", mContext.getString(R.string.add), 3, 0, "", "", false));
                                mFamilyItemListAdapter.notifyDataSetChanged();
                                CommonUtils.dismissProgress(progressDialog);
                            }
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
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            pActivity.finishChildActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                //super.onBackPressed();
                pActivity.finishChildActivity();
            } else {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
