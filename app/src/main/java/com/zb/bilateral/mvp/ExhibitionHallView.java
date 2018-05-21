package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.ShowRoomListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface ExhibitionHallView extends BaseView {
    void SendResultSuccess(ShowRoomListModel showRoomListModel);

    void nearbyShowroomSuccess(ShowRoomListModel showRoomListModel);

    void SubmitSuccess(CultrueDetailModel cultrueDetailModel);

    void SendResultFail(String msg);
}
