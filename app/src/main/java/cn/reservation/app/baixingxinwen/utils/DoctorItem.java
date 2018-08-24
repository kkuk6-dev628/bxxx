package cn.reservation.app.baixingxinwen.utils;

import java.io.Serializable;

import cn.reservation.app.baixingxinwen.api.APIManager;

/**
 * Created by LiYin on 3/15/2017.
 */
public class DoctorItem implements Serializable{
    private long mDoctorID;
    private long mHospitalID;
    private long mRoomID;
    private String mPhoto;
    private String mName;
    private String mHospital;
    private String mRoom;
    private String mDegree;
    private String mFee;
    private String mPhone;
    private int mFloor;
    private boolean mIsDoctor;

    public DoctorItem(long doctorId, long hospitalId, long roomID, String photo, String name, String hospital,
                      String room, String degree, String fee, String phone, int floor, boolean isDoctor) {
        this.mDoctorID = doctorId;
        this.mHospitalID = hospitalId;
        this.mRoomID = roomID;
        //this.mPhoto = APIManager.Sever_URL + "/DocImg/" + photo;
        this.mPhoto = CommonUtils.getUrlEncoded(APIManager.Sever_URL + "/" + hospitalId + "/yuyue/img/docImg/" + photo);
        this.mName = name;
        this.mHospital = hospital;
        this.mRoom = room;
        this.mDegree = degree;
        this.mFee = fee;
        this.mPhone = phone;
        this.mFloor = floor;
        this.mIsDoctor = isDoctor;
    }

    public String getmDegree() {
        return mDegree;
    }

    public void setmDegree(String mDegree) {
        this.mDegree = mDegree;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public long getmDoctorID() {
        return mDoctorID;
    }

    public void setmDoctorID(long mDoctorID) {
        this.mDoctorID = mDoctorID;
    }

    public String getmFee() {
        return mFee;
    }

    public void setmFee(String mFee) {
        this.mFee = mFee;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRoom() {
        return mRoom;
    }

    public void setmRoom(String mRoom) {
        this.mRoom = mRoom;
    }

    public int getmFloor() {
        return mFloor;
    }

    public void setmFloor(int mFloor) {
        this.mFloor = mFloor;
    }

    public boolean isDoctor() {
        return this.mIsDoctor;
    }

    public void setDoctor(boolean doctor) {
        this.mIsDoctor = doctor;
    }

    public long getmRoomID() {
        return mRoomID;
    }

    public void setmRoomID(long mRoomID) {
        this.mRoomID = mRoomID;
    }

    public String getmHospital() {
        return mHospital;
    }

    public void setmHospital(String mHospital) {
        this.mHospital = mHospital;
    }

    public long getmHospitalID() {
        return mHospitalID;
    }

    public void setmHospitalID(long mHospitalID) {
        this.mHospitalID = mHospitalID;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
