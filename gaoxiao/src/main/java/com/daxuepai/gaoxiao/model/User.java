package com.daxuepai.gaoxiao.model;

import java.util.Date;

public class User {
    int id;
    String phone;
    String username;
    int school;
    String headurl;
    String ticket;
    Date ticketTimeout;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getTicketTimeout() {
        return ticketTimeout;
    }

    public void setTicketTimeout(Date ticketTimeout) {
        this.ticketTimeout = ticketTimeout;
    }
}
