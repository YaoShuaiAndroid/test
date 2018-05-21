package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 * 展览列表
 */

public class ShowListModel {
    private int totalPage;//
    private int page;
    private ShowModel exh;
    private List<ShowModel> temporaryList;
    private List<ShowModel> basicList;

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

    public List<ShowModel> getTemporaryList() {
        return temporaryList;
    }

    public void setTemporaryList(List<ShowModel> temporaryList) {
        this.temporaryList = temporaryList;
    }

    public ShowModel getExh() {
        return exh;
    }

    public void setExh(ShowModel exh) {
        this.exh = exh;
    }

    public List<ShowModel> getBasicList() {
        return basicList;
    }

    public void setBasicList(List<ShowModel> basicList) {
        this.basicList = basicList;
    }
}
