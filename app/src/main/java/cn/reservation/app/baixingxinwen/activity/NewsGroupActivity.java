package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;

@SuppressWarnings("deprecation")
public class NewsGroupActivity extends AnimatedActivity {
    public static NewsGroupActivity NewsGroupStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsGroupStack = NewsGroupActivity.this;

        startChildActivity("NewsActivity", new Intent(this,
                NewsActivity.class));

    }

    @Override
    public void onBackPressed() {
        TabHostActivity.setCurrentTab(0);
    }
}
