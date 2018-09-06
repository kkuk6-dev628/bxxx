package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.SearchItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DictionaryUtils;
import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public class FullSearchActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = FullSearchActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private int showAction;
    private ListView lstSearch;
    public SearchItemListAdapter searchItemListAdapter;
    public ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText editSearchTxt;
    private String keyword = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        showAction = 0;
        setContentView(R.layout.activity_full_search);
        editSearchTxt = (EditText) findViewById(R.id.edit_search_title);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) FullSearchActivity.this.getParent();
        CommonUtils.customActionBar(mContext, this, false, "");
        RelativeLayout rltBack = (RelativeLayout) findViewById(R.id.rlt_back);
        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullSearchActivity.this, HomeActivity.class);
                pActivity.startChildActivity("home", intent);
            }
        });
        lstSearch = (ListView) findViewById(R.id.lst_full_search_content);
        searchItemListAdapter = new SearchItemListAdapter(this);
        searchItemListAdapter.setListItems(searchItems);
        lstSearch.setAdapter(searchItemListAdapter);
        getSearchItems();
        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem searchItem = (SearchItem) searchItemListAdapter.getItem(position);
                Long long_id = searchItem.getmSearchID();
                Intent intent = new Intent(FullSearchActivity.this, RoomDetailActivity.class);
                String fid = searchItem.getmFid();
                String sortid = searchItem.getmSortid();
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", sortid);
                intent.putExtra("newsId", Long.toString(long_id));
                intent.putExtra("title", searchItem.getmDesc());
                intent.putExtra("desc", searchItem.getmTitle01()+" "+searchItem.getmTitle02()+" "+searchItem.getmTitle03());
                String img = searchItem.getmThumbnail();
                CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                if(!img.equals(""))
                    CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                if(CommonUtils.share_bmp==null)
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                FullSearchActivity.this.startActivity(intent);
                /*
                Intent intent = new Intent(SearchActivity.this, RoomDetailActivity.class);
                String fid = searchItem.getmFid();
                String sortid = searchItem.getmSortid();
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", sortid);
                intent.putExtra("newsId", Long.toString(long_id));
                pActivity.startChildActivity("room_detail", intent);
                */
            }
        });

        lstSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getSearchItems();
                        isLoadMore = false;
                    }
                }
            }
        });
        RelativeLayout rltSearch = (RelativeLayout)findViewById(R.id.rlt_search);
        rltSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = ((TextView) findViewById(R.id.edit_search_title)).getText().toString();
                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                lyt_search_panel.removeAllViews();
                if(!keyword.equals("")){
                    //getSearchKey();
                    mIntPage = 1;
                    searchItemListAdapter.clearItems();
                    getSearchItems();
                }else{
                    Toast.makeText(mContext, "请输入搜索字", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSearchItems() {
        CommonUtils.hideKeyboard(FullSearchActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, FullSearchActivity.this);
                RequestParams params = new RequestParams();
                //params.put("fid", fid);
                params.put("page", mIntPage);
                //if(selectedVTab!=null && selectedVTab!=-1) {
                //    params.put(selectedSTab, selectedVTab);
                //}
                params.put("keyword", keyword);
                String url = "news/list";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                JSONArray list = response.getJSONArray("ret");
                                if((list==null || list.length()<1) && mIntPage==1){
                                    isLoadMore = false;
                                    ((LinearLayout)FullSearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
                                    return;
                                }else if(list.length()<8){
                                    isLoadMore = false;
                                }else{
                                    isLoadMore = true;
                                }
                                ((LinearLayout)FullSearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.GONE);
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String tid = item.optString("tid");
                                    String fid = item.optString("fid");
                                    String sortid = item.optString("sortid");
                                    DictionaryUtils dictionaryUtils = new DictionaryUtils();
                                    dictionaryUtils.setProperty(item);
                                    String img_url = "";
                                    if(item.optJSONObject("fields")!=null && item.optJSONObject("fields").optJSONObject("picture")!=null && !item.optJSONObject("fields").optJSONObject("picture").optString("url").equals("")){
                                        img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
                                    }
                                    System.out.println("img"+i+img_url);
                                    String desc = item.optString("title");
                                    String txt_home_favor_price = dictionaryUtils.getProperty("txt_home_favor_price");
                                    System.out.println("price"+i+txt_home_favor_price);
                                    String property01 = dictionaryUtils.getProperty("txt_property1");
                                    String property02 = dictionaryUtils.getProperty("txt_property2");
                                    String property03 = dictionaryUtils.getProperty("txt_property3");
                                    String poststick = item.optString("poststick");
                                    if(!tid.equals("")) {
                                        searchItemListAdapter.addItem(new SearchItem(
                                                Long.parseLong(tid), img_url, desc, dictionaryUtils, item));
//                                        searchItemListAdapter.addItem(new SearchItem(
//                                                Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, sortid, poststick, "", ""));
                                    }
                                }
                                mIntPage++;
                            } else {
                                isLoadMore = false;
                                ((LinearLayout)FullSearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
                                if (mIntPage == 1) {
                                    searchItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            searchItemListAdapter.notifyDataSetChanged();
                            lstSearch.invalidateViews();
                            //CommonUtils.dismissProgress(progressDialog);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5);
    }

    @Override
    public void onBackPressed() {
        //this.getParent().getParent().onBackPressed();
        Intent intent = new Intent(FullSearchActivity.this, HomeActivity.class);
        pActivity.startChildActivity("home", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(FullSearchActivity.this, HomeActivity.class);
            pActivity.startChildActivity("home", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
