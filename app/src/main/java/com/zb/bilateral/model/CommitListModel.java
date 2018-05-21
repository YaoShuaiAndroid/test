package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by bt on 2018/1/26.
 */

public class CommitListModel {
    List<CommentModel> comList;
    private int totalPage;
    private int page;

    public List<CommentModel> getComList() {
        return comList;
    }

    public void setComList(List<CommentModel> comList) {
        this.comList = comList;
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
}
