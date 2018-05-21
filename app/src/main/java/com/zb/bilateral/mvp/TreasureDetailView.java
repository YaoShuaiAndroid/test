package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CollectListModel;
import com.zb.bilateral.model.CollectModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface TreasureDetailView extends BaseView {
    void SendResultSuccess(CollectModel collectModel);

    void CommentListSuccess(CommitListModel commitListModel);

    void SubmitCollectSuccess(String status);

    void SubmitResultFail(String result);

    void SubmitCommitSuccess(String result);

    void SendResultFail(String msg);
}
