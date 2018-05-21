package com.zb.bilateral.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/1/25.
 * 展览model
 */

public class ShowModel implements Serializable{
    private String reTitle;//": "没有标题",
    private String RECOVER;//": "/zbjj/userfiles/1/images/exhibition/imgExh/2018/01/timg2.jpg",
    private String reId;//": "3da1d61861e1420eaf064c92a2dfea0b"
    private String id;//"": "eLDVeUzXjT1rjvXede8RM2bWUUsvxOSI",
    private String name;//"": "宣兰梵",
    private String area;//"": null,
    private String museumName;
    private String cover;//"": "https:\/\/dummyimage.com\/146x216\/",
    private String isColl;//"": 0,
    private String collCount;//"": 26
    private int likeCount;
    private String isLike;
    private String content;
    private String title;
    private String exhId;

    List<BannerModel> imgList;

    public String getReTitle() {
        return reTitle;
    }

    public void setReTitle(String reTitle) {
        this.reTitle = reTitle;
    }

    public String getRECOVER() {
        return RECOVER;
    }

    public void setRECOVER(String RECOVER) {
        this.RECOVER = RECOVER;
    }

    public String getReId() {
        return reId;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }

    public String getExhId() {
        return exhId;
    }

    public void setExhId(String exhId) {
        this.exhId = exhId;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }
}
