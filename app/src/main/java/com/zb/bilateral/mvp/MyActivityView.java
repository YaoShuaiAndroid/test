package com.zb.bilateral.mvp;

import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.AppointmentListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface MyActivityView extends BaseView {
    void SendResultSuccess(ActivityListModel activityListModel);

    void SendResultFail(String msg);
}
