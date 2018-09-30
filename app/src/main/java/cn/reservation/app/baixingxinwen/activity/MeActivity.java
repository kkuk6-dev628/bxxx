package cn.reservation.app.baixingxinwen.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class MeActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener, CropImageView.OnCropImageCompleteListener{
    private static String TAG = MeActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    private  CircleImageView imgMyPhoto;
    public AnimatedActivity pActivity;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath = null;
    private int showAction;
    public int mYear = 0;
    public int mMonth = 0;
    public int mDay = 0;
    public int mWeekDay = 0;
    public String mPatientID;
    private int mTmpYear = 0;
    private int mTmpMonth = 0;
    private int mTmpDay = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) MeActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, false, "");
        showAction = 0;
        TextView txtMyName = (TextView) findViewById(R.id.txt_my_name);
        final RelativeLayout lytLogout = (RelativeLayout) findViewById(R.id.rlt_my_logout);
        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lytLogout.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lytLogout.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, AboutActivity.class);
                pActivity.startChildActivity("user_about", intent);
            }
        });
        /*
        ImageView imgMeIcon = (ImageView) findViewById(R.id.img_me_icon);
        imgMeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeActivity.this, MemberActivity.class);
                pActivity.startChildActivity("member", intent);
            }
        });
        */
        final RelativeLayout rltSafe = (RelativeLayout) findViewById(R.id.rlt_my_safe);
        rltSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltSafe.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltSafe.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, UserSafeActivity.class);
                pActivity.startChildActivity("user_safe", intent);
            }
        });
        final RelativeLayout rltHistory = (RelativeLayout) findViewById(R.id.rlt_my_history);
        rltHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltHistory.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltHistory.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, UserHistoryListActivity.class);
                pActivity.startChildActivity("user_history", intent);
            }
        });
        final RelativeLayout rltSave = (RelativeLayout) findViewById(R.id.rlt_my_save);
        rltSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltSave.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltSave.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, UserSaveListActivity.class);
                pActivity.startChildActivity("user_save", intent);
            }
        });
        final RelativeLayout rltNews = (RelativeLayout) findViewById(R.id.rlt_my_news);
        rltNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltNews.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltNews.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, UserNewsActivity.class);
                pActivity.startChildActivity("user_news", intent);
            }
        });
        final RelativeLayout rltMoney = (RelativeLayout) findViewById(R.id.rlt_my_adver);
        rltMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltMoney.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltMoney.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, AdverActivity.class);
                pActivity.startChildActivity("adver", intent);
            }
        });
        final RelativeLayout rltHelp = (RelativeLayout) findViewById(R.id.rlt_my_help);
        rltHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rltHelp.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rltHelp.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, HelpActivity.class);
                pActivity.startChildActivity("help", intent);
            }
        });
        final TextView txt_my_money = (TextView) findViewById(R.id.txt_my_money);
        txt_my_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_my_money.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_my_money.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, MoreActivity.class);
                pActivity.startChildActivity("more", intent);
            }
        });
        final TextView txt_my_level_title = (TextView) findViewById(R.id.txt_my_level_title);
        txt_my_level_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_my_level_title.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_my_level_title.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, MoreActivity.class);
                pActivity.startChildActivity("more", intent);
//                Intent intent = new Intent(MeActivity.this, MemberActivity.class);
//                pActivity.startChildActivity("member", intent);
            }
        });

        TextView txt_my_level_date = (TextView) findViewById(R.id.txt_my_level_date);
        String dateline = CommonUtils.userInfo.getDateline();
        if(dateline.isEmpty() || dateline.equals("0")){
            txt_my_level_date.setText("");
            txt_my_level_date.setVisibility(View.GONE);
        }
        else{
            txt_my_level_date.setText(dateline);
            txt_my_level_date.setVisibility(View.VISIBLE);
        }

        final TextView txt_my_name_set = (TextView) findViewById(R.id.txt_my_name_set);
        String changeid = CommonUtils.userInfo.getChangeid();
        if(changeid == null || changeid.equals("0")){
            txt_my_name_set.setVisibility(TextView.GONE);
        }
        txt_my_name_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_my_name_set.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_my_name_set.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, SetNameActivity.class);
                startActivityForResult(intent, SetNameActivity.SET_NAME_REQUEST_CODE);
            }
        });
        final TextView txt_my_level = (TextView) findViewById(R.id.txt_my_level);
        txt_my_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_my_level.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_my_level.setClickable(true);
                    }
                }, 500);
                Intent intent = new Intent(MeActivity.this, MemberActivity.class);
                pActivity.startChildActivity("member", intent);
            }
        });
        txtMyName.setText(CommonUtils.userInfo.getUserName());
        TextView mTxtMoney = (TextView) findViewById(R.id.txt_my_money);
        if(CommonUtils.userInfo.getBaixingbi().equals("")){
            mTxtMoney.setText("0");
        }else {
            mTxtMoney.setText(CommonUtils.userInfo.getBaixingbi());
        }
        TextView mTxtLevel = (TextView) findViewById(R.id.txt_my_level);
        if(CommonUtils.userInfo.getLevel().equals("")){
            mTxtLevel.setText("新手");
        }else{
            mTxtLevel.setText(CommonUtils.userInfo.getLevel());
        }
        String myIconPath = CommonUtils.userInfo.getUserPhoto();
        System.out.println(myIconPath+"::::::");
        if(myIconPath.equals("")) {
            myIconPath = "http://bbs.bxxx.cn/uc_server/images/noavatar_small.gif";
        }else {
            myIconPath = myIconPath.replace("\\/", "/");
        }
        System.out.println(myIconPath+"++++");
        imgMyPhoto = (CircleImageView) findViewById(R.id.img_me_icon);
        Drawable mImgPlaceholder = mContext.getResources().getDrawable(R.drawable.new_my_face);
        /*
        if(myIconPath.contains(".gif")) {
            Glide.with(getApplicationContext()).load(myIconPath).into(imgMyPhoto);
        }else{*/
            Picasso
                    .with(mContext)
                    .load(myIconPath).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .placeholder(mImgPlaceholder)
                    .transform(CommonUtils.getTransformation(mContext))
                    .into(imgMyPhoto);
        //}
        imgMyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(showAction==0) {
                    createActionSheet();
                    showAction = 1;
                }
                */
                onSelectFaceImageClick();
            }
        });
        final LinearLayout lyt_sign_every = (LinearLayout) findViewById(R.id.lyt_sign_every);
        lyt_sign_every.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lyt_sign_every.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lyt_sign_every.setClickable(true);
                    }
                }, 500);
                signEvery();
            }
        });
        final TextView txt_my_desc = (TextView) findViewById(R.id.txt_my_desc);
        txt_my_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_my_desc.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_my_desc.setClickable(true);
                    }
                }, 500);
                signEvery();
            }
        });

        txt_my_desc.setText(CommonUtils.userInfo.getAbsence() + "天");


        this.continuousAbsence();
    }

    private void loadUserInfo(){
        TextView txtMyName = findViewById(R.id.txt_my_name);

        TextView txt_my_level_date = (TextView) findViewById(R.id.txt_my_level_date);
        String dateline = CommonUtils.userInfo.getDateline();
        if(!dateline.isEmpty() && !dateline.equals("0")){
            txt_my_level_date.setText(dateline);
            txt_my_level_date.setVisibility(View.VISIBLE);
        }
        else{
            txt_my_level_date.setText("");
            txt_my_level_date.setVisibility(View.GONE);
        }

        txtMyName.setText(CommonUtils.userInfo.getUserName());
        TextView mTxtMoney = findViewById(R.id.txt_my_money);
        if(CommonUtils.userInfo.getBaixingbi().equals("")){
            mTxtMoney.setText("0");
        }else {
            mTxtMoney.setText(CommonUtils.userInfo.getBaixingbi());
        }

        TextView mTxtLevel = findViewById(R.id.txt_my_level);
        if(CommonUtils.userInfo.getLevel().equals("")){
            mTxtLevel.setText("新手");
        }else{
            mTxtLevel.setText(CommonUtils.userInfo.getLevel());
        }
        String myIconPath = CommonUtils.userInfo.getUserPhoto();
        if(myIconPath.equals("")) {
            myIconPath = "http://bbs.bxxx.cn/uc_server/images/noavatar_small.gif";
        }else {
            myIconPath = myIconPath.replace("\\/", "/");
        }
        System.out.println(myIconPath+"++++");
        imgMyPhoto = (CircleImageView) findViewById(R.id.img_me_icon);
        Picasso
                .with(mContext)
                .load(myIconPath).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .transform(CommonUtils.getTransformation(mContext))
                .into(imgMyPhoto);
        final LinearLayout lyt_sign_every = (LinearLayout) findViewById(R.id.lyt_sign_every);
        final TextView txt_my_desc = findViewById(R.id.txt_my_desc);
        final TextView txt_my_name_set = findViewById(R.id.txt_my_name_set);
        String changeid = CommonUtils.userInfo.getChangeid();
        if(changeid == null || changeid.equals("0")){
            txt_my_name_set.setVisibility(TextView.GONE);
        }
        else{
            txt_my_name_set.setVisibility(TextView.VISIBLE);
        }

    }

    private void getUserInfo(String _uid) {
        //mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, this);
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", _uid);
        String url = "api/user/profile/";
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
                    JSONObject response = resp.body();
                    Integer code = response.getInt("code");
                    System.out.println(response);
                    if (code==1) {
                        JSONObject userObj = response.optJSONObject("ret");
                        System.out.println(userObj);
                        CommonUtils.userInfo.setUserName(userObj.optString("username"));
                        CommonUtils.userInfo.setChangeid(userObj.optString("changeid"));
                        CommonUtils.userInfo.setDateline(userObj.optString("groupexpiry"));
                        CommonUtils.userInfo.setBaixingbi(userObj.optString("credits"));
                        CommonUtils.userInfo.setLevel(userObj.optString("grouptitle"));
                        CommonUtils.userInfo.setUserPhoto(userObj.optString("avatar"));

                        CommonUtils.userInfo.save(mContext);

                        loadUserInfo();


                    } else if(code==-1) {
                        //Toast.makeText(mContext, "用户名不存在", Toast.LENGTH_LONG).show();
                    } else if(code==-2) {
                        //Toast.makeText(mContext, "密码错误", Toast.LENGTH_LONG).show();
                    } else{
                        //Toast.makeText(mContext, "网络有点问题，请稍后再试", Toast.LENGTH_LONG).show();
                        /*
                        CommonUtils.userInfo = new UserInfo(Long.valueOf(uid), "",
                                0, "", mobile,
                                "", "", "", "",uid, qq, wechat, mobile);
                        gotoForward();
                        */
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t){

            }

        });
    }
    private void showAlertLogout() {
        final Dialog dialog = new Dialog(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_exit, null);
        TextView btnLogout = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnExit = (TextView) view.findViewById(R.id.btn_cancel);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CommonUtils.userInfo.setUserID(0);
                CommonUtils.userInfo.setUserName("");
                CommonUtils.userInfo.setUserGender(0);
                CommonUtils.userInfo.setUserBirthday("");
                CommonUtils.userInfo.setUserPhone("");
                CommonUtils.userInfo.setUserPhoto("");
                CommonUtils.userInfo.setToken("");
                CommonUtils.userInfo.setUserIdentify("");
                CommonUtils.userInfo.setUserPassword("");
                CommonUtils.userInfo.setBaixingbi("0");
                CommonUtils.userInfo.setLevel("");
                CommonUtils.userInfo.setLoginType("normal");
                CommonUtils.userInfo.setLoginUsername("");
                CommonUtils.userInfo.setLoginPassword("");
                CommonUtils.isLogin = false;

                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                editor.putLong("userID", CommonUtils.userInfo.getUserID());
                editor.putString("userName", CommonUtils.userInfo.getUserName());
                editor.putInt("userGender", CommonUtils.userInfo.getUserGender());
                editor.putString("userBirthday", CommonUtils.userInfo.getUserBirthday());
                editor.putString("userPhone", CommonUtils.userInfo.getUserPhone());
                editor.putString("userPhoto", CommonUtils.userInfo.getUserPhoto());
                editor.putString("token", CommonUtils.userInfo.getToken());
                editor.putString("identify", CommonUtils.userInfo.getUserIdentify());
                editor.putString("password", CommonUtils.userInfo.getUserPassword());
                editor.putString("baixingbi", CommonUtils.userInfo.getBaixingbi());
                editor.putString("level", CommonUtils.userInfo.getLevel());
                editor.putString("login_type", CommonUtils.userInfo.getLoginType());
                editor.putString("login_username", CommonUtils.userInfo.getLoginUsername());
                editor.putString("login_password", CommonUtils.userInfo.getLoginPassword());
                editor.apply();

                TabHostActivity.TabHostStack.gotoLogin();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //TabHostActivity.TabHostStack.finish();
            }
        });

        CommonUtils.showAlertDialog(mContext, dialog, view, 216);
    }
    private void signEvery() {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_LONG).show();
            return;
        }
//        RequestParams params = new RequestParams();
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", CommonUtils.userInfo.getUid());
        System.out.println("uuu:"+CommonUtils.userInfo.getUid());
        String url = "api/user/sign";
        final MeActivity self = this;

        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
                    JSONObject response = resp.body();
                    if (response.getInt("code") == 1) {
                        CommonUtils.userInfo.setBaixingbi(String.valueOf(Integer.valueOf(CommonUtils.userInfo.getBaixingbi()) + 2));
                        TextView mTxtMoney = (TextView) findViewById(R.id.txt_my_money);
                        mTxtMoney.setText(CommonUtils.userInfo.getBaixingbi());
                        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        editor.putString("baixingbi", CommonUtils.userInfo.getBaixingbi());
                        editor.apply();

                        self.continuousAbsence();

                        CommonUtils.showAlertDialog(mContext, response.optString("message"), null);
                    }else {
                            CommonUtils.showAlertDialog(mContext, "您已签到，明天再来哦", null);
                        }

                } catch (JSONException e) {
                    //CommonUtils.dismissProgress(progressDialog);
                    e.printStackTrace();
                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t){
                //CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void continuousAbsence() {
        //final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, VerifyPhoneActivity.this);
        if(!CommonUtils.isLogin){
            Toast.makeText(mContext, "请登录吧", Toast.LENGTH_LONG).show();
            return;
        }
//        RequestParams params = new RequestParams();
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid", CommonUtils.userInfo.getUid());
        System.out.println("uuu:"+CommonUtils.userInfo.getUid());
        String url = "api/user/absence";

        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                try {
                    JSONObject response = resp.body();
                    if (response.getInt("code") == 1) {
                        TextView mTxtTian = findViewById(R.id.txt_my_desc);
                        CommonUtils.userInfo.setAbsence(response.getString("ret"));
                        mTxtTian.setText(CommonUtils.userInfo.getAbsence() + "天");
                    }else {
                    }

                } catch (JSONException e) {
                    //CommonUtils.dismissProgress(progressDialog);
                    e.printStackTrace();
                    Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t){
                //CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();

            }
        }, 1);




    }

    public void selectDate() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.calendar_dialog, null);
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar);
        if (Build.VERSION.SDK_INT >= 23) {
            calendarView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        Calendar curDate = Calendar.getInstance();
        curDate.add(Calendar.DAY_OF_YEAR, 1);
        long minTime = curDate.getTimeInMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        long milliTime = calendar.getTimeInMillis();

        CalendarView cal = (CalendarView) view.findViewById(R.id.calendar);
        //cal.setMinDate(minTime - 10000);
        //cal.setMinDate(minTime - 360000000);
        cal.setDate(milliTime, false, true);

        long current = cal.getDate();
        cal.setDate(cal.getMaxDate(), false, true);
        cal.setDate(current, false, true);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                mTmpYear = year;
                mTmpMonth = month;
                mTmpDay = dayOfMonth;
            }
        });
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(res.getText(R.string.str_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mYear != mTmpYear || mMonth != mTmpMonth || mDay != mTmpDay) {
                    mYear = mTmpYear;
                    mMonth = mTmpMonth;
                    mDay = mTmpDay;
                    //setCurrentDate();
                    //getLastDate();
                    //scrollListener.resetState();
                    //setMonthDays(mYear, mMonth, mDay);
                    //loadTimesList();
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        HomeGroupActivity.HomeGroupStack.finishChildActivity();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String uid = CommonUtils.userInfo.getUid();
        getUserInfo(uid);

        System.out.println("comp1");
        SharedPreferences cropData = getSharedPreferences("cropData", MODE_PRIVATE);
        String crop = cropData.getString("crop","");
        System.out.println("path++"+crop);
        if(!crop.equals("")){
            Bitmap _face_bmp = showImageTwo(crop);
            CommonUtils.face_bmp = Bitmap.createScaledBitmap(_face_bmp, 128, 128, true);
            storeImageTosdCard(CommonUtils.face_bmp);
            SharedPreferences.Editor editor = getSharedPreferences("cropData", MODE_PRIVATE).edit();
            editor.putString("crop", "");
            editor.apply();
        }else if (CommonUtils.userInfo.getUserPhoto().equals("") || CommonUtils.userInfo.getUserPhoto().equals(null) ) {
            String photo_path = "http://bbs.bxxx.cn/uc_server/images/noavatar_small.gif";
            Picasso.with(MeActivity.this).load(photo_path).into(imgMyPhoto);
        } else if(CommonUtils.face_bmp!=null) {
            imgMyPhoto.setImageBitmap(CommonUtils.face_bmp);
        }
        else
        {
            String photo_path = CommonUtils.userInfo.getUserPhoto();
            Picasso.with(MeActivity.this).load(photo_path).into(imgMyPhoto);
        }
    }
    public Bitmap showImageTwo(String surl) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bm = null;
        try {
            URL url = new URL(surl);
            bm = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (IOException e) {
            System.out.println("e++" + e.getMessage());
            //Log.e(TAG, e.getMessage());
        }
        return bm;
    }

    private void createActionSheet(){
        ActionSheet.createBuilder(mContext, MeActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("从文件夹")
                .setCancelableOnTouchOutside(true)
                .setListener(MeActivity.this)
                .show();
    }

    public void selectResult(int requestCode, int resultCode, Intent data) {

        bitmap = null;

        if (resultCode == RESULT_OK && requestCode == CommonUtils.CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {
                Toast.makeText(getBaseContext(), "同时采集图像误差", Toast.LENGTH_LONG).show();
                return;
            }

            try {

                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                storeImageTosdCard(bitmap);
                f.delete();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == CommonUtils.GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };

                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);

                c.close();

                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                bitmap = BitmapFactory.decodeFile(selectedImagePath, options); // load
                bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);

                storeImageTosdCard(bitmap);

            } else {
                Toast.makeText(getApplicationContext(), "取消",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        showAction = 0;
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        TabActivity tabActivity = (TabActivity) MeActivity.this.getParent().getParent();
        /*
        if (index == 0) { // Camera
            Log.e(TAG, "Camera button");
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            tabActivity.startActivityForResult(intent, CommonUtils.CAMERA_REQUEST);
        } else if (index == 1) { // Gallery
            Log.e(TAG, "Gallery button");

            pictureActionIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            tabActivity.startActivityForResult(pictureActionIntent, CommonUtils.GALLERY_PICTURE);
        }
        */
        if (index == 0) { // Gallery
            Log.e(TAG, "Gallery button");

            pictureActionIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            tabActivity.startActivityForResult(pictureActionIntent, CommonUtils.GALLERY_PICTURE);
        }
        showAction = 0;
    }

    private void storeImageTosdCard(Bitmap processedBitmap) {
        try {
            // TODO Auto-generated method stub
            OutputStream output;

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            Log.e(TAG, "selectedImagePath0:" + String.valueOf(selectedImagePath));
            File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!storageDirectory.exists()) {
                storageDirectory = Environment.getExternalStoragePublicDirectory("data");
            }
            File mediaFile = new File(storageDirectory + File.separator + "IMG_" + timeStamp + ".png");

            if (mediaFile.exists()) {
                mediaFile.delete();
                mediaFile.createNewFile();
            } else {
                mediaFile.createNewFile();
            }

            try {

                output = new FileOutputStream(mediaFile);

                // Compress into png format image from 0% - 100%
                processedBitmap
                        .compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();

                int file_size = Integer
                        .parseInt(String.valueOf(mediaFile.length() / 1024));
                System.out.println("size ===>>> " + file_size);
                System.out.println("file.length() ===>>> " + mediaFile.length());

                selectedImagePath = mediaFile.getAbsolutePath();
                imgMyPhoto.setImageResource(android.R.color.transparent);
                imgMyPhoto.setImageBitmap(processedBitmap);
                System.out.println("sel+++"+selectedImagePath);
                uploadUserPhoto(processedBitmap);
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void uploadUserPhoto(Bitmap processedBitmap) {
        if (selectedImagePath == null) {
            return;
        }
        RequestParams params = new RequestParams();
        final Bitmap uploadedPhoto = processedBitmap;
        System.out.println("sel11+++"+selectedImagePath);
        try {
            params.put("uid", CommonUtils.userInfo.getUid());
            params.put("photo", new File(selectedImagePath));
            params.put("act", "head");
        } catch (FileNotFoundException e) {
            System.out.println("sel22+++"+selectedImagePath);
            e.printStackTrace();
        }
        CommonUtils.face_bmp = uploadedPhoto;
        String url = APIManager.Ucenter_URL;
        final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, MeActivity.this);
        System.out.println("sel33+++"+selectedImagePath);
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                CommonUtils.dismissProgress(progressDialog);
                System.out.println("img upload"+response);
                try{
                    //if (response.optBoolean("status") == true) {
                        JSONArray js_path = response.optJSONArray("path");
                        System.out.println("js_path"+js_path);
                        System.out.println("js 111"+js_path.optString(0));
                        if(js_path!=null && js_path.length()>0) {
                            String photo_path = js_path.optString(0);
                            if (photo_path.substring(0, 2).equals("..")) {
                                photo_path = CommonUtils.getUrlEncoded(APIManager.IMAGE_URL + photo_path.substring(2));
                            }else if (photo_path.substring(0, 1).equals(".")){
                                photo_path = CommonUtils.getUrlEncoded(APIManager.IMAGE_URL + photo_path.substring(1));
                            } else {
                                photo_path = CommonUtils.getUrlEncoded(APIManager.IMAGE_URL + photo_path);
                            }
                            CommonUtils.face_bmp = uploadedPhoto;
                            System.out.println("logo face"+photo_path);
                            CommonUtils.userInfo.setUserPhoto(photo_path);
                            Picasso.with(MeActivity.this).load(photo_path).into(imgMyPhoto);
                        }
                        //imgMyPhoto.setImageResource(android.R.color.transparent);
                        //imgMyPhoto.setImageBitmap(uploadedPhoto);
                    //} else {
                    //    Toast.makeText(mContext, response.optString("message"), Toast.LENGTH_LONG).show();
                    //}
                    Log.e(TAG, response.getString("message"));
                    File tmpFile = new File(selectedImagePath);
                    if (tmpFile.exists())tmpFile.delete();
                } catch (JSONException e) {
                    System.out.println("img upload22"+response);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                CommonUtils.dismissProgress(progressDialog);
                Log.e(TAG, "onFailure");
                File tmpFile = new File(selectedImagePath);
                if (tmpFile.exists())tmpFile.delete();
                System.out.println(errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                CommonUtils.dismissProgress(progressDialog);
                Log.e(TAG, "onFailure");
                File tmpFile = new File(selectedImagePath);
                if (tmpFile.exists())tmpFile.delete();
                System.out.println(responseString);
                imgMyPhoto.setImageResource(android.R.color.transparent);
                imgMyPhoto.setImageBitmap(uploadedPhoto);
                CommonUtils.face_bmp =uploadedPhoto;
            }
        });
    }
    public void onSelectFaceImageClick() {
//        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
        CropImage.activity().setAspectRatio(1, 1).start(this);
    }
    public void onSelectFaceImageClick(View view) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        System.out.println("tetstetse");

            CommonUtils.face_bmp = result.getBitmap();
            imgMyPhoto.setImageResource(android.R.color.transparent);
            imgMyPhoto.setImageBitmap(CommonUtils.face_bmp);
            uploadUserPhoto(CommonUtils.face_bmp);
            //imgMyPhoto.setImageURI(result.getUri());
            Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result of CropImageActivity
        System.out.println("re111"+requestCode);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("re"+requestCode);
            if (resultCode == RESULT_OK) {
                CommonUtils.face_bmp = result.getBitmap();
                imgMyPhoto.setImageResource(android.R.color.transparent);
                imgMyPhoto.setImageBitmap(CommonUtils.face_bmp);
                uploadUserPhoto(CommonUtils.face_bmp);
                //imgMyPhoto.setImageURI(result.getUri());
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "失败: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == SetNameActivity.SET_NAME_REQUEST_CODE){
            onResume();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            pActivity.finishChildActivity();
//            intent = new Intent(MeActivity.this, HomeActivity.class);
//            pActivity.startChildActivity("home_activity", intent);
            TabHostActivity.setCurrentTab(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
