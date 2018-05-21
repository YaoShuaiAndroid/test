package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/27.
 */

public class MuseumListModel {
    private int totalPage;//
    private int page;
    private List<MuseumModel> actList;
    private List<MuseumModel> museumList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MuseumModel> getMuseumList() {
        return museumList;
    }

    public void setMuseumList(List<MuseumModel> museumList) {
        this.museumList = museumList;
    }

    public List<MuseumModel> getActList() {
        return actList;
    }

    public void setActList(List<MuseumModel> actList) {
        this.actList = actList;
    }
}
