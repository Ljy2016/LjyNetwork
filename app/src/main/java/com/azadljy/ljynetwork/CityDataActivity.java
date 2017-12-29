package com.azadljy.ljynetwork;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azadljy.ljynetwork.bean.JsonBean;
import com.azadljy.ljynetwork.city.CityPicker;
import com.azadljy.ljynetwork.city.Cityinfo;
import com.azadljy.ljynetwork.city.ScrollerNumberPicker;
import com.azadljy.ljynetwork.modle.NetModel;
import com.azadljy.ljynetwork.retrofit.RetrofitRequest;
import com.azadljy.ljynetwork.utils.CommonUtils;
import com.azadljy.ljynetwork.utils.ConstantUtil;
import com.azadljy.ljynetwork.utils.JsonUtils;
import com.bigkoo.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class CityDataActivity extends AppCompatActivity {
    RetrofitRequest retrofitRequest;
    public static List<Cityinfo> province_list = new ArrayList<>();
    public static HashMap<String, List<Cityinfo>> city_map = new HashMap<>();
    public static HashMap<String, List<Cityinfo>> couny_map = new HashMap<>();


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    private String url = "http://121.201.67.222:16990/api.post/";
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_data);
        textView2 = (TextView) findViewById(R.id.textView2);
        retrofitRequest = RetrofitRequest.getInstance();
    }

    public void gogo(View view) {
//        retrofitRequest.sendPostRequest(getModel("0", "0", "0", "privance"));
        retrofitRequest.sendPostRequest(getModelOne("一级", "", "", "one"));
    }

    public void show(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CityDataActivity.this);
        View view = LayoutInflater.from(CityDataActivity.this).inflate(R.layout.addressdialog, null);
        builder.setView(view);
        LinearLayout addressdialog_linearlayout = view.findViewById(R.id.addressdialog_linearlayout);
        CityPicker cityPicker1 = view.findViewById(R.id.citypicker);
//        cityPicker1.setInfo(province_list, city_map, couny_map);
        final ScrollerNumberPicker provincePicker = view.findViewById(R.id.province);
        final ScrollerNumberPicker cityPicker = view.findViewById(R.id.city);
        final ScrollerNumberPicker counyPicker = view.findViewById(R.id.couny);
        final AlertDialog dialog = builder.show();
        addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView2.setText(provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText());
                Log.i("kkkk", provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText());
                dialog.dismiss();

            }
        });

//        Log.e("TAG", "show: "+province_list.toString());
//        Log.e("TAG", "show: "+city_map.toString());
//        Log.e("TAG", "show: "+couny_map.toString());


    }

    int count = 0;
    private Observer<NetModel> observer = new Observer<NetModel>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull NetModel s) {
            ResponseResult result = s.getResult();
            Log.e("TAG", "onNext: " + result.getContent());
            Log.e("TAG", "onNext: " + s.getTag());
            try {
                JSONObject object = new JSONObject((String) result.getContent());
                switch (s.getTag()) {
                    case "privance":
                        JSONArray array = object.getJSONArray("省列表");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject privance = array.getJSONObject(i);
                            Cityinfo privanceInfo = new Cityinfo();
                            privanceInfo.setCity_name(privance.getString("省份"));
                            privanceInfo.setId(privance.getString("id"));
                            province_list.add(privanceInfo);
                            NetModel model = getModel("", privanceInfo.getId(), "0", "city");
                            model.setObject(privanceInfo);
                            retrofitRequest.sendPostRequest(model);
                        }
                        break;
                    case "city":
                        JSONArray cityArray = object.getJSONArray("市列表");
                        List<Cityinfo> cityinfoList = new ArrayList<>();
                        Cityinfo privanceInfo = (Cityinfo) s.getObject();
                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject city = cityArray.getJSONObject(i);
                            Cityinfo cityInfo = new Cityinfo();
                            cityInfo.setCity_name(city.getString("城市"));
                            cityInfo.setId(city.getString("id"));
                            cityinfoList.add(cityInfo);
                            NetModel model = getModel("", privanceInfo.getId(), cityInfo.getId(), "county");
                            model.setObject(cityInfo);
                            retrofitRequest.sendPostRequest(model);
                        }
                        city_map.put(privanceInfo.getId(), cityinfoList);
                        break;
                    case "county":
                        JSONArray countyArray = object.getJSONArray("区列表");
                        List<Cityinfo> countyList = new ArrayList<>();
                        Cityinfo cityInfo = (Cityinfo) s.getObject();
                        for (int i = 0; i < countyArray.length(); i++) {
                            JSONObject county = countyArray.getJSONObject(i);
                            Cityinfo countyInfo = new Cityinfo();
                            countyInfo.setCity_name(county.getString("区"));
                            countyInfo.setId(county.getString("id"));
                            countyList.add(countyInfo);
                        }
                        couny_map.put(cityInfo.getId(), countyList);
                        count++;
                        Log.e("TAG", "onNext: " + count);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("", "onNext: " + e.getMessage());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
    private Observer<NetModel> observerOne = new Observer<NetModel>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull NetModel s) {
            ResponseResult result = s.getResult();
            Log.e("TAG", "onNext: " + result.getContent());
            Log.e("TAG", "onNext: " + s.getTag());
            try {
                JSONObject object = new JSONObject((String) result.getContent());
                switch (s.getTag()) {
                    case "one":
                        JSONArray array = object.getJSONArray("一级列表");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject privance = array.getJSONObject(i);
                            Cityinfo privanceInfo = new Cityinfo();
                            privanceInfo.setCity_name(privance.getString("一级"));
                            privanceInfo.setId(privance.getString("一级id"));
                            province_list.add(privanceInfo);
                            NetModel model = getModelOne("二级", privanceInfo.getId(), "", "two");
                            model.setObject(privanceInfo);
                            retrofitRequest.sendPostRequest(model);
                        }
                        break;
                    case "two":
                        JSONArray cityArray = object.getJSONArray("二级列表");
                        List<Cityinfo> cityinfoList = new ArrayList<>();
                        Cityinfo privanceInfo = (Cityinfo) s.getObject();
                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject city = cityArray.getJSONObject(i);
                            Cityinfo cityInfo = new Cityinfo();
                            cityInfo.setCity_name(city.getString("二级"));
                            cityInfo.setId(city.getString("二级id"));
                            cityinfoList.add(cityInfo);
                            NetModel model = getModelOne("三级", "", cityInfo.getId(), "three");
                            model.setObject(cityInfo);
                            retrofitRequest.sendPostRequest(model);
                        }
                        city_map.put(privanceInfo.getId(), cityinfoList);
                        break;
                    case "three":
                        JSONArray countyArray = object.getJSONArray("三级列表");
                        List<Cityinfo> countyList = new ArrayList<>();
                        Cityinfo cityInfo = (Cityinfo) s.getObject();
                        for (int i = 0; i < countyArray.length(); i++) {
                            JSONObject county = countyArray.getJSONObject(i);
                            Cityinfo countyInfo = new Cityinfo();
                            countyInfo.setCity_name(county.getString("三级"));
                            countyInfo.setId(county.getString("三级id"));
                            countyList.add(countyInfo);
                        }
                        couny_map.put(cityInfo.getId(), countyList);
                        count++;
                        Log.e("TAG", "onNext: " + count);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("", "onNext: " + e.getMessage());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    private NetModel getModel(String type, String provinceId, String cityId, String tag) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "13530390365");
        hashMap.put("随机码", ConstantUtil.SP_RANDOMCODE);
//        hashMap.put("type", type);
        hashMap.put("省id", provinceId);
        hashMap.put("市id", cityId);

        log(hashMap);

        Map<String, Object> params = new HashMap<>();
        params.put("func", "shop_wl_sheng");
        params.put("words", ConstantUtil.SP_RANDOMCODE + CommonUtils.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), ConstantUtil.SP_RANDOMCODE));
        NetModel model = new NetModel(params, url);
        model.setObserver(observer);
        model.setTag(tag);
        return model;
    }

    private NetModel getModelOne(String type, String firseId, String secondId, String tag) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", "13530390365");
        hashMap.put("随机码", ConstantUtil.SP_RANDOMCODE);
        hashMap.put("类别", type);
        hashMap.put("一级id", firseId);
        hashMap.put("二级id", secondId);

        log(hashMap);

        Map<String, Object> params = new HashMap<>();
        params.put("func", "shop_wl_uplmA");
        params.put("words", ConstantUtil.SP_RANDOMCODE + CommonUtils.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), ConstantUtil.SP_RANDOMCODE));
        NetModel model = new NetModel(params, url);
        model.setObserver(observerOne);
        model.setTag(tag);
        return model;
    }


    public void log(HashMap<String, Object> hashMap) {
        Log.e("HttpRequest", "请求信息如下：");
        String requestContent = "";
        Iterator<Map.Entry<String, Object>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> itEntry = iterator.next();
            Object value = itEntry.getValue();
            if (value != null && !TextUtils.isEmpty(value.toString())) {
                itEntry.setValue(value.toString().replaceAll("'", "’"));
            }
            requestContent += itEntry.getKey() + "=" + itEntry.getValue() + "\n";
        }
        Log.e("HttpRequest", requestContent);
    }


    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = province_list.get(options1).getCity_name() +
                        city_map.get(options1).get(options2) +
                        couny_map.get(options1).get(options2).getCity_name();

                Toast.makeText(CityDataActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(province_list, city_map, couny_map);//三级选择器
        pvOptions.show();
    }

}
