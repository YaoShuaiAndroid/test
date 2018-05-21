package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.FloorModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class GalleryTourPresenter extends BasePresenter<GalleryTourView> {
    Context context;
    public GalleryTourPresenter(GalleryTourView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void voice(String id ) {
        Map map=new HashMap();
        map.put("id",id);

        addSubscription(apiBaseStores.voice(id, AppUtil.getMD5Str(map)),
                new ApiCallback<FloorListModel>() {
                    @Override
                    public void onSuccess(FloorListModel floorListModel) {
                        mvpView.SendResultSuccess(floorListModel);
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

    public void showroomsForSmall(String id ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("museumId",id);

        addSubscription(apiBaseStores.showroomsForSmall(id, AppUtil.getMD5Str(map)),
                new ApiCallback<FloorModel>() {
                    @Override
                    public void onSuccess(FloorModel floorModel) {
                        mvpView.SendFloorSuccess(floorModel);
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

    public void antiques1(String id ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("no",id);

        addSubscription(apiBaseStores.antiques1(id, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueDetailModel>() {
                    @Override
                    public void onSuccess(CultrueDetailModel cultrueDetailModel) {
                        mvpView.SubmitSuccess(cultrueDetailModel);
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
