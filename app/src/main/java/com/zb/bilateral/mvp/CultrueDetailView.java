package com.zb.bilateral.mvp;

import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.HomePageModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface CultrueDetailView extends BaseView {
    void SendResultSuccess(CultrueModel cultrueModel);

    void CommentListSuccess(CommitListModel commitListModel);

    void SubmitCollectSuccess(String result);

    void SubmitLikeSuccess(String result);

    void SubmitCommitSuccess(String result);

    void SendResultFail(String msg);

    void SubmitResultFail(String msg);
}
