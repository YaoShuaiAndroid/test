package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.FloorModel;
import com.zb.bilateral.model.SearchListModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface GalleryTourView extends BaseView {
    void SendResultSuccess(FloorListModel floorListModel);

    void SendFloorSuccess(FloorModel floorModel);

    void SubmitSuccess(CultrueDetailModel cultrueDetailModel);

    void SendResultFail(String msg);
}
