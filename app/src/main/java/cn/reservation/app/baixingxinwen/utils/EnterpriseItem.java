package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by NSC on 6/5/2018.
 */
public class EnterpriseItem {
    private long mEnterpriseID;
    private String mThumbnail;
    private String mName;
    private String mDesc;
    private String mPhone;

    public EnterpriseItem(long eID, String thumbnail, String name, String desc, String phone) {
        mEnterpriseID = eID;
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
        mName = name;
        mDesc = desc;
        mPhone = phone;
    }

    public long getmEnterpriseID() {
        return mEnterpriseID;
    }

    public void setmEnterpriseID(long mEID) {
        this.mEnterpriseID = mEID;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

}
