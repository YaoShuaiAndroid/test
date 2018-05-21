package com.zb.bilateral.mvp;

import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.model.MyMessageModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface MessageView extends BaseView {
    void SendResultSuccess(MyMessageModel messageModel);

    void delRsultSuccess(String result);

    void SendResultFail(String msg);
}
