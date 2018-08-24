package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/13/2017.
 */
public class HospitalItem {
    private long mHospitalID;
    private String mThumbnail;
    private String mTitle;
    private String mDesc;
    private String mRegion;

    public HospitalItem(long hID, String thumbnail, String title, String desc, String region) {
        mHospitalID = hID;
        if (thumbnail.equals("")) {
            mThumbnail = "";
        } else {
            if (thumbnail.substring(0, 1).equals(".")){
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail.substring(1));
            } else {
                mThumbnail = CommonUtils.getUrlEncoded(APIManager.Sever_URL + thumbnail);
            }
        }
        mTitle = title;
        mDesc = desc;
        mRegion = region;
    }

    public long getmHospitalID() {
        return mHospitalID;
    }

    public void setmHospitalID(long mHospitalID) {
        this.mHospitalID = mHospitalID;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmRegion() {
        return mRegion;
    }

    public void setmRegion(String mRegion) {
        this.mRegion = mRegion;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
