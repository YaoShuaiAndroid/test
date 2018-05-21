package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.FloorModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface LoginView extends BaseView {
    void sendResultSuccess(CommitModel commitModel);

    void sendThirdResultSuccess(CommitModel commitModel);

    void sendResultFail(String msg);
}
