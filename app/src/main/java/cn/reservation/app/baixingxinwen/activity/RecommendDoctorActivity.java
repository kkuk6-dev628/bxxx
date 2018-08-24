package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.RecommendDoctorItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cz.msebera.android.httpclient.Header;

public class RecommendDoctorActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    private ListView lstDoctor;
    public ArrayList<DoctorItem> doctorItems = new ArrayList<DoctorItem>();
    RecommendDoctorItemListAdapter recommendDoctorItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText mEditKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_doctor);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) RecommendDoctorActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.recommend_doctor));

        mEditKeyword = (EditText) findViewById(R.id.search_keyword);
        mEditKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mIntPage = 1;
                    recommendDoctorItemListAdapter.clearItems();
                    getDoctors();
                    return true;
                }
                return false;
            }
        });

        lstDoctor = (ListView) findViewById(R.id.lst_recommend_doctor);
        recommendDoctorItemListAdapter = new RecommendDoctorItemListAdapter(this);
        recommendDoctorItemListAdapter.setListItems(doctorItems);
        lstDoctor.setAdapter(recommendDoctorItemListAdapter);

        lstDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorItem doctorItem = (DoctorItem) recommendDoctorItemListAdapter.getItem(position);
                gotoDoctorView(doctorItem);
            }
        });
        lstDoctor.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getDoctors();
                        isLoadMore = false;
                    }
                }
            }
        });
        getDoctors();
    }

    private void getDoctors() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, RecommendDoctorActivity.this);
                RequestParams params = new RequestParams();
                params.put("page", mIntPage);
                params.put("lang", CommonUtils.mIntLang);
                params.put("keyword", mEditKeyword.getText().toString());

                String url = "getrecommend";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 0) {
                                isLoadMore = false;
                                if (mIntPage == 1) {
                                    recommendDoctorItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            } else {
                                isLoadMore = response.getBoolean("hasmore");
                                JSONArray list = response.getJSONArray("list");
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    recommendDoctorItemListAdapter.addItem(new DoctorItem(
                                            (long)item.getInt("CourseNo"), (long)item.getInt("WHospNo"), (long)item.getInt("HosNo"),
                                            item.getString("Picture"), item.getString("CourseName"), item.getString("HospName"),
                                            item.getString("HosName"), item.getString("Title"), item.getString("Price"),
                                            item.getString("HospTel"), 5, true));
                                }
                                mIntPage++;
                            }
                            recommendDoctorItemListAdapter.notifyDataSetChanged();
                            lstDoctor.invalidateViews();
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

    @SuppressWarnings("deprecation")
    private void gotoDoctorView(DoctorItem doctorItem) {
        TabActivity tabActivity = (TabActivity) RecommendDoctorActivity.this.getParent().getParent();
        Intent intent = new Intent(RecommendDoctorActivity.this, SummaryViewActivity.class);
        intent.putExtra("Doctor", doctorItem);
        tabActivity.startActivity(intent);
        tabActivity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
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
    protected void onResume() {
        if (!mEditKeyword.getText().toString().equals("")) {
            mEditKeyword.setText("");
            isLoadMore = false;
            mIntPage = 1;
            recommendDoctorItemListAdapter.clearItems();
            getDoctors();
        }
        super.onResume();
    }
}
