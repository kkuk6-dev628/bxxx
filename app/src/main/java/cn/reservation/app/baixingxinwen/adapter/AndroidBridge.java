package cn.reservation.app.baixingxinwen.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.LoginActivity;
import cn.reservation.app.baixingxinwen.activity.RoomDetailActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

public class AndroidBridge {
    private AppCompatActivity activity;

    public AndroidBridge(AppCompatActivity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void callAndroidFromJavascript (String phoneNumber) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if(CommonUtils.isLogin==true) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phoneNumber));
            this.activity.startActivity(intent);
        }else{
            Intent intent = new Intent(this.activity, LoginActivity.class);
            intent.putExtra("from_activity", "room_detail");
            this.activity.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
            this.activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        }
//        Log.i(getClass().getSimpleName(), "changeNavbarBackground " + color);

//        Field f = R.color.class.getField(color);
//        final int col = (Integer) f.get(null);
//
//        activity.changeNavbarBackground(col);
    }
}
