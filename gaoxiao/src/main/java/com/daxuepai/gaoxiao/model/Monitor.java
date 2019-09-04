package com.daxuepai.gaoxiao.model;

import java.util.Date;

public class Monitor {
    int id;
    int userId;
    String url;
    Long takeTime;
    Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Long takeTime) {
        this.takeTime = takeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
