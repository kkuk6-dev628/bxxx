package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/13/2017.
 */
public class SearchItem {
    private long mSearchID;
    private String mThumbnail;
    private String mProperty01;
    private String mProperty02;
    private String mProperty03;
    private String mTitle01;
    private String mTitle02;
    private String mTitle03;
    private String mDesc;
    private String mPrice;
    private String mFid;
    private String mSortid;
    private String mPostState;
    private String mAdver;
    private String mAdverUrl;

    public SearchItem(long sID, String thumbnail, String desc, String price, String title01, String property01, String title02, String property02, String title03, String property03, String fid, String sortId, String postState, String adver, String adverUrl) {
        mSearchID = sID;
        if (thumbnail.equals("")) {
            mThumbnail = "";
        } else {
            if (thumbnail.substring(0, 1).equals(".")){
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail.substring(1));
            } else {
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail);
            }
        }
        mTitle01 = title01;
        mProperty01 = property01;
        mTitle02 = title02;
        mProperty02 = property02;
        mTitle03 = title03;
        mProperty03 = property03;
        mDesc = desc;
        mPrice = price;
        mFid = fid;
        mSortid = sortId;
        mPostState = postState;
        if (adver.equals("")) {
            mAdver = "";
        } else {
            if (adver.substring(0, 4).equals("http")) {
                mAdver = CommonUtils.getUrlEncoded(adver);
            }else if (adver.substring(0, 1).equals(".")){
                mAdver = CommonUtils.getUrlEncoded(APIManager.Sever_URL + adver.substring(1));
            } else {
                mAdver = CommonUtils.getUrlEncoded(APIManager.Sever_URL + adver);
            }
        }
        mAdverUrl = adverUrl;
    }

    public long getmSearchID() {
        return mSearchID;
    }

    public void setmSearchID(long mSearchID) {
        this.mSearchID = mSearchID;
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

    public String getmTitle01() {
        return mTitle01;
    }

    public void setmTitle01(String mTitle01) {
        this.mTitle01 = mTitle01;
    }

    public String getmTitle02() {
        return mTitle02;
    }

    public void setmTitle02(String mTitle02) {
        this.mTitle02 = mTitle02;
    }

    public String getmTitle03() {
        return mTitle03;
    }

    public void setmTitle03(String mTitle03) {
        this.mTitle03 = mTitle03;
    }

    public String getmProperty01() {
        return mProperty01;
    }

    public void setmProperty01(String mProperty01) {
        this.mProperty01 = mProperty01;
    }

    public String getmProperty02() {
        return mProperty02;
    }

    public void setmProperty02(String mProperty02) {
        this.mProperty02 = mProperty02;
    }

    public String getmProperty03() {
        return mProperty03;
    }

    public void setmProperty03(String mProperty03) {
        this.mProperty03 = mProperty03;
    }

    public String getmFid() {
        return mFid;
    }

    public void setmFid(String mFid) {
        this.mFid = mFid;
    }

    public String getmSortid() {
        return mSortid;
    }

    public void setmSortid(String mSortid) {
        this.mSortid = mSortid;
    }

    public String getmPostState() {
        return mPostState;
    }

    public void setmPostState(String mPostState) {
        this.mPostState = mPostState;
    }

    public String getmAdver() {
        return mAdver;
    }

    public void setmAdver(String adver) {
        this.mAdver = adver;
    }

    public String getmAdverUrl() {
        return mAdverUrl;
    }

    public void setmAdverUrl(String adverUrl) {
        this.mAdverUrl = adverUrl;
    }
}
