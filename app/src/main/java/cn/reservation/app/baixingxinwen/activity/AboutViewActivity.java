package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class AboutViewActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = AboutViewActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private WebView webview;
    private String fId;
    private String sortId;
    private String userID;
    private String deviceID;
    private String hId;
    private String title;
    private String isWebNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_view);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) AboutViewActivity.this.getParent();
        Intent intent = getIntent();
        webview =(WebView)findViewById(R.id.webHelpView);
        title = (String) intent.getSerializableExtra("title");
        hId = (String) intent.getSerializableExtra("hId");

        String loadUrl = APIManager.User_URL+"help/paper/"+hId;
        isWebNotification = (String) intent.getSerializableExtra("isWebNotification");
        if(isWebNotification != null && isWebNotification.equals("1")){
            loadUrl = APIManager.User_URL+"announce/paper/"+hId;
        }

        CommonUtils.customActionBar(mContext, this, true, title);
        webview.setWebViewClient(new WebViewClient());
//        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(loadUrl);
    }
    @Override
    public void onBackPressed() {
//        AboutViewActivity.this.finish();

        Intent intent;
        if(isWebNotification != null && isWebNotification.equals("1")){
            intent = new Intent(AboutViewActivity.this, NotificationsActivity.class);
            intent.putExtra("notificationType", "website");
        }
        else{
            intent = new Intent(AboutViewActivity.this, AboutActivity.class);
        }

        pActivity.startChildActivity("about_activity", intent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            AboutViewActivity.this.finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);


        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            if(isWebNotification != null && isWebNotification.equals("1")){
                intent = new Intent(AboutViewActivity.this, NotificationsActivity.class);
                intent.putExtra("notificationType", "website");
            }
            else{
                intent = new Intent(AboutViewActivity.this, HelpActivity.class);
            }

            pActivity.startChildActivity("help_activity", intent);
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

}
