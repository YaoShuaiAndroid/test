package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.FloorListModel;
import com.zb.bilateral.model.ShowRoomListModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class ExhibitionHallPresenter extends BasePresenter<ExhibitionHallView> {
    Context context;
    public ExhibitionHallPresenter(ExhibitionHallView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void showroom(String museum_id,String id ,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("museumId",museum_id);
        map.put("id",id);
        map.put("token",token);

        addSubscription(apiBaseStores.showroom(museum_id,id,token, AppUtil.getMD5Str(map)),
                new ApiCallback<ShowRoomListModel>() {
                    @Override
                    public void onSuccess(ShowRoomListModel showRoomListModel) {
                        mvpView.SendResultSuccess(showRoomListModel);
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


    public void nearbyShowroom(String showroomId ) {
        Map map=new HashMap();
        map.put("showroomId",showroomId);

        addSubscription(apiBaseStores.nearbyShowroom(showroomId, AppUtil.getMD5Str(map)),
                new ApiCallback<ShowRoomListModel>() {
                    @Override
                    public void onSuccess(ShowRoomListModel showRoomListModel) {
                        mvpView.nearbyShowroomSuccess(showRoomListModel);
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
