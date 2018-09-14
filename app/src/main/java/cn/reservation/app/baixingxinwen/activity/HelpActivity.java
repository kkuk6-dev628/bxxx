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
public class HelpActivity extends AppCompatActivity{
    private static String TAG = HelpActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) HelpActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "帮助中心");

        RelativeLayout rlt_help_login = (RelativeLayout) findViewById(R.id.rlt_help_login);
        rlt_help_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "2");
                intent.putExtra("title", "注册与登录");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_help_relate = (RelativeLayout) findViewById(R.id.rlt_help_relate);
        rlt_help_relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "8");
                intent.putExtra("title", "帖子相关");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_help_core = (RelativeLayout) findViewById(R.id.rlt_help_core);
        rlt_help_core.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "26");
                intent.putExtra("title", "审核相关");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_help_safe = (RelativeLayout) findViewById(R.id.rlt_help_safe);
        rlt_help_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "0");
                intent.putExtra("title", "账号安全");
                pActivity.startChildActivity("help_activity", intent);
                */
                Intent intent = new Intent(HelpActivity.this, UserSafeActivity.class);
                pActivity.startChildActivity("user_safe", intent);
            }
        });
        RelativeLayout rlt_help_social = (RelativeLayout) findViewById(R.id.rlt_help_social);
        rlt_help_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "37");
                intent.putExtra("title", "社区相关");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_help_vip = (RelativeLayout) findViewById(R.id.rlt_help_vip);
        rlt_help_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "51");
                intent.putExtra("title", "积分规则");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_adver_adver = (RelativeLayout) findViewById(R.id.rlt_adver_adver);
        rlt_adver_adver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "0");
                intent.putExtra("title", "广告业务");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
        RelativeLayout rlt_adver_report = (RelativeLayout) findViewById(R.id.rlt_adver_report);
        rlt_adver_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, HelpViewActivity.class);
                intent.putExtra("hId", "5");
                intent.putExtra("title", "举报与投诉");
                pActivity.startChildActivity("help_activity", intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent;
        intent = new Intent(HelpActivity.this, MeActivity.class);
        pActivity.startChildActivity("me_activity", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(HelpActivity.this, MeActivity.class);
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
