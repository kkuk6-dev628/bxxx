package cn.reservation.app.baixingxinwen.activity;

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

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.HealthArticleItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cz.msebera.android.httpclient.Header;

public class HealthArticleActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    private ListView lstArticle;
    HealthArticleItemListAdapter healthArticleItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText mEditKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_article);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) HealthArticleActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, res.getString(R.string.health_article));
        mEditKeyword = (EditText) findViewById(R.id.search_keyword);
        mEditKeyword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View arg0, boolean gotfocus)
            {
                // TODO Auto-generated method stub
                if(gotfocus) {
                    mEditKeyword.setCompoundDrawables(null, null, null, null);
                } else if(!gotfocus) {
                    if(mEditKeyword.getText().length()==0)
                        mEditKeyword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.navi_search, 0, 0, 0);
                }
            }
        });
        mEditKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mIntPage = 1;
                    healthArticleItemListAdapter.clearItems();
                    getArticles();
                    return true;
                }
                return false;
            }
        });

        lstArticle = (ListView) findViewById(R.id.lst_health_article);
        healthArticleItemListAdapter = new HealthArticleItemListAdapter(this);
        lstArticle.setAdapter(healthArticleItemListAdapter);

        lstArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HealthArticleItem healthArticleItem = (HealthArticleItem) healthArticleItemListAdapter.getItem(position);
                gotoArticleView(healthArticleItem);
            }
        });
        lstArticle.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getArticles();
                        isLoadMore = false;
                    }
                }
            }
        });
        getArticles();
    }

    private void getArticles() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HealthArticleActivity.this);
                RequestParams params = new RequestParams();
                params.put("page", mIntPage);
                params.put("lang", CommonUtils.mIntLang);
                params.put("keyword", mEditKeyword.getText().toString());

                String url = "get_health_article";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                isLoadMore = response.getBoolean("hasmore");
                                JSONArray list = response.getJSONArray("articles");
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    healthArticleItemListAdapter.addItem(new HealthArticleItem(
                                            item.getLong("id"), item.getString("img"), item.getString("title"), item.getString("contents")));
                                }
                                mIntPage++;
                            } else {
                                isLoadMore = false;
                                if (mIntPage == 1) {
                                    healthArticleItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            healthArticleItemListAdapter.notifyDataSetChanged();
                            lstArticle.invalidateViews();
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

    private void gotoArticleView(HealthArticleItem healthArticleItem) {
        Intent intent = new Intent(HealthArticleActivity.this, HealthArticleViewActivity.class);
        intent.putExtra("Article", healthArticleItem);
        pActivity.startChildActivity("health_article", intent);
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
}
