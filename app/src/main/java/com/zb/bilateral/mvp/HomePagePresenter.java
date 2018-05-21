package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.BannerListModel;
import com.zb.bilateral.model.HomePageModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class HomePagePresenter extends BasePresenter<HomePageView> {
    Context context;
    public HomePagePresenter(HomePageView view, Context context) {
        attachView(view);
        this.context=context;
    }
    //加载轮播图
    public void banner(String type) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("type",type);

        addSubscription(apiBaseStores.banner(type, AppUtil.getMD5Str(map)),
                new ApiCallback<BannerListModel>() {
                    @Override
                    public void onSuccess(BannerListModel bannerListModel) {
                         mvpView.SendBannerSuccess(bannerListModel.getHomePageList());
                    }@Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }


                    @Override
                    public void onFailure(String msg) {
                            mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    //首页数据
    public void getIndex(String city) {
        Map map=new HashMap();
        map.put("currentCity",city);

        addSubscription(apiBaseStores.getIndex(city, AppUtil.getMD5Str(map)),
                new ApiCallback<HomePageModel>() {
                    @Override
                    public void onSuccess(HomePageModel baseModel) {
                        mvpView.SendResultSuccess(baseModel);
                    }
                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void getCityIdIndex(String city) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("currentCity",city);

        addSubscription(apiBaseStores.getCityIdIndex(city, AppUtil.getMD5Str(map)),
                new ApiCallback<HomePageModel>() {
                    @Override
                    public void onSuccess(HomePageModel baseModel) {
                        mvpView.SendResultSuccess(baseModel);
                    }
                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }
}
