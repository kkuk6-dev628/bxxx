package cn.reservation.app.baixingxinwen.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;

@SuppressWarnings("deprecation")
public class PostGroupActivity extends AnimatedActivity {
    public static PostGroupActivity PostGroupStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostGroupStack = PostGroupActivity.this;

        startChildActivity("PostCategoryActivity", new Intent(this,
                PostCategoryActivity.class));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PostGroupActivity.this, HomeActivity.class);
        startChildActivity("home", intent);
        TabHostActivity.tabWidget.setCurrentTab(0);
    }

}
