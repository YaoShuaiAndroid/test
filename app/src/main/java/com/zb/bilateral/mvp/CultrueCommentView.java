package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface CultrueCommentView extends BaseView {
    void CommentListSuccess(CommitListModel commitListModel);

    void SubmitCommitSuccess(String status);

    void SendResultFail(String msg);
}
