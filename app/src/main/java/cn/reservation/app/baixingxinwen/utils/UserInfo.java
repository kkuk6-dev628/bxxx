package cn.reservation.app.baixingxinwen.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LiYin on 3/23/2017.
 */
public class UserInfo {
    private long userID;
    private String userName;
    private int userGender;
    private String userBirthday;
    private String userPhone;
    private String userPhoto;
    private String token;
    private String userIdentify;
    private String userPassword;
    private String uid;
    private String userQQ;
    private String userWeixin;
    private String userJoinMobile;
    private String baixingbi;
    private String level;
    private String loginType;
    private String loginUsername;
    private String loginPassword;
    private String changeid;
    private String dateline;
    private String absence;

    public UserInfo(long userID, String userName, int userGender, String userBirthday,
                    String userPhone, String userPhoto, String token, String userIdentify,
                    String userPassword, String uid, String userQQ, String userWeixin,
                    String userJoinMobile, String baixingbi, String level, String loginType,
                    String loginUsername, String loginPassword, String changeid, String dateline) {
        this.userID = userID;
        this.userName = userName;
        this.userGender = userGender;
        this.userBirthday = userBirthday;
        this.userPhone = userPhone;
        this.userPhoto = userPhoto;
        this.token = token;
        this.userIdentify = userIdentify;
        this.userPassword = userPassword;
        this.uid = uid;
        this.userQQ = userQQ;
        this.userWeixin = userWeixin;
        this.userJoinMobile = userJoinMobile;
        this.baixingbi = baixingbi;
        this.level =level;
        this.loginType = loginType;
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
        this.changeid = changeid;
        this.dateline = dateline;
        this.absence = "0";
    }

    public void save(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("userData", MODE_PRIVATE).edit();
        editor.putLong("userID", getUserID());
        editor.putString("userName", getUserName());
        editor.putInt("userGender", getUserGender());
        editor.putString("userBirthday", getUserBirthday());
        editor.putString("userPhone", getUserPhone());
        editor.putString("userPhoto", getUserPhoto());
        editor.putString("credits", getToken());
        editor.putString("identify", getUserIdentify());
        editor.putString("realname", getUserPassword());
        editor.putString("uid", getUid());
        editor.putString("qq", getUserQQ());
        editor.putString("wechat", getUserWeixin());
        editor.putString("userJoinMobile", getUserJoinMobile());
        editor.putString("baixingbi", getBaixingbi());
        editor.putString("level", getLevel());
        editor.putString("login_type", getLoginType());
        editor.putString("login_username", getLoginUsername());
        editor.putString("login_password", getLoginPassword());
        editor.putString("changeid", getChangeid());
        editor.putString("dateline", getDateline());
        editor.putString("channel_id", changeid);
        editor.putString("absence", absence);
        editor.apply();

    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(String userIdentify) {
        this.userIdentify = userIdentify;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBaixingbi() { return baixingbi; }

    public void setBaixingbi(String baixingbi) { this.baixingbi = baixingbi; }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    public String getUserQQ() {
        return userQQ;
    }

    public void setUserQQ(String userQQ) {
        this.userQQ = userQQ;
    }

    public String getUserWeixin() {
        return userWeixin;
    }

    public void setUserWeixin(String userWeixin) {
        this.userWeixin = userWeixin;
    }

    public String getUserJoinMobile() {
        return userJoinMobile;
    }

    public void setUserJoinMobile(String userJoinMobile) {
        this.userJoinMobile = userJoinMobile;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getChangeid() {
        return changeid;
    }

    public void setChangeid(String changeid) {
        this.changeid = changeid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
