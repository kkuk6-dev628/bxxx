package cn.reservation.app.baixingxinwen.utils;

import java.io.Serializable;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/20/2017.
 */
public class HealthArticleItem implements Serializable {
    private long mArticleID;
    private String mImage;
    private String mTitle;
    private String mContent;

    public HealthArticleItem(long mArticleID, String mImage, String mTitle, String mContent) {
        this.mArticleID = mArticleID;
        this.mContent = mContent;
        if (mImage.equals("")) {
            this.mImage = APIManager.Sever_URL;
        } else {
            if (mImage.substring(0, 1).equals(".")) {
                this.mImage = APIManager.Sever_URL + mImage.substring(1);
            } else {
                this.mImage = APIManager.Sever_URL + mImage;
            }
        }
        this.mTitle = mTitle;
    }
    public long getmArticleID() {
        return mArticleID;
    }

    public void setmArticleID(long mArticleID) {
        this.mArticleID = mArticleID;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
