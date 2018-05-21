package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.QuestionListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface RegisterView extends BaseView {
    void SendCodeSuccess(String code);

    void SendSuccess(CommitModel commitModel);

    void SendResultFail(String msg);
}
