package cn.reservation.app.baixingxinwen.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by NewLand on 2017. 11. 7..
 */

public interface RetrofitService {
    @GET("users/{user}/repos")
    Call<ArrayList<JsonObject>> getListRepos(@Path("user") String id);

    @FormUrlEncoded
    @POST
    Call<JSONObject> post(@Url String url, @FieldMap HashMap<String, Object> parameters);
}
