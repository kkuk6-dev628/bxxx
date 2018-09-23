package cn.reservation.app.baixingxinwen.api;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        final Call<JSONObject> res = NetRetrofit.getInstance().getService().post(url, params);
        res.enqueue(new RetryableCallback<JSONObject>(res, 3) {
            @Override
            public void onFinalResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    callback.onResponse(call, response);
                }
                else{
                    callback.onFailure(call, new Throwable(response.raw().toString()));
                }
            }

            @Override
            public void onFinalFailure(Call<JSONObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });

    }

    public void post(String url, HashMap<String, Object> params, final Callback<JSONObject> callback, int retryCount){
        final Call<JSONObject> res = NetRetrofit.getInstance().getService().post(url, params);
        res.enqueue(new RetryableCallback<JSONObject>(res, retryCount) {
            @Override
            public void onFinalResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    callback.onResponse(call, response);
                }
                else{
                    callback.onFailure(call, new Throwable(response.raw().toString()));
                }
            }

            @Override
            public void onFinalFailure(Call<JSONObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });

    }

    public void upload(String url, HashMap<String, Object> params, File[] files, final Callback<JSONObject> callback){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            builder.addFormDataPart(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        MultipartBody requestBody = builder.build();
        RetrofitService service = NetRetrofit.getInstance().getService();
        Call<JSONObject> call = service.uploadMultiFile(url, requestBody);
        call.enqueue(new RetryableCallback<JSONObject>(call, 3) {
            @Override
            public void onFinalResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    callback.onResponse(call, response);
                }
                else{
                    callback.onFailure(call, new Throwable(response.raw().toString()));
                }
            }

            @Override
            public void onFinalFailure(Call<JSONObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void upload(String url, HashMap<String, Object> params, ArrayList<File> files, final Callback<JSONObject> callback){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            builder.addFormDataPart(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        for (File file : files) {
            builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        MultipartBody requestBody = builder.build();
        RetrofitService service = NetRetrofit.getInstance().getService();
        Call<JSONObject> call = service.uploadMultiFile(url, requestBody);
        call.enqueue(new RetryableCallback<JSONObject>(call, 3) {
            @Override
            public void onFinalResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    callback.onResponse(call, response);
                }
                else{
                    callback.onFailure(call, new Throwable(response.raw().toString()));
                }
            }

            @Override
            public void onFinalFailure(Call<JSONObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}

