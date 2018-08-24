package cn.reservation.app.baixingxinwen.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.LinearLayout.LayoutParams;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.SearchItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.BasePopupWindow;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DictionaryUtils;
import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

@SuppressWarnings("deprecation")
public class SearchActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener, View.OnClickListener {
    private static String TAG = SearchActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private int showAction;
    private View footer;// 底部布局
    private ListView lstSearch;
    public SearchItemListAdapter searchItemListAdapter;
    public ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText editSearchTxt;
    private String fid;
    private String postItem;
    private String sortid;
    private String[] option_ids = {"", "", "", ""};
    private JSONObject[] arrayObj;
    private String selectedSTab = "";
    private Dictionary selectedKeyTab;
    private String selectedVTab = "";
    private String keyword = "";
    private String min_price = "";
    private String max_price = "";
    private String price = "";
    private int preLast;
    BasePopupWindow popUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        showAction = 0;
        selectedKeyTab = new Hashtable();
        setContentView(R.layout.activity_search);
        postItem = (String) intent.getSerializableExtra("PostItem");
        pActivity = (AnimatedActivity) SearchActivity.this.getParent();
        editSearchTxt = (EditText) findViewById(R.id.edit_search_title);
        preLast = 4;
        findViewById(R.id.rlt_post).setOnClickListener(this);
        if(postItem!=null){
            switch(postItem){
                case "1":
                    //setContentView(R.layout.activity_search1);
                    editSearchTxt.setHint("搜索房屋出售");
                    fid = "2";
                    sortid = "1";
                    break;
                case "1a":
                    editSearchTxt.setHint("搜索房屋求购");
                    fid = "2";
                    sortid = "56";
                    break;
                case "2":
                    //setContentView(R.layout.activity_search2);
                    editSearchTxt.setHint("搜索房屋出租");
                    fid = "2";
                    sortid = "4";
                    break;
                case "2a":
                    editSearchTxt.setHint("搜索房屋求租");
                    fid = "2";
                    sortid = "9";
                    break;
                case "3":
                    //setContentView(R.layout.activity_search3);
                    editSearchTxt.setHint("搜索店铺出兑");
                    fid = "93";
                    sortid = "33";
                    break;
                case "4":
                    //setContentView(R.layout.activity_search4);
                    editSearchTxt.setHint("搜索招聘信息");
                    fid = "38";
                    sortid = "7,53";
                    break;
                case "4a":
                    editSearchTxt.setHint("搜索求兼职");
                    fid = "38";
                    sortid = "28";
                    break;
                case "4b":
                    editSearchTxt.setHint("搜索招聘全职");
                    fid = "38";
                    sortid = "53";
                case "5":
                    //setContentView(R.layout.activity_search5);
                    editSearchTxt.setHint("搜索求职简历");
                    fid = "38";
                    sortid = "8,28";
                    break;
                case "6":
                    //setContentView(R.layout.activity_search6);
                    editSearchTxt.setHint("搜索便民服务");
                    fid = "42";
                    sortid = "13,34";
                    break;
                case "6a":
                    editSearchTxt.setHint("搜索便民求助");
                    fid = "42";
                    sortid = "34";
                    break;
                case "7":
                    //setContentView(R.layout.activity_search7);
                    editSearchTxt.setHint("搜索车辆交易");
                    fid = "39";
                    sortid = "58,2";
                    break;
                case "7a":
                    editSearchTxt.setHint("搜索车辆求租");
                    fid = "39";
                    sortid = "57";
                    break;
                case "7b":
                    editSearchTxt.setHint("搜索车辆出售");
                    fid = "39";
                    sortid = "2";
                    break;
                case "7c":
                    editSearchTxt.setHint("搜索车辆求购");
                    fid = "39";
                    sortid = "30";
                    break;
                case "8":
                    //setContentView(R.layout.activity_search8);
                    editSearchTxt.setHint("搜索二手买卖");
                    fid = "40";
                    //sortid = "3";
                    sortid = "0";
                    break;
                case "8a":
                    editSearchTxt.setHint("搜索物品求购");
                    fid = "40";
                    sortid = "29";
                    break;
                case "9":
                    //setContentView(R.layout.activity_search9);
                    editSearchTxt.setHint("搜索打折促销");
                    fid = "107";
                    //sortid = "54";
                    sortid = "0";
                    break;
                case "10":
                    //setContentView(R.layout.activity_search10);
                    editSearchTxt.setHint("搜索招商加盟");
                    fid = "44";
                    //.sortid = "60";
                    sortid = "0";
                    break;
                case "11":
                    //setContentView(R.layout.activity_search11);
                    editSearchTxt.setHint("搜索婚姻交友");
                    fid = "48";
                    sortid = "0";
                    break;
                case "12":
                    //setContentView(R.layout.activity_search12);
                    editSearchTxt.setHint("搜索教育培训");
                    fid = "74";
                    //sortid = "21";
                    sortid = "0";
                    break;
                case "13":
                    //setContentView(R.layout.activity_search13);
                    editSearchTxt.setHint("搜索求购号码");
                    fid = "94";
                    sortid = "40,41";
                    break;
                case "13a":
                    editSearchTxt.setHint("搜索出售号码");
                    fid = "94";
                    sortid = "41";
                    break;
                case "14":
                    //setContentView(R.layout.activity_search14);
                    editSearchTxt.setHint("搜索出国资讯");
                    fid = "83";
                    //sortid = "24";
                    sortid = "0";
                    break;
                case "15":
                    //setContentView(R.layout.activity_search15);
                    editSearchTxt.setHint("搜索宠物天地");
                    fid = "92";
                    //sortid = "22";
                    sortid = "0";
                    break;
                case "16":
                    //setContentView(R.layout.activity_search16);
                    editSearchTxt.setHint("搜索旅游专栏");
                    fid = "50";
                    sortid = "0";
                    //sortid = "61";
                    break;
                case "17":
                    //setContentView(R.layout.activity_search16);
                    editSearchTxt.setHint("搜索物品买卖");
                    fid = "40";
                    sortid = "3";
                    break;
                case "18":
                    //setContentView(R.layout.activity_search16);
                    editSearchTxt.setHint("搜索同城换物");
                    fid = "91";
                    sortid = "26";
                    break;
            }
        }else{
            //setContentView(R.layout.activity_search1);
            fid = (String) intent.getSerializableExtra("fid");
            sortid = (String) intent.getSerializableExtra("sortid");
            editSearchTxt.setHint("搜索");
        }
        getSearchOption();
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) SearchActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, false, "");
        findViewById(R.id.rlt_back).setOnClickListener(this);
        lstSearch = (ListView) findViewById(R.id.lst_search_content);
        searchItemListAdapter = new SearchItemListAdapter(this);
        searchItemListAdapter.setListItems(searchItems);
        lstSearch.setAdapter(searchItemListAdapter);
        getSearchItems();
        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem searchItem = (SearchItem) searchItemListAdapter.getItem(position);
                Long long_id = searchItem.getmSearchID();
                String fid = searchItem.getmFid();
                System.out.println("LongID+"+long_id);
                if(!fid.equals("-1")) {
                    String sortid = searchItem.getmSortid();
                    Intent intent = new Intent(SearchActivity.this, RoomDetailActivity.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("sortid", sortid);
                    intent.putExtra("newsId", Long.toString(long_id));
                    intent.putExtra("title", searchItem.getmDesc());
                    intent.putExtra("desc", searchItem.getmTitle01() + " " + searchItem.getmTitle02() + " " + searchItem.getmTitle03());
                    String img = searchItem.getmThumbnail();
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    if (!img.equals(""))
                        CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                    if (CommonUtils.share_bmp == null)
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    popUp.dismiss();
                    SearchActivity.this.startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchActivity.this, AdverViewActivity.class);
                    intent.putExtra("adver_url", searchItem.getmAdverUrl());
                    popUp.dismiss();
                    SearchActivity.this.startActivityForResult(intent,501);
                }
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
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem>2)
                {
                    findViewById(R.id.lyt_search_parent).post(new Runnable() {
                        @Override
                        public void run() {
                            if(popUp!=null && !popUp.isShowing()) {
                                popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 100);
                            }
                        }
                    });
                    preLast = lastItem;
                }else {
                    if (popUp != null && popUp.isShowing()) {
                        popUp.dismiss();
                    }
                }
            }
        });
        findViewById(R.id.rlt_search).setOnClickListener(this);
        popUp = new BasePopupWindow(this);
        popUp.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popUp.setContentView(LayoutInflater.from(this).inflate(R.layout.to_top_view, null));
        popUp.setBackgroundDrawable(null);
        popUp.setOutsideTouchable(false);
        popUp.setFocusable(false);
        popUp.getContentView().findViewById(R.id.lyt_pop_menu).setOnClickListener(this);
        TabHostActivity.tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeGroupActivity.HomeGroupStack != null && HomeGroupActivity.HomeGroupStack.mIdList.size() > 1) {
                    HomeGroupActivity.HomeGroupStack.getLocalActivityManager().removeAllActivities();
                    HomeGroupActivity.HomeGroupStack.mIdList.clear();
                    HomeGroupActivity.HomeGroupStack.mIntents.clear();
                    HomeGroupActivity.HomeGroupStack.mAnimator.removeAllViews();
                    Intent intent;
                    intent = new Intent(HomeGroupActivity.HomeGroupStack, HomeActivity.class);
                    HomeGroupActivity.HomeGroupStack.startChildActivity("HomeActivity", intent);
                }
                TabHostActivity.tabWidget.setCurrentTab(0);
                TabHostActivity.tabs.setCurrentTab(0);
                popUp.dismiss();
            }
        });
        TabHostActivity.tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popUp.dismiss();
                if (!CommonUtils.isLogin) {
                    Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "me_activity");
                    SearchActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    SearchActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }else {
                    if (PostGroupActivity.PostGroupStack != null && PostGroupActivity.PostGroupStack.mIdList.size() > 1) {
                        PostGroupActivity.PostGroupStack.getLocalActivityManager().removeAllActivities();
                        PostGroupActivity.PostGroupStack.mIdList.clear();
                        PostGroupActivity.PostGroupStack.mIntents.clear();
                        PostGroupActivity.PostGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(PostGroupActivity.PostGroupStack, PostActivity.class);
                        PostGroupActivity.PostGroupStack.startChildActivity("PostCategoryActivity", intent);
                    }
                    TabHostActivity.tabWidget.setCurrentTab(1);
                    TabHostActivity.tabs.setCurrentTab(1);
                }
            }
        });

        // Me tab click

        TabHostActivity.tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popUp.dismiss();
                if (!CommonUtils.isLogin) {
                    Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "me_activity");
                    SearchActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    SearchActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }else{
                    if (NewsGroupActivity.NewsGroupStack != null && NewsGroupActivity.NewsGroupStack.mIdList.size() > 1) {
                        NewsGroupActivity.NewsGroupStack.getLocalActivityManager().removeAllActivities();
                        NewsGroupActivity.NewsGroupStack.mIdList.clear();
                        NewsGroupActivity.NewsGroupStack.mIntents.clear();
                        NewsGroupActivity.NewsGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(NewsGroupActivity.NewsGroupStack, NewsActivity.class);
                        NewsGroupActivity.NewsGroupStack.startChildActivity("NewsActivity", intent);
                    }
                    TabHostActivity.tabWidget.setCurrentTab(2);
                    TabHostActivity.tabs.setCurrentTab(2);
                }
            }
        });
        TabHostActivity.tabWidget.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popUp.dismiss();
                if (!CommonUtils.isLogin) {
                    Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "me_activity");
                    SearchActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    SearchActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                } else {
                    if (MeGroupActivity.MeGroupStack != null && MeGroupActivity.MeGroupStack.mIdList.size() > 1) {
                        MeGroupActivity.MeGroupStack.getLocalActivityManager().removeAllActivities();
                        MeGroupActivity.MeGroupStack.mIdList.clear();
                        MeGroupActivity.MeGroupStack.mIntents.clear();
                        MeGroupActivity.MeGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(MeGroupActivity.MeGroupStack, MeActivity.class);
                        MeGroupActivity.MeGroupStack.startChildActivity("MeActivity", intent);
                    }
                    TabHostActivity.tabWidget.setCurrentTab(3);
                    TabHostActivity.tabs.setCurrentTab(3);
                }
            }
        });
        //showCustomTopMenu();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if (id == R.id.rlt_post) {//点击发布按钮
            intent = new Intent(SearchActivity.this, PostCategoryActivity.class);
            switch (postItem){
                case "4"://招聘信息
                    fid = "38";
                    sortid = "7,53";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "5"://求职简历
                    fid = "38";
                    sortid = "8,28";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "6"://便民服务
                    fid = "42";
                    sortid = "13,34";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "7"://车辆交易
                    fid = "39";
                    sortid = "58,2";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "8"://二手买卖
                    //搜索二手买卖
                    fid = "40";
                    sortid = "0";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "11"://婚姻交友
                    //搜索婚姻交友
                    fid = "48";
                    sortid = "0";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                case "13"://电话号码
                    //搜索求购号码
                    fid = "94";
                    sortid = "40,41";
                    intent.putExtra("PostItem", postItem);
                    pActivity.startChildActivity("activity_search", intent);
                    break;
                default:
                    intent = new Intent(SearchActivity.this, PostActivity.class);
                    intent.putExtra("PostItem", postItem);
                    SearchActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_ANOTHER);
                    break;
            }
        }
        else if (id == R.id.rlt_back){//点击返回按钮
            intent = new Intent(SearchActivity.this, HomeActivity.class);
            popUp.dismiss();
            pActivity.startChildActivity("home", intent);
        }
        else if (id == R.id.rlt_search){//点击搜索按钮
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
        else if (id == R.id.lyt_pop_menu){//点击MoveToTop按钮？
            ((ListView)findViewById(R.id.lst_search_content)).smoothScrollToPosition(0);
        }
    }
    private void showCustomTopMenu()
    {
        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.to_top_view, null);
        WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity=Gravity.BOTTOM|Gravity.RIGHT;
        params.x=0;
        params.y=0;
        windowManager.addView(view, params);
    }
    private void getSearchOption() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, SearchActivity.this);
                RequestParams params = new RequestParams();
                params.put("fid", fid);
                if(sortid!=null && sortid!="") {
                    params.put("sortid", sortid);
                }
                String url = "news/searchoption";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                JSONArray list = response.getJSONArray("ret");
                                boolean price_state = false;
//                                System.out.println("hasprice+"+response);
                                if(response.optString("hasprice").equals("1")){
                                    price_state = true;
                                }
                                initSearchOptionView(list, price_state);
                            }
                            //CommonUtils.dismissProgress(progressDialog);

                        } catch (JSONException e) {
                            //CommonUtils.dismissProgress(progressDialog);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void getSearchItems() {
        CommonUtils.hideKeyboard(SearchActivity.this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, SearchActivity.this);
                if(fid==null || fid==""){
                    //CommonUtils.dismissProgress(progressDialog);
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("fid", fid);
                params.put("sortid", sortid);
                params.put("page", mIntPage);
//                System.out.println("hkey"+selectedKeyTab.size());
                //if(selectedKeyTab.size()==0){
                    Enumeration keys = selectedKeyTab.keys();
                    while (keys.hasMoreElements()) {
                        Object key =  keys.nextElement();
                        if(!(String.valueOf(selectedKeyTab.get(key))).equals(""))
                            params.put(String.valueOf(key), String.valueOf(selectedKeyTab.get(key)));
//                        System.out.println(key+"++kkkk++"+selectedKeyTab.get(key));
                    }
                //}
                /*
                if(selectedVTab!=null && selectedVTab!=-1) {
                    params.put(selectedSTab, selectedVTab);
                }
                */
                params.put("keyword", keyword);
                if(!price.equals(""))
                    params.put("price", price);
                String url = "news/list";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                System.out.println("response++"+response);
                                JSONArray list = response.getJSONArray("ret");
                                System.out.println("list++"+list);
                                if((list==null || list.length()<1) && mIntPage==1){
                                    isLoadMore = false;
                                    ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
                                    //CommonUtils.dismissProgress(progressDialog);
                                    return;
                                }else if(list.length()<8){
                                    isLoadMore = false;
                                }else{
                                    isLoadMore = true;
                                }
                                ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.GONE);
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    DictionaryUtils dictionaryUtils = new DictionaryUtils();
                                    String tid = item.optString("tid");
                                    //String topic_sortid = item.optString("sortid");
                                    dictionaryUtils.setProperty(item,fid);
                                    String img_url = "";
                                    if(item.optJSONObject("fields")!=null && item.optJSONObject("fields").optJSONObject("picture")!=null && !item.optJSONObject("fields").optJSONObject("picture").optString("url").equals("")){
                                        img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
                                    }
                                    String desc = item.optString("title");
                                    String txt_home_favor_price = dictionaryUtils.getProperty("txt_home_favor_price");
                                    String property01 = dictionaryUtils.getProperty("txt_property1");
                                    String property02 = dictionaryUtils.getProperty("txt_property2");
                                    String property03 = dictionaryUtils.getProperty("txt_property3");
                                    String poststick = item.optString("poststick");
                                    //if(i%5==0 && i!=0) {
                                    //    searchItemListAdapter.addItem(new SearchItem(
                                    //            Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, img_url));
                                    //}else {
                                    if(!tid.equals("")) {
                                        searchItemListAdapter.addItem(new SearchItem(
                                                Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, "", ""));
                                        //}
                                    }else{
                                        String advert_url =  item.optString("link");
                                        String advert_img_url = item.optString("advert");
                                        if (!advert_url.substring(0, 4).equals("http")) {
                                            advert_url = "http://"+advert_url;
                                        }
                                        searchItemListAdapter.addItem(new SearchItem(
                                                -1, "", "", "", "", "", "", "", "", "", "-1", "", "", advert_img_url, advert_url));
                                    }
                                }
                                mIntPage++;
                            } else {
                                isLoadMore = false;
                                ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
                                if (mIntPage == 1) {
                                    searchItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            searchItemListAdapter.notifyDataSetChanged();
                            lstSearch.invalidateViews();
                            //CommonUtils.dismissProgress(progressDialog);

                        } catch (JSONException e) {
                            //CommonUtils.dismissProgress(progressDialog);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        //CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void initSearchOptionView(JSONArray list, boolean price_state) {
        arrayObj = new JSONObject[4];
        LinearLayout lyt_search_first = (LinearLayout) findViewById(R.id.lyt_search_first);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                0,
                LayoutParams.MATCH_PARENT,
                0.0f
        );
        LinearLayout lyt_search_second = (LinearLayout) findViewById(R.id.lyt_search_second);
        LinearLayout lyt_search_third = (LinearLayout) findViewById(R.id.lyt_search_third);
        LinearLayout lyt_search_fourth = (LinearLayout) findViewById(R.id.lyt_search_fourth);
        lyt_search_first.setLayoutParams(param);
        lyt_search_second.setLayoutParams(param);
        lyt_search_third.setLayoutParams(param);
        lyt_search_fourth.setLayoutParams(param);
        float weight = 0.0f;
        if(list!=null && list.length()>0) {
            if(price_state){
                if(list.length()<4){
                    weight = (float) 2.7 / (list.length()+1);
                }else{
                    weight = (float) 2.7 / list.length();
                }
            }else {
                weight = (float) 2.7 / list.length();
            }
            for (int i = 0; i < list.length(); i++) {
                try {
                    JSONObject item = list.getJSONObject(i);
                    switch (i) {
                        case 0:
                            ((TextView) findViewById(R.id.txt_search_first)).setText(item.optString("title"));
                            option_ids[0] = item.optString("optionid");
                            arrayObj[0] = item.optJSONObject("choice");
                            param = new LinearLayout.LayoutParams(
                                    0,
                                    LayoutParams.MATCH_PARENT,
                                    weight
                            );
                            lyt_search_first.setLayoutParams(param);
                            lyt_search_first.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                                    lyt_search_panel.removeAllViews();
                                    selectedVTab = option_ids[0];
                                    createActionSheet(arrayObj[0]);
                                }
                            });
                            break;
                        case 1:
                            ((TextView) findViewById(R.id.txt_search_second)).setText(item.optString("title"));
                            option_ids[1] = item.optString("optionid");
                            arrayObj[1] = item.optJSONObject("choice");
                            param = new LinearLayout.LayoutParams(
                                    0,
                                    LayoutParams.MATCH_PARENT,
                                    weight
                            );
                            lyt_search_second.setLayoutParams(param);
                            lyt_search_second.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                                lyt_search_panel.removeAllViews();
                                selectedVTab = option_ids[1];
                                createActionSheet(arrayObj[1]);
                                }
                            });
                            break;
                        case 2:
                            ((TextView) findViewById(R.id.txt_search_third)).setText(item.optString("title"));
                            option_ids[2] = item.optString("optionid");
                            arrayObj[2] = item.optJSONObject("choice");
                            param = new LinearLayout.LayoutParams(
                                    0,
                                    LayoutParams.MATCH_PARENT,
                                    weight
                            );
                            lyt_search_third.setLayoutParams(param);
                            lyt_search_third.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                                lyt_search_panel.removeAllViews();
                                selectedVTab = option_ids[2];
                                createActionSheet(arrayObj[2]);
                                }
                            });
                            break;
                        default:
                            //((TextView) findViewById(R.id.txt_search_fourth)).setText("更多");
                            ((TextView) findViewById(R.id.txt_search_fourth)).setText(item.optString("title"));
                            option_ids[3] = item.optString("optionid");
                            arrayObj[3] = item.optJSONObject("choice");
                            param = new LinearLayout.LayoutParams(
                                    0,
                                    LayoutParams.MATCH_PARENT,
                                    weight
                            );
                            lyt_search_fourth.setLayoutParams(param);
                            lyt_search_fourth.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                                lyt_search_panel.removeAllViews();
                                selectedVTab = option_ids[3];
                                createActionSheet(arrayObj[3]);
                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(price_state){
                param = new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        weight
                );
                if(list.length()>2) {
                    ((TextView) findViewById(R.id.txt_search_fourth)).setText("价格");
                    lyt_search_fourth.setLayoutParams(param);
                    lyt_search_fourth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                            lyt_search_panel.removeAllViews();
                            createSearchPrice();
                        }
                    });
                }else if(list.length()>1) {
                    ((TextView) findViewById(R.id.txt_search_third)).setText("价格");
                    lyt_search_third.setLayoutParams(param);
                    lyt_search_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                            lyt_search_panel.removeAllViews();
                            createSearchPrice();
                        }
                    });
                }
            }
        }
    }
    /*
    public void createActionSheet(JSONObject state_name){
        if(state_name==null || (state_name!=null && state_name.length()==0)){
            return;
        }
        String[] arr = new String[state_name.length()];
        Integer j = 0;
        for(int i=0; i < state_name.length(); i++){
            j++;
            arr[i] = state_name.optString(j.toString());
        }
        ActionSheet.createBuilder(mContext, SearchActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(arr)
                .setCancelableOnTouchOutside(true)
                .setListener(SearchActivity.this)
                .show();
    }
    */
    public void createActionSheet(JSONObject state_name){
        if(state_name==null || (state_name!=null && state_name.length()==0)){
            return;
        }
        LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
        lyt_search_panel.removeAllViews();
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String[] arr = new String[state_name.length()];
        Integer j = 0;
        for(int i=0; i <= state_name.length()/3; i++){
            if(state_name.length()%3==0 && i==state_name.length()/3){
                return;
            }
            j=i*3+1;
            arr[i] = state_name.optString(j.toString());
            View rowView1 = inflater.inflate(R.layout.dropdown_rows, null);
            ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_first)).setText(state_name.optString(j.toString()));
            if(selectedKeyTab.get("option_"+selectedVTab)!=null && selectedKeyTab.get("option_"+selectedVTab).equals(j)){
                ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_first)).setTextColor(getResources().getColor(R.color.colorConfirmText));
            }
            final LinearLayout lyt_dropdown_sub_first = (LinearLayout) rowView1.findViewById(R.id.lyt_dropdown_sub_first);
            LinearLayout lyt_dropdown_sub_second = (LinearLayout) rowView1.findViewById(R.id.lyt_dropdown_sub_second);
            LinearLayout lyt_dropdown_sub_third = (LinearLayout) rowView1.findViewById(R.id.lyt_dropdown_sub_third);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    0.0f
            );
            lyt_dropdown_sub_first.setLayoutParams(param);
            lyt_dropdown_sub_second.setLayoutParams(param);
            lyt_dropdown_sub_third.setLayoutParams(param);
            if(state_name.optString(j.toString())!=""){
                param = new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        1.0f
                );
                lyt_dropdown_sub_first.setId(j);
                lyt_dropdown_sub_first.setLayoutParams(param);
                lyt_dropdown_sub_first.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //selectedVTab = view.getId();
                        selectedSTab = "option_"+selectedVTab;
                        selectedKeyTab.put(selectedSTab, view.getId());
                        LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                        lyt_search_panel.removeAllViews();
                        searchItemListAdapter.clearItems();
                        showAction = 0;
                        mIntPage = 1;
                        getSearchItems();
                    }
                });
            }
            j++;
            ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_second)).setText(state_name.optString(j.toString()));
            if(selectedKeyTab.get("option_"+selectedVTab)!=null && selectedKeyTab.get("option_"+selectedVTab).equals(j)){
                ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_second)).setTextColor(getResources().getColor(R.color.colorConfirmText));
            }
            if(state_name.optString(j.toString())!=""){
                param = new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        1.0f
                );
                lyt_dropdown_sub_second.setLayoutParams(param);
                lyt_dropdown_sub_second.setId(j);
                lyt_dropdown_sub_second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //selectedVTab = view.getId();
                        selectedSTab = "option_"+selectedVTab;
                        selectedKeyTab.put(selectedSTab, view.getId());
                        LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                        lyt_search_panel.removeAllViews();
                        searchItemListAdapter.clearItems();
                        showAction = 0;
                        mIntPage = 1;
                        getSearchItems();
                    }
                });
            }
            j++;
            ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_third)).setText(state_name.optString(j.toString()));
            if(selectedKeyTab.get("option_"+selectedVTab)!=null && selectedKeyTab.get("option_"+selectedVTab).equals(j)){
                ((TextView) rowView1.findViewById(R.id.txt_dropdown_sub_third)).setTextColor(getResources().getColor(R.color.colorConfirmText));
            }
            if(state_name.optString(j.toString())!=""){
                param = new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        1.0f
                );
                lyt_dropdown_sub_third.setLayoutParams(param);
                lyt_dropdown_sub_third.setId(j);
                lyt_dropdown_sub_third.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //selectedVTab = view.getId();
                        selectedSTab = "option_"+selectedVTab;
                        selectedKeyTab.put(selectedSTab, view.getId());
                        LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                        lyt_search_panel.removeAllViews();
                        searchItemListAdapter.clearItems();
                        showAction = 0;
                        mIntPage = 1;
                        getSearchItems();
                    }
                });
            }
            lyt_search_panel.addView(rowView1, lyt_search_panel.getChildCount());
        }
    }
    public void createSearchPrice(){
        LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
        lyt_search_panel.removeAllViews();
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView1 = inflater.inflate(R.layout.dropdown_search, null);
        RelativeLayout rlt_price_search = (RelativeLayout) rowView1.findViewById(R.id.rlt_price_search);
        if(fid.equals("40") || fid.equals("92")){
            ((EditText) rowView1.findViewById(R.id.edit_min_price)).setHint("最低价");
            ((EditText) rowView1.findViewById(R.id.edit_max_price)).setHint("最高价");
        }else{
            ((EditText) rowView1.findViewById(R.id.edit_min_price)).setHint("最低价（万）");
            ((EditText) rowView1.findViewById(R.id.edit_max_price)).setHint("最高价（万）");
        }
        rlt_price_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min_price = ((EditText) findViewById(R.id.edit_min_price)).getText().toString();
                max_price = ((EditText) findViewById(R.id.edit_max_price)).getText().toString();
                if(min_price.equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入最低价格", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)(findViewById(R.id.edit_min_price))).setFocusableInTouchMode(true);
                    ((EditText)(findViewById(R.id.edit_min_price))).requestFocus();
                    return;
                }
                if(max_price.equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入最高价格", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)(findViewById(R.id.edit_max_price))).setFocusableInTouchMode(true);
                    ((EditText)(findViewById(R.id.edit_max_price))).requestFocus();
                    return;
                }
                price = min_price+"-"+max_price;
                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                lyt_search_panel.removeAllViews();
                searchItemListAdapter.clearItems();
                showAction = 0;
                mIntPage = 1;
                getSearchItems();
            }
        });
        RelativeLayout rlt_price_search_cancel = (RelativeLayout) rowView1.findViewById(R.id.rlt_price_search_cancel);
        rlt_price_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = "";
                ((EditText)(findViewById(R.id.edit_min_price))).setText("");
                ((EditText)(findViewById(R.id.edit_max_price))).setText("");
                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
                lyt_search_panel.removeAllViews();
            }
        });
        lyt_search_panel.addView(rowView1, lyt_search_panel.getChildCount());
    }
    @Override
    public void onBackPressed() {
        //this.getParent().getParent().onBackPressed();
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        pActivity.startChildActivity("home", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
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
    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        showAction=0;
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        //String[] phone = mHospitalTel.split(",");
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone[index]));
        //startActivity(intent);
        showAction=0;
        switch(index){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        actionSheet.dismiss();
    }
}