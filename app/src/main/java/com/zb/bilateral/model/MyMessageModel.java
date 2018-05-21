package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/3/9.
 */

public class MyMessageModel {
    private List<MessageModel> messageList;
    private int totalPage;
    private int page;

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

    public List<MessageModel> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageModel> messageList) {
        this.messageList = messageList;
    }
}
