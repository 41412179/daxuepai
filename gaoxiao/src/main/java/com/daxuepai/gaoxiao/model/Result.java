package com.daxuepai.gaoxiao.model;

import com.daxuepai.gaoxiao.util.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class Result {
    int code;
    String msg;
    Map<String, String> data = new HashMap<>();

    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.msg = errorCode.getText();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
