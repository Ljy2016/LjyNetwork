package com.azadljy.ljynetwork.retrofit;

import android.util.Log;

import com.azadljy.ljynetwork.IRequest;
import com.azadljy.ljynetwork.MainActivity;
import com.azadljy.ljynetwork.ResponseResult;
import com.azadljy.ljynetwork.modle.NetModel;
import com.azadljy.ljynetwork.retrofit.RetrofitService;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

/**
 * 作者：Ljy on 2017/12/14.
 * 功能：我的——我的资料
 */


public class RetrofitRequest implements IRequest {

    Retrofit retrofit;
    RetrofitService service;
    Call<ResponseBody> call;

    public RetrofitRequest() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://121.201.67.222:16990/") // 设置网络请求的Url地址
                .build();
        service = retrofit.create(RetrofitService.class);

    }


    @Override
    public void sendGetRequest(NetModel action) {

    }

    @Override
    public void sendPostRequest(final NetModel action) {
        call = service.post(action.getUrl(), action.getParameters());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                try {
                    Observable<NetModel> observable = Observable.create(new ObservableOnSubscribe<NetModel>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<NetModel> e) throws Exception {
                            ResponseResult result = new ResponseResult();
                            result.setContent(response.body().string());
                            action.setResult(result);
                            e.onNext(action);
                            e.onComplete();
                        }
                    });

                    observable.subscribe(MainActivity.observer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "onResponse2: " + t.getMessage());
            }
        });
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
