package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class AdverActivity extends AppCompatActivity{
    private static String TAG = AdverActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adver);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) AdverActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "广告业务");

        RelativeLayout rlt_adver_member = (RelativeLayout) findViewById(R.id.rlt_adver_member);
        rlt_adver_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdverActivity.this, MemberActivity.class);
                pActivity.startChildActivity("member", intent);
            }
        });
//        RelativeLayout rlt_adver_cate = (RelativeLayout) findViewById(R.id.rlt_adver_cate);
//        rlt_adver_cate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "开发中", Toast.LENGTH_LONG).show();
//                //Intent intent = new Intent(AdverActivity.this, UserHistoryListActivity.class);
//                //pActivity.startChildActivity("member", intent);
//            }
//        });
//        RelativeLayout rlt_adver_money = (RelativeLayout) findViewById(R.id.rlt_adver_money);
//        rlt_adver_money.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdverActivity.this, MoreActivity.class);
//                pActivity.startChildActivity("more", intent);
//            }
//        });
        RelativeLayout rlt_adver_info = (RelativeLayout) findViewById(R.id.rlt_adver_info);
        rlt_adver_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "开发中", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(AdverActivity.this, UserNewsActivity.class);
                //pActivity.startChildActivity("more", intent);
            }
        });
        RelativeLayout rlt_adver_image = (RelativeLayout) findViewById(R.id.rlt_adver_image);
        rlt_adver_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "开发中", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(AdverActivity.this, MoreActivity.class);
                //pActivity.startChildActivity("more", intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AdverActivity.this, MeActivity.class);
        pActivity.startChildActivity("me_activity", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(AdverActivity.this, MeActivity.class);
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
