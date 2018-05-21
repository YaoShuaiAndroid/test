package com.zb.bilateral.mvp;

import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.HomePageModel;
import com.zb.bilateral.model.SearchListModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface SearchView extends BaseView {
    void SendResultSuccess(SearchListModel searchListModel);

    void SendResultFail(String msg);
}
