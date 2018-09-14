package cn.reservation.app.baixingxinwen.utils;

import org.json.JSONObject;

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

    private String mPostTime = "";
    private String mTypeVal = "";
    private String mPhoneNumber = "";
    private String mDefaultImageName = "";
    private boolean mHasImgProperty1 = false;
    private boolean mHasImgProperty2 = false;
    private boolean mHasImgProperty3 = false;
    private boolean mIsNoImage = false;

    private boolean isMarriedPage = false;



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

    public SearchItem(long sID, String thumbnail, String desc, DictionaryUtils dictionaryUtils, JSONObject item) {
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
//        mTitle01 = title01;
        mProperty01 = dictionaryUtils.getProperty("txt_property1").trim();
//        mTitle02 = title02;
        mProperty02 = dictionaryUtils.getProperty("txt_property2").trim();
//        mTitle03 = title03;
        mProperty03 = dictionaryUtils.getProperty("txt_property3").trim();
        mDesc = desc;
        mPrice = dictionaryUtils.getProperty("txt_home_favor_price");
        mFid = dictionaryUtils.getProperty("fid");
        mSortid = dictionaryUtils.getProperty("sortid");

        if(mSortid.equals("15") || mSortid.equals("16"))
            isMarriedPage = true;

        mPostState = item.optString("poststick");
        mDefaultImageName = dictionaryUtils.getProperty("default_image_name");

        mPostTime = item.optString("dateline");
        mTypeVal = dictionaryUtils.getProperty("txt_type");
        mPhoneNumber = dictionaryUtils.getProperty("phone_number");
        mDefaultImageName = dictionaryUtils.getProperty("default_image_name");
        mHasImgProperty1 = dictionaryUtils.getProperty("has_img_property1").equals("1");
        mHasImgProperty2 = dictionaryUtils.getProperty("has_img_property2").equals("1");
        mHasImgProperty3 = dictionaryUtils.getProperty("has_img_property3").equals("1");
        mIsNoImage = dictionaryUtils.getProperty("is_no_image").equals("1");


        String adverUrl =  item.optString("link");
        String adver = item.optString("advert");
        if (adverUrl != null && !adverUrl.equals("") && !adverUrl.substring(0, 4).equals("http")) {
            adverUrl = "http://"+adverUrl;
        }


        if (adver == null || adver.equals("")) {
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

    public String getGeneralDescription() {
        String result = "";
        if(mProperty01 == null) {
            mProperty01 = "";
        }
        if(mProperty02 == null)
            mProperty02 = "";
        if(mProperty03 == null)
            mProperty03 = "";


        return mProperty01 + "\n" + mProperty02 + "\n" + mProperty03;
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

    public String getmPostTime() {
        return mPostTime;
    }

    public void setmPostTime(String mPostTime) {
        this.mPostTime = mPostTime;
    }

    public String getmTypeVal() {
        return mTypeVal;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmDefaultImageName() {
        return mDefaultImageName;
    }

    public void setmDefaultImageName(String mDefaultImageName) {
        this.mDefaultImageName = mDefaultImageName;
    }

    public boolean ismHasImgProperty1() {
        return mHasImgProperty1;
    }

    public void setmHasImgProperty1(boolean mHasImgProperty1) {
        this.mHasImgProperty1 = mHasImgProperty1;
    }

    public boolean ismHasImgProperty2() {
        return mHasImgProperty2;
    }

    public void setmHasImgProperty2(boolean mHasImgProperty2) {
        this.mHasImgProperty2 = mHasImgProperty2;
    }

    public boolean ismHasImgProperty3() {
        return mHasImgProperty3;
    }

    public void setmHasImgProperty3(boolean mHasImgProperty3) {
        this.mHasImgProperty3 = mHasImgProperty3;
    }

    public boolean ismIsNoImage() {
        return mIsNoImage;
    }

    public void setmIsNoImage(boolean mIsNoImage) {
        this.mIsNoImage = mIsNoImage;
    }

    public boolean isMarriedPage() {
        return isMarriedPage;
    }

    public void setMarriedPage(boolean marriedPage) {
        isMarriedPage = marriedPage;
    }
}
