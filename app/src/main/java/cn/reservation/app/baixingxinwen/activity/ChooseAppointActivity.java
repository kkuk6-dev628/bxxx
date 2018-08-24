package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.walnutlabs.android.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.adapter.MonthDateListAdapter;
import cn.reservation.app.baixingxinwen.adapter.OtherDoctorListAdapter;
import cn.reservation.app.baixingxinwen.adapter.TimesItemListAdapter;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import cn.reservation.app.baixingxinwen.utils.DoctorItem;
import cn.reservation.app.baixingxinwen.utils.EndlessRecyclerViewScrollListener;
import cn.reservation.app.baixingxinwen.utils.MonthDateItem;
import cn.reservation.app.baixingxinwen.utils.TimesItem;
import cz.msebera.android.httpclient.Header;

public class ChooseAppointActivity extends AppCompatActivity implements DialogInterface.OnCancelListener, ActionSheet.ActionSheetListener {

    public Resources res;
    public Context mContext;

    public MonthDateListAdapter mMonthDateListAdapter;
    public TimesItemListAdapter mTimesItemListAdapter;
    public OtherDoctorListAdapter mOtherDoctorListAdapter;
    public List<MonthDateItem> mMonthDateItems = new ArrayList<MonthDateItem>();
    public List<DoctorItem> mOtherDoctorItems = new ArrayList<DoctorItem>();

    public int[] weekStatus = {0, 0, 0, 0, 0, 0, 0};

    public int mYear = 0;
    public int mMonth = 0;
    public int mDay = 0;
    public int mWeekDay = 0;
    public DoctorItem mDoctorItem;
    public String mPatientID;
    private int mTmpYear = 0;
    private int mTmpMonth = 0;
    private int mTmpDay = 0;

    private int mLastYear;
    private int mLastMonth;
    private int mLastDay;
    public boolean mIsMoreDate = true;
    public int mCurIndex = 0;

    public TextView mTxtCurrentYear;
    public TextView mTxtCurrentMonth;
    public TextView mTxtCurrentDay;
    public TextView mTxtCurrentWeekDay;
    public RecyclerView mHlvMonthDate;
    public ListView mLstTimes;
    public RecyclerView mHlvOtherDoctors;
    public TextView mTxtOtherDoctors;

    public boolean isForDoctor = true;
    public boolean isExpert = false;

    public Drawable mImgPlaceholder;

    public EndlessRecyclerViewScrollListener scrollListener;
    public ProgressHUD mProgressDialog;

    public ImageView mImgUpTime;
    public ImageView mImgDownTime;
    public ImageView mImgLeftDate;
    public ImageView mImgRightDate;
    public ImageView mImgLeftDoctor;
    public ImageView mImgRightDoctor;

    public ArrayList<TimesItem> mTiemsItems = new ArrayList<TimesItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean checkDate(int iYear, int iMonth, int iDay) {
        Calendar curDate = Calendar.getInstance();
        curDate.add(Calendar.DAY_OF_YEAR, 1);
        long currentTime = curDate.getTimeInMillis();
        Calendar tmpDate = Calendar.getInstance();
        tmpDate.set(iYear, iMonth, iDay);
        long tmpTime = tmpDate.getTimeInMillis();
        return tmpTime < currentTime;
    }

    public void setDateFirstly() {
        Calendar curDate = Calendar.getInstance();
        curDate.add(Calendar.DAY_OF_YEAR, 1);

        mTxtCurrentYear.setText(Integer.toString(curDate.get(Calendar.YEAR)));
        mTxtCurrentMonth.setText(Integer.toString(curDate.get(Calendar.MONTH)+1));
        mTxtCurrentDay.setText(Integer.toString(curDate.get(Calendar.DAY_OF_MONTH)));
        mTxtCurrentWeekDay.setText("(" + CommonUtils.getWeekDay(mContext, curDate.get(Calendar.DAY_OF_WEEK)) + ")");
    }

    public void setCurrentDate() {
        Calendar curDate = Calendar.getInstance();
        curDate.set(mYear, mMonth, mDay);
        mWeekDay = curDate.get(Calendar.DAY_OF_WEEK);

        mTxtCurrentYear.setText(Integer.toString(mYear));
        mTxtCurrentMonth.setText(Integer.toString(mMonth+1));
        mTxtCurrentDay.setText(Integer.toString(mDay));
        mTxtCurrentWeekDay.setText("(" + CommonUtils.getWeekDay(mContext, mWeekDay) + ")");
    }

    public void getLastDate() {
        Calendar curDate = Calendar.getInstance();
        curDate.set(mYear, mMonth, mDay);
        curDate.add(Calendar.DAY_OF_YEAR, 30);
        mLastYear = curDate.get(Calendar.YEAR);
        mLastMonth = curDate.get(Calendar.MONTH);
        mLastDay = curDate.get(Calendar.DAY_OF_MONTH);
        mIsMoreDate = true;
    }

    public void getWeekStatus() {
        mMonthDateListAdapter.setClear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChooseAppointActivity.this);
                RequestParams params = new RequestParams();
                params.put("whospno", mDoctorItem.getmHospitalID());
                params.put("hosno", mDoctorItem.getmRoomID());
                params.put("courseno", mDoctorItem.getmDoctorID());
                params.put("lang", CommonUtils.mIntLang);

                String url = "getweekttable";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for(int i=0; i<list.length(); i++) {
                                JSONObject item = list.getJSONObject(i);
                                weekStatus[item.getInt("Week")] = item.getInt("state");
                            }
                            setDefaultDay();
                            loadFirstTimesList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        mProgressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 300);
    }

    public void setMonthDays(int iYear, int iMonth, int iDay) {
        //int diff = getDiffDates(iYear, iMonth, iDay) + 3;
        int diff = 3;
        Calendar calendar = Calendar.getInstance();
        calendar.set(iYear, iMonth, iDay);
        int iWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (iWeek == 7) {
            diff = 0;
        } else {
            diff = iWeek;
        }
        calendar.add(Calendar.DAY_OF_YEAR, -diff);

        mMonthDateListAdapter.setClear();
        mMonthDateListAdapter.notifyDataSetChanged();
        for(int i=0; i<=7; i++) {
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isRest = weekStatus[week - 1] == 0;
            boolean isLast = checkDate(y, m, d);
            boolean isSelected = false;
            if (y == mYear && m == mMonth && d == mDay) {
                isSelected = true;
                mCurIndex = i;
            }
            mMonthDateListAdapter.addItem(new MonthDateItem(isRest, y, m, d, calendar.get(Calendar.DAY_OF_WEEK)-1, isLast, isSelected));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        mMonthDateListAdapter.notifyDataSetChanged();
        mHlvMonthDate.scrollToPosition(0);
    }

    private void setDefaultDay() {
        Calendar curDate = Calendar.getInstance();
        curDate.add(Calendar.DAY_OF_YEAR, 1);
        int iYear = curDate.get(Calendar.YEAR);
        int iMonth = curDate.get(Calendar.MONTH);
        int iDay = curDate.get(Calendar.DAY_OF_MONTH);
        int week = curDate.get(Calendar.DAY_OF_WEEK);

        boolean isRest = weekStatus[week - 1] == 0;
        int i = 0;
        while(isRest && i < 7) {
            curDate.add(Calendar.DAY_OF_YEAR, 1);
            week = curDate.get(Calendar.DAY_OF_WEEK);
            isRest = weekStatus[week - 1] == 0;
            i++;
        }
        if (!isRest) {
            mYear = curDate.get(Calendar.YEAR);
            mMonth = curDate.get(Calendar.MONTH);
            mDay = curDate.get(Calendar.DAY_OF_MONTH);
        } else {
            mYear = iYear;
            mMonth = iMonth;
            mDay = iDay;
        }

        mTmpYear = mYear;
        mTmpMonth = mMonth;
        mTmpDay = mDay;

        setCurrentDate();
        getLastDate();
        setMonthDays(mYear, mMonth, mDay);
    }

    public void loadMoreNextDate(int index) {
        MonthDateItem monthDateItem = mMonthDateListAdapter.getItem(index);
        Calendar calendar = Calendar.getInstance();
        calendar.set(monthDateItem.getmYear(), monthDateItem.getmMonth(), monthDateItem.getmDay());
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        for(int i=0; i<7; i++) {
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isRest = weekStatus[week - 1] == 0;
            boolean isLast = checkDate(y, m, d);
            mMonthDateListAdapter.addItem(new MonthDateItem(isRest, y, m, d, calendar.get(Calendar.DAY_OF_WEEK)-1, isLast, false));
            if (y == mLastYear && m == mLastMonth && d == mLastDay) {
                mIsMoreDate = false;
                break;
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        mMonthDateListAdapter.notifyDataSetChanged();
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
        long milliTime = calendar.getTimeInMillis();

        CalendarView cal = (CalendarView) view.findViewById(R.id.calendar);
        cal.setMinDate(minTime - 10000);
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
                    setCurrentDate();
                    getLastDate();
                    scrollListener.resetState();
                    setMonthDays(mYear, mMonth, mDay);
                    loadTimesList();
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void loadTimesList() {
        mTimesItemListAdapter.clearItems();
        mTiemsItems.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ProgressHUD progressDialog = ProgressHUD.show(mContext, res.getString(R.string.processing), true, false, ChooseAppointActivity.this);
                RequestParams params = new RequestParams();
                params.put("whospno", mDoctorItem.getmHospitalID());
                params.put("hosno", mDoctorItem.getmRoomID());
                params.put("courseno", mDoctorItem.getmDoctorID());
                params.put("day", Integer.toString(mYear)+"-"+Integer.toString(mMonth+1)+"-"+Integer.toString(mDay));
                params.put("weekday", Integer.toString(mWeekDay-1));

                String url = "gettimetable";
                APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject list = response.getJSONObject("list");
                            JSONArray timetable = list.getJSONArray("timetable");
                            for(int i=0; i<timetable.length(); i++) {
                                JSONObject item = timetable.getJSONObject(i);
                                String tt = formattedTime(item.getDouble("time"));
                                boolean isActive = item.getInt("status") == 1;
                                mTiemsItems.add(new TimesItem(tt, isActive));
                            }
                            if (mTiemsItems.size()>0) {
                                mTimesItemListAdapter.setListItems(preProcessTimes());
                            }
                            mTimesItemListAdapter.notifyDataSetChanged();
                            mLstTimes.smoothScrollToPosition(0);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 300);
    }

    public void loadFirstTimesList() {
        mTimesItemListAdapter.clearItems();
        mTiemsItems.clear();
        RequestParams params = new RequestParams();
        params.put("whospno", mDoctorItem.getmHospitalID());
        params.put("hosno", mDoctorItem.getmRoomID());
        params.put("courseno", mDoctorItem.getmDoctorID());
        params.put("day", Integer.toString(mYear)+"-"+Integer.toString(mMonth+1)+"-"+Integer.toString(mDay));
        params.put("weekday", Integer.toString(mWeekDay-1));

        String url = "gettimetable";
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject list = response.getJSONObject("list");
                    JSONArray timetable = list.getJSONArray("timetable");
                    for(int i=0; i<timetable.length(); i++) {
                        JSONObject item = timetable.getJSONObject(i);
                        String tt = formattedTime(item.getDouble("time"));
                        boolean isActive = item.getInt("status") == 1;
                        mTiemsItems.add(new TimesItem(tt, isActive));
                    }
                    if (mTiemsItems.size()>0) {
                        mTimesItemListAdapter.setListItems(preProcessTimes());
                    }
                    mTimesItemListAdapter.notifyDataSetChanged();
                    loadOtherDoctors();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<TimesItem> preProcessTimes() {
        ArrayList<TimesItem> timesItems = new ArrayList<TimesItem>();
        for(int i=0; i< mTiemsItems.size(); i++) {
            TimesItem timesItem = mTiemsItems.get(i);
            if (timesItem.ismActive()) {
                timesItems.add(timesItem);
            } else {
                if (!timesItem.getmTime().equals("12:00") && !timesItem.getmTime().equals("12:30") && !checkTimesItem(i)) {
                    timesItems.add(timesItem);
                }
            }
        }
        return timesItems;
    }

    private boolean checkTimesItem(int index) {
        boolean beforeActive = false;
        boolean afterActive = false;
        for(int i=0; i<index; i++) {
            if (mTiemsItems.get(i).ismActive()) {
                beforeActive = true;
                break;
            }
        }
        for(int i=index+1; i<mTiemsItems.size(); i++) {
            if (mTiemsItems.get(i).ismActive()) {
                afterActive = true;
                break;
            }
        }
        return (!beforeActive && afterActive) || (beforeActive && afterActive);
    }

    public String formattedTime(double t) {
        String h = Integer.toString((int) t);
        if (h.length() == 1) h = "0" + h;
        String m = Integer.toString ((int)((t * 10) % 10) * 6);
        if (m.length() == 1) m = "0" + m;
        return h + ":" + m;
    }

    public void setAppoint(String time) {

    }

    public void selectOtherDoctor(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
    }

    public class ListViewTouchListener implements ListView.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;
        }
    }

    public void loadOtherDoctors() {
        mOtherDoctorListAdapter.setClear();
        RequestParams params = new RequestParams();
        String url = "";
        if (isExpert) {
            params.put("whospno", mDoctorItem.getmHospitalID());
            params.put("hosno", mDoctorItem.getmRoomID());
            params.put("courseno", mDoctorItem.getmDoctorID());
            params.put("lang", CommonUtils.mIntLang);
            params.put("roomname", mDoctorItem.getmRoom());

            url = "getexpertmates";
        } else {
            params.put("whospno", mDoctorItem.getmHospitalID());
            params.put("hosno", mDoctorItem.getmRoomID());
            params.put("courseno", mDoctorItem.getmDoctorID());
            params.put("lang", CommonUtils.mIntLang);

            url = "getroommates";
        }
        APIManager.post(mContext, url, params, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getString("code").equals("0")) {
                        mProgressDialog.dismiss();
                        mOtherDoctorListAdapter.setClear();
                    } else {
                        JSONArray list = response.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            if (isExpert) {
                                mOtherDoctorListAdapter.addItem(new DoctorItem(
                                        (long) item.getInt("CourseNo"), (long) item.getInt("WHospNo"), (long) item.getInt("HosNo"),
                                        item.getString("Picture"), item.getString("CourseName"), item.getString("HospName"),
                                        mDoctorItem.getmRoom(), item.getString("Title"), item.getString("Price"), item.getString("HospTel"), 5, true));
                            } else {
                                mOtherDoctorListAdapter.addItem(new DoctorItem(
                                        (long) item.getInt("CourseNo"), mDoctorItem.getmHospitalID(), mDoctorItem.getmRoomID(),
                                        item.getString("Picture"), item.getString("CourseName"), mDoctorItem.getmHospital(),
                                        mDoctorItem.getmRoom(), item.getString("Title"), item.getString("Price"), mDoctorItem.getmPhone(), 5, true));
                            }
                        }
                        CommonUtils.dismissProgress(mProgressDialog);
                    }
                    mOtherDoctorListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createActionSheet(String phones){
        String[] phone = phones.split(",");
        ActionSheet.createBuilder(mContext, ChooseAppointActivity.this.getSupportFragmentManager())
                .setCancelButtonTitle(res.getString(R.string.str_cancel))
                .setOtherButtonTitles(phone)
                .setCancelableOnTouchOutside(true)
                .setListener(ChooseAppointActivity.this)
                .show();
    }

    public static void smoothScrollToPosition(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) { }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        String[] phone = mDoctorItem.getmPhone().split(",");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone[index]));
        startActivity(intent);
    }
}
