package com.zb.bilateral.mvp;

import com.zb.bilateral.model.AnsweiListModel;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.QuestionListModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface AnswerQuestionView extends BaseView {
    void SendResultSuccess(AnsweiListModel answeiListModel);

    void SubmitResultSuccess(CommitModel commitModel);

    void SendResultFail(String msg);
}
