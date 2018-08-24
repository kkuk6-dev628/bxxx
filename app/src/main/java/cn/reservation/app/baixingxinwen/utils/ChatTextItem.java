package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/13/2017.
 */
public class ChatTextItem {
    private long mChatID;
    private String mThumbnail;
    private String emThumbnail;
    private String mName;
    private String emName;
    private String mDesc;
    private String emDesc;
    private String mTime;
    private String mUid;
    private String mPlid;

    public ChatTextItem(long cID, String thumbnail, String desc, String name, String methumbnail, String medesc, String mename, String time, String plid, String uid) {
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
        if (methumbnail.equals("")) {
            emThumbnail = "";
        } else {
            if(methumbnail.contains("http")){
                emThumbnail = methumbnail;
            }
            else if (methumbnail.substring(0, 1).equals(".")){
                emThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + methumbnail.substring(1));
            } else {
                emThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + methumbnail);
            }
        }
        mTime = time;
        mName = name;
        emName = mename;
        mDesc = desc;
        emDesc = medesc;
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

    public String getmeDesc() {
        return emDesc;
    }

    public void setmeDesc(String mDesc) {
        this.emDesc = mDesc;
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

    public String getmeThumbnail() {
        return emThumbnail;
    }

    public void setmeThumbnail(String thumbnail) {
        this.emThumbnail = thumbnail;
    }

    public String getmeName() {
        return emName;
    }

    public void setmeName(String mName) {
        this.emName = mName;
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
