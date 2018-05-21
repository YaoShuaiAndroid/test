package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/26.
 */

public class ActivityListModel {
    List<ActivityModel> actList;
    List<ActivityModel> infoList;
    List<ActivityModel> noticeList;
    private ActivityModel act;
    private ActivityModel info;

    private int totalPage;
    private int page;

    public List<ActivityModel> getActList() {
        return actList;
    }

    public void setActList(List<ActivityModel> actList) {
        this.actList = actList;
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

    public ActivityModel getAct() {
        return act;
    }

    public void setAct(ActivityModel act) {
        this.act = act;
    }

    public List<ActivityModel> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ActivityModel> infoList) {
        this.infoList = infoList;
    }

    public List<ActivityModel> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<ActivityModel> noticeList) {
        this.noticeList = noticeList;
    }

    public ActivityModel getInfo() {
        return info;
    }

    public void setInfo(ActivityModel info) {
        this.info = info;
    }
}
