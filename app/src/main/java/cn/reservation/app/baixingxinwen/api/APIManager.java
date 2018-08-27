package cn.reservation.app.baixingxinwen.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.tauth.Tencent;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public class APIManager {
/*
    private static final String URL = "http://192.168.1.111:2020/index.php/api/";
    public static final String Sever_URL = "http://bbs.bxxx.cn/";
    public static final String User_URL ="http://192.168.1.111:2020/index.php/";
    public static final String Ucenter_URL= "http://192.168.1.111:1212/api/passport.php";
*/
    private static final String URL = "http://app.bxxx.cn/index.php/api/";
    private static final String URL_LOCAL = "http://192.168.1.32:2020/index.php/api/";
    public static final String Sever_URL = "http://bbs.bxxx.cn/";
    public static final String User_URL ="http://app.bxxx.cn/index.php/";
    public static final String Ucenter_URL= "http://bbs.bxxx.cn/api/passport.php";


/*    private static final String URL = "http://192.168.1.108/appbackend/index.php/api/";
    public static final String Sever_URL = "http://bbs.bxxx.cn/";
    public static final String User_URL ="http://192.168.1.108/appbackend/index.php/";
    public static final String Ucenter_URL= "http://192.168.1.108/bbs/api/passport.php";
*/
    public static final String IMAGE_URL = "http://bbs.bxxx.cn/";
    public static final String APP_DOMAIN = "http://app.bxxx.cn";

    //----------------QQ Login-----------------------
    public static Tencent mTencent = null;
    public static final String QQ_APP_ID = "1106887707";
    //----------------Wechat Login---------------------
    public static final String WC_APP_ID = "wxda12dcea475e19d9";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Header[] headers = {
                new BasicHeader("Accept", "application/json"),
        };
        client.get(context, getUrl(url), headers, params, responseHandler);
    }

    public static void post(Context context, String url, RequestParams params, String contentType, AsyncHttpResponseHandler responseHandler) {
        Header[] headers = {
                new BasicHeader("Accept", "application/json")
        };

        client.post(context, getUrl(url), headers, params, contentType, responseHandler);
    }

    private static String getUrl(String relativeUrl) {
        if(relativeUrl.contains("http://")){
            return relativeUrl;
        }else {
//            return URL + relativeUrl;
            // for test
            return URL + relativeUrl;
        }
    }

}
