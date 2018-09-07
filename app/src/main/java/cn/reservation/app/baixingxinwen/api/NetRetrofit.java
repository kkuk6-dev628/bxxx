package cn.reservation.app.baixingxinwen.api;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by NewLand on 2017. 11. 7..
 */

public class NetRetrofit {
    private static NetRetrofit ourInstance = new NetRetrofit();

    public static NetRetrofit getInstance() {
        return ourInstance;
    }

    private NetRetrofit() {
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://app.bxxx.cn/index.php/")
            .addConverterFactory(JSONConverterFactory.create()) // 파싱등록
            .build();

    RetrofitService service = retrofit.create(RetrofitService.class);

    public RetrofitService getService() {
        return service;
    }

    public void post(String url, HashMap<String, Object> params, final Callback<JSONObject> callback){
        Call<JSONObject> res = NetRetrofit.getInstance().getService().post(url, params);
        res.enqueue(new RetryableCallback<JSONObject>(res, 3) {
            @Override
            public void onFinalResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFinalFailure(Call<JSONObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });

    }
}

