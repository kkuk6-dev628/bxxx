package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.NotificationItemListAdapter;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cn.reservation.app.baixingxinwen.utils.NotificationItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String userID;
    private ListView lstNotifications;
    NotificationItemListAdapter notificationItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    private String notificationType;
    public EditText mEditKeyword;
    public ArrayList<NotificationItem> chatItems = new ArrayList<NotificationItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) NotificationsActivity.this.getParent();
        Intent intent = getIntent();
        notificationType = (String) intent.getSerializableExtra("notificationType");

        String title = "网站通知";
        if (notificationType.equals("master")) {
            title = "站长通知";
        }
        CommonUtils.customActionBar(mContext, this, true, title);
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        //userID = pref.getString("uid","");
        lstNotifications = (ListView) findViewById(R.id.lst_notifications);
        notificationItemListAdapter = new NotificationItemListAdapter(this);
        notificationItemListAdapter.setListItems(chatItems);
        lstNotifications.setAdapter(notificationItemListAdapter);

        mIntPage = 1;
        getWebNotifications();

        if (!notificationType.equals("master")) {
            lstNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NotificationItem notiItem = (NotificationItem) notificationItemListAdapter.getItem(position);
                    String tid = notiItem.getmTID();
                    String uid = notiItem.getmUid();
                    String name = notiItem.getmTitle();
                    SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
                    String myid = Long.toString(pref.getLong("userID", 0));


                    if (!notificationType.equals("master")) {
                        Intent intent = new Intent(NotificationsActivity.this, AboutViewActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("hId", tid);
                        intent.putExtra("isWebNotification", "1");
                        intent.putExtra("title", "网站通知");
                        pActivity.startChildActivity("about_activity", intent);
                    }
                }
            });
        }


//        lstNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                NotificationItem notiItem = (NotificationItem) notificationItemListAdapter.getItem(position);
//                String tid = notiItem.getmTID();
//                String uid = notiItem.getmUid();
//                String name = notiItem.getmTitle();
//                SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
//                String myid = Long.toString(pref.getLong("userID", 0));
//
//
//
//                if(!notificationType.equals("master")){
//                    Intent intent = new Intent(NotificationsActivity.this, AboutViewActivity.class);
//                    intent.putExtra("uid", uid);
//                    intent.putExtra("hId", tid);
//                    intent.putExtra("isWebNotification", "1");
//                    intent.putExtra("title","网站通知");
//                    NotificationsActivity.this.startActivity(intent);
//                }
//            }
//        });

        lstNotifications.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {
                        getWebNotifications();
                        isLoadMore = false;
                    }
                }
            }
        });
    }

    private void getWebNotifications() {
        HashMap<String, Object> params = new HashMap<>();
        System.out.println("UserID+++:" + userID);

        String url = "api/message/announcelist";
        if (notificationType.equals("master")) {
            //점장통지인 경우
            url = "api/message/adminhistory";
            params.put("uid", userID);
        } else {
            // 웹통지인 경우
//            params.put("fid", "69");
//            params.put("sortid", "0");
            params.put("page", mIntPage);
        }
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                JSONObject response = resp.body();
                try {
                    if (response.optInt("code") == 1) {
                        JSONArray list;
                        if (!notificationType.equals("master")) {
                            // 웹통지인 경우
                            list = response.optJSONArray("ret");
                        } else {
                            // 점장통지인 경우
                            if (response.optJSONObject("ret") == null ||
                                    response.optJSONObject("ret").length() == 0 ||
                                    response.optJSONObject("ret").optJSONArray("history") == null ||
                                    response.optJSONObject("ret").optJSONArray("history").length() == 0) {
                                //아무것도 없으면 빈 array 를 창조
                                list = new JSONArray();
                            } else {
                                list = response.optJSONObject("ret").optJSONArray("history");

                            }
                        }

                        System.out.println(list.length() + "++++" + response);
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String desc = "";
                            String title = "";
                            String tid = "";
                            String dateline = "";

                            if (notificationType.equals("master")) {
                                //점장통지인 경우
                                desc = item.optString("message");
                                dateline = item.optString("sendtime");
                            } else {
                                //웹통지인 경우
                                tid = item.optString("tid");
                                title = item.optString("title");
                                dateline = item.optString("dateline");
                            }


                            if (item.has("desc")) {
                                desc = item.optString("desc");
                            }
                            notificationItemListAdapter.addItem(new NotificationItem(notificationType, tid, userID,
                                    title, dateline, desc));
//                                    notificationItemListAdapter.addItem(new ChatItem(
//                                            item.getLong("plid"), item.optString("avatar"), item.optString("lastmessage"), item.optString("name"), item.optString("time"), item.optString("plid"), item.optString("uid")));
                        }
                        if (list.length() == 10) {
                            isLoadMore = true;
                            mIntPage++;
                        } else {
                            isLoadMore = false;
                        }

                    } else {
                        if (mIntPage == 1) {
                            notificationItemListAdapter.clearItems();
                        }
                        mIntPage = 1;
                    }
                    if (notificationItemListAdapter != null) {
                        notificationItemListAdapter.notifyDataSetChanged();
                    }
                    lstNotifications.invalidateViews();
                    //CommonUtils.dismissProgress(progressDialog);

                } catch (JSONException e) {
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

    private void gotoArticleView(HealthArticleItem healthArticleItem) {
        Intent intent = new Intent(NotificationsActivity.this, NewsViewActivity.class);
        intent.putExtra("Article", healthArticleItem);
        pActivity.startChildActivity("health_article", intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(NotificationsActivity.this, NewsActivity.class);

        pActivity.startChildActivity("news_activity", intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(NotificationsActivity.this, NewsActivity.class);

            pActivity.startChildActivity("news_activity", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
