package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.ChatItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.ChatItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cz.msebera.android.httpclient.Header;

public class NewsActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String userID;
    private ListView lstChat;
    ChatItemListAdapter chatItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText mEditKeyword;
    public ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) NewsActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "我的消息");
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        //userID = pref.getString("uid","");
        lstChat = (ListView) findViewById(R.id.lst_chat_friends);
        chatItemListAdapter = new ChatItemListAdapter(this);
        chatItemListAdapter.setListItems(chatItems);
        lstChat.setAdapter(chatItemListAdapter);

        getMasterNotifications();
        getWebNotifications();
        getChatFriends();
        lstChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatItem chatItem = (ChatItem) chatItemListAdapter.getItem(position);
                Long long_id = chatItem.getmChatID();
                String uid = chatItem.getmUid();
                String name = chatItem.getmName();
                String avatar = chatItem.getmThumbnail();
                SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
                String myid = Long.toString(pref.getLong("userID", 0));
                if(uid!=myid) {
                    Intent intent = new Intent(NewsActivity.this, ChatActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("name", name);
                    intent.putExtra("avatar",avatar);
                    NewsActivity.this.startActivity(intent);
                }
            }
        });

        lstChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (isLoadMore) {

                    }
                }
            }
        });


        LinearLayout webLinearlayout = (LinearLayout)findViewById(R.id.lyt_web_notification_thumbnail);
        webLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, NotificationsActivity.class);
                intent.putExtra("notificationType", "website");
                pActivity.startChildActivity("notification_activity", intent);
//                NewsActivity.this.startActivity(intent);
            }
        });

        LinearLayout masterLinearlayout = (LinearLayout)findViewById(R.id.lyt_master_notification_thumbnail);
        masterLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, NotificationsActivity.class);
                intent.putExtra("notificationType", "master");
                pActivity.startChildActivity("notification_activity", intent);
//                NewsActivity.this.startActivity(intent);
            }
        });


    }

    private void getChatFriends() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, NewsActivity.this);
                RequestParams params = new RequestParams();
                System.out.println("UserID+++:"+userID);
                if(userID=="" || userID=="0"){
                    //progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);

                String url = "message/friends";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("chat Friends"+response);
                        try {
                            if (response.optInt("code") == 1) {
                                JSONArray list = response.optJSONArray("friends");
                                System.out.println(list.length()+"++++"+response);
                                for(int i=0; i<list.length(); i++) {
                                    JSONObject item = list.getJSONObject(i);
                                    chatItemListAdapter.addItem(new ChatItem(
                                            item.getLong("plid"), item.optString("avatar"), item.optString("lastmessage"), item.optString("name"), item.optString("time"), item.optString("plid"), item.optString("uid")));
                                }
                                mIntPage++;
                            } else {
                                if (mIntPage == 1) {
                                    chatItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            if(chatItemListAdapter!=null) {
                                chatItemListAdapter.notifyDataSetChanged();
                            }
                            lstChat.invalidateViews();
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

    private void getWebNotifications() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, NewsActivity.this);
                RequestParams params = new RequestParams();
//                System.out.println("UserID+++:"+userID);
//                if(userID=="" || userID=="0"){
//                    //progressDialog.dismiss();
//                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                params.put("uid", userID);

                String url = "message/announce";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("chat Friends"+response);
                        if (response.optInt("code") == 1) {
                            JSONObject retObj = response.optJSONObject("ret");
                            String webNotificationCount = retObj.optString("count");
                            String subject = retObj.optJSONObject("last").optString("subject");
                            String last_dateline = retObj.optJSONObject("last").optString("dateline");

                            TextView txtCount = (TextView) findViewById(R.id.txt_web_notification_count);
                            txtCount.setText(webNotificationCount);

                            TextView txtLastDateline = (TextView) findViewById(R.id.txt_web_notification_time);
                            txtLastDateline.setText(last_dateline);

                            TextView txtSubject = (TextView) findViewById(R.id.txt_web_notification_last_title);
                            txtSubject.setText(subject);

                        } else {

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

    private void getMasterNotifications() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, NewsActivity.this);
                RequestParams params = new RequestParams();
//                System.out.println("UserID+++:"+userID);
//                if(userID=="" || userID=="0"){
//                    //progressDialog.dismiss();
//                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                params.put("uid", userID);

                String url = "message/adminhistory";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("chat Friends"+response);
                        if (response.optInt("code") == 1) {
                            if(response.optJSONObject("ret") == null ||
                                    response.optJSONObject("ret").length() == 0 ||
                                    response.optJSONObject("ret").optJSONArray("history") == null ||
                                    response.optJSONObject("ret").optJSONArray("history").length() == 0){
                                TextView txtCount = (TextView) findViewById(R.id.txt_master_notification_count);
                                txtCount.setText("");

                                TextView txtLastDateline = (TextView) findViewById(R.id.txt_master_notification_time);
                                txtLastDateline.setText("");

                                TextView txtSubject = (TextView) findViewById(R.id.txt_master_notification_last_title);
                                txtSubject.setText("");
                            }
                            else{
                                JSONArray retArray = response.optJSONObject("ret").optJSONArray("history");
                                String webNotificationCount = Integer.toString(retArray.length());
                                String desc = null;
                                try {
                                    JSONObject retObj = retArray.getJSONObject(0);
                                    desc = retObj.optString("message");
                                    String last_dateline = retObj.optString("sendtime");

                                    TextView txtCount = (TextView) findViewById(R.id.txt_master_notification_count);
                                    txtCount.setText(webNotificationCount);

                                    TextView txtLastDateline = (TextView) findViewById(R.id.txt_master_notification_time);
                                    txtLastDateline.setText(last_dateline);

                                    TextView txtSubject = (TextView) findViewById(R.id.txt_master_notification_last_title);
                                    txtSubject.setText(desc);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        } else {

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

    private void gotoArticleView(HealthArticleItem healthArticleItem) {
        Intent intent = new Intent(NewsActivity.this, NewsViewActivity.class);
        intent.putExtra("Article", healthArticleItem);
        pActivity.startChildActivity("health_article", intent);
    }

    @Override
    public void onBackPressed() {

        //pActivity.finishChildActivity();
        //this.getParent().getParent().onBackPressed();
        /*
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            pActivity.finishChildActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        */
//        Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
//        pActivity.startChildActivity("home", intent);
        TabHostActivity.tabWidget.setCurrentTab(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
//            pActivity.startChildActivity("home", intent);
            TabHostActivity.tabWidget.setCurrentTab(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
