package cn.reservation.app.baixingxinwen.api;

/**
 * Created by kkuk6 on 7/7/2017.
 */

public interface JsonResponseListener<T> {
    public void getResult(T object);
    public void errorHandler(String errorMessage);
}
