package com.zb.bilateral.mvp;

import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.ShowModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface ShowDetailView extends BaseView {
    void SendResultSuccess(ShowModel showModel);

    void CommentListSuccess(CommitListModel commitListModel);

    void SubmitLikeSuccess(String result);

    void SubmitCommitSuccess(String result);

    void SendResultFail(String msg);

    void SubmitResultFail(String msg);
}
