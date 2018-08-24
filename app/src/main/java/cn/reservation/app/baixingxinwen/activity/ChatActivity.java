package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.ChatTextItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.ChatTextItem;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.HealthArticleItem;
import cz.msebera.android.httpclient.Header;

public class ChatActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String userID;
    private String tID;
    private String toID;
    private String toName;
    private String toAvatar;
    private String message;
    private long maxid;
    private String meAatar;
    private ListView lstChat;
    ChatTextItemListAdapter chatTextItemListAdapter;
    private boolean isLoadMore = false;
    private int mIntPage = 1;
    public EditText mEditKeyword;
    public ArrayList<ChatTextItem> chatItems = new ArrayList<ChatTextItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) ChatActivity.this.getParent();
        message = "";
        maxid = 0;
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));

        //tID = pref.getString("tid","");
        meAatar = pref.getString("userPhoto","");
        String userName = pref.getString("userName", "");
        Intent intent = getIntent();
        toID = (String) intent.getSerializableExtra("uid");
        tID = (String) intent.getSerializableExtra("tid");
        toName = (String) intent.getSerializableExtra("name");
        toAvatar = (String) intent.getSerializableExtra("avatar");
        //userID = pref.getString("uid","");
        CommonUtils.customActionBar(mContext, this, true, toName);
        lstChat = (ListView) findViewById(R.id.lst_chat_history);
        chatTextItemListAdapter = new ChatTextItemListAdapter(this);
        chatTextItemListAdapter.setListItems(chatItems);
        lstChat.setAdapter(chatTextItemListAdapter);
        lstChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatTextItem chatItem = (ChatTextItem) chatTextItemListAdapter.getItem(position);
                Long long_id = chatItem.getmChatID();
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
        ImageView img_write_pen = (ImageView) findViewById(R.id.img_write_pen);
        img_write_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatFriends();
            }
        });
        RelativeLayout rlt_send_chat =(RelativeLayout) findViewById(R.id.rlt_chat_send);
        rlt_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatFriends();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatFriends();
    }

    private void getChatFriends() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                System.out.println("UserID+++:"+userID);
                if(userID.equals("") || userID.equals("0")){
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("from", userID);
                if(toID==null || toID.equals("null") || toID.equals("")){
                    params.put("tid",tID);
                    System.out.println("from++"+userID);
                    System.out.println("to++"+toID);
                    System.out.println("tID++"+tID);
                }else{
                    params.put("to", toID);
                    System.out.println("fromtt++"+userID);
                    System.out.println("tott++"+toID);
                    System.out.println("tIDtt++"+tID);
                }
                //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChatActivity.this);
                //params.put("from", 1);
                //params.put("to", 1665256);
                String url = "message/history";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.optInt("code") == 1) {
                                if(response.optJSONObject("ret")==null || response.optJSONObject("ret").optString("me")=="" || response.optJSONObject("ret").optString("history")==""){
                                    //CommonUtils.dismissProgress(progressDialog);
                                    return;
                                }
                                JSONArray list = response.getJSONObject("ret").optJSONArray("history");
                                maxid = list.length()+1;
                                //meAatar = response.optJSONObject("ret").optString("me");
                                //if(list!=null) {
                                    for (int i = 0; i < list.length(); i++) {
                                        JSONObject item = list.getJSONObject(i);
                                        System.out.println(item.optString("message")+"++"+i);
                                        System.out.println(item.optString("username")+"--"+i);
                                        System.out.println(item.optString("sendtime")+"-time-"+i);
                                        if(item.optInt("isme")==1){
                                            if(!item.optString("message").trim().equals("")) {
                                                chatTextItemListAdapter.addItem(new ChatTextItem(
                                                        i, "", "", "", response.getJSONObject("ret").optString("me"), item.optString("message"), item.optString("username"), item.optString("sendtime"), "", String.valueOf(i)));
                                            }
                                        }else{
                                            if(!item.optString("message").trim().equals("")) {
                                                chatTextItemListAdapter.addItem(new ChatTextItem(
                                                        i, response.getJSONObject("ret").optString("partner"), item.optString("message"), item.optString("username"), "", "", "", item.optString("sendtime"), "", String.valueOf(i)));
                                            }
                                        }

                                    }
                                //}
                                mIntPage++;
                            } else {
                                if (mIntPage == 1) {
                                    chatTextItemListAdapter.clearItems();
                                }
                                mIntPage = 1;
                            }
                            if(chatTextItemListAdapter!=null) {
                                chatTextItemListAdapter.notifyDataSetChanged();
                            }
                            lstChat.invalidateViews();
                            ImageView img_write_pen = (ImageView) findViewById(R.id.img_write_pen);
                            RelativeLayout rlt_send_chat =(RelativeLayout) findViewById(R.id.rlt_chat_send);
                            rlt_send_chat.setEnabled(true);
                            img_write_pen.setEnabled(true);
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
    private void sendChatFriends() {
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChatActivity.this);
        ImageView img_write_pen = (ImageView) findViewById(R.id.img_write_pen);
        RelativeLayout rlt_send_chat =(RelativeLayout) findViewById(R.id.rlt_chat_send);
        rlt_send_chat.setEnabled(false);
        img_write_pen.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                message = ((EditText)findViewById(R.id.edit_chat_text)).getText().toString();
                System.out.println("UserID+++:"+userID);
                System.out.println("toId++"+toID);
                if(userID=="" || userID=="0"){
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(message.equals("")){
                    Toast.makeText(mContext, "请输入文字", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                if(toID==null || toID.equals("null") || toID.equals("")){
                    params.put("tid",tID);
                    System.out.println("from1++"+userID);
                    System.out.println("to1++"+toID);
                    System.out.println("tID1++"+tID);
                }else{
                    params.put("ruid", toID);
                    System.out.println("fromtt1++"+userID);
                    System.out.println("tott1++"+toID);
                    System.out.println("tIDtt1++"+tID);
                }
                params.put("message", message);
                String url = "message/send";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        /*
                        if (response.optInt("code") == 1) {
                            chatTextItemListAdapter.addItem(new ChatTextItem(
                                      maxid, "", "", "", meAatar, message, toName, "刚刚", "", String.valueOf(maxid)));

                            mIntPage++;
                        } else {
                            if (mIntPage == 1) {
                                chatTextItemListAdapter.clearItems();
                            }
                            mIntPage = 1;
                        }
                        if(chatTextItemListAdapter!=null) {
                            chatTextItemListAdapter.notifyDataSetChanged();
                        }
                        lstChat.invalidateViews();

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lstChat.getWindowToken(), 0);
                        lstChat.post(new Runnable(){
                            public void run() {
                                lstChat.setSelection(lstChat.getCount() - 1);
                            }});
                         */
                        ((EditText)findViewById(R.id.edit_chat_text)).setText("");
                        Toast toast = Toast.makeText(mContext, "发送成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);;
                        toast.show();
                        getChatFriends();
                        CommonUtils.dismissProgress(progressDialog);
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
        Intent intent = new Intent(ChatActivity.this, NewsViewActivity.class);
        intent.putExtra("Article", healthArticleItem);
        pActivity.startChildActivity("health_article", intent);
    }

    @Override
    public void onBackPressed() {
        ChatActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ChatActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
