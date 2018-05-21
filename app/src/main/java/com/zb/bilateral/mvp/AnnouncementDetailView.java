package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface AnnouncementDetailView extends BaseView {
    void SendResultSuccess(ActivityModel activityModel);

    void SubmitLikeSuccess(String result);

    void SendResultFail(String msg);

    void SubmitResultFail(String msg);
}
