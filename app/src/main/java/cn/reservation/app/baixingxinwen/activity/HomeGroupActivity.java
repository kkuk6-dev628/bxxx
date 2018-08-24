package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;

public class HomeGroupActivity extends AnimatedActivity {
    public static HomeGroupActivity HomeGroupStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeGroupStack = HomeGroupActivity.this;
        Intent intent = new Intent(this, HomeActivity.class);
        startChildActivity("HomeActivity", intent);
    }

    @Override
    public void onBackPressed() {
        this.getParent().onBackPressed();
    }

    public static void forwardAppointHelp() {
        Intent intent = new Intent(HomeGroupStack, AppointHelpActivity.class);
        HomeGroupStack.startChildActivity("appoint_help", intent);
        HomeGroupStack.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
