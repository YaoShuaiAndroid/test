package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by bt on 2018/1/26.
 */

public class AppointmentListModel {
    List<AppointmentModel> commList;
    List<AppointmentModel> actList;
    private int totalPage;
    private int page;

    public List<AppointmentModel> getCommList() {
        return commList;
    }

    public void setCommList(List<AppointmentModel> commList) {
        this.commList = commList;
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

    public List<AppointmentModel> getActList() {
        return actList;
    }

    public void setActList(List<AppointmentModel> actList) {
        this.actList = actList;
    }
}
