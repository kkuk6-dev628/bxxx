package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/13/2017.
 */
public class ChatItem {
    private long mChatID;
    private String mThumbnail;
    private String mName;
    private String mDesc;
    private String mTime;
    private String mUid;
    private String mPlid;

    public ChatItem(long cID, String thumbnail, String desc, String name, String time, String plid, String uid) {
        mChatID = cID;
        if (thumbnail.equals("")) {
            mThumbnail = "";
        } else {
            if(thumbnail.contains("http")){
                mThumbnail = thumbnail;
            }
            else if (thumbnail.substring(0, 1).equals(".")){
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail.substring(1));
            } else {
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail);
            }
        }
        mTime = time;
        mName = name;
        mDesc = desc;
        mUid = uid;
        mPlid = plid;
    }

    public long getmChatID() {
        return mChatID;
    }

    public void setmChatID(long mChatID) {
        this.mChatID = mChatID;
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

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmPlid() {
        return mPlid;
    }

    public void setmPlid(String mPlid) {
        this.mPlid = mPlid;
    }
}
