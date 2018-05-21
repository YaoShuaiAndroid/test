package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface CultrueRelicsView extends BaseView {
    void SendResultSuccess(CultrueDetailModel cultrueDetailModel);

    void SendResultFail(String msg);

    void SubmitSuccess(CultrueDetailModel cultrueListModel);

    void nearResultSuccess(CultrueListModel cultrueListModel);
}
