package cn.reservation.app.baixingxinwen.utils;

import java.io.Serializable;

/**
 * Created by LiYin on 3/17/2017.
 */
public class FamilyMemberItem implements Serializable {
    private String mMemberID;
    private String mMemberName;
    private int mMemberType;
    private int mMemberGender;
    private boolean ismSelected;
    private String mRelation;
    private String mIdentify;

    public FamilyMemberItem(String mMemberID, String mMemberName, int mMemeberType, int mMemberGender, String mRelation, String mIdentify, boolean selected) {
        this.mMemberID = mMemberID;
        this.mMemberName = mMemberName;
        this.mMemberType = mMemeberType;
        this.mMemberGender = mMemberGender;
        this.mRelation = mRelation;
        this.mIdentify = mIdentify;
        this.ismSelected = selected;
    }

    public int getmMemberGender() {
        return mMemberGender;
    }

    public void setmMemberGender(int mMemberGender) {
        this.mMemberGender = mMemberGender;
    }

    public int getmMemberType() {
        return mMemberType;
    }

    public void setmMemberType(int mMemberType) {
        this.mMemberType = mMemberType;
    }

    public boolean isSelected() {
        return this.ismSelected;
    }

    public void setSelected(boolean selected) {
        this.ismSelected = selected;
    }

    public String getmMemberID() {
        return mMemberID;
    }

    public void setmMemberID(String mMemberID) {
        this.mMemberID = mMemberID;
    }

    public String getmMemberName() {
        return mMemberName;
    }

    public void setmMemberName(String mMemberName) {
        this.mMemberName = mMemberName;
    }

    public String getmIdentify() {
        return mIdentify;
    }

    public void setmIdentify(String mIdentify) {
        this.mIdentify = mIdentify;
    }

    public String getmRelation() {
        return mRelation;
    }

    public void setmRelation(String mRelation) {
        this.mRelation = mRelation;
    }
}
