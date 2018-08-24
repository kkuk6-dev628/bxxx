package cn.reservation.app.baixingxinwen.utils;

/**
 * Created by LiYin on 3/16/2017.
 */
public class TimesItem {
    private String mTime;
    private boolean mActive;

    public TimesItem(String time, boolean active) {
        this.mTime = time;
        this.mActive = active;
    }

    public boolean ismActive() {
        return mActive;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
