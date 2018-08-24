package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by NSC on 6/5/2018.
 */
public class HistoryItem {
    private long mHistoryID;
    private String mThumbnail;
    private String mDate;
    private String mDesc;
    private String mPrice;
    private boolean mChk;
    private boolean mChkPanel;
    private String mFid;
    private String mSortId;
    private String mPostState;

    public HistoryItem(long hID, String thumbnail, String desc, String price, String date, boolean chkp, String fid, String sortId, String postState) {
        mHistoryID = hID;
        if (thumbnail.equals("")) {
            mThumbnail = "";
        } else {
            if(thumbnail.contains("http:")){
                mThumbnail = thumbnail;
            }
            else if (thumbnail.substring(0, 1).equals(".")){
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail.substring(1));
            } else {
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail);
            }
        }
        mDate = date;
        mDesc = desc;
        mPrice = price;
        mChk = false;
        mChkPanel = chkp;
        mFid = fid;
        mSortId = sortId;
        mPostState =postState;
    }

    public long getmHistoryID() {
        return mHistoryID;
    }

    public void setmHistoryID(long mHistoryID) {
        this.mHistoryID = mHistoryID;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmChk(boolean mChk){ this.mChk = mChk; }

    public boolean getmChk(){ return mChk; }

    public void setmChkPanel(boolean mChkPanel){ this.mChkPanel = mChkPanel; }

    public boolean getmChkPanel(){ return mChkPanel; }

    public String getmFid() {
        return mFid;
    }

    public void setmFid(String mFid) {
        this.mFid = mFid;
    }

    public String getmSortId() {
        return mSortId;
    }

    public void setmSortId(String mSortId) {
        this.mSortId = mSortId;
    }

    public String getmPostState() {
        return mPostState;
    }

    public void setmPostState(String mPostState) {
        this.mPostState = mPostState;
    }
}
