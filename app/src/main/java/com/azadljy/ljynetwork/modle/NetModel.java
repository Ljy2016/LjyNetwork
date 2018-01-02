package com.azadljy.ljynetwork.modle;

import com.azadljy.ljynetwork.NetModelStatus;
import com.azadljy.ljynetwork.ResponseResult;
import com.azadljy.ljynetwork.bean.CityTreeNode;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;

/**
 * Created by azadljy on 2017/12/14.
 */

public class NetModel {

    private Map<String, Object> parameters;
    private ResponseResult result;
    private NetModelStatus status;
    private String url = "http://121.201.67.222:16990/api.post";
    private Observer<NetModel> observer;
    private String tag;
    private Object object;
    private String firstId;
    private String secondId;

    public NetModel(Map<String, Object> parameters) {
        this.parameters = parameters;
        status = NetModelStatus.READY;
    }

    public NetModel(Map<String, Object> parameters, String url) {
        this.parameters = parameters;
        status = NetModelStatus.READY;
        this.url = url;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public ResponseResult getResult() {
        return result;
    }

    public void setResult(ResponseResult result) {
        this.result = result;
    }


    public NetModelStatus getStatus() {
        return status;
    }

    public void setStatus(NetModelStatus status) {
        this.status = status;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Observer<NetModel> getObserver() {
        return observer;
    }

    public void setObserver(Observer<NetModel> observer) {
        this.observer = observer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


}
