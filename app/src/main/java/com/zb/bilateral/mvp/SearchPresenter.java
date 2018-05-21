package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.BannerListModel;
import com.zb.bilateral.model.HomePageModel;
import com.zb.bilateral.model.SearchListModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class SearchPresenter extends BasePresenter<SearchView> {
    Context context;
    public SearchPresenter(SearchView view, Context context) {
        attachView(view);
        this.context=context;
    }

    //首页数据
    public void search(String searchParam) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("searchParam",searchParam);

        addSubscription(apiBaseStores.search(searchParam, AppUtil.getMD5Str(map)),
                new ApiCallback<SearchListModel>() {
                    @Override
                    public void onSuccess(SearchListModel searchListModel) {
                        mvpView.SendResultSuccess(searchListModel);
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
