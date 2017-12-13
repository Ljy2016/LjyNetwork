package com.azadljy.ljynetwork;

/**
 * Created by azadljy on 2017/12/14.
 */

public class ResponseResult {
   private int statusCode;
   private Object content;
   private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
