package cn.reservation.app.baixingxinwen.activity;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import cn.reservation.app.baixingxinwen.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, TabHostActivity.class);
                //Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_DISPLAY_DURATION);

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"v84V3j62Tu3LIbn9l94pBOUc");
        /**
         * 以下通知栏设置2选1。使用默认通知时，无需添加以下设置代码。
         */

        // 1.默认通知
        // 若您的应用需要适配Android O（8.x）系统，且将目标版本targetSdkVersion设置为26及以上时：
        // SDK提供设置Android O（8.x）新特性---通知渠道的设置接口。
        // 若不额外设置，SDK将使用渠道名默认值"Push"；您也可以仿照以下3行代码自定义channelId/channelName。
        // 注：非targetSdkVersion 26的应用无需以下调用且不会生效
        BasicPushNotificationBuilder bBuilder = new BasicPushNotificationBuilder();
        bBuilder.setChannelId("testDefaultChannelId");
        bBuilder.setChannelName("testDefaultChannelName");
        // PushManager.setDefaultNotificationBuilder(this, bBuilder); //使自定义channel生效

        // 2.自定义通知
        // 设置自定义的通知样式，具体API介绍见用户手册
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                R.layout.notification_custom_builder,
                R.id.notification_icon,
                R.id.notification_title,
                R.id.notification_text);

        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(this.getApplicationInfo().icon);
        cBuilder.setNotificationSound(Uri.withAppendedPath(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 若您的应用需要适配Android O（8.x）系统，且将目标版本targetSdkVersion设置为26及以上时：
        // 可自定义channelId/channelName, 若不设置则使用默认值"Push"；
        // 注：非targetSdkVersion 26的应用无需以下2行调用且不会生效
        cBuilder.setChannelId("testId");
        cBuilder.setChannelName("testName");
        // 推送高级设置，通知栏样式设置为下面的ID，ID应与server下发字段notification_builder_id值保持一致
        PushManager.setNotificationBuilder(this, 1, cBuilder);

    }
}
