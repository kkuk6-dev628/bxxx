package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class HomeAdverActivity extends AppCompatActivity{
    private static String TAG = HomeAdverActivity.class.getSimpleName();

    public AnimatedActivity pActivity;
    private int state = 0;
    private final int SPLASH_DISPLAY_DURATION = 5000;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adver);
        CommonUtils.customActionBar(HomeAdverActivity.this, this, false, "");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(state==0) {
                    Intent mainIntent = new Intent(HomeAdverActivity.this, TabHostActivity.class);
                    HomeAdverActivity.this.startActivity(mainIntent);
                    HomeAdverActivity.this.finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        }, SPLASH_DISPLAY_DURATION);
        RelativeLayout rlt_adver_skip = (RelativeLayout) findViewById(R.id.rlt_adver_skip);
        rlt_adver_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(HomeAdverActivity.this, TabHostActivity.class);
                HomeAdverActivity.this.startActivity(mainIntent);
                state = 1;


                HomeAdverActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 4; i >-1; i--) {
                    final int value = i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.txt_adver_time)).setText(value+"S跳过");
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
