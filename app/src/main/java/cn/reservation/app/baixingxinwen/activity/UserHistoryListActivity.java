package cn.reservation.app.baixingxinwen.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@SuppressWarnings("deprecation")
public class UserHistoryListActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = UserHistoryListActivity.class.getSimpleName();

    private Context mContext;
    AnimatedActivity pActivity;
    private Resources res;
    private LinearLayout lyt_history_parent;
    private String userID;
    public int mRegionID;
    private ImageView img_history_delete;
    private int del_state;
    private RelativeLayout rlt_my_history_del_btn;
    private LinearLayout lyt_my_history;
    private boolean allsel;
    private String[] arr_tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        mContext = TabHostActivity.TabHostStack;
        pActivity = (AnimatedActivity) UserHistoryListActivity.this.getParent();
        res = mContext.getResources();
        arr_tid = new String[10];
        Intent intent = getIntent();
        mRegionID = intent.getIntExtra("RegionID", 0);
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userID = Long.toString(pref.getLong("userID", 0));
        CommonUtils.customActionBar(mContext, this, false, "");
        lyt_history_parent = (LinearLayout) findViewById(R.id.lyt_history_parent);
        RelativeLayout lytBack = (RelativeLayout) findViewById(R.id.layout_back);
        lytBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHistoryListActivity.this, MeActivity.class);
                pActivity.startChildActivity("me", intent);
            }
        });
        img_history_delete = (ImageView) findViewById(R.id.img_history_delete);
        del_state = 0;
        img_history_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyt_my_history = (LinearLayout) findViewById(R.id.rlt_my_history_btn);
                if(del_state==0) {
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            5.0f

                    );
                    img_history_delete.setImageResource(R.drawable.navi_time_up);
                    lyt_my_history.setLayoutParams(param1);
                    del_state=-1;

                    System.out.println(lyt_history_parent.getChildCount()+"++ccc");

                    for(int i=0;i<lyt_history_parent.getChildCount();i++){
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        lyt_history_parent.getChildAt(i).findViewById(R.id.lyt_select).setLayoutParams(param2);
                    }
                }else{
                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                            0,
                            0,
                            0.0f

                    );
                    img_history_delete.setImageResource(R.drawable.new_delete_icon);
                    lyt_my_history.setLayoutParams(param1);
                    del_state=0;
                    for(int i=0;i<lyt_history_parent.getChildCount();i++){
                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        lyt_history_parent.getChildAt(i).findViewById(R.id.lyt_select).setLayoutParams(param2);
                    }
                }
            }
        });
        rlt_my_history_del_btn = (RelativeLayout)findViewById(R.id.rlt_my_history_del_btn);
        rlt_my_history_del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        0,
                        0,
                        0.0f

                );
                img_history_delete = (ImageView) findViewById(R.id.img_history_delete);
                lyt_my_history = (LinearLayout) findViewById(R.id.rlt_my_history_btn);
                img_history_delete.setImageResource(R.drawable.new_delete_icon);
                lyt_my_history.setLayoutParams(param1);
                del_state = 0;
                allsel = true;
                ((TextView)findViewById(R.id.txt_my_history_all)).setText("全  选");
                delPapers();
            }
        });
        RelativeLayout rlt_my_history_all_btn = (RelativeLayout)findViewById(R.id.rlt_my_history_all_btn);
        ((TextView)rlt_my_history_all_btn.findViewById(R.id.txt_my_history_all)).setText("全  选");
        allsel = true;
        rlt_my_history_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<lyt_history_parent.getChildCount();i++){
                    CheckBox chk = (CheckBox)lyt_history_parent.getChildAt(i).findViewById(R.id.img_select);
                    chk.setChecked(allsel);
                }
                if(allsel == true){
                    ((TextView)findViewById(R.id.txt_my_history_all)).setText("反  选");
                    allsel = false;
                }else{
                    allsel = true;
                    ((TextView)findViewById(R.id.txt_my_history_all)).setText("全  选");
                }
            }
        });
    }
    @Override
    protected  void onResume(){
        super.onResume();
        getPapers();
    }
    private void getPapers() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserHistoryListActivity.this);
                RequestParams params = new RequestParams();
                System.out.println("UserID+++:"+userID);
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                params.put("device",deviceID);
                System.out.println("userid++"+userID);
                String url = "log/browse";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.optInt("code") == 1) {
                                System.out.println(response);

                                JSONArray list = response.getJSONArray("ret");
                                initFavorView(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    private void initFavorView(JSONArray list)
    {
        LinearLayout lyt_home_bottom_favor = (LinearLayout) findViewById(R.id.lyt_history_parent);
        lyt_home_bottom_favor.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < list.length(); i++)
        {
            //R.layout.search_item;
            try {
                JSONObject item = list.getJSONObject(i);
                View rowView1 = inflater.inflate(R.layout.history_item, null);
                String img_url = item.optString("imgPath");
                String desc = item.optString("title");
                String tid = item.optString("tid");
                String postState = item.optString("poststick");
                String dateline = item.optString("dateline");
                ((TextView) rowView1.findViewById(R.id.txt_room_favor_desc)).setText(desc);
                ImageView mThumbnail = (ImageView) rowView1.findViewById(R.id.img_favor_thumbnail);
                Drawable mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.default_img);
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
                            .resize(CommonUtils.getPixelValue(mContext, 80), CommonUtils.getPixelValue(mContext, 90))
                            .into(mThumbnail);
                }

                ((TextView)rowView1.findViewById(R.id.txt_room_favor_date)).setText("");
                ((TextView)rowView1.findViewById(R.id.txt_room_favor_price)).setText("");
                if(!(dateline.equals("") || dateline.equals("null"))){
                    ((TextView)rowView1.findViewById(R.id.txt_room_favor_date)).setText(dateline);
                }else{
                    ((TextView)rowView1.findViewById(R.id.txt_room_favor_date)).setText("");
                }
                if(!(item.optString("price").equals("") || item.optString("price").equals("null"))){
                    ((TextView)rowView1.findViewById(R.id.txt_room_favor_price)).setText(item.optString("price"));
                }else{
                    ((TextView)rowView1.findViewById(R.id.txt_room_favor_price)).setText("");
                }

                LinearLayout lyt_home_favor = (LinearLayout) rowView1.findViewById(R.id.lyt_room_favor_thumbnail);

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
                arr_tid[i] = tid;
                System.out.println("ttt+" + i + ":" + tid);
                lyt_home_favor.setId(Integer.parseInt(tid));
                lyt_home_favor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(HomeActivity.this, RoomDetailActivity.class);
                        Intent intent = new Intent(UserHistoryListActivity.this, RoomDetailActivity.class);
                        String newsId = String.valueOf(view.getId());
                        String sortid = "";
                        String title = ((TextView)view.findViewById(R.id.txt_room_favor_desc)).getText().toString();
                        String desc = ((TextView)view.findViewById(R.id.txt_room_favor_price)).getText()+ " " +((TextView)view.findViewById(R.id.txt_room_favor_date)).getText();
                        ImageView image = (ImageView) view.findViewById(R.id.img_favor_thumbnail);
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
                        UserHistoryListActivity.this.startActivity(intent);
                    }
                });
                lyt_home_bottom_favor.addView(rowView1, lyt_home_bottom_favor.getChildCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void delPapers() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, UserHistoryListActivity.this);
                RequestParams params = new RequestParams();
                System.out.println("UserID+++:"+userID);
                if(userID=="" || userID=="0"){
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "请登录吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                params.put("uid", userID);
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                params.put("device",deviceID);
                System.out.println("userid++"+userID);
                int k = 0;
                int e = 0;
                for(int i=0;i<lyt_history_parent.getChildCount();i++){
                    CheckBox chk = (CheckBox)lyt_history_parent.getChildAt(i).findViewById(R.id.img_select);
                    System.out.println("ppp+"+i+chk.isChecked());
                    System.out.println(lyt_history_parent.getChildAt(i).getId()+"::"+i);
                    if (chk.isChecked() == true) {
                        System.out.println(lyt_history_parent.getChildAt(i).getId()+"::"+i);
                        String tID = arr_tid[i];
                        System.out.println("ttt+" + i + ":" + tID);
                        params.put("tid["+k+"]", tID);
                        k++;
                    }else{
                        e++;
                    }
                }
                String url = "log/news/delete";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            System.out.println("response:"+response);

                            if (response.optInt("code") == 1) {
                                try {
                                    System.out.println(response);
                                    JSONArray list = response.getJSONArray("ret");
                                    LinearLayout lyt_home_bottom_favor = (LinearLayout) findViewById(R.id.lyt_history_parent);
                                    lyt_home_bottom_favor.removeAllViews();
                                    if(list==null){
                                        CommonUtils.dismissProgress(progressDialog);
                                        return;
                                    }
                                    initFavorView(list);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
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
    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(UserHistoryListActivity.this, MeActivity.class);
        pActivity.startChildActivity("me_activity", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(UserHistoryListActivity.this, MeActivity.class);
            pActivity.startChildActivity("me_activity", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
