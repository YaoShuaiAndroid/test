package com.zb.bilateral.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/1/25.
 * 馆藏model
 */

public class CollectModel implements Serializable{
    private String rtId;//": "1",
    private String rtCover;//": "/zbjj/userfiles/1/images/exhibition/imgExh/2018/01/timg2.jpg",
    private String rtName;//": "旺旺"
    private String id;//": "Bl5gvv3V5sgdMy0Z7rcL3vk2VEZe8IkY",
    private String name;//": "易眉巧",
    private String years;//": "司空敬卿",
    private String cover;//": "https:\/\/dummyimage.com\/440x312\/"
    private String introduce;//": "容凤羽",
    private String isColl;//": 0,
    private String museumName;
    private List<BannerModel> imgList;
    private String anName;
    private String anId;
    public String getRtId() {
        return rtId;
    }

    public void setRtId(String rtId) {
        this.rtId = rtId;
    }

    public String getRtCover() {
        return rtCover;
    }

    public void setRtCover(String rtCover) {
        this.rtCover = rtCover;
    }

    public String getRtName() {
        return rtName;
    }

    public void setRtName(String rtName) {
        this.rtName = rtName;
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

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
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

    public String getIsColl() {
        return isColl;
    }

    public void setIsColl(String isColl) {
        this.isColl = isColl;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }

    public String getAnName() {
        return anName;
    }

    public void setAnName(String anName) {
        this.anName = anName;
    }

    public String getAnId() {
        return anId;
    }

    public void setAnId(String anId) {
        this.anId = anId;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }
}
