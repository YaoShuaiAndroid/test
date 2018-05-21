package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/2/5.
 */

public class SearchListModel {
    private List<ShowModel> exhList;//展览数组
    private List<MuseumModel> musemuList;//博物馆数组
    private List<CollectModel> antList;//文物数组

    public List<ShowModel> getExhList() {
        return exhList;
    }

    public void setExhList(List<ShowModel> exhList) {
        this.exhList = exhList;
    }

    public List<MuseumModel> getMusemuList() {
        return musemuList;
    }

    public void setMusemuList(List<MuseumModel> musemuList) {
        this.musemuList = musemuList;
    }

    public List<CollectModel> getAntList() {
        return antList;
    }

    public void setAntList(List<CollectModel> antList) {
        this.antList = antList;
    }
}
