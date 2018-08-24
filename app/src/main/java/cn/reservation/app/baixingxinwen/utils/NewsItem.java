package cn.reservation.app.baixingxinwen.utils;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by NSC on 6/3/2018.
 */
public class NewsItem {
    private long mNewsID;
    private String mTitle;
    private String mStatus;
    private String mImage;
    private String mStartTime;
    private String mTopTime;
    private String mEndTime;
    private String mViewCnt;
    private String mFid;
    private String mSortid;
    private String mPostState;
    private String mPaid;

    public NewsItem(long newsId, String title, String status, String image_path, String startTime, String topTime, String viewCnt, String postState, String fid, String sortid, String paid, String endTime) {
        if (image_path.equals("")) {
            this.mImage = "";
        } else {
            if(image_path.contains("http:")){
                mImage = image_path;
            }
            else if (image_path.substring(0, 1).equals(".")){
                this.mImage = CommonUtils.getUrlEncoded(APIManager.Sever_URL + image_path.substring(1));
            } else {
                this.mImage = CommonUtils.getUrlEncoded(APIManager.Sever_URL + image_path);
            }
        }
        this.mNewsID = newsId;
        this.mTitle = title;
        this.mStatus = status;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mTopTime = topTime;
        if(endTime.equals("null")) this.mEndTime = "";
        if(topTime.equals("null")) this.mTopTime = "";
        this.mViewCnt = viewCnt;
        this.mFid = fid;
        this.mSortid = sortid;
        this.mPostState = postState;
        this.mPaid = paid;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setmNewsID(long mNewsID) {
        this.mNewsID = mNewsID;
    }

    public Long getmNewsID() {
        return mNewsID;
    }

    public void setmNewsID(Long mNewsID) {
        this.mNewsID = mNewsID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmStartTime() { return mStartTime; }

    public void setmStartTime(String mStartTime) { this.mStartTime = mStartTime; }

    public String getmEndTime() { return mEndTime; }

    public void setmEndTime(String mEndTime){ this.mEndTime = mEndTime; }

    public String getmTopTime() { return mTopTime; }

    public void setmTopTime(String mTopTime){ this.mTopTime = mTopTime; }

    public String getmViewCnt() { return mViewCnt; }

    public void setmViewCnt(String mViewCnt){ this.mViewCnt = mViewCnt; }

    public String getmPostState() { return mPostState; }

    public void setmPostState(String mPostState){ this.mPostState = mPostState; }

    public String getmFid() { return mFid; }

    public void setmFid(String mFid){ this.mFid = mFid; }

    public String getmSortid() { return mSortid; }

    public void setmSortid(String mSortid){ this.mSortid = mSortid; }

    public String getmPaid() { return mPaid; }

    public void setmPaid(String mPaid){ this.mPaid = mPaid; }

}
