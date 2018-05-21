package com.zb.bilateral.mvp;

import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.HomePageModel;

import java.util.List;

/**
 * Created by yaos on 2017/5/10.
 */
public interface PublicView<T> extends BaseView {
    void SendResultSuccess(T t);

    void SendBannerSuccess(List<BannerModel> bannerModels);

    void SendResultFail(String msg);
}
