package com.azadljy.ljynetwork.retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 作者：Ljy on 2017/12/14.
 * 功能：我的——我的资料
 */


public interface RetrofitService {

    @GET()
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String, String> maps);

    @POST()
    @FormUrlEncoded
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> map);
}
