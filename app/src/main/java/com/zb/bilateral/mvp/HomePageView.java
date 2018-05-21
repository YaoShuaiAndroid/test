package com.zb.bilateral.mvp;

import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.BaseModel;
import com.zb.bilateral.model.HomePageModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface HomePageView extends BaseView {
    void SendBannerSuccess(List<BannerModel> bannerModels);

    void SendResultSuccess(HomePageModel homePageModel);

    void SendResultFail(String msg);
}
