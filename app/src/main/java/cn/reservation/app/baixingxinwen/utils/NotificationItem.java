package cn.reservation.app.baixingxinwen.utils;

/**
 * Created by LiYin on 3/13/2017.
 */
public class NotificationItem {
    private String mTID;
    private String mTitle;
    private String mDesc;
    private String mTime;
    private String mUid;
    private String mType;

    public NotificationItem(String type, String tid, String uid, String title, String time, String desc) {

        mTime = time;
        mTitle = title;
        mDesc = desc;
        mUid = uid;
        mTID = tid;
        mType = type;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTID() {
        return mTID;
    }

    public void setmTID(String mTID) {
        this.mTID = mTID;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
