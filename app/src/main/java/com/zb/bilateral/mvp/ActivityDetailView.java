package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface ActivityDetailView extends BaseView {
    void SendResultSuccess(ActivityModel activityModel);

    void CommentListSuccess(CommitListModel commitListModel);

    void CancelSignSuccess(String result);

    void SubmitSignSuccess(String result);

    void SubmitCommitSuccess(String result);

    void SubmitLikeSuccess(String result);

    void SubmitCollectSuccess(String result);

    void SendResultFail(String msg);

    void SubmitResultFail(String msg);
}
