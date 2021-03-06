package cn.reservation.app.baixingxinwen.activity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.HomeSliderAdapter;
import cn.reservation.app.baixingxinwen.adapter.SearchItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.CustomCategoryLayout;
import cn.reservation.app.baixingxinwen.utils.CustomServiceLayout;
import cn.reservation.app.baixingxinwen.utils.DictionaryUtils;
import cn.reservation.app.baixingxinwen.utils.SearchItem;
import cn.reservation.app.baixingxinwen.widget.ImbeddedListView;
import cn.reservation.app.baixingxinwen.widget.PageListScrollView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class HomeActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, PageListScrollView.OnScrollToBottomListener, View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static SharedPreferences sharedPref;

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
    final ArrayList<Target> targets = new ArrayList<>(20);

    private LinearLayout linearLayoutCategory;
    private LinearLayout linearLayoutService;
    private CustomCategoryLayout customCategoryLayout;
    private CustomServiceLayout customServiceLayout;

    String[] advertUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        // badge ????????? ????????? ????????? ???????????????.
        SharedPreferences sharedPref = this.getSharedPreferences(
                "Notifications", Context.MODE_PRIVATE);
        int badgeCount = sharedPref.getInt("badgeCount", -1);
        if (badgeCount > 0) {
            if (TabHostActivity.TabHostStack.getTextViewNotificationsObject() != null) {
                TabHostActivity.TabHostStack.setTextViewNotificationsBadge(badgeCount);
            }
        }
        //////////////////////////////////////////////////////


        config = new Configuration();
        mContext = TabHostActivity.TabHostStack;
        res = mContext.getResources();
        pActivity = (AnimatedActivity) HomeActivity.this.getParent();


        homeActivity = HomeActivity.this;
        tabActivity = (TabActivity) HomeActivity.this.getParent().getParent();
        CommonUtils.customActionBar(mContext, this, false, "");
        /*
        LinearLayout lytFavor1 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail1); //????????????
        lytFavor1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor2 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail2); //????????????
        lytFavor2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor3 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail3); //????????????
        lytFavor3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        LinearLayout lytFavor4 = (LinearLayout) findViewById(R.id.lyt_room_favor_thumbnail4); //????????????
        lytFavor4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        */
//        findViewById(R.id.btn_home_room).setOnClickListener(this);
//        findViewById(R.id.btn_home_invite).setOnClickListener(this);
//        findViewById(R.id.btn_home_car).setOnClickListener(this);
//        findViewById(R.id.btn_home_join).setOnClickListener(this);
//        findViewById(R.id.btn_home_call).setOnClickListener(this);
//        findViewById(R.id.btn_home_travel).setOnClickListener(this);
//        findViewById(R.id.btn_home_tax).setOnClickListener(this);
//        findViewById(R.id.btn_home_old).setOnClickListener(this);
//        findViewById(R.id.btn_home_history).setOnClickListener(this);
//        findViewById(R.id.btn_home_anniver).setOnClickListener(this);
//        findViewById(R.id.btn_home_outland).setOnClickListener(this);
//        findViewById(R.id.btn_home_waitred).setOnClickListener(this);
//        findViewById(R.id.btn_home_house).setOnClickListener(this);
//        findViewById(R.id.btn_home_service).setOnClickListener(this);
//        findViewById(R.id.btn_home_dazhe).setOnClickListener(this);
//        findViewById(R.id.btn_home_educate).setOnClickListener(this);
//        findViewById(R.id.btn_home_animal).setOnClickListener(this);
//        findViewById(R.id.btn_home_waitblue).setOnClickListener(this);
//        findViewById(R.id.img_home_middle_01).setOnClickListener(this);
//        findViewById(R.id.img_home_middle_02).setOnClickListener(this);
//        findViewById(R.id.img_home_middle_03).setOnClickListener(this);
//        findViewById(R.id.rlt_search).setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        linearLayoutCategory = (LinearLayout) findViewById(R.id.linearLayoutCategory);
        try {
            customCategoryLayout = new CustomCategoryLayout(mContext, null, pActivity);
            linearLayoutCategory.addView(customCategoryLayout);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        linearLayoutService = (LinearLayout) findViewById(R.id.img_home_bottom_service);
        try {
            customServiceLayout = new CustomServiceLayout(mContext, null, pActivity);
            linearLayoutService.addView(customServiceLayout);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        ImageView imageView = (ImageView) viewPager.findViewById(R.id.homeImageView);
//        imageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int selectedIndex = homeSliderAdapter.selectedIndex;
//                if(selectedIndex == -1)
//                    return;
////                Intent mainIntent = new Intent(HomeActivity.this, RoomDetailActivity.class);
////                HomeActivity.this.startActivity(mainIntent);
////                HomeActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//                Intent intent = new Intent(HomeActivity.this, AdverViewActivity.class);
//                intent.putExtra("adver_url", advertUrls[selectedIndex]);
////                popUp.dismiss();
//                HomeActivity.this.startActivityForResult(intent,501);
//            }
//        });


//        homeSliderAdapter = new HomeSliderAdapter(HomeActivity.this, images);
//        viewPager.setAdapter(homeSliderAdapter);
        RelativeLayout rlSearch = (RelativeLayout) findViewById(R.id.rlt_search);
        rlSearch.setOnClickListener(this);

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
                intent.putExtra("desc", searchItem.getmTitle01() + " " + searchItem.getmTitle02() + " " + searchItem.getmTitle03());
                String img = searchItem.getmThumbnail();
                CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                if (!img.equals(""))
                    CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                if (CommonUtils.share_bmp == null)
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                HomeActivity.this.startActivity(intent);
            }
        });
        initViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
//        if (id == R.id.btn_home_room){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "1");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_tax){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "2");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_house){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "3");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_invite){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "4");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_history){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "5");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_service){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "6");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_car){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "7");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_old){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "8");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_dazhe){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "9");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_join){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "10");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_anniver){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "11");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_educate){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "12");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_call){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "13");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_outland){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "14");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_animal) {//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "15");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_travel){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//            intent.putExtra("PostItem", "16");
//            pActivity.startChildActivity("activity_search", intent);
//        }
//        else if (id == R.id.btn_home_waitred){//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//        }
//        else if (id == R.id.btn_home_waitblue) {//????????????
//            intent = new Intent(HomeActivity.this, SearchActivity.class);
//        }
//        else if (id == R.id.img_home_middle_01){//????????????-????????????
//            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
//            intent.putExtra("enterprise", "????????????");
//            pActivity.startChildActivity("activity_enterprise", intent);
//        }
//        else if (id == R.id.img_home_middle_02){//????????????-??????
//            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
//            intent.putExtra("enterprise", "??????");
//            pActivity.startChildActivity("activity_enterprise", intent);
//        }
//        else if (id == R.id.img_home_middle_03){//????????????-??????
//            intent = new Intent(HomeActivity.this, EnterpriseListActivity.class);
//            intent.putExtra("enterprise", "??????");
//            pActivity.startChildActivity("activity_enterprise", intent);
//        }
        if (id == R.id.rlt_search) {//??????????????????
            intent = new Intent(HomeActivity.this, SearchActivity.class);
            intent.putExtra("fid", "0");
            intent.putExtra("sortid", "0");
            pActivity.startChildActivity("full_search", intent);
        }
    }

    private void initViews() {
        scrollView = (PageListScrollView) findViewById(R.id.scrollview_home);
        lstSearch.setFocusable(false);
        scrollView.setOnScrollToBottomListener(HomeActivity.this);
        getBanner();
        getCategory();
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
        CommonUtils.showAlertDialog(mContext, dialog, view, 216);
    }

    private void getBanner() {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HomeActivity.this);
        RequestParams params = new RequestParams();
        String url = "api/ui/updateHomeAdverts";
        NetRetrofit.getInstance().post(url, new HashMap<String, Object>(), new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
                    JSONObject response = resp.body();
                    if (response.getInt("code") == 1) {
                        //isLoadMore = response.getBoolean("hasmore");
                        System.out.println("img" + response);
                        JSONArray list = response.getJSONArray("ret");
                        if (list == null || list.length() == 0) {
//                                    isLoadMore = false;
                            //CommonUtils.dismissProgress(progressDialog);
                            return;
                        }
                        SharedPreferences sharedPref = homeActivity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        String[] imageUrls = new String[list.length()];
                        int[] images = new int[list.length()];
                        final Bitmap[] imageBmps = new Bitmap[list.length()];

                        advertUrls = new String[list.length()];

                        final int finalCount = list.length();

                        for (int i = 0; i < finalCount; i++) {
                            JSONObject item = list.getJSONObject(i);

                            editor.putString("advert" + Integer.toString(i + 1), item.toString());

                            String imgUrl = item.optString("advert");
                            imageUrls[i] = imgUrl;
                            images[i] = i;
                            advertUrls[i] = item.optString("link");
                            ;
                        }
                        homeSliderAdapter = new HomeSliderAdapter(HomeActivity.this, imageUrls, advertUrls);
                        viewPager.setAdapter(homeSliderAdapter);
                        editor.apply();

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
                                    if (i == (homeSliderAdapter.getCount() - 1)) {
                                        i = -1;
                                    }
                                }
                            }
                        };
                        new Thread(runnable).start();
                    } else {
                    }

                } catch (JSONException e) {
                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getCategory() {
//        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HomeActivity.this);
        HashMap<String, Object> params = new HashMap<String, Object>();
        SharedPreferences prefs = getSharedPreferences("bxxx", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        String updateTime = prefs.getString("updateTime", null);
        if (updateTime == null) {
            updateTime = "2018-08-23 14:00:00";
        }

        params.put("updateTime", updateTime);

        final String url = "api/ui/update";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
//                    CommonUtils.dismissProgress(progressDialog);
                    JSONObject response = resp.body();
                    assert response != null;
                    if (response.getInt("code") == 1) {
                        //isLoadMore = response.getBoolean("hasmore");
                        System.out.println("img" + response);
                        JSONArray list = response.getJSONArray("ret");
                        if (list == null || list.length() == 0) {
                            return;
                        }


                        final int finalCount = list.length();

                        for (int i = 0; i < finalCount; i++) {
                            final JSONObject item = list.getJSONObject(i);

                            String imgUrl = item.optString("item_icon");
                            if (!imgUrl.toLowerCase().contains("http://")) {
                                imgUrl = APIManager.APP_DOMAIN + imgUrl;
                            }
                            String[] separated = imgUrl.split("/");
                            String real_img_file_name = separated[separated.length - 1];

                            final int finalI = Integer.parseInt(item.optString("item_order"));
                            ContextWrapper cw = new ContextWrapper(mContext);
                            final File directory = cw.getDir("category_icons", Context.MODE_PRIVATE);

                            String iconName = "";
                            final String itemPage = item.optString("item_page");
                            if (itemPage.equals("cat_button")) {
                                iconName = "category_icon_" + real_img_file_name;
                                item.put("imageLocalFilePath", directory + "/" + iconName);
                                editor.putString("category" + Integer.toString(finalI), item.toString());
                            } else {
                                iconName = "service_icon_" + real_img_file_name;
                                item.put("imageLocalFilePath", directory + "/" + iconName);
                                editor.putString("service" + Integer.toString(finalI), item.toString());
                            }

                            final String fileName = iconName;

                            final String finalImgUrl = imgUrl;

                            File check_file = new File(directory, fileName);

                            //???????????? ????????? ???????????? ?????????????????? ???????????? ?????? ????????? ??????????????? ????????????.
                            if (check_file == null || !check_file.exists()) {
                                Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                                        File file = new File(directory, fileName);
                                        try {
                                            file.createNewFile();
                                            FileOutputStream ostream = new FileOutputStream(file);
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                                            ostream.flush();
                                            ostream.close();
                                            if (itemPage.equals("cat_button"))
                                                customCategoryLayout.setCategoryItemData(finalI, file.getAbsolutePath(), item);
                                            else
                                                customServiceLayout.setCategoryItemData(finalI, file.getAbsolutePath(), item);
                                        } catch (IOException e) {
                                            Log.e("IOException", e.getLocalizedMessage());
                                        }
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                };

                                targets.add(target);
                                if (itemPage.equals("cat_button")) {
                                    Picasso.with(mContext)
                                            .load(imgUrl)
                                            .into(target);
                                } else {
                                    Picasso.with(mContext)
                                            .load(imgUrl)
                                            .resize(CommonUtils.getPixelValue(HomeActivity.this, 140), CommonUtils.getPixelValue(HomeActivity.this, 80))
                                            .into(target);
                                }
                            } else {
                                if (itemPage.equals("cat_button"))
                                    customCategoryLayout.setCategoryItemData(finalI, check_file.getAbsolutePath(), item);
                                else
                                    customServiceLayout.setCategoryItemData(finalI, check_file.getAbsolutePath(), item);
                            }
                        }
                        Date currentTime = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + currentTime);

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(currentTime);
                        editor.putString("updateTime", formattedDate);
                        editor.apply();

                    } else {
                    }


                } catch (JSONException e) {
                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
//                CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getArticle() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, HomeActivity.this);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ishome", "true");
        params.put("page", mIntPage);
        String url = "api/news/list";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
                    JSONObject response = resp.body();
                    if (response.getInt("code") == 1) {
                        System.out.println("img" + response);
                        JSONArray list = response.getJSONArray("ret");
                        if (list == null) {
                            isLoadMore = false;
                            return;
                        }

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String tid = item.optString("tid");
                            String fid = item.optString("fid");
                            String sortid = item.optString("sortid");
                            DictionaryUtils dictionaryUtils = new DictionaryUtils();
                            dictionaryUtils.setProperty(item);
                            String img_url = "";
                            if (item.optJSONObject("fields") != null && item.optJSONObject("fields").optJSONObject("picture") != null && !item.optJSONObject("fields").optJSONObject("picture").optString("url").equals("")) {
                                img_url = item.optJSONObject("fields").optJSONObject("picture").optString("url");
                            }
                            System.out.println("img" + i + img_url);
                            String desc = item.optString("title");
                            String txt_home_favor_price = dictionaryUtils.getProperty("txt_home_favor_price");
                            System.out.println("price" + i + txt_home_favor_price);
//                                    String property01 = dictionaryUtils.getProperty("txt_property1");
//                                    String property02 = dictionaryUtils.getProperty("txt_property2");
//                                    String property03 = dictionaryUtils.getProperty("txt_property3");
//                                    String poststick = item.optString("poststick");
                            if (!tid.equals("")) {
                                searchItemListAdapter.addItem(new SearchItem(
                                        Long.parseLong(tid), img_url, desc, dictionaryUtils, item));
//                                        searchItemListAdapter.addItem(new SearchItem(
//                                                Long.parseLong(tid), img_url, desc, txt_home_favor_price, property01, "", property02, "", property03, "", fid, sortid, poststick, "",""));
                            }
                        }
                        isLoadMore = list.length() >= 10;
                        mIntPage++;
                    } else {
                        isLoadMore = false;
                        if (mIntPage == 1) {
                            searchItemListAdapter.clearItems();
                        }
                        mIntPage = 1;
                    }
                    searchItemListAdapter.notifyDataSetChanged();
//                    lstSearch.invalidateViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        //progressDialog.dismiss();
//                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
//                    }
        });
//            }
//        }, 5);
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
                        case "2"://????????????
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
                        case "39"://????????????
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
                        case "40"://????????????
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
                        case "38"://????????????
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
                        case "42"://????????????
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
                        case "48"://????????????
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
                        case "74"://????????????
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
                        case "92"://????????????
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
                        case "83"://????????????
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
                        case "91"://????????????
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
                        case "108"://????????????
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
                        case "93"://????????????
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
                        case "94"://????????????
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
                        case "44"://????????????
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
                        case "50"://????????????
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
        //?????????????????????????????????
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
            }, 5);//???????????????????????????2??????
        }
    }

    @Override
    public void onBackPressed() {
        this.getParent().getParent().onBackPressed();
    }

}
