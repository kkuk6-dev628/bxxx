package cn.reservation.app.baixingxinwen.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Transformation;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.activity.ConfirmDialogActivity;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtils {

    public static UserInfo userInfo;
    public static boolean isLogin = false;
    public static int mIntLang = 0;

    public static int appointYear = 0;
    public static int appointMonth = 0;
    public static int appointDay = 0;
    public static int appointWeekday = 0;
    public static String appointTime = "";
    public static DoctorItem appointDoctorItem;

    public static final int REQUEST_CODE_APPOINT = 1000;
    public static final int RESULT_CODE_APPOINT = 1001;
    public static final int REQUEST_CODE_FAMILY = 1002;
    public static final int RESULT_CODE_FAMILY = 1003;
    public static final int CAMERA_REQUEST = 1004;
    public static final int GALLERY_PICTURE = 1005;
    public static final int REQUEST_CODE_ANOTHER = 1006;
    public static final int REQUEST_CODE_MODIFY = 1007;
    public static final int RESULT_CODE_MODIFY = 1008;
    public static final int REQUEST_CODE_LOGIN = 1009;
    public static final int RESULT_CODE_LOGIN = 1010;
    public static final int RESULT_CODE_APPOINT_HELP = 1011;
    public static final int SDK_PAY_FLAG = 1;
    public static Bitmap face_bmp = null;
    public static Bitmap share_bmp = null;

    public  static  String orderString = "";
    public static File[] mFiles;
    public static Dictionary data1;
    public static Dictionary regionData;
    public static int pay_type = -1;
    public static String tradeNo = "";

    public static String channel_id = "";

    public static void initPostData(){
        data1.put("userID","");
        data1.put("fId","");
        data1.put("sortId","");
        data1.put("source","");
        data1.put("house_number","");
        data1.put("floors","");
        data1.put("contact","");
        data1.put("phone","");
        data1.put("region","");
        data1.put("house_type","");
        data1.put("house_level","");
        data1.put("house_type","");
        data1.put("house_level","");
        data1.put("havesun","");
        data1.put("villiage","");
        data1.put("award_method","");
        data1.put("square","");
        data1.put("square_range","");
        data1.put("price","");
        data1.put("picture","");
        data1.put("message","");
        data1.put("title","");
        data1.put("house_number_desc","");
        data1.put("numbers","");
        data1.put("level","");
        data1.put("salary_range","");
        data1.put("award_period","");
        data1.put("sex_demand","");
        data1.put("salary","");
        data1.put("experience","");
        data1.put("education","");
        data1.put("nation","");
        data1.put("qq","");
        data1.put("telephone","");
        data1.put("current_level","");
        data1.put("ask_region","");
        data1.put("home_region","");
        data1.put("name","");
        data1.put("age","");
        data1.put("sex","");
        data1.put("option","");
        data1.put("company_address","");
        data1.put("company_name","");
        data1.put("car_type","");
        data1.put("car_brand","");
        data1.put("brand_years","");
        data1.put("car_speed","");
        data1.put("car_price","");
        data1.put("test_estimate","");
        data1.put("abaility_estimate","");
        data1.put("brand_years","");
        data1.put("car_speed","");
        data1.put("years_estimate","");
        data1.put("abaility_estimate","");
        data1.put("group","");
        data1.put("type","");
        data1.put("company_address","");
        data1.put("company_name","");
        data1.put("age","");
        data1.put("star_pos","");
        data1.put("height","");
        data1.put("weight","");
        data1.put("education","");
        data1.put("salary","");
        data1.put("hobby","");
        data1.put("future","");
        data1.put("address","");
        data1.put("company","");
        data1.put("region","");
        data1.put("education","");
        data1.put("type","");
        data1.put("period","");
        data1.put("education_price","");
        data1.put("carry_period","");
        data1.put("order","");
    }
    public static void customActionBarWithRightButton(Context context, final AppCompatActivity activity,
                                                      boolean isShow, String title, String rightText, final View.OnClickListener rightClick) {
        ActionBar actionBar =  activity.getSupportActionBar();
        if (!isShow) {
            actionBar.hide();
            return;
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.top_bg));
        actionBar.setBackgroundDrawable(background);

        LayoutInflater mInflater = LayoutInflater.from(context);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleText = (TextView) mCustomView.findViewById(R.id.actionbar_title);
        mTitleText.setText(title);

        RelativeLayout mBackLayout = (RelativeLayout) mCustomView.findViewById(R.id.layout_back);
        mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        TextView mRightButton = mCustomView.findViewById(R.id.textButton_right);
        mRightButton.setText(rightText);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightClick != null){
                    rightClick.onClick(v);
                }
            }
        });

        ActionBar.LayoutParams p = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        actionBar.setCustomView(mCustomView, p);
        actionBar.setDisplayShowCustomEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            setStatusBarColor(activity, activity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    public static void customActionBar(Context context, final AppCompatActivity activity, boolean isShow, String title) {
        ActionBar actionBar =  activity.getSupportActionBar();
        if (!isShow) {
            actionBar.hide();
            return;
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.top_bg));
        actionBar.setBackgroundDrawable(background);

        LayoutInflater mInflater = LayoutInflater.from(context);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleText = (TextView) mCustomView.findViewById(R.id.actionbar_title);
        mTitleText.setText(title);

        RelativeLayout mBackLayout = (RelativeLayout) mCustomView.findViewById(R.id.layout_back);
        mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        TextView mRightButton = mCustomView.findViewById(R.id.textButton_right);
        mRightButton.setVisibility(View.GONE);

        ActionBar.LayoutParams p = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        actionBar.setCustomView(mCustomView, p);
        actionBar.setDisplayShowCustomEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            setStatusBarColor(activity, activity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColor(AppCompatActivity activity, int color){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showConfirmDialog(Context context, final ConfirmDialogActivity activity, int confirmType, String confirmMsg) {
        final Dialog dialog = new Dialog(context);
        View view = new ConfirmDialogView(context, confirmType, confirmMsg);

        TextView btnOK = (TextView) view.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.callBack();
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout(CommonUtils.getPixelValue(context, 200), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static void showIndicator(Context context){

    }

    public static void showAlertDialog(Context context, String message, final View.OnClickListener clickListener){
        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alert_message, null);

        TextView messageText = view.findViewById(R.id.txt_message);
        messageText.setText(message);
        TextView btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(clickListener != null){
                    clickListener.onClick(view);
                }
            }
        });
        CommonUtils.showAlertDialog(context, dialog, view, 250);
    }

    public static void showAlertDialog(Context context, Dialog dialog, View view, int width) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Rect displayRectangle = new Rect();
        Window window = ((Activity)context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout(CommonUtils.getPixelValue(context, width), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static Transformation getTransformation(final Context context) {
        final int height = 110;
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int targetHeight = CommonUtils.getPixelValue(context, height);

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }
            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        return transformation;
    }

    public static String getWeekDay(Context context, int weekday) {
        String[] weekdays = context.getResources().getStringArray(R.array.weekday_simply);
        return weekdays[weekday-1];
    }

    private static String getWeekDay2(Context context, int weekday) {
        String[] weekdays = context.getResources().getStringArray(R.array.weekday);
        return weekdays[weekday-1];
    }

    public static String getFormattedDateTime(Context context, String date, String hour, String min) {
        String result = "";
        try {
            long dt = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
            //Locale locale = new Locale("zh");
            String d = new SimpleDateFormat("yyyy.MM.dd").format(dt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt);
            result = d + "(" + getWeekDay2(context, calendar.get(Calendar.DAY_OF_WEEK)) + ")";
            long tt = new SimpleDateFormat("HH:mm").parse(hour + ":" + min).getTime();
            String t = new SimpleDateFormat("hh:mm").format(tt);
            result += " " + t + " " + getAMPM(hour + ":" + min);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFormattedDateTime(Context context, int year, int month, int day, String time) {
        String result = "";
        try {
            String dd = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
            long dt = new SimpleDateFormat("yyyy-MM-dd").parse(dd).getTime();
            //Locale locale = new Locale("zh");
            String d = new SimpleDateFormat("yyyy.MM.dd").format(dt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt);
            result = d + "(" + getWeekDay2(context, calendar.get(Calendar.DAY_OF_WEEK)) + ")";
            long tt = new SimpleDateFormat("HH:mm").parse(time).getTime();
            //String t = new SimpleDateFormat("hh:mm a").format(tt);
            String t = new SimpleDateFormat("hh:mm").format(tt);
            result += " " + t + " " + getAMPM(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFormattedDate(Context context, String date) {
        String result = "";
        try {
            long dt = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
            //Locale locale = new Locale("zh");
            String d = new SimpleDateFormat("yyyy.MM.dd").format(dt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dt);
            result = d + "(" + getWeekDay2(context, calendar.get(Calendar.DAY_OF_WEEK)) + ")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFormattedTime(Context context, String hour, String min) {
        String result = "";
        try {
            long tt = new SimpleDateFormat("HH:mm").parse(hour + ":" + min).getTime();
            String t = new SimpleDateFormat("hh:mm").format(tt);
            result = t + " " + getAMPM(hour + ":" + min);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getAMPM(String time) {
        long tt = 0;
        String ft = "0";
        try {
            tt = new SimpleDateFormat("HH").parse(time).getTime();
            ft = new SimpleDateFormat("HH").format(tt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(ft) < 12) {
            return "am";
        } else {
            return "pm";
        }
    }

    public static String getLeftTime(Context context, int seconds) {
        int m = seconds / 60;
        int mod = seconds % 60;
        return Integer.toString(m) + context.getResources().getString(R.string.str_minute) + " " +
                Integer.toString(mod) + context.getResources().getString(R.string.str_second);
    }

    public static RelativeLayout.LayoutParams getLayoutParams(Activity activity, int divide, int rest) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (width - rest) / divide;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                width, ViewGroup.LayoutParams.WRAP_CONTENT);
        return layoutParams;
    }

    public static int getPixelValue(Context context, float dipValue) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                resources.getDisplayMetrics()
        );
    }

    public static int getDpValue(Context context, int px) {
        Resources resources = context.getResources();
        return (int) (px / resources.getDisplayMetrics().density);
    }

    public static void dismissProgress(final ProgressHUD progressHUD) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressHUD.dismiss();
            }
        }, 800);
    }

    public static String getUrlEncoded(String url) {
        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        return Uri.encode(url, ALLOWED_URI_CHARS);
    }
    public static void registerChannelId(final Context context){
        final HashMap<String, Object> params = new HashMap<>();
        final String url = "api/user/regdevice";
        params.put("uid", userInfo.getUserID());
        params.put("cid", channel_id);
        params.put("platform", "android");
        NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                try {
                    JSONObject responseBody = response.body();
                    if (responseBody != null && responseBody.getInt("code") == 1) {
                        Log.d("Register Channel ID", "Channel ID : " + responseBody.getString("ret"));
                    }
                } catch (JSONException ex) {

                }
            }
            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                //CommonUtils.dismissProgress(progressDialog);
                Toast.makeText(context, "Add Channel Request failed", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public static boolean isDateValid(int y, int m, int d){
        String dateToValidate = Integer.toString(y) + "-" + Integer.toString(m + 1) + "-" + Integer.toString(d);
        String dateFormat = "yyyy-MM-dd";

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Boolean isValidInteger(String value) {
        try {
            Integer val = Integer.valueOf(value);
            if (val != null)
                return true;
            else
                return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getMonthFromString(String m) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int result = 0;
        for(int i=0; i<12; i++) {
            if (months[i].equals(m)) {
                result = i + 1;
                break;
            }
        }
        return result;
    }

    /**
     * Move to destination activity class with animate transition.
     */
    public static void moveNextActivity(Activity source, Class<?> destinationClass, boolean removeSource) {
        Intent intent = new Intent(source, destinationClass);

        if (removeSource) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        source.startActivity(intent);

        if (removeSource) {
            source.finish();
        }

        source.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    public static void moveNextActivity(Activity source, Class<?> destinationClass, boolean removeSource, boolean isForResult) {
        Intent intent = new Intent(source, destinationClass);

        if (removeSource) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (isForResult) {
            source.startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        } else {
            source.startActivity(intent);
        }

        if (removeSource) {
            source.finish();
        }

        source.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    public static void movePreviousActivity(Activity source, Class<?> destinationClass, boolean removeSource) {
        Intent intent = new Intent(source, destinationClass);

        if (removeSource) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        source.startActivity(intent);

        if (removeSource) {
            source.finish();
        }
        source.overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }
    public static Bitmap getBitmapFromURL(String src) {
        Bitmap bm = null;
        try {
            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                System.out.println("con::"+conn.getResponseCode());
                return bm;
            }
            conn.connect();
            InputStream is = conn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                bm = BitmapFactory.decodeStream(bis);
            } catch (OutOfMemoryError ex) {
                System.out.println("ex::"+ex);
                bm = null;
            }
            bis.close();
            is.close();
        } catch (Exception e) {
            System.out.println("eeee::"+e);
        }
        return bm;
    }

}
