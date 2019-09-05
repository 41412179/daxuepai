package com.daxuepai.gaoxiao.exception;

import com.daxuepai.gaoxiao.util.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class ServiceException extends Exception{
    int code;
    String msg;

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(StatusCode status){
        this.code = status.getCode();
        this.msg = status.getText();
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

    @Override
    public String toString() {
        return "ServiceException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
