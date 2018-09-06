package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.DropMenuAdapter;
import cn.reservation.app.baixingxinwen.adapter.SearchItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.JsonResponseListener;
import cn.reservation.app.baixingxinwen.api.NetworkManager;
import cn.reservation.app.baixingxinwen.dropdownmenu.entity.FilterUrl;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.BasePopupWindow;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DictionaryUtils;
import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cz.msebera.android.httpclient.Header;

import static com.android.volley.Request.Method.POST;


@SuppressWarnings("deprecation")
public class SearchActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener, View.OnClickListener, OnFilterDoneListener {
    @BindView(R.id.dropDownMenu) DropDownMenu dropDownMenu;
    @BindView(R.id.mFilterContentView) LinearLayout mFilterContentView;
//    DropDownMenu dropDownMenu;
//    LinearLayout mFilterContentView;

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
    private View contentView;
    private boolean isKeyboardShown = false;
    private int countOfListItemsFromStartToShowing = 0;
    private NavigationView areaNavView;

    private SearchActivity self;

    private Map<String, String> paramsUpdated;
    private JSONArray searchResultJsonArray;
    private View numberSearchView;

    private String optionIdForNumber = "";

    private LinearLayout indicatorLayout;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();


        showAction = 0;
        selectedKeyTab = new Hashtable();
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        self = this;

        contentView = findViewById(R.id.activity_search);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                TabHostActivity.tabWidget.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                // 0.15 ratio is perhaps enough to determine keypad height.
                isKeyboardShown = keypadHeight > screenHeight * 0.15;
                if(isKeyboardShown && popUp.isShowing()){
                    popUp.dismiss();
                }
                else{
//                    if(popUp!=null && !popUp.isShowing()) {
//                        popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
//                    }
                }

//                if(popUp.isShowing()){
//                    popUp.dismiss();
//                }

            }
        });

        dropDownMenu = (DropDownMenu)findViewById(R.id.dropDownMenu);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicatorLayout);
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) indicatorLayout.findViewById(R.id.AVLoadingIndicatorView);
        avi.setIndicator("LineScaleIndicator");
        avi.setIndicatorColor(Color.RED);



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

        ///메뉴를정의한다
//        areaNavView = (NavigationView) findViewById(R.id.area_nav_view);
//        Menu m = areaNavView.getMenu();
//        SubMenu topChannelMenu = m.addSubMenu("Top Channels");
//        topChannelMenu.add("Foo");
//        topChannelMenu.add("Bar");
//        topChannelMenu.add("Baz");


//        getSearchOption();
        mContext = TabHostActivity.TabHostStack;
        getSearchOptionVolley();
        res = getResources();
        pActivity = (AnimatedActivity) SearchActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, false, "");
        findViewById(R.id.rlt_back).setOnClickListener(this);
        lstSearch = (ListView) findViewById(R.id.lst_search_content);
        searchItemListAdapter = new SearchItemListAdapter(this);
        searchItemListAdapter.setListItems(searchItems);
        lstSearch.setAdapter(searchItemListAdapter);

        // 이페지에 들어올때 먼저 해당 fid, sortid에 의하여 검색을 진행한다.
        paramsUpdated = new HashMap<>();
        paramsUpdated.put("fid", fid);
        paramsUpdated.put("sortid", sortid);
        paramsUpdated.put("page", String.valueOf(mIntPage));
        getSearchItemsVolley();
        ///////////////////////////////////////////////////////////////

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

        lstSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        CommonUtils.hideKeyboard(SearchActivity.this);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
//                        int totalItemCountOfListSearch = lstSearch.getAdapter().getCount();
                        if(preLast>6)
                        {
                            if(popUp!=null && !popUp.isShowing()) {
                                popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
                            }
//                            findViewById(R.id.lyt_search_parent).post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(popUp!=null && !popUp.isShowing()) {
//                                        popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
//                                    }
//                                }
//                            });
//                            preLast = totalItemCountOfListSearch;
                        }
                        else if (popUp != null && popUp.isShowing() ) {
                            popUp.dismiss();
                        }


                        break;

                    default:
                        break;
                }

                return false;

            }
        });

        lstSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        paramsUpdated.put("page", String.valueOf(mIntPage));
                        getSearchItemsVolley();
                        isLoadMore = false;
                    }
                }
                final int lastItem = firstVisibleItem + visibleItemCount;
//                if(lstSearch.getChildCount() == 0)
//                    if(popUp != null)
//                        popUp.dismiss();

                if(lastItem <= 6){
                    if (popUp != null && popUp.isShowing()) {
                        popUp.dismiss();
                    }
                }
//                else{
//                    Rect r = new Rect();
//                    TabHostActivity.tabWidget.getWindowVisibleDisplayFrame(r);
//                    int screenHeight = contentView.getRootView().getHeight();
//
//                    // r.bottom is the position above soft keypad or device button.
//                    // if keypad is shown, the r.bottom is smaller than that before.
//                    int keypadHeight = screenHeight - r.bottom;
//
//                    // 0.15 ratio is perhaps enough to determine keypad height.
//                    if(!isKeyboardShown && !popUp.isShowing()){
//                        if(popUp!=null && !popUp.isShowing()) {
//                            popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
//                        }
////                        findViewById(R.id.lyt_search_parent).post(new Runnable() {
////                            @Override
////                            public void run() {
////                                if(popUp!=null && !popUp.isShowing()) {
////                                    popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
////                                }
////                            }
////                        });
//                    }
//                }
                preLast = lastItem;

//                if(lastItem>6 && !isKeyboardShown)
//                {
//                    findViewById(R.id.lyt_search_parent).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(popUp!=null && !popUp.isShowing()) {
//                                popUp.showAtLocation(findViewById(R.id.lyt_search_parent), Gravity.BOTTOM | Gravity.RIGHT, 0, 200);
//                            }
//                        }
//                    });
//                    preLast = lastItem;
//                }else {
//                    if (popUp != null && popUp.isShowing()) {
//                        popUp.dismiss();
//                    }
//                }
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        // MenuInflater inflater = getMenuInflater();
//        getMenuInflater().inflate(R.menu.navigation_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        drawerLayout.openDrawer(Gravity.LEFT);
//        switch (item.getItemId()){
//            //case R.id.action_settings:
//            //    return true;
//
//            case 1:
////                drawerLayout.openDrawer(Gravity.LEFT);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//
//
//        }
//
//    }


    public boolean checkKeyboardShown(){
        InputMethodManager imm = (InputMethodManager) SearchActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            return true;
        } else {
            return false;
        }
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
            popUp.dismiss();
            CommonUtils.hideKeyboard(SearchActivity.this);
            intent = new Intent(SearchActivity.this, HomeActivity.class);
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
                getSearchItemsVolley();
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

                                //메뉴를 위한 코드
                                searchResultJsonArray = list;
                                initFilterDropDownView(list);
//                                mFilterContentView.setOnClickListener(self);
                                ////////////////////////////////////////////////////

//                                boolean price_state = false;
////                                System.out.println("hasprice+"+response);
//                                if(response.optString("hasprice").equals("1")){
//                                    price_state = true;
//                                }
//                                initSearchOptionView(list, price_state);
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
    private void getSearchOptionVolley() {
        Map<String, String> params = new HashMap<>();
        params.put("fid", fid);
        if(sortid!=null && sortid!="") {
            params.put("sortid", sortid);
        }
        String url = APIManager.getUrl("news/searchoption");

        NetworkManager.getInstance().sendJsonRequest(url, Request.Method.POST, params, new JsonResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    if (response.getInt("code") == 1) {
                        JSONArray list = response.getJSONArray("ret");

                        //메뉴를 위한 코드
                        searchResultJsonArray = list;
                        initFilterDropDownView(list);
//                                mFilterContentView.setOnClickListener(self);
                        ////////////////////////////////////////////////////

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
            public void errorHandler(String errorMessage) {
                //CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        });

    }
//    private void getSearchItems() {
//        indicatorLayout.setVisibility(View.VISIBLE);
////        if(dropDownMenu != null){
////            dropDownMenu.setVisibility(View.GONE);
////        }
//
//        CommonUtils.hideKeyboard(SearchActivity.this);
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, SearchActivity.this);
//                if(fid==null || fid==""){
//                    //CommonUtils.dismissProgress(progressDialog);
//                    return;
//                }
//                RequestParams params = new RequestParams();
//                params.put("fid", fid);
//                params.put("sortid", sortid);
//                params.put("page", mIntPage);
////                System.out.println("hkey"+selectedKeyTab.size());
//                //if(selectedKeyTab.size()==0){
//                    Enumeration keys = selectedKeyTab.keys();
//                    while (keys.hasMoreElements()) {
//                        Object key =  keys.nextElement();
//                        if(!(String.valueOf(selectedKeyTab.get(key))).equals(""))
//                            params.put(String.valueOf(key), String.valueOf(selectedKeyTab.get(key)));
////                        System.out.println(key+"++kkkk++"+selectedKeyTab.get(key));
//                    }
//                //}
//                /*
//                if(selectedVTab!=null && selectedVTab!=-1) {
//                    params.put(selectedSTab, selectedVTab);
//                }
//                */
////                params.put("keyword", keyword);
////                if(!price.equals(""))
////                    params.put("price", price);
//
//
//                String url = "news/list";
//                APIManager.post(mContext, url, paramsUpdated, null, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        try {
//                            if (response.getInt("code") == 1) {
//                                //isLoadMore = response.getBoolean("hasmore");
//                                System.out.println("response++"+response);
//                                JSONArray list = response.getJSONArray("ret");
//                                System.out.println("list++"+list);
//                                if((list==null || list.length()<1) && mIntPage==1){
//                                    isLoadMore = false;
//                                    ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
//                                    //CommonUtils.dismissProgress(progressDialog);
//                                    return;
//                                }else if(list.length()<8){
//                                    isLoadMore = false;
//                                }else{
//                                    isLoadMore = true;
//                                }
//                                ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.GONE);
//                                for(int i=0; i<list.length(); i++) {
//                                    JSONObject item = list.getJSONObject(i);
//                                    DictionaryUtils dictionaryUtils = new DictionaryUtils();
//                                    String tid = item.optString("tid");
//                                    //String topic_sortid = item.optString("sortid");
//                                    dictionaryUtils.setProperty(item,fid);
//                                    String img_url = "";
//                                    if(item.optJSONObject("fields")!=null && item.optJSONObject("fields").optJSONObject("picture")!=null && !item.optJSONObject("fields").optJSONObject("picture").optString("url").equals("")){
//                                        img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
//                                    }
//                                    String desc = item.optString("title");
//                                    String txt_home_favor_price = dictionaryUtils.getProperty("txt_home_favor_price");
//                                    String property01 = dictionaryUtils.getProperty("txt_property1");
//                                    String property02 = dictionaryUtils.getProperty("txt_property2");
//                                    String property03 = dictionaryUtils.getProperty("txt_property3");
//                                    String poststick = item.optString("poststick");
//                                    //if(i%5==0 && i!=0) {
//                                    //    searchItemListAdapter.addItem(new SearchItem(
//                                    //            Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, img_url));
//                                    //}else {
//                                    if(!tid.equals("")) {
//                                        searchItemListAdapter.addItem(new SearchItem(
//                                                Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, "", ""));
//                                        //}
//                                    }else{
//                                        String advert_url =  item.optString("link");
//                                        String advert_img_url = item.optString("advert");
//                                        if (!advert_url.substring(0, 4).equals("http")) {
//                                            advert_url = "http://"+advert_url;
//                                        }
//                                        searchItemListAdapter.addItem(new SearchItem(
//                                                -1, "", "", "", "", "", "", "", "", "", "-1", "", "", advert_img_url, advert_url));
//                                    }
//                                }
//                                mIntPage++;
//                            } else {
//                                isLoadMore = false;
//                                ((LinearLayout)SearchActivity.this.findViewById(R.id.lyt_result_panel)).setVisibility(View.VISIBLE);
//                                if (mIntPage == 1) {
//                                    searchItemListAdapter.clearItems();
//                                }
//                                mIntPage = 1;
//                            }
//                            searchItemListAdapter.notifyDataSetChanged();
//                            lstSearch.invalidateViews();
//
//
//                            indicatorLayout.setVisibility(View.GONE);
////                            if(dropDownMenu != null){
////                                dropDownMenu.setVisibility(View.VISIBLE);
////                            }
//                            //CommonUtils.dismissProgress(progressDialog);
//
//                        } catch (JSONException e) {
//                            //CommonUtils.dismissProgress(progressDialog);
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                        //CommonUtils.dismissProgress(progressDialog);
//                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        //CommonUtils.dismissProgress(progressDialog);
//                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }

    private void getSearchItemsVolley() {
        indicatorLayout.setVisibility(View.VISIBLE);
        CommonUtils.hideKeyboard(SearchActivity.this);
        if(fid==null || fid==""){
            //CommonUtils.dismissProgress(progressDialog);
            return;
        }

        String url = APIManager.getUrl("news/list");
        NetworkManager.getInstance().sendJsonRequest(url, POST, paramsUpdated, new JsonResponseListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    if (response.getInt("code") == 1) {
                        //isLoadMore = response.getBoolean("hasmore");
//                        System.out.println("response++"+response);
                        JSONArray list = response.getJSONArray("ret");
//                        System.out.println("list++"+list);
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
                            dictionaryUtils.setProperty(item);
                            String img_url = "";
                            if(item.optJSONObject("fields")!=null && item.optJSONObject("fields").optJSONObject("picture")!=null && !item.optJSONObject("fields").optJSONObject("picture").optString("url").equals("")){
                                img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
                            }
                            String desc = item.optString("title");
//                            String txt_home_favor_price = dictionaryUtils.getProperty("txt_home_favor_price");
//                            String property01 = dictionaryUtils.getProperty("txt_property1");
//                            String property02 = dictionaryUtils.getProperty("txt_property2");
//                            String property03 = dictionaryUtils.getProperty("txt_property3");
//                            String poststick = item.optString("poststick");
                            //if(i%5==0 && i!=0) {
                            //    searchItemListAdapter.addItem(new SearchItem(
                            //            Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, img_url));
                            //}else {
                            if(!tid.equals("")) {
                                searchItemListAdapter.addItem(new SearchItem(
                                        Long.parseLong(tid), img_url, desc, dictionaryUtils, item));
//                                searchItemListAdapter.addItem(new SearchItem(
//                                        Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, String.valueOf(sortid), poststick, "", ""));
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


                    indicatorLayout.setVisibility(View.GONE);
//                            if(dropDownMenu != null){
//                                dropDownMenu.setVisibility(View.VISIBLE);
//                            }
                    //CommonUtils.dismissProgress(progressDialog);

                } catch (JSONException e) {
                    //CommonUtils.dismissProgress(progressDialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void errorHandler(String errorMessage) {
                //CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
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
                                    createActionSheet(arrayObj[0], 0);
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
                                createActionSheet(arrayObj[1], 1);
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
                                createActionSheet(arrayObj[2], 2);
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
                                createActionSheet(arrayObj[3], 3);
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
    public void createActionSheet(JSONObject state_name_input, int type){
        if(state_name_input==null || (state_name_input!=null && state_name_input.length()==0)){
            return;
        }


        JSONObject state_name;

        if(type == 0){
            if(areaNavView.getVisibility() == NavigationView.VISIBLE){
                areaNavView.setVisibility(NavigationView.GONE);
            }
            else{
                areaNavView.setVisibility(NavigationView.VISIBLE);
            }

            state_name = (JSONObject)state_name_input.opt("main");
        }
        else{
            state_name = state_name_input;
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
                        getSearchItemsVolley();
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
                        getSearchItemsVolley();
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
                        getSearchItemsVolley();
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
                getSearchItemsVolley();
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

    private void initFilterDropDownView(JSONArray list) {

//        mFilterContentView = (TextView)findViewById(R.id.mFilterContentView);
        String[] titleList = new String[] { "第一个", "第二个", "第三个", "第四个" };
        dropDownMenu.setMenuAdapter(new DropMenuAdapter(this, list, this));

        char[] flags = new char[list.length() + 1];
        flags[0] = 0;
        for(int i = 0; i < list.length(); i++){
            try {
                JSONObject mainItem = list.getJSONObject(i);
                String type = mainItem.optString("type");
                if(type.contains("click"))
                    flags[i + 1] = 0;
                else
                    flags[i + 1] = 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dropDownMenu.setShowDropdownFlags(flags);

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        numberSearchView = inflater.inflate(R.layout.dropdown_search, null);
        RelativeLayout rlt_price_search = (RelativeLayout) numberSearchView.findViewById(R.id.rlt_price_search);

        rlt_price_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min_price = ((EditText) numberSearchView.findViewById(R.id.edit_min_price)).getText().toString();
                max_price = ((EditText) numberSearchView.findViewById(R.id.edit_max_price)).getText().toString();
                if(min_price.equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入最低值", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)(numberSearchView.findViewById(R.id.edit_min_price))).setFocusableInTouchMode(true);
                    ((EditText)(numberSearchView.findViewById(R.id.edit_min_price))).requestFocus();
                    return;
                }
                if(max_price.equals("")){
                    Toast toast = Toast.makeText(mContext, "请输入最高值", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                    toast.show();
                    ((EditText)(numberSearchView.findViewById(R.id.edit_max_price))).setFocusableInTouchMode(true);
                    ((EditText)(numberSearchView.findViewById(R.id.edit_max_price))).requestFocus();
                    return;
                }
                price = min_price+"<->"+max_price;
//                LinearLayout lyt_search_panel = (LinearLayout) findViewById(R.id.lyt_search_panel);
//                lyt_search_panel.removeAllViews();

                if(!optionIdForNumber.equals("")){
                    paramsUpdated.put("option_"+ optionIdForNumber, price);
                    searchItemListAdapter.clearItems();
//                    showAction = 0;
//                    mIntPage = 1;


                    getSearchItemsVolley();
                }

            }
        });

        numberSearchView.setVisibility(View.GONE);
//        dropDownMenu.setAllViews(numberSearchView);
        dropDownMenu.setNumberSearchView(numberSearchView);
    }

    private void initSearchOptionMenu(JSONArray list, boolean price_state) {
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
                                    createActionSheet(arrayObj[0], 0);
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
                                    createActionSheet(arrayObj[1], 1);
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
                                    createActionSheet(arrayObj[2], 2);
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
                                    createActionSheet(arrayObj[3], 3);
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

    @Override
    public void onFilterDone(int position, String positionTitle, String urlValue) {

        String priceRange = urlValue;
        String optionId = positionTitle;

        paramsUpdated.put("option_" + optionId, priceRange);
        searchItemListAdapter.clearItems();
        getSearchItemsVolley();
//        dropDownMenu.hideNumberSearchView();
//        if (position != 0) {
//            dropDownMenu.setPositionIndicatorText(FilterUrl.instance().columnPosition, FilterUrl.instance().positionTitle);
//        }

//        dropDownMenu.setPositionIndicatorText(FilterUrl.instance().columnPosition, FilterUrl.instance().positionTitle);
//
//        dropDownMenu.close();
//        mFilterContentView.setText(FilterUrl.instance()
//                .toString());
    }

    @Override
    public void onFilterDoneReturnPosition(int columnPosition, int rowPosition, int itemPosition) {
        dropDownMenu.setPositionIndicatorText(columnPosition, FilterUrl.instance().positionTitle);
        if (columnPosition == 0) {
            dropDownMenu.close();
        }


        Log.d("ColumnPosition", Integer.toString(columnPosition));
        Log.d("RowPosition", Integer.toString(rowPosition));
        Log.d("ItemPosition", Integer.toString(itemPosition));

        if(columnPosition == 0){
            //이 경우에는 remove 단추가 눌렸을때이므로 검색키들을 재설정하고 reload 한다.
            resetSearch();
            dropDownMenu.resetMainMenuTitles();
            return;
        }

        boolean isNumberSearch = false;

        try {
            paramsUpdated.put("page", "1");
            JSONObject mainItem = searchResultJsonArray.getJSONObject(columnPosition - 1);
            String analysis = mainItem.optString("analysis");
            String type = mainItem.optString("type");
            if(analysis.contains("sortid")){
                String sortid = mainItem.optString("optionid");
                paramsUpdated.put("sortid", sortid);

                searchItemListAdapter.clearItems();
                getSearchItemsVolley();



            }
            else{
                if(type.contains("click")){
                    String sortid = mainItem.optString("optionid");
                    paramsUpdated.put("sortid", sortid);

                    searchItemListAdapter.clearItems();
                    getSearchItemsVolley();
                }
                else if(type.contains("region")){
                    String optionid = mainItem.optString("optionid");
                    String paramKey = "option_" + optionid;

                    String paramValue;
                    if(itemPosition == -1)
                        paramValue = Integer.toString(rowPosition + 1);
                    else
                        paramValue = Integer.toString(rowPosition + 1) + "." + Integer.toString(itemPosition + 1);

                    paramsUpdated.put(paramKey, paramValue);
                    searchItemListAdapter.clearItems();
                    getSearchItemsVolley();
                }
                else if(type.contains("more")){

                    JSONArray infoArray = mainItem.getJSONArray("info");
                    JSONObject infoItem = infoArray.getJSONObject(rowPosition);
                    String typeInfoItem = infoItem.optString("type");
                    String analysisInfoItem = infoItem.optString("analysis");

                    if(analysisInfoItem.contains("sortid")) {
                        String sortid = infoItem.optString("optionid");
                        String[] sortids = sortid.split(",");
                        if (sortids.length > 1) {
                            sortid = sortids[itemPosition];
                            paramsUpdated.put("sortid", sortid);

                            searchItemListAdapter.clearItems();
                            getSearchItemsVolley();
                        }
                    }
                    else{
                        if(typeInfoItem.contains("click")){

                            String sortid = infoItem.optString("optionid");
                            String[] sortids = sortid.split(",");
                            if(sortids.length > 1){
                                sortid = sortids[rowPosition];
                                paramsUpdated.put("sortid", sortid);

                                searchItemListAdapter.clearItems();
                                getSearchItemsVolley();
                            }
                        }
                        else if(!typeInfoItem.contains("number") && !typeInfoItem.contains("text")){
                            String optionid = infoItem.optString("optionid");

                            String paramKey = "option_" + optionid;
                            String paramValue = Integer.toString(itemPosition + 1);

                            paramsUpdated.put(paramKey, paramValue);

                            searchItemListAdapter.clearItems();
                            getSearchItemsVolley();
                        }
                        else{
                            String unit = infoItem.optString("unit");
                            optionIdForNumber = infoItem.optString("optionid");
                            RelativeLayout rlt_price_search = (RelativeLayout) numberSearchView.findViewById(R.id.rlt_price_search);
                            ((EditText) numberSearchView.findViewById(R.id.edit_min_price)).setText("");
                            ((EditText) numberSearchView.findViewById(R.id.edit_min_price)).setHint("最低值 (" + unit + ")");
                            ((EditText) numberSearchView.findViewById(R.id.edit_max_price)).setText("");
                            ((EditText) numberSearchView.findViewById(R.id.edit_max_price)).setHint("最高值 (" + unit + ")");

                            dropDownMenu.showNumberSearchView();
                            isNumberSearch = true;

//                            numberSearchView.setVisibility(View.VISIBLE);
                        }
                    }



                }
                else if(!type.contains("number") && !type.contains("text")){
                    String optionid = mainItem.optString("optionid");

                    String paramKey = "option_" + optionid;
                    String paramValue = Integer.toString(rowPosition + 1);
                    if(itemPosition > -1){
                        paramValue += "." + Integer.toString(itemPosition + 1);
                    }

                    paramsUpdated.put(paramKey, paramValue);

                    searchItemListAdapter.clearItems();
                    getSearchItemsVolley();
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (rowPosition != -1 && !isNumberSearch) {
            dropDownMenu.close();
        }
    }

    private void resetSearch(){
        numberSearchView.setVisibility(View.GONE);

        paramsUpdated.clear();
        paramsUpdated.put("fid", fid);
        paramsUpdated.put("sortid", sortid);
        paramsUpdated.put("page", "1");
        mIntPage = 1;

        searchItemListAdapter.clearItems();
        getSearchItemsVolley();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterUrl.instance()
                .clear();
    }
}
