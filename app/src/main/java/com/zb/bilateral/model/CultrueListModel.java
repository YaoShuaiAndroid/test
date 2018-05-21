package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 * 文化拾遗列表
 */

public class CultrueListModel {
    private int totalPage;//
    private int page;
    private List<CultrueModel> actList;
    private List<CultrueModel> gleaList;
    private List<CultrueModel> antiquesList;
    private CultrueModel glea;

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

    public List<CultrueModel> getGleaList() {
        return gleaList;
    }

    public void setGleaList(List<CultrueModel> gleaList) {
        this.gleaList = gleaList;
    }

    public CultrueModel getGlea() {
        return glea;
    }

    public void setGlea(CultrueModel glea) {
        this.glea = glea;
    }

    public List<CultrueModel> getActList() {
        return actList;
    }

    public void setActList(List<CultrueModel> actList) {
        this.actList = actList;
    }

    public List<CultrueModel> getAntiquesList() {
        return antiquesList;
    }

    public void setAntiquesList(List<CultrueModel> antiquesList) {
        this.antiquesList = antiquesList;
    }
}
