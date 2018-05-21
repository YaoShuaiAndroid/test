package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 */

public class ActivityModel {
    private String id;//": "akG7V0GkdHXAE1S9dzfS0rMLfzUb7MWX",
    private String title;//": "姜行思",
    private String startTime;//": "2018-01-26 18:29:05",
    private String endTime;//": "2018-01-26 18:29:05",
    private String logo;//": "https:\/\/dummyimage.com\/231x291\/",
    private String status;//": 1
    private String cover;//": "https:\/\/dummyimage.com\/239x234\/",
    private String address;//": "幸琪苑",
    private String speaker;//": "伏军磊",
    private String numLimit;//": 27,
    private String introduce;//": "唐育世",
    private int likeCount;//": 44,
    private String isLike;//": 0,
    private String isColl;
    private String isSignIn;
    private String collCount;//": 14,
    private String content;//"": "尚仪环"
    private String createDate;

    List<BannerModel> imgList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getNumLimit() {
        return numLimit;
    }

    public void setNumLimit(String numLimit) {
        this.numLimit = numLimit;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(String isSignIn) {
        this.isSignIn = isSignIn;
    }

    public String getIsColl() {
        return isColl;
    }

    public void setIsColl(String isColl) {
        this.isColl = isColl;
    }

    public String getCollCount() {
        return collCount;
    }

    public void setCollCount(String collCount) {
        this.collCount = collCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }
}
