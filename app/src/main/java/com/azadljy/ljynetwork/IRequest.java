package com.azadljy.ljynetwork;

import com.azadljy.ljynetwork.modle.NetModel;

/**
 * Created by azadljy on 2017/12/14.
 */

public interface IRequest {
    //状态码
    String TAG_STATUS_CODE = "statusCode";

    //请求失败后的失败内容
    String TAG_MESSAGE = "message";

    //json对象
    String TAG_OBJECT = "object";

    //json集合
    String TAG_OBJECTS = "objects";

    //默认超时时间
    int VALUE_DEFAULT_TIME_OUT = 20 * 1000;

    /**
     * 发送get请求
     */
    void sendGetRequest(NetModel action);

    /**
     * 发送post请求，包含多文件上传方式的传文件
     *
     * @param action 请求对象
     */
    void sendPostRequest(NetModel action);

    /**
     * 取消所有请求，可能中断请求
     */
    void cancelAllRequests(boolean mayInterruptIfRunning);

    /**
     * 重新设置请求超时时间
     */
    void setTimeOut(int value);

    /**
     * 下载文件
     */
    void downloadFile(String url);
}
