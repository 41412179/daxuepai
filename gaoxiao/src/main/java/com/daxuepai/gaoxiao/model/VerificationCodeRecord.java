package com.daxuepai.gaoxiao.model;

import java.util.Date;

public class VerificationCodeRecord {
    int id;
    int userId;
    String ip;
    Date date;
    String phone;
    int isLimited;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsLimited() {
        return isLimited;
    }

    public void setIsLimited(int isLimited) {
        this.isLimited = isLimited;
    }

    @Override
    public String toString() {
        return "VerificationCodeRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                ", date=" + date +
                ", phone='" + phone + '\'' +
                ", isLimited=" + isLimited +
                '}';
    }
}
