package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by bt on 2018/2/6.
 */

public class FloorModel {
    List<ShowRoomModel> showroomList;
    List<ShowRoomListModel> floorList;

    private String no;
    private boolean is_select;

    public List<ShowRoomModel> getShowroomList() {
        return showroomList;
    }

    public void setShowroomList(List<ShowRoomModel> showroomList) {
        this.showroomList = showroomList;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public List<ShowRoomListModel> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<ShowRoomListModel> floorList) {
        this.floorList = floorList;
    }

    @Override
    public String toString() {
        return "FloorModel{" +
                "showroomList=" + showroomList +
                ", no='" + no + '\'' +
                ", is_select=" + is_select +
                '}';
    }
}
