package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/2/6.
 * 展厅详情model
 */

public class ShowRoomModel {
    private String voiceCount;//": 0,
    private String name;//": "深圳博物馆1F展厅1",
    private String id;//": "fdf8ba5615bb4841851a5e6611784dbe"
    private String voice;//": "",
    private int ls;//音频长度
    private float x;//标记点据左部宽度
    private float y;//标记点据顶部高度
    private String hasQuestion;//是否还有答题
    private String cover;//": "/zbjj/userfiles/1/images/museum/2018/01/t01cc7d038a62a12dc8.jpg",
    private String introduce;//": "不介绍",
    private String plan;
    private String title;//": ""
    private String no;

    private List<BannerModel> imgList;

    public String getVoiceCount() {
        return voiceCount;
    }

    public void setVoiceCount(String voiceCount) {
        this.voiceCount = voiceCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(String hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getLs() {
        return ls;
    }

    public void setLs(int ls) {
        this.ls = ls;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }

    @Override
    public String toString() {
        return "ShowRoomModel{" +
                "voiceCount='" + voiceCount + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", voice='" + voice + '\'' +
                ", ls=" + ls +
                ", x=" + x +
                ", y=" + y +
                ", hasQuestion='" + hasQuestion + '\'' +
                ", cover='" + cover + '\'' +
                ", introduce='" + introduce + '\'' +
                ", plan='" + plan + '\'' +
                ", title='" + title + '\'' +
                ", no='" + no + '\'' +
                '}';
    }
}
