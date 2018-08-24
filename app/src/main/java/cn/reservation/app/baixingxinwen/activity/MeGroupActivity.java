package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;

public class MeGroupActivity extends AnimatedActivity {
    public static MeGroupActivity MeGroupStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MeGroupStack = MeGroupActivity.this;

        startChildActivity("MeActivity", new Intent(this,
                MeActivity.class));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MeGroupActivity.this, HomeActivity.class);
        startChildActivity("home", intent);
        TabHostActivity.tabWidget.setCurrentTab(0);
    }

}
