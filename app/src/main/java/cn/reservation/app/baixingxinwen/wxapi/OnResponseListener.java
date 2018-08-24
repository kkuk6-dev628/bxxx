package cn.reservation.app.baixingxinwen.wxapi;

public interface OnResponseListener {
    void onSuccess();

    void onCancel();

    void onFail(String message);
}
