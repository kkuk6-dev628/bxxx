package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.wxapi.OnResponseListener;
import cn.reservation.app.baixingxinwen.wxapi.WXShare;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.squareup.picasso.Utils.getResources;

@SuppressWarnings("deprecation")
public class RoomDetailActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener {
    private static String TAG = RoomDetailActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private WebView webview;
    private String fId;
    private String sortId;
    private String userID;
    private String deviceID;
    private String newsId;
    private String phone;
    public String title;
    public String desc;
    public String url;
    private String uid;
    private WXShare wxShare;
    private String authorid;
    private String author;
    private int showAction;
    private GestureDetector gestureDetector;
    private ProgressHUD mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        showAction = 0;
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) RoomDetailActivity.this.getParent();
        Intent intent = getIntent();
        CommonUtils.customActionBar(mContext, this, false, "");
        //SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        //userID = Long.toString(pref.getLong("userID", 0));
        if (CommonUtils.isLogin && CommonUtils.userInfo != null)
            userID = CommonUtils.userInfo.getUid();
        else
            userID = "";
        deviceID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        webview = (WebView) findViewById(R.id.webView);
//        mProgressDialog = ProgressHUD.show(mContext, "", true, false, this);

//        webview.setWebViewClient(new WebViewClient() {
//
//            public void onPageFinished(WebView view, String url) {
//                mProgressDialog.dismiss();
//            }
//        });
        fId = (String) intent.getSerializableExtra("fid");
        sortId = (String) intent.getSerializableExtra("sortid");
        newsId = (String) intent.getSerializableExtra("newsId");
        title = (String) intent.getSerializableExtra("title");
        desc = (String) intent.getSerializableExtra("desc");
        getSavePaper();
        phone = "";
        webview.setFilterTouchesWhenObscured(true);
        //webview.setWebChromeClient(new WebChromeClient());
        webview.setWebChromeClient(new MyBrowser());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        /*webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        */
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(APIManager.User_URL + "news/paper/" + newsId);
//        webview.addJavascriptInterface(new AndroidBridge(this), "AndroidBridge");
        //webview.loadDataWithBaseURL(APIManager.User_URL+"news/paper/"+newsId, "","text/html", "UTF-8", null);

        //webview.setWebViewClient(new WebViewClient());
        /*
        webview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        */
/*
        webview.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
                Log.i(TAG, "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
                return true;
            }
        });
*/
        saveHistoryPaper();
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomDetailActivity.this.finish();
            }
        });
        final ImageView imgReport = (ImageView) findViewById(R.id.imageDetailRport);
        imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgReport.setClickable(false);
                if (CommonUtils.isLogin) {

                    HashMap<String, Object> params = new HashMap<String, Object>();

                    params.put("uid", userID);
                    params.put("tid", newsId);

                    final String url = "api/news/checkreport";
                    NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {

                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            imgReport.setClickable(true);
                            JSONObject responseBody = response.body();
                            assert responseBody != null;
                            try {
                                if (responseBody.getInt("code") == 1) {
                                    if (responseBody.getInt("ret") == 1) {
                                        CommonUtils.showAlertDialog(RoomDetailActivity.this,
                                                res.getString(R.string.report_received_message), null);
                                    } else {
                                        Intent intent = new Intent(RoomDetailActivity.this, ReportActivity.class);
                                        intent.putExtra("from_activity", "room_detail");
                                        intent.putExtra("tid", newsId);
                                        intent.putExtra("fid", fId);
                                        RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                                        RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                                    }
                                } else {
                                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            imgReport.setClickable(true);
                            Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    imgReport.setClickable(true);
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        ImageView imgSave = (ImageView) findViewById(R.id.imageDetailStar);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isLogin == true) {
                    savePaper();
                } else {
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        ImageView imgShare = (ImageView) findViewById(R.id.imageDetailShare);
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                String name_state = "????????????,???????????????";
                if(showAction==0){
                    createActionSheet(name_state);
                    showAction=1;
                }
                */
                createActionSheet();
            }
        });
        RelativeLayout rlt_detail_message = (RelativeLayout) findViewById(R.id.rlt_detail_message);
        rlt_detail_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isLogin == true) {
                    if (userID.equals(authorid)) {
                        Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(RoomDetailActivity.this, ChatActivity.class);
                    System.out.println("newsId" + newsId);
                    intent.putExtra("tid", newsId);
                    intent.putExtra("name", author);
                    intent.putExtra("avatar", "");
                    RoomDetailActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        RelativeLayout rlt_detail_call = (RelativeLayout) findViewById(R.id.rlt_detail_call);
        rlt_detail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isLogin == true) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        TextView txt_client_call = (TextView) findViewById(R.id.txt_client_call);
        txt_client_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isLogin == true) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        TextView txt_client_msg = (TextView) findViewById(R.id.txt_client_msg);
        txt_client_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isLogin == true) {
                    if (userID.equals(authorid)) {
                        Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(RoomDetailActivity.this, ChatActivity.class);
                    System.out.println("newsId" + newsId);
                    intent.putExtra("tid", newsId);
                    intent.putExtra("name", author);
                    intent.putExtra("avatar", "");
                    RoomDetailActivity.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(RoomDetailActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "room_detail");
                    RoomDetailActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    RoomDetailActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                }
            }
        });
        wxShare = new WXShare(this);
        wxShare.register();
        wxShare.setListener(new OnResponseListener() {
            @Override
            public void onSuccess() {
                // ????????????
            }

            @Override
            public void onCancel() {
                // ????????????
            }

            @Override
            public void onFail(String message) {
                // ????????????
            }
        });
    }

    private class MyBrowser extends WebChromeClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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

    private void getSavePaper() {
        HashMap<String, Object> params = new HashMap<>();
        System.out.println("UserID+++:" + userID);
        System.out.println("tId++" + newsId);
        if (!CommonUtils.isLogin) {
            return;
        }
        if (newsId == "") {
            return;
        }
//                final ProgressHUD progressDialog = ProgressHUD.show(RoomDetailActivity.this, res.getString(R.string.processing), true, false, RoomDetailActivity.this);
        params.put("uid", userID);
        params.put("tid", newsId);
        String url = "api/news/feature";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                JSONObject response = res.body();
                phone = response.optString("phone");
                authorid = response.optString("authorid");
                author = response.optString("author");
                System.out.println("SaveResult++++re" + response);
//                        CommonUtils.dismissProgress(progressDialog);
                if (response.optInt("islike") == 1) {
                    //Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                    ((ImageView) findViewById(R.id.imageDetailStar)).setImageResource(R.drawable.new_my_saved);
                } else {
                    //Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void savePaper() {
        HashMap<String, Object> params = new HashMap<>();
        System.out.println("UserID+++:" + userID);
        System.out.println("tId++" + newsId);
        if (!CommonUtils.isLogin) {
            Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newsId == "") {
            Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressHUD progressDialog = ProgressHUD.show(RoomDetailActivity.this, res.getString(R.string.processing), true, false, RoomDetailActivity.this);
        params.put("uid", userID);
        params.put("tid", newsId);
        String url = "api/log/like";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                CommonUtils.dismissProgress(progressDialog);
                JSONObject response = res.body();
                System.out.println("SaveResult111++++re" + response);
                if (response.optInt("code") == 1) {
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    ((ImageView) findViewById(R.id.imageDetailStar)).setImageResource(R.drawable.new_my_saved);
                } else {
                    Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_SHORT).show();
                    ((ImageView) findViewById(R.id.imageDetailStar)).setImageResource(R.drawable.new_detail_top_star);
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveHistoryPaper() {
        HashMap<String, Object> params = new HashMap<>();
        System.out.println("deID+++:" + deviceID);
        System.out.println("tId++" + newsId);
        if (!CommonUtils.isLogin) {
            //Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newsId == "") {
            Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("uid", userID);
        params.put("device", deviceID);
        params.put("tid", newsId);
        String url = "api/log/news";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> res) {
                JSONObject response = res.body();
                System.out.println("Result++++re" + response);
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void createActionSheet(String state_name) {
        String[] state = state_name.split(",");
        ActionSheet.createBuilder(mContext, RoomDetailActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(state)
                .setCancelableOnTouchOutside(true)
                .setListener(RoomDetailActivity.this)
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
                if (CommonUtils.share_bmp == null) {
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                }
                String url = APIManager.User_URL + "news/paper/" + newsId + "/1";
                System.out.println(title);
                System.out.println(desc);
                System.out.println(url);
                wxShare.sharePaper(title, desc, url, CommonUtils.share_bmp, 0);
            }
        });
        RelativeLayout share_friend = (RelativeLayout) rowView1.findViewById(R.id.lyt_share_friends);
        share_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout lyt_share_panel = (LinearLayout) findViewById(R.id.lyt_share_panel);
                lyt_share_panel.removeAllViews();
                if (CommonUtils.share_bmp == null) {
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                }
                url = APIManager.User_URL + "news/paper/" + newsId + "/1";
                System.out.println(title);
                System.out.println(desc);
                System.out.println(url);
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

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        //String[] phone = mHospitalTel.split(",");
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone[index]));
        //startActivity(intent);
        showAction = 0;
        switch (index) {
            case 0:
                if (CommonUtils.share_bmp == null) {
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                }
                /*
                if(CommonUtils.share_bmp.getByteCount()>23000) {
                    if (CommonUtils.share_bmp != null && !CommonUtils.share_bmp.isRecycled()) {
                        CommonUtils.share_bmp = Bitmap.createScaledBitmap(CommonUtils.share_bmp, 32, 32, true);
                    }
                }
                */
                String url = APIManager.User_URL + "news/paper/" + newsId;
                System.out.println(title);
                System.out.println(desc);
                System.out.println(url);
                wxShare.sharePaper(title, desc, url, CommonUtils.share_bmp, 0);
                //wxShare.share(title, desc,0);
                break;
            case 1:
                if (CommonUtils.share_bmp == null) {
                    CommonUtils.share_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
                }
                /*
                if(CommonUtils.share_bmp.getByteCount()>23000) {
                    if (CommonUtils.share_bmp != null && !CommonUtils.share_bmp.isRecycled()) {
                        CommonUtils.share_bmp = Bitmap.createScaledBitmap(CommonUtils.share_bmp, 32, 32, true);
                    }
                }
                */
                url = APIManager.User_URL + "news/paper/" + newsId;
                System.out.println(title);
                System.out.println(desc);
                System.out.println(url);
                wxShare.sharePaper(title, desc, url, CommonUtils.share_bmp, 1);
                //wxShare.share(title, desc,1);
                break;
        }
        actionSheet.dismiss();
    }

    @Override
    public void onBackPressed() {
        RoomDetailActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RoomDetailActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        showAction = 0;
    }

}
