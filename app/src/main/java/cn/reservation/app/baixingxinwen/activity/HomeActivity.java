package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.walnutlabs.android.ProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.adapter.HomeSliderAdapter;
import cn.reservation.app.baixingxinwen.adapter.SearchItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DictionaryUtils;
import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cn.reservation.app.baixingxinwen.widget.ImbeddedListView;
import cn.reservation.app.baixingxinwen.widget.PageListScrollView;
import cz.msebera.android.httpclient.Header;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@SuppressWarnings("deprecation")
public class HomeActivity extends AppCompatActivity implements DialogInterface.OnCancelListener,PageListScrollView.OnScrollToBottomListener, View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public ProgressHUD mProgressDialog;
    public Resources res;
    private Context mContext;
    public Configuration config;
    public AnimatedActivity pActivity;
    public static HomeActivity homeActivity;
    public TabActivity tabActivity;
    ViewPager viewPager;
    private final Handler handler = new Handler();
    int images[] = {R.drawable.banner, R.drawable.default_img, R.drawable.banner, R.drawable.default_img};
    HomeSliderAdapter homeSliderAdapter;
    public SearchItemListAdapter searchItemListAdapter;
    public ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    private PageListScrollView scrollView;
    private ImbeddedListView lstSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        config = new Configuration();
        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) HomeActivity.this.getParent();

        homeActivity = HomeActivity.this;
        tabActivity = (TabActivity) HomeActivity.this.getParent().getParent();

        CommonUtils.customActionBar(mContext, this, false, "");
        /*
        LinearLayout lytFavor1 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail1); //房屋出售
        lytFavor1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor2 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail2); //招聘信息
        lytFavor2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor3 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail3); //车辆交易
        lytFavor3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor4 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail4); //招商加盟
        lytFavor4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        */
        findViewById(R.id.btn_home_room).setOnClickListener(this);
        findViewById(R.id.btn_home_invite).setOnClickListener(this);
        findViewById(R.id.btn_home_car).setOnClickListener(this);
        findViewById(R.id.btn_home_join).setOnClickListener(this);
        findViewById(R.id.btn_home_call).setOnClickListener(this);
        findViewById(R.id.btn_home_travel).setOnClickListener(this);
        findViewById(R.id.btn_home_tax).setOnClickListener(this);
        findViewById(R.id.btn_home_old).setOnClickListener(this);
        findViewById(R.id.btn_home_history).setOnClickListener(this);
        findViewById(R.id.btn_home_anniver).setOnClickListener(this);
        findViewById(R.id.btn_home_outland).setOnClickListener(this);
        findViewById(R.id.btn_home_waitred).setOnClickListener(this);
        findViewById(R.id.btn_home_house).setOnClickListener(this);
        findViewById(R.id.btn_home_service).setOnClickListener(this);
        findViewById(R.id.btn_home_dazhe).setOnClickListener(this);
        findViewById(R.id.btn_home_educate).setOnClickListener(this);
        findViewById(R.id.btn_home_animal).setOnClickListener(this);
        findViewById(R.id.btn_home_waitblue).setOnClickListener(this);
        findViewById(R.id.img_home_middle_01).setOnClickListener(this);
        findViewById(R.id.img_home_middle_02).setOnClickListener(this);
        findViewById(R.id.img_home_middle_03).setOnClickListener(this);
        findViewById(R.id.rlt_search).setOnClickListener(this);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        homeSliderAdapter = new HomeSliderAdapter(HomeActivity.this, images);
        viewPager.setAdapter(homeSliderAdapter);
        lstSearch = (ImbeddedListView) findViewById(R.id.lst_home_bottom_favor);
        searchItemListAdapter = new SearchItemListAdapter(this);
        searchItemListAdapter.setListItems(searchItems);
        lstSearch.setAdapter(searchItemListAdapter);
        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem searchItem = (SearchItem) searchItemListAdapter.getItem(position);
                Long long_id = searchItem.getmSearchID();
                Intent intent = new Intent(HomeActivity.this, RoomDetailActivity.class);
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
                HomeActivity.this.startActivity(intent);
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
        /*
        lstSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getArticle();
                        isLoadMore = false;
                    }
                }
            }
        });
        */
        initViews();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < homeSliderAdapter.getCount(); i++) {
                    final int value = i;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(value, true);
                        }
                    });
                    if(i==(homeSliderAdapter.getCount()-1)){
                        i=-1;
                    }
                }
            }
        };
        new Thread(runnable).start();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if (id == R.id.btn_home_room){//房屋出售
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "1");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_tax){//房屋出租
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "2");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_house){//店铺出兑
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "3");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_invite){//招聘信息
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "4");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_history){//求职简历
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "5");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_service){//便民服务
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "6");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_car){//车辆交易
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "7");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_old){//二手买卖
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "8");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_dazhe){//打折促销
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "9");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_join){//招商加盟
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "10");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_anniver){//婚姻交友
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "11");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_educate){//教育培训
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "12");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_call){//电话号码
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "13");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_outland){//出国咨询
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "14");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_animal) {//宠物天地
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "15");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_travel){//旅游专栏
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("PostItem", "16");
            pActivity.startChildActivity("activity_search", intent);
        }
        else if (id == R.id.btn_home_waitred){//敬请期待
            intent = new Intent(HomeActivity.this, SearchActivity.class);
        }
        else if (id == R.id.btn_home_waitblue) {//敬请期待
            intent = new Intent(HomeActivity.this, SearchActivity.class);
        }
        else if (id == R.id.img_home_middle_01){//便民电话-房屋维修
            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
            intent.putExtra("enterprise", "房屋维修");
            pActivity.startChildActivity("activity_enterprise", intent);
        }
        else if (id == R.id.img_home_middle_02){//便民电话-代驾
            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
            intent.putExtra("enterprise", "代驾");
            pActivity.startChildActivity("activity_enterprise", intent);
        }
        else if (id == R.id.img_home_middle_03){//便民电话-跑腿
            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
            intent.putExtra("enterprise", "跑腿");
            pActivity.startChildActivity("activity_enterprise", intent);
        }
        else if (id == R.id.rlt_search){//点击搜索按钮
            intent = new Intent(HomeActivity.this, FullSearchActivity.class);
            pActivity.startChildActivity("full_search", intent);
        }
    }
    private void initViews() {
        scrollView = (PageListScrollView) findViewById(R.id.scrollview_home);
        lstSearch.setFocusable(false);
        scrollView.setOnScrollToBottomListener(HomeActivity.this);
        getArticle();
    }
    private void alertDialogLang() {
        final Dialog dialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_lang, null);

        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        final String lang = pref.getString("lang", "zh");
        TextView btnLangKO = (TextView) view.findViewById(R.id.btn_lang_ko);
        btnLangKO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("zh")) setLocale("ko");
                dialog.dismiss();
            }
        });
        TextView btnLangCH = (TextView) view.findViewById(R.id.btn_lang_ch);
        btnLangCH.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("ko")) setLocale("zh");
                dialog.dismiss();
            }
        });
        CommonUtils.showAlertDialog(mContext, this, dialog, view, 216);
    }

    private void getArticle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HomeActivity.this);
                RequestParams params = new RequestParams();
                params.put("ishome", "true");
                params.put("page", mIntPage);
                String url = "news/list";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getInt("code") == 1) {
                                //isLoadMore = response.getBoolean("hasmore");
                                System.out.println("img"+response);
                                JSONArray list = response.getJSONArray("ret");
                                if(list==null){
                                    isLoadMore = false;
                                    //CommonUtils.dismissProgress(progressDialog);
                                    return;
                                }else if(list.length()<8){
                                    isLoadMore = false;
                                }else{
                                    isLoadMore = true;
                                }
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    String tid = item.optString("tid");
                                    String fid = item.optString("fid");
                                    String sortid = item.optString("sortid");
                                    DictionaryUtils dictionaryUtils = new DictionaryUtils();
                                    dictionaryUtils.setProperty(item,fid);
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
                                                Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, sortid, poststick, "",""));
                                    }
                                }
                                mIntPage++;
                            } else {
                                isLoadMore = false;
                                if (mIntPage == 1) {
                                    searchItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            searchItemListAdapter.notifyDataSetChanged();
                            lstSearch.invalidateViews();

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
/*
    private void initFavorView(JSONArray list)
    {
        LinearLayout lyt_home_bottom_favor = (LinearLayout) findViewById(R.id.lyt_home_bottom_favor);
        lyt_home_bottom_favor.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list.length(); i++)
        {
            //R.layout.search_item;
            try {
                JSONObject item = list.getJSONObject(i);
                View rowView1 = inflater.inflate(R.layout.search_item, null);
                ((TextView) rowView1.findViewById(R.id.txt_home_favor_desc)).setText(item.optString("title"));
                ImageView mThumbnail = (ImageView) rowView1.findViewById(R.id.img_home_favor_thumbnail);
                Drawable mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);
                String img_url = "";
                if(item.optJSONObject("fields")!=null && item.optJSONObject("fields").optJSONObject("picture")!=null && item.optJSONObject("fields").optJSONObject("picture").optString("url")!=""){
                    img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
                }

                if (img_url.equals("")) {
                    img_url = "";
                } else {
                    if (img_url.substring(0, 1).equals(".")){
                        img_url = CommonUtils.getUrlEncoded(APIManager.Sever_URL + img_url.substring(1));
                    } else {
                        img_url = CommonUtils.getUrlEncoded(APIManager.Sever_URL + img_url);
                    }
                }
                if(img_url!="") {
                    Picasso
                            .with(mContext)
                            .load(img_url)
                            .placeholder(mImgPlaceholder)
                            .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 80))
                            .into(mThumbnail);
                }
                switch(item.getString("fid")){
                    case "2"://房产信息
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("house_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("floors_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "39"://车辆交易
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("car_type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("abaility_estimate_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("car_price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "40"://物品买卖
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("level_label"));
                            if(item.optJSONObject("fields").optString("level_label")==""){
                                ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("car_level_label"));
                            }
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "38"://招聘求职
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("experience_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("educattion_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("salary_range_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "42"://便民服务
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("type_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                        }
                        break;
                    case "48"://婚姻交友
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("sex_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("age_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "74"://教育培训
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("education_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("period_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("education_price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "92"://宠物天地
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("source_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "83"://出国资讯
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        ((TextView) rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("type_label"));
                        }else {
                            ((TextView) rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property3)).setText("");
                        }
                        break;
                    case "91"://同城换物
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        ((TextView) rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("type_label"));
                        }else {
                            ((TextView) rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property3)).setText("");
                        }
                        break;
                    case "108"://家教专栏
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        ((TextView) rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        if(!item.optJSONObject("fields").equals(null)){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("education_label"));
                        }else {
                            ((TextView) rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView) rowView1.findViewById(R.id.txt_property3)).setText("");
                        }
                        break;
                    case "93"://出兑求兑
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("floor_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "94"://电话号码
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("company_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "44"://招商加盟
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("group_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "50"://游山玩水
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("order_label"));
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText(item.optJSONObject("fields").optString("price_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                            ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        }
                        break;
                    case "107":
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText(item.optJSONObject("fields").optString("region"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                        }
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText(item.optJSONObject("fields").optString("type_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                        }
                        if(item.optJSONObject("fields")!=null){
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText(item.optJSONObject("fields").optString("group_label"));
                        }else{
                            ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                        }
                        ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        break;
                    default:
                        ((TextView)rowView1.findViewById(R.id.txt_property_val1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property_val3)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property1)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property2)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_property3)).setText("");
                        ((TextView)rowView1.findViewById(R.id.txt_home_favor_price)).setText("");
                        break;
                }
                LinearLayout lyt_home_favor = (LinearLayout) rowView1.findViewById(R.id.lyt_home_favor_thumbnail);
                String postState = item.optString("poststick");
                ImageView mTop = (ImageView)rowView1.findViewById(R.id.img_news_top);
                switch(postState){
                    case "0":
                        mTop.setVisibility(INVISIBLE);
                        break;
                    case "1":
                        mTop.setVisibility(VISIBLE);
                        break;
                    default:
                        mTop.setVisibility(INVISIBLE);
                        break;
                }
                String tid = item.optString("tid");
                lyt_home_favor.setId(Integer.parseInt(tid));
                lyt_home_favor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                        Intent intent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                        String newsId = String.valueOf(view.getId());
                        String sortid = "";
                        String title = ((TextView)view.findViewById(R.id.txt_home_favor_desc)).getText().toString();
                        String desc = ((TextView)view.findViewById(R.id.txt_home_favor_price)).getText()+ " " +((TextView)view.findViewById(R.id.txt_property1)).getText()+ " " + ((TextView)view.findViewById(R.id.txt_property2)).getText()+ " " +((TextView)view.findViewById(R.id.txt_property3)).getText();
                        ImageView image = (ImageView) view.findViewById(R.id.img_home_favor_thumbnail);
                        CommonUtils.share_bmp = ((BitmapDrawable)image.getDrawable()).getBitmap();
                        if(CommonUtils.share_bmp==null) {
                            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        }

                        String url = APIManager.User_URL+"news/paper/"+newsId;
                        intent.putExtra("fid", "");
                        intent.putExtra("sortid", sortid);
                        intent.putExtra("newsId", newsId);
                        intent.putExtra("url", url);
                        intent.putExtra("title", title);
                        intent.putExtra("desc", desc);
                        HomeActivity.this.startActivity(intent);
                        //pActivity.startActivity("room_detail", intent);
                    }
                });
                lyt_home_bottom_favor.addView(rowView1, lyt_home_bottom_favor.getChildCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    */
    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub
    }

    public void setLocale(String lang) {
        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();

        Intent intent = new Intent(tabActivity, TabHostActivity.class);
        tabActivity.startActivity(intent);
        tabActivity.finish();
        tabActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    @Override
    public void onScrollBottomListener(boolean isBottom) {
        //模拟进行数据的分页加载
        if (isLoadMore && isBottom && !lstSearch.isLoading()) {
            lstSearch.startLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //initAdapter(getCommentList());
                    //commentLv.loadComplete();
                    //initLoadMoreTagOp();
                    getArticle();
                    lstSearch.loadComplete();
                }
            }, 500);//模拟网络请求，延缓2秒钟
        }
    }
    @Override
    public void onBackPressed() {
        this.getParent().getParent().onBackPressed();
    }

}