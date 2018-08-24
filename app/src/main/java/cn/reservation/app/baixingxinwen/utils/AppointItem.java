package cn.reservation.app.baixingxinwen.utils;

import java.io.Serializable;

/**
 * Created by LiYin on 3/19/2017.
 */
public class AppointItem implements Serializable{
    private long mID;
    private String mDate;
    private String mHour;
    private String mMinute;
    private DoctorItem mDoctor;
    private String mPatient;
    private String mPatientID;
    private boolean isCanceled;

    public AppointItem(long id, String date, String hour, String min, DoctorItem doctor, String patientId, String patient, Boolean isCanceled) {
        this.mID = id;
        this.mDate = date;
        this.mHour = hour;
        this.mMinute = min;
        this.mDoctor = doctor;
        this.mPatient = patient;
        this.mPatientID = patientId;
        this.isCanceled = isCanceled;
    }

    public long getmID() {
        return mID;
    }

    public void setmID(long mID) {
        this.mID = mID;
    }

    public DoctorItem getmDoctor() {
        return mDoctor;
    }

    public void setmDoctor(DoctorItem mDoctor) {
        this.mDoctor = mDoctor;
    }

    public String getmPatient() {
        return mPatient;
    }

    public void setmPatient(String mPatient) {
        this.mPatient = mPatient;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmHour() {
        return mHour;
    }

    public void setmHour(String mHour) {
        this.mHour = mHour;
    }

    public String getmMinute() {
        return mMinute;
    }

    public void setmMinute(String mMinute) {
        this.mMinute = mMinute;
    }

    public String getmPatientID() {
        return mPatientID;
    }

    public void setmPatientID(String mPatientID) {
        this.mPatientID = mPatientID;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }
}
