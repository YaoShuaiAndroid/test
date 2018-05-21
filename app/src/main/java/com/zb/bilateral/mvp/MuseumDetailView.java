package com.zb.bilateral.mvp;

import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.model.MuseumDetailModel;

/**
 * Created by yaos on 2017/5/10.
 */
public interface MuseumDetailView extends BaseView {
    void SendResultSuccess(MuseumDetailModel museumDetailModel);

    void SendCollectSuccess(String msg);

    void SendResultFail(String msg);
}
