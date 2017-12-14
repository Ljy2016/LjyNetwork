package com.azadljy.ljynetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.azadljy.ljynetwork.modle.NetModel;
import com.azadljy.ljynetwork.retrofit.RetrofitRequest;
import com.azadljy.ljynetwork.utils.CommonUtils;
import com.azadljy.ljynetwork.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "http://121.201.67.222:16990/";
    RetrofitRequest retrofitRequest;
    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        retrofitRequest = new RetrofitRequest();

    }

    public void go(View view) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "13530390365");
        hashMap.put("随机码", CommonUtils.Md5("shop_new_loginA"));
        hashMap.put("密码", CommonUtils.Md5("123456"));
        Map<String, Object> params = new HashMap<>();
        params.put("func", "shop_new_loginA");
        params.put("words", CommonUtils.Md5("shop_new_loginA") + CommonUtils.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), CommonUtils.Md5("shop_new_loginA")));
        NetModel model = new NetModel(params);

        retrofitRequest.sendPostRequest(model);
    }

    public static Observer<NetModel> observer = new Observer<NetModel>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull NetModel s) {
            ResponseResult result = s.getResult();

            textView.setText((String) result.getContent());
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };


}
