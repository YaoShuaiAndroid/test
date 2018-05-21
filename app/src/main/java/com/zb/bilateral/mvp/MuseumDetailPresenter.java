package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.MuseumDetailModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class MuseumDetailPresenter extends BasePresenter<MuseumDetailView> {
    Context context;
    public MuseumDetailPresenter(MuseumDetailView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void museum(String id,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("id",id);
        map.put("token",token);

        addSubscription(apiStores.museum(id, token,AppUtil.getMD5Str(map)),
                new ApiCallback<MuseumDetailModel>() {
                    @Override
                    public void onSuccess(MuseumDetailModel museumDetailModel) {
                        mvpView.SendResultSuccess(museumDetailModel);
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

    public void museumColl(String museumId,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("museumId",museumId);
        map.put("token",token);

        addSubscription(apiStores.museumColl(museumId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendCollectSuccess(commitModel.getStatus());
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
