package cn.reservation.app.baixingxinwen.utils;

import java.io.Serializable;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/20/2017.
 */
public class ChargeDetailItem implements Serializable {
    private long mChargeID;
    private String mDate;
    private String mContent;
    private String mValue;

    public ChargeDetailItem(long mChargeID, String mContent, String mDate, String mValue) {
        this.mChargeID = mChargeID;
        this.mContent = mContent;
        this.mDate = mDate;
        this.mValue = mValue;
    }
    public long getmChargeID() {
        return mChargeID;
    }

    public void setmChargeID(long mChargeID) {
        this.mChargeID = mChargeID;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }
}
