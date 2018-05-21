package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/27.
 */

public class CollectListModel {
    List<CollectModel> treaList;
    List<CollectModel> actList;
    private CollectModel trea;
    private int totalPage;
    private int page;

    public List<CollectModel> getTreaList() {
        return treaList;
    }

    public void setTreaList(List<CollectModel> treaList) {
        this.treaList = treaList;
    }

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

    public CollectModel getTrea() {
        return trea;
    }

    public void setTrea(CollectModel trea) {
        this.trea = trea;
    }

    public List<CollectModel> getActList() {
        return actList;
    }

    public void setActList(List<CollectModel> actList) {
        this.actList = actList;
    }
}
