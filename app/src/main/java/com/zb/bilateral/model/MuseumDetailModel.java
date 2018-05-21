package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/30.
 */

public class MuseumDetailModel {
    MuseumModel museum;
    List<ActivityModel> actList;

    List<ActivityModel> noticeList;

    List<ActivityModel> infoList;

    public MuseumModel getMuseum() {
        return museum;
    }

    public void setMuseum(MuseumModel museum) {
        this.museum = museum;
    }

    public List<ActivityModel> getActList() {
        return actList;
    }

    public void setActList(List<ActivityModel> actList) {
        this.actList = actList;
    }

    public List<ActivityModel> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<ActivityModel> noticeList) {
        this.noticeList = noticeList;
    }

    public List<ActivityModel> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ActivityModel> infoList) {
        this.infoList = infoList;
    }
}
