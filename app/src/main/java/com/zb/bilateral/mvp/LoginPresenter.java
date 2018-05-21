package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mycommon.util.LogUtil;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    Context context;

    public LoginPresenter(LoginView view, Context context) {
        attachView(view);
        this.context = context;
    }

    public void main(String loginName, String password) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("loginName", loginName);
        map.put("password", password);

        addSubscription(apiStores.main(loginName, password, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.sendResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.sendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void thirdPartyLogin(String openId, String name,String photo,String type) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("openId", openId);
        map.put("name", name);
        map.put("photo", photo);
        map.put("type", type);
        LogUtil.i("tag","photo-->"+photo);
        addSubscription(apiStores.thirdPartyLogin(openId, name, photo, type, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.sendThirdResultSuccess(commitModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.sendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

}
