package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.ExhibitionListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface ExhibitionDetailView extends BaseView {
    void SendResultSuccess(CultrueListModel cultrueListModel);

    void SubmitResultSuccess(ExhibitionListModel exhibitionListModel);

    void SubmitSuccess(CultrueDetailModel cultrueDetailModel);

    void SendResultFail(String msg);
}
