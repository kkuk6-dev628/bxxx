package cn.reservation.app.baixingxinwen.utils;

/**
 * Created by LiYin on 3/15/2017.
 */
public class MonthDateItem {
    private boolean mRest;
    private int mWeekday;
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mLast;
    private boolean mSelected;

    public MonthDateItem(boolean mRest, int mYear, int mMonth, int mDay, int mWeekday, boolean mLast, boolean mSelected ) {
        this.mRest = mRest;
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mWeekday = mWeekday;
        this.mDay = mDay;
        this.mLast = mLast;
        this.mSelected = mSelected;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public boolean ismRest() {
        return mRest;
    }

    public void setmRest(boolean mRest) {
        this.mRest = mRest;
    }

    public int getmWeekday() {
        return mWeekday;
    }

    public void setmWeekday(int mWeekday) {
        this.mWeekday = mWeekday;
    }

    public boolean getmLast() {
        return mLast;
    }

    public void setmLast(boolean mLast) {
        this.mLast = mLast;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
