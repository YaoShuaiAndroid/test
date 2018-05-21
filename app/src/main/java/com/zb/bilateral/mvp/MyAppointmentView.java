package com.zb.bilateral.mvp;

import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CommitModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface MyAppointmentView extends BaseView {
    void SendResultSuccess(AppointmentListModel appointmentListModel);

    void submitSuccess(CommitModel commitModel);

    void SendResultFail(String msg);
}
