package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class AboutActivity extends AppCompatActivity{
    private static String TAG = AboutActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) AboutActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "关于我们");

        RelativeLayout rlt_version = (RelativeLayout) findViewById(R.id.rlt_version);
        rlt_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutViewActivity.class);
                intent.putExtra("hId", "101");
                intent.putExtra("title", "版本信息");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_web = (RelativeLayout) findViewById(R.id.rlt_web);
        rlt_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutViewActivity.class);
                intent.putExtra("hId", "102");
                intent.putExtra("title", "网站简介");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_service = (RelativeLayout) findViewById(R.id.rlt_service);
        rlt_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutViewActivity.class);
                intent.putExtra("hId", "103");
                intent.putExtra("title", "服务协议");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_contact = (RelativeLayout) findViewById(R.id.rlt_contact);
        rlt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutViewActivity.class);
                intent.putExtra("hId", "104");
                intent.putExtra("title", "联系我们");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AboutActivity.this, MeActivity.class);
        pActivity.startChildActivity("me_activity", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(AboutActivity.this, MeActivity.class);
            pActivity.startChildActivity("me_activity", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
