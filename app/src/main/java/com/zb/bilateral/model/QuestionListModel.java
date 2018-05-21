package com.zb.bilateral.model;

import java.util.List;

/**
 * Created by yaos on 2018/1/29.
 */

public class QuestionListModel {
    private List<Question> notReplyList;

    private List<Question> replyList;

    public List<Question> getNotReplyList() {
        return notReplyList;
    }

    public void setNotReplyList(List<Question> notReplyList) {
        this.notReplyList = notReplyList;
    }

    public List<Question> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Question> replyList) {
        this.replyList = replyList;
    }
}
