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
public class SetChargeActivity extends AppCompatActivity{
    private static String TAG = SetChargeActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safe);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) SetChargeActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, true, "置  顶");

        RelativeLayout lytBack = (RelativeLayout) findViewById(R.id.layout_back);
        lytBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetChargeActivity.this, MeActivity.class);
                pActivity.startChildActivity("me", intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetChargeActivity.this, MeActivity.class);
        pActivity.startChildActivity("me", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SetChargeActivity.this, MeActivity.class);
            pActivity.startChildActivity("me", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
