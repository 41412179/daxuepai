package com.daxuepai.gaoxiao.model;

public enum ResultStatus {
    Ok(0,"ok"),
    Failed(1,"failed");

    int code;
    String val;
    ResultStatus(int code,String val){
        code = code;
        val = val;
    }
}
