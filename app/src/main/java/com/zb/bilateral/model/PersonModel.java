package com.zb.bilateral.model;

import java.io.Serializable;

/**
 * Created by yaos on 2018/2/1.
 */

public class PersonModel implements Serializable{
    private String photo;
    private String msgCount;
    private String point;
    private String token;
    private String phone;
    private String name;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "PersonModel{" +
                "photo='" + photo + '\'' +
                ", msgCount='" + msgCount + '\'' +
                ", token='" + token + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
