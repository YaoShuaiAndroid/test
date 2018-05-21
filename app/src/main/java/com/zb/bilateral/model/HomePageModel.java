package com.zb.bilateral.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaos on 2018/1/25.
 * 首页接口model
 */

public class HomePageModel implements Serializable{
    List<CultrueHomeModel> gleaList;//文化拾遗

    List<CollectModel> recoTreaList;//馆藏

    List<ShowModel> recoExhList;//展览

    List<MuseumModel> recoMuseumList;//推荐博物馆

    List<MuseumModel> localMuseumList;//本地博物馆

    public List<CultrueHomeModel> getGleaList() {
        return gleaList;
    }

    public void setGleaList(List<CultrueHomeModel> gleaList) {
        this.gleaList = gleaList;
    }

    public List<CollectModel> getRecoTreaList() {
        return recoTreaList;
    }

    public void setRecoTreaList(List<CollectModel> recoTreaList) {
        this.recoTreaList = recoTreaList;
    }

    public List<ShowModel> getRecoExhList() {
        return recoExhList;
    }

    public void setRecoExhList(List<ShowModel> recoExhList) {
        this.recoExhList = recoExhList;
    }

    public List<MuseumModel> getRecoMuseumList() {
        return recoMuseumList;
    }

    public void setRecoMuseumList(List<MuseumModel> recoMuseumList) {
        this.recoMuseumList = recoMuseumList;
    }

    public List<MuseumModel> getLocalMuseumList() {
        return localMuseumList;
    }

    public void setLocalMuseumList(List<MuseumModel> localMuseumList) {
        this.localMuseumList = localMuseumList;
    }
}
