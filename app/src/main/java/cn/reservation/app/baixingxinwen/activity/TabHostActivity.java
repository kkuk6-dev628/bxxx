package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Locale;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.NetworkManager;
import cn.reservation.app.baixingxinwen.api.WXAPI;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.UserInfo;


//import com.theartofdev.edmodo.cropper.CropImage;

@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity implements TabHost.OnTabChangeListener {

    private static final String TAG = TabHostActivity.class.getSimpleName();

    private static final String[] TABS = {"HomeGroupActivity", "PostGroupActivity", "NewsGroupActivity", "MeGroupActivity"};
    private static final int[] TAB_INDICATOR = {R.drawable.home_selector, R.drawable.post_selector, R.drawable.news_selector, R.drawable.me_selector};
    private final int SPLASH_DISPLAY_DURATION = 5000;
    private int state = 0;
    private final Handler handler = new Handler();

    public static TabHost tabs;
    public static TabWidget tabWidget;
    public boolean checkTabsListener = false;
    public static TabHostActivity TabHostStack;
    private TextView textViewNotificationsBadge;

    public static void setCurrentTab(int tabIndex){
        TabHostActivity.tabWidget.setCurrentTab(tabIndex);
        TabHostActivity.tabs.setCurrentTab(tabIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLocale();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);

        final RelativeLayout splash = (RelativeLayout) findViewById(R.id.adver_back);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(state==0) {
                    splash.animate()
                            .alpha(0f)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    splash.setVisibility(View.GONE);
                                }
                            })
                            .start();
                }
            }
        }, SPLASH_DISPLAY_DURATION);
        RelativeLayout rlt_adver_skip = (RelativeLayout) findViewById(R.id.rlt_adver_skip);
        rlt_adver_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                splash.animate()
                        .alpha(0f)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                splash.setVisibility(View.GONE);
                            }
                        })
                        .start();
                state = 0;
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

        TabHostStack = TabHostActivity.this;

        checkLogin();

        WXAPI.Init(TabHostStack);
        NetworkManager.getInstance(TabHostStack);

        tabs = getTabHost();

        tabWidget = tabs.getTabWidget();

        tabs.setOnTabChangedListener(this);

        for (int i = 0; i < TABS.length; i++) {
            TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);

            //Asociating Components
            ComponentName oneActivity = new ComponentName("cn.reservation.app.baixingxinwen", "cn.reservation.app.baixingxinwen.activity." + TABS[i]);
            Intent intent = new Intent().setComponent(oneActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            tab.setContent(intent);
            //Setting the Indicator
//            tab.setIndicator("", getResources().getDrawable(TAB_INDICATOR[i]));
            if(i == 2){
                View chatTab = getLayoutInflater().inflate(R.layout.badge, null);
                textViewNotificationsBadge = chatTab.findViewById(R.id.new_notifications);
                tab.setIndicator(chatTab);

//                BadgeView badge = new BadgeView(this, item);
//                badge.setText("1");
//                badge.show();
            }
            else{
                tab.setIndicator("", getResources().getDrawable(TAB_INDICATOR[i]));
            }
            tabs.addTab(tab);
        }
        tabWidget.setDividerDrawable(null);

        // badge 개수를 얻어서 그것을 현시시킨다.
        SharedPreferences sharedPref = this.getSharedPreferences(
                "Notifications", Context.MODE_PRIVATE);
        int badgeCount = sharedPref.getInt("badgeCount", -1);
        if(badgeCount > 0){
                setTextViewNotificationsBadge(badgeCount);
        }
        //////////////////////////////////////////////////////





        checkTabsListener = true;

        for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
            tabs.getTabWidget().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }

        Intent intent = getIntent();
        final boolean isToAppointHelp = intent.getBooleanExtra("isToAppointHelp", false);
        if (isToAppointHelp) {
            HomeGroupActivity.forwardAppointHelp();
        }

        // Home Tab Click

        tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeGroupActivity.HomeGroupStack != null && HomeGroupActivity.HomeGroupStack.mIdList.size() > 1) {
                    HomeGroupActivity.HomeGroupStack.getLocalActivityManager().removeAllActivities();
                    HomeGroupActivity.HomeGroupStack.mIdList.clear();
                    HomeGroupActivity.HomeGroupStack.mIntents.clear();
                    HomeGroupActivity.HomeGroupStack.mAnimator.removeAllViews();
                    Intent intent;
                    intent = new Intent(HomeGroupActivity.HomeGroupStack, HomeActivity.class);
                    HomeGroupActivity.HomeGroupStack.startChildActivity("HomeActivity", intent);
                }
                tabWidget.setCurrentTab(0);
                tabs.setCurrentTab(0);
            }
        });
        tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!CommonUtils.isLogin) {
                    gotoLogin();
                }else {
                    if (PostGroupActivity.PostGroupStack != null && PostGroupActivity.PostGroupStack.mIdList.size() > 1) {
                        PostGroupActivity.PostGroupStack.getLocalActivityManager().removeAllActivities();
                        PostGroupActivity.PostGroupStack.mIdList.clear();
                        PostGroupActivity.PostGroupStack.mIntents.clear();
                        PostGroupActivity.PostGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(PostGroupActivity.PostGroupStack, PostActivity.class);
                        PostGroupActivity.PostGroupStack.startChildActivity("PostCategoryActivity", intent);
                    }
                    tabWidget.setCurrentTab(1);
                    tabs.setCurrentTab(1);
                }
            }
        });

        // Me tab click

        tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!CommonUtils.isLogin) {
                    gotoLogin();
                }else{
                    if (NewsGroupActivity.NewsGroupStack != null && NewsGroupActivity.NewsGroupStack.mIdList.size() > 1) {
                        NewsGroupActivity.NewsGroupStack.getLocalActivityManager().removeAllActivities();
                        NewsGroupActivity.NewsGroupStack.mIdList.clear();
                        NewsGroupActivity.NewsGroupStack.mIntents.clear();
                        NewsGroupActivity.NewsGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(NewsGroupActivity.NewsGroupStack, NewsActivity.class);
                        NewsGroupActivity.NewsGroupStack.startChildActivity("NewsActivity", intent);
                    }
                    tabWidget.setCurrentTab(2);
                    tabs.setCurrentTab(2);
                }
            }
        });
        tabWidget.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!CommonUtils.isLogin) {
                    Intent intent = new Intent(TabHostActivity.this, LoginActivity.class);
                    intent.putExtra("from_activity", "me_activity");
                    TabHostActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
                    TabHostActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                } else {
                    if (MeGroupActivity.MeGroupStack != null && MeGroupActivity.MeGroupStack.mIdList.size() > 1) {
                        MeGroupActivity.MeGroupStack.getLocalActivityManager().removeAllActivities();
                        MeGroupActivity.MeGroupStack.mIdList.clear();
                        MeGroupActivity.MeGroupStack.mIntents.clear();
                        MeGroupActivity.MeGroupStack.mAnimator.removeAllViews();
                        Intent intent;
                        intent = new Intent(MeGroupActivity.MeGroupStack, MeActivity.class);
                        MeGroupActivity.MeGroupStack.startChildActivity("MeActivity", intent);
                    }
                    tabWidget.setCurrentTab(3);
                    tabs.setCurrentTab(3);
                }
            }
        });
    }

    public void gotoLogin() {
        tabs.setCurrentTab(0);
        Intent intent = new Intent(TabHostActivity.this, LoginActivity.class);
        intent.putExtra("from_activity", "me_activity");
        TabHostActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_LOGIN);
        TabHostActivity.this.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void checkLogin() {
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        long userID = pref.getLong("userID", 0);
        String userName = pref.getString("userName", "");
        int userGender = pref.getInt("userGender", 0);
        String userBirthday = pref.getString("userBirthday", "");
        String userPhone = pref.getString("userPhone", "");
        String userPhoto = pref.getString("userPhoto", "");
        String token = pref.getString("credits", "");
        String identify = pref.getString("identify", "");
        String password = pref.getString("realname", "");
        String uid = pref.getString("uid", "");
        String wechat = pref.getString("wechat", "");
        String qq = pref.getString("qq", "");
        String mobile = pref.getString("userJoinMobile","");
        String baixingbi = pref.getString("baixingbi","0");
        String level = pref.getString("level", "");
        String login_type = pref.getString("login_type", "normal");
        String login_username = pref.getString("login_username", "");
        String login_password = pref.getString("login_password", "");
        String changeid = pref.getString("changeid", "");
        String dateline = pref.getString("dateline", "");

        if (userID != 0) {
            CommonUtils.userInfo = new UserInfo(userID, userName, userGender, userBirthday, userPhone, userPhoto, token, identify, password, uid, qq, wechat, mobile, baixingbi, level, login_type, login_username, login_password, changeid, dateline);
            CommonUtils.isLogin = true;
            if(!CommonUtils.channel_id.isEmpty()){
                CommonUtils.registerChannelId(this);
            }
        } else {
            CommonUtils.isLogin = false;
        }
    }

    public void setLocale() {
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        String lang = pref.getString("lang", "zh");
        if (lang.equals("zh")) {
            CommonUtils.mIntLang = 0;
        } else {
            CommonUtils.mIntLang = 1;
        }
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_exit, null);

        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                dialog.dismiss();
            }
        });

        CommonUtils.showAlertDialog(this, dialog, view, 250);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode:"+requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.REQUEST_CODE_LOGIN && resultCode == CommonUtils.RESULT_CODE_LOGIN) {
            tabs.setCurrentTab(2);
        } else if (requestCode == CommonUtils.REQUEST_CODE_MODIFY && resultCode == CommonUtils.RESULT_CODE_MODIFY) {
            MeGroupActivity meGroupActivity = (MeGroupActivity) getCurrentActivity();
            FamilyInfoViewActivity familyInfoViewActivity = (FamilyInfoViewActivity) meGroupActivity.getLocalActivityManager().getCurrentActivity();
            familyInfoViewActivity.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == RESULT_OK && (requestCode == CommonUtils.CAMERA_REQUEST || requestCode == CommonUtils.GALLERY_PICTURE)) {
            MeGroupActivity meGroupActivity = (MeGroupActivity) getCurrentActivity();
            MeActivity meActivity = (MeActivity) meGroupActivity.getLocalActivityManager().getCurrentActivity();
            meActivity.selectResult(requestCode, resultCode, data);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("request1:"+resultCode);
            System.out.println("request2:"+RESULT_OK);
            if (resultCode == RESULT_OK) {
                CommonUtils.face_bmp = result.getBitmap();
                MeGroupActivity meGroupActivity = (MeGroupActivity) getCurrentActivity();
                MeActivity meActivity = (MeActivity) meGroupActivity.getLocalActivityManager().getCurrentActivity();
                meActivity.onActivityResult(requestCode, resultCode, data);
                //imgMyPhoto.setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "失败: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public int getTextViewNotificationsBadge() {
        return Integer.parseInt((String) textViewNotificationsBadge.getText());
    }

    public TextView getTextViewNotificationsObject() {
        return textViewNotificationsBadge;
    }

    @SuppressLint("SetTextI18n")
    public void setTextViewNotificationsBadge(int badgeCount) {
        if(badgeCount <= 0){
            this.textViewNotificationsBadge.setVisibility(TextView.GONE);
        }
        else{
            this.textViewNotificationsBadge.setVisibility(TextView.VISIBLE);
            this.textViewNotificationsBadge.setText(Integer.toString(badgeCount));
        }

    }
}
