package cn.reservation.app.baixingxinwen.activity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.NewsItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.NewsItem;
import cn.reservation.app.baixingxinwen.wxapi.OnResponseListener;
import cn.reservation.app.baixingxinwen.wxapi.WXShare;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class UserNewsActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private int showAction;
    private int mIntPage = 1;
    private ListView lstNews;
    private boolean isLoadMore = false;
    public NewsItem newsItem;
    private Integer sel_position;
    public NewsItemListAdapter newsItemListAdapter;
    public ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
    private WXShare wxShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) UserNewsActivity.this.getParent();
        final TabActivity tabActivity = (TabActivity) UserNewsActivity.this.getParent().getParent();


        CommonUtils.customActionBar(mContext, this, true, "我的信息");
        showAction = 0;
        mIntPage = 1;
        Intent intent = getIntent();

        lstNews = (ListView) findViewById(R.id.lst_my_news);
        newsItemListAdapter = new NewsItemListAdapter(this);
        newsItemListAdapter.setListItems(newsItems);
        lstNews.setAdapter(newsItemListAdapter);
        //getArticle();
        lstNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast toast = Toast.makeText(mContext, "再点击一下吧。", Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                //toast.show();
                LinearLayout lyt_room_favor_thumbnail = (LinearLayout) view.findViewById(R.id.lyt_room_favor_thumbnail);
                lyt_room_favor_thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NewsItem searchItem = (NewsItem) newsItemListAdapter.getItem(position);
                        Long long_id = searchItem.getmNewsID();
                        System.out.println("tid+++" + long_id);
                        Intent intent = new Intent(UserNewsActivity.this, RoomDetailActivity.class);
                        String fid = searchItem.getmFid();
                        String sortid = searchItem.getmSortid();
                        intent.putExtra("fid", fid);
                        intent.putExtra("sortid", sortid);
                        intent.putExtra("newsId", Long.toString(long_id));
                        String url = APIManager.User_URL + "news/paper/" + String.valueOf(long_id);
                        String title = searchItem.getmTitle();
                        String desc = searchItem.getmPostState() + " " + searchItem.getmStartTime() + " " + searchItem.getmViewCnt();
                        String img = searchItem.getmImage();
                        CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        if (!img.equals(""))
                            CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                        if (CommonUtils.share_bmp == null)
                            CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                        /*
                        if(CommonUtils.share_bmp.getByteCount()>23000) {
                            if (CommonUtils.share_bmp != null && !CommonUtils.share_bmp.isRecycled()) {
                                CommonUtils.share_bmp = Bitmap.createScaledBitmap(CommonUtils.share_bmp, 32, 32, true);
                            }
                        }
                        */
                        intent.putExtra("url", url);
                        intent.putExtra("title", title);
                        intent.putExtra("desc", desc);
                        intent.putExtra("img", img);
                        UserNewsActivity.this.startActivity(intent);
                    }
                });
                RelativeLayout updateInfoRLT = (RelativeLayout) view.findViewById(R.id.rlt_news_btn1);
                updateInfoRLT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sel_position = position;
                        updateArticle();
                    }
                });
                RelativeLayout etcInfoRLT = (RelativeLayout) view.findViewById(R.id.rlt_news_btn4);
                etcInfoRLT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sel_position = position;
                        System.out.println("position:" + position);
                        //String name_state = "置顶,删除,点击清零,下架";
                        String name_state = "置顶,删除,点击清零";
                        if (showAction == 0) {
                            createActionSheet(name_state);
                            showAction = 1;
                        }
                    }
                });
                RelativeLayout editInfoRLT = (RelativeLayout) view.findViewById(R.id.rlt_news_btn2);
                editInfoRLT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sel_position = position;
                        gotoUpdate();
                    }
                });
                RelativeLayout shareInfoRLT = (RelativeLayout) view.findViewById(R.id.rlt_news_btn3);
                shareInfoRLT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sel_position = position;
                        /*
                        System.out.println("position:"+position);
                        String name_state = "分享好友,分享朋友圈";
                        if(showAction==0){
                            createActionSheet(name_state);
                            showAction=2;
                        }
                        */
                        createActionSheet();
                    }
                });
            }

        });

        lstNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getArticle();
                        isLoadMore = false;
                    }
                }
            }
        });
        wxShare = new WXShare(this);
        wxShare.register();
        wxShare.setListener(new OnResponseListener() {
            @Override
            public void onSuccess() {
                // 分享成功
            }

            @Override
            public void onCancel() {
                // 分享取消
            }

            @Override
            public void onFail(String message) {
                // 分享失败
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        wxShare.register();
    }

    @Override
    protected void onDestroy() {
        wxShare.unregister();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntPage = 1;
        newsItemListAdapter.clearItems();
        getArticle();
    }

    private void getArticle() {
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserNewsActivity.this);
        if (!CommonUtils.isLogin) {
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", CommonUtils.userInfo.getUserID());
        params.put("page", mIntPage);
        String url = "api/news/mine";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                try {
                    CommonUtils.dismissProgress(progressDialog);
                    JSONObject response = res.body();
                    if (response != null && response.getInt("code") == 1) {
                        //isLoadMore = response.getBoolean("hasmore");
                        JSONArray list = response.getJSONArray("ret");
                        if (list == null) {
                            isLoadMore = false;
                            //CommonUtils.dismissProgress(progressDialog);
                            return;
                        } else if (list.length() < 8) {
                            isLoadMore = false;
                        } else {
                            isLoadMore = true;
                        }
                        System.out.println(response);
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String img_url = item.optString("picture");
                            String title = item.optString("subject");
                            String tid = item.optString("tid");
                            String starttime = item.optString("dateline");
                            String toptime = item.optString("orderdate");
                            String endtime = item.optString("poststickdate");
                            String status = item.optString("status");
                            String post = item.optString("poststick");
                            String views = item.optString("views");
                            String paid = item.optString("paid");
                            if (!(starttime.equals("") || starttime.equals("null"))) {
                                starttime = starttime + " 发布";
                            }
                            if (!(endtime.equals("") || endtime.equals("null"))) {
                                endtime = endtime + " 到期";
                            } else {
                                endtime = "";
                            }
                            if (!(toptime.equals("") || toptime.equals("null"))) {
                                toptime = toptime + " 置顶";
                            } else {
                                toptime = "";
                            }
                            System.out.println(toptime + ":toptime");
                            System.out.println(endtime + ":endtime");
                            newsItemListAdapter.addItem(new NewsItem(
                                    Long.parseLong(tid), title, status, img_url, starttime, toptime, views, post, item.optString("fid"), item.optString("sortid"), paid, endtime));
                        }
                        mIntPage++;
                    } else {
                        isLoadMore = false;
                        if (mIntPage == 1) {
                            newsItemListAdapter.clearItems();
                        }
                        mIntPage = 1;
                    }
                    newsItemListAdapter.notifyDataSetChanged();
                    lstNews.invalidateViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void updateArticle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserNewsActivity.this);
                RequestParams params = new RequestParams();
                if (CommonUtils.isLogin) {
                    params.put("uid", CommonUtils.userInfo.getUserID());
                } else {
                    //progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmFid();
                Long tid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                params.put("fid", fid);
                params.put("tid", tid);
                String url = "news/update";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println(response);
                        try {
                            if (response.getInt("code") == 1) {


                            }
                            //CommonUtils.dismissProgress(progressDialog);
                            Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
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
        }, 500);
    }

    private void clearArticle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserNewsActivity.this);
                RequestParams params = new RequestParams();
                if (CommonUtils.isLogin) {
                    params.put("uid", CommonUtils.userInfo.getUserID());
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmFid();
                Long tid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                params.put("fid", fid);
                params.put("tid", tid);
                String url = "news/clear";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("11" + response);
                        try {
                            if (response.getInt("code") == 1) {
                                mIntPage = 1;
                                newsItemListAdapter.clearItems();
                                getArticle();
                            }
                            CommonUtils.dismissProgress(progressDialog);
                            Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
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

    private void deleteArticle() {

        if (!CommonUtils.isLogin) {
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
            return;
        }
        String paid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPaid();
        String post = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPostState();
        if (post.equals("1")) {
            //progressDialog.dismiss();
            Toast.makeText(mContext, "置顶帖不可以删除", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paid.equals("1")) {
            //progressDialog.dismiss();
            Toast.makeText(mContext, "付费帖不可以删除", Toast.LENGTH_SHORT).show();
            return;
        }

        View logout_view = getLayoutInflater().inflate(R.layout.alert_exit, null);
        TextView txt_alert_dlg_content = (TextView) logout_view.findViewById(R.id.txt_alert_dialog_content);
        final Dialog dialog = new Dialog(mContext);

        txt_alert_dlg_content.setText(getString(R.string.delete_news_message));
        TextView btnLogout_weixin = (TextView) logout_view.findViewById(R.id.btn_ok);
        TextView btnExit = (TextView) logout_view.findViewById(R.id.btn_cancel);
        btnLogout_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p_view) {
                dialog.dismiss();
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserNewsActivity.this);
                HashMap<String, Object> params = new HashMap<>();
                String fid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmFid();
                Long tid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                params.put("uid", CommonUtils.userInfo.getUserID());
                params.put("fid", fid);
                params.put("tid", tid);
                String url = "api/news/delete";
                NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                        try {
                            CommonUtils.dismissProgress(progressDialog);
                            JSONObject response = res.body();
                            if (response != null && response.getInt("code") == 1) {
                                mIntPage = 1;
                                newsItemListAdapter.clearItems();
                                getArticle();
                            }
                            Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        CommonUtils.dismissProgress(progressDialog);
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p_view) {
                dialog.dismiss();
            }
        });
        CommonUtils.showAlertDialog(mContext, dialog, logout_view, 216);

    }

    public void createActionSheet(String state_name) {
        String[] state = state_name.split(",");
        ActionSheet.createBuilder(mContext, UserNewsActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(state)
                .setCancelableOnTouchOutside(true)
                .setListener(UserNewsActivity.this)
                .show();
    }

    public void createActionSheet() {

        LinearLayout lyt_share_panel = (LinearLayout) findViewById(R.id.lyt_share_panel);
        lyt_share_panel.removeAllViews();
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView1 = inflater.inflate(R.layout.dropdown_share, null);
        RelativeLayout share_weixin = (RelativeLayout) rowView1.findViewById(R.id.lyt_share_weixin);
        share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout lyt_share_panel = (LinearLayout) findViewById(R.id.lyt_share_panel);
                lyt_share_panel.removeAllViews();
                String img = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmImage();
                if (!img.equals(""))
                    CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                if (CommonUtils.share_bmp == null)
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                Long newsId = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                String url = APIManager.User_URL + "news/paper/" + String.valueOf(newsId) + "/1";
                String title = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmTitle();
                String desc = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPostState() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmStartTime() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmViewCnt() + "浏览";
                ;
                wxShare.sharePaper(title, desc, url, CommonUtils.share_bmp, 0);
            }
        });
        RelativeLayout share_friend = (RelativeLayout) rowView1.findViewById(R.id.lyt_share_friends);
        share_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout lyt_share_panel = (LinearLayout) findViewById(R.id.lyt_share_panel);
                lyt_share_panel.removeAllViews();
                String img = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmImage();
                if (!img.equals(""))
                    CommonUtils.share_bmp = CommonUtils.getBitmapFromURL(img);
                if (CommonUtils.share_bmp == null)
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                Long newsId = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                String url = APIManager.User_URL + "news/paper/" + String.valueOf(newsId) + "/1";
                String title = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmTitle();
                String desc = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPostState() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmStartTime() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmViewCnt() + "浏览";
                ;
                wxShare.sharePaper(title, desc, url, CommonUtils.share_bmp, 1);
            }
        });
        RelativeLayout share_cancel = (RelativeLayout) rowView1.findViewById(R.id.lyt_share_cancel);
        share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout lyt_share_panel = (LinearLayout) findViewById(R.id.lyt_share_panel);
                lyt_share_panel.removeAllViews();
            }
        });
        lyt_share_panel.addView(rowView1, lyt_share_panel.getChildCount());
    }

    public void gotoUpdate() {
        Intent intent = new Intent(UserNewsActivity.this, UpdateActivity.class);
        String fid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmFid();
        String tid = String.valueOf(((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID());
        String sortid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmSortid();
        intent.putExtra("fid", fid);
        intent.putExtra("tid", tid);
        intent.putExtra("sortid", sortid);
        pActivity.startActivityForResult(intent, 9);
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
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        showAction = 0;
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if (showAction == 2) {
            switch (index) {
                case 0:
                    String img = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmImage();
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    if (!img.equals(""))
                        thumb = CommonUtils.getBitmapFromURL(img);
                    if (thumb == null)
                        thumb = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    /*
                    if(thumb.getByteCount()>23000) {
                        if (thumb != null && !thumb.isRecycled()) {
                            thumb = Bitmap.createScaledBitmap(thumb, 32, 32, true);
                        }
                    }
                    */
                    Long newsId = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                    String url = APIManager.User_URL + "news/paper/" + String.valueOf(newsId);
                    String title = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmTitle();
                    String desc = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPostState() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmStartTime() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmViewCnt() + "浏览";
                    ;
                    wxShare.sharePaper(title, desc, url, thumb, 0);
                    break;
                case 1:
                    img = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmImage();
                    thumb = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    if (!img.equals(""))
                        thumb = CommonUtils.getBitmapFromURL(img);
                    if (thumb == null)
                        thumb = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                    /*
                    if(thumb.getByteCount()>23000) {
                        if (thumb != null && !thumb.isRecycled()) {
                            thumb = Bitmap.createScaledBitmap(thumb, 32, 32, true);
                        }
                    }
                    */
                    newsId = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID();
                    url = APIManager.User_URL + "news/paper/" + String.valueOf(newsId);
                    title = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmTitle();
                    desc = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmPostState() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmStartTime() + " " + ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmViewCnt() + "浏览";
                    ;
                    wxShare.sharePaper(title, desc, url, thumb, 1);
                    //wxShare.share(title, desc,1);
                    break;
            }
        } else {
            switch (index) {
                case 0:
                    Intent intent = new Intent(UserNewsActivity.this, ChargeActivitySet.class);
                    String fid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmFid();
                    String tid = String.valueOf(((NewsItem) lstNews.getItemAtPosition(sel_position)).getmNewsID());
                    String sortid = ((NewsItem) lstNews.getItemAtPosition(sel_position)).getmSortid();
                    intent.putExtra("fid", fid);
                    intent.putExtra("tid", tid);
                    intent.putExtra("sortid", sortid);
                    UserNewsActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_ANOTHER);
                    break;
                case 1:
                    deleteArticle();
                    break;
                case 2:
                    clearArticle();
                    break;
                case 3:
                    break;
            }
        }
        showAction = 0;
        actionSheet.dismiss();
    }

}
