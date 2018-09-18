package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;

@SuppressWarnings("deprecation")
public class MoreGroupActivity extends AnimatedActivity {
    public static MoreGroupActivity MoreGroupStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoreGroupStack = MoreGroupActivity.this;

        startChildActivity("MoreActivity", new Intent(this,
                MoreActivity.class));

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(MoreGroupActivity.this, HomeActivity.class);
//        startChildActivity("home", intent);
        TabHostActivity.tabWidget.setCurrentTab(0);
    }

}
