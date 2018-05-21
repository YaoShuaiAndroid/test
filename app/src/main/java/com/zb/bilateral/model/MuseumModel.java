package com.zb.bilateral.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/1/25.
 * 博物馆model
 */

public class MuseumModel implements Serializable{
    private String rmId;//": "1",
    private String rmLogo;//": "",
    private String rmName;//": "啊啊"

    private String lmName;//": "深圳博物馆",
    private String lmId;//": "3"
    private String lmCover;
    //收藏博物馆
    private String msName;//": "深圳博物馆",
    private String msId;//": "3"

    private String id;//": "2ENNSPyqBlSCASNRR0AZNEtijXABFutf",
    private String name;//": "唐飘安",
    private String cover;//": "https:\/\/dummyimage.com\/199x231\/"
    private String area;//": null,
    private String AREA;
    private String isColl;//": 1,
    private String collCount;//": 3
    private String introduce;//
    List<BannerModel> imgList;

    public String getRmId() {
        return rmId;
    }

    public void setRmId(String rmId) {
        this.rmId = rmId;
    }

    public String getRmLogo() {
        return rmLogo;
    }

    public void setRmLogo(String rmLogo) {
        this.rmLogo = rmLogo;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getLmId() {
        return lmId;
    }

    public void setLmId(String lmId) {
        this.lmId = lmId;
    }

    public String getLmCover() {
        return lmCover;
    }

    public void setLmCover(String lmCover) {
        this.lmCover = lmCover;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<BannerModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<BannerModel> imgList) {
        this.imgList = imgList;
    }

    public String getMsName() {
        return msName;
    }

    public void setMsName(String msName) {
        this.msName = msName;
    }

    public String getMsId() {
        return msId;
    }

    public void setMsId(String msId) {
        this.msId = msId;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    @Override
    public String toString() {
        return "MuseumModel{" +
                "rmId='" + rmId + '\'' +
                ", rmLogo='" + rmLogo + '\'' +
                ", rmName='" + rmName + '\'' +
                ", lmName='" + lmName + '\'' +
                ", lmId='" + lmId + '\'' +
                ", lmCover='" + lmCover + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", area='" + area + '\'' +
                ", isColl='" + isColl + '\'' +
                ", collCount='" + collCount + '\'' +
                '}';
    }
}
