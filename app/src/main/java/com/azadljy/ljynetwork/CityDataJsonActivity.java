package com.azadljy.ljynetwork;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azadljy.ljynetwork.city.CityPicker;
import com.azadljy.ljynetwork.city.Cityinfo;
import com.azadljy.ljynetwork.city.FileUtil;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;

public class CityDataJsonActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    RetrofitRequest retrofitRequest;
    public static List<Cityinfo> province_list = new ArrayList<>();
    public static HashMap<String, List<Cityinfo>> city_map = new HashMap<>();
    public static HashMap<String, List<Cityinfo>> couny_map = new HashMap<>();


    private ArrayList<String> options1Items = new ArrayList<>();
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

        try {
            praseData();
//            ShowPickerView();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG", "show------------------------------------------------: \n" + e.getMessage());
            return;
        }
//        Log.e("TAG", "show: " + province_list.size() + "-------------" + city_map.size() + "----------------" + couny_map.size());
        AlertDialog.Builder builder = new AlertDialog.Builder(CityDataJsonActivity.this);
        View view = LayoutInflater.from(CityDataJsonActivity.this).inflate(R.layout.addressdialog, null);
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
//        if (checkPro()) {
//            save();
//        }
    }


    public void save() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path, "myjson.txt");

        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(dataJson.toString().getBytes());
            stream.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "show: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "show: " + e.getMessage());
        }

    }

    //解析省市区控件的数据
    public void praseData() throws JSONException {
        province_list.clear();
        city_map.clear();
        couny_map.clear();
        JSONArray array = dataJson.getJSONArray("一级");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Cityinfo cityinfo = new Cityinfo();
            if (TextUtils.isEmpty(object.getString("一级id")) || TextUtils.isEmpty(object.getString("一级"))) {
                continue;
            }
            cityinfo.setId(object.getString("一级id"));
            cityinfo.setCity_name(object.getString("一级"));
            province_list.add(cityinfo);
            JSONArray second = dataJson.getJSONArray("一级id:" + cityinfo.getId());
            List<Cityinfo> secondinfo = new ArrayList<>();
            for (int j = 0; j < second.length(); j++) {
                JSONObject secondObj = second.getJSONObject(j);
                Cityinfo cityinfo1 = new Cityinfo();
                try {
                    if (TextUtils.isEmpty(secondObj.getString("二级id")) || TextUtils.isEmpty(secondObj.getString("二级"))) {
                        continue;
                    }
                    cityinfo1.setId(secondObj.getString("二级id"));
                    cityinfo1.setCity_name(secondObj.getString("二级"));
                    secondinfo.add(cityinfo1);
                    city_map.put(cityinfo.getId(), secondinfo);
                } catch (Exception e) {
                    continue;
                }

                JSONArray third = dataJson.getJSONArray("二级id:" + cityinfo1.getId());
                List<Cityinfo> thirdinfo = new ArrayList<>();
                for (int k = 0; k < third.length(); k++) {
                    JSONObject thirdObj = third.getJSONObject(k);
                    Cityinfo cityinfo2 = new Cityinfo();
                    try {
                        if (TextUtils.isEmpty(thirdObj.getString("三级id")) || TextUtils.isEmpty(thirdObj.getString("三级"))) {
                            continue;
                        }
                        cityinfo2.setId(thirdObj.getString("三级id"));
                        cityinfo2.setCity_name(thirdObj.getString("三级"));
                        thirdinfo.add(cityinfo2);
                        couny_map.put(cityinfo1.getId(), thirdinfo);
                    } catch (Exception e) {

                    }
                }
            }

        }

    }


    JSONObject dataJson = new JSONObject();
    int count = 0;

    //规格解析
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
                        dataJson.put("一级", array);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject privance = array.getJSONObject(i);
                            NetModel model = getModelOne("二级", privance.getString("一级id"), "", "two");
                            retrofitRequest.sendPostRequest(model);
                        }
                        break;
                    case "two":
                        JSONArray cityArray = object.getJSONArray("二级列表");
                        dataJson.put("一级id:" + s.getFirstId(), cityArray);
                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject city = cityArray.getJSONObject(i);
                            NetModel model = getModelOne("三级", "", city.getString("二级id"), "three");
                            retrofitRequest.sendPostRequest(model);
                        }
                        break;
                    case "three":
                        JSONArray countyArray = object.getJSONArray("三级列表");
                        dataJson.put("二级id:" + s.getSecondId(), countyArray);
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
        model.setFirstId(firseId);
        model.setSecondId(secondId);
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

                Toast.makeText(CityDataJsonActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("类别选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        //pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(province_list, city_map, couny_map);//三级选择器
        pvOptions.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }


    String[] params = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    public boolean checkPro() {
        if (EasyPermissions.hasPermissions(this, params)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "给老子权限",
                    10, params);
            return false;
        }
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        save();
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }
}
