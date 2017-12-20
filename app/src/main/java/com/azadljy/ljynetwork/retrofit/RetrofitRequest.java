package com.azadljy.ljynetwork.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.azadljy.ljynetwork.IRequest;
import com.azadljy.ljynetwork.MainActivity;
import com.azadljy.ljynetwork.ResponseResult;
import com.azadljy.ljynetwork.modle.NetModel;
import com.azadljy.ljynetwork.retrofit.RetrofitService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Url;


/**
 * 作者：Ljy on 2017/12/14.
 * 功能：我的——我的资料
 */


public class RetrofitRequest implements IRequest {

    Retrofit retrofit;
    RetrofitService service;
    Call<ResponseBody> call;

    private volatile static RetrofitRequest instance;


    private RetrofitRequest() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://121.201.67.222:16990/")// 设置网络请求的Url地址
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(RetrofitService.class);
    }

    public static RetrofitRequest getInstance() {
        if (instance == null) {
            synchronized (RetrofitRequest.class) {
                if (instance == null) {
                    instance = new RetrofitRequest();
                }
            }
        }
        return instance;
    }


    @Override
    public void sendGetRequest(NetModel action) {

    }

    @Override
    public void sendPostRequest(final NetModel action) {
        service.post1(action.getUrl(), action.getParameters())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Response<ResponseBody>, NetModel>() {
                    @Override
                    public NetModel apply(@NonNull Response<ResponseBody> responseBody) throws Exception {
                        try {
                            ResponseResult result = new ResponseResult();
                            result.setContent(responseBody.body().string());
                            action.setResult(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return action;
                    }
                }).subscribe(action.getObserver());

    }


    @Override
    public void cancelAllRequests(boolean mayInterruptIfRunning) {

    }

    @Override
    public void setTimeOut(int value) {

    }

    @Override
    public void downloadFile(String url) {

    }
}
