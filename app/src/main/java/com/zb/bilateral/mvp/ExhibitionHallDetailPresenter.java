package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.CultrueDetailModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.model.ExhibitionListModel;
import com.zb.bilateral.model.SignModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class ExhibitionHallDetailPresenter extends BasePresenter<ExhibitionDetailView> {
    Context context;

    public ExhibitionHallDetailPresenter(ExhibitionDetailView view, Context context) {
        attachView(view);
        this.context = context;
    }

    public void nearbyAllAntiques(String antiquesId,String token ) {
        Map map=new HashMap();
        map.put("showroomId",antiquesId);
        map.put("token",token);

        addSubscription(apiBaseStores.nearbyAllAntiques(antiquesId,token, AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueListModel>() {
                    @Override
                    public void onSuccess(CultrueListModel cultrueListModel) {
                        mvpView.SendResultSuccess(cultrueListModel);
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

    public void antiquesListForSmall(String antiquesId) {
        Map map=new HashMap();
        map.put("showroomId",antiquesId);

        addSubscription(apiBaseStores.antiquesListForSmall(antiquesId, AppUtil.getMD5Str(map)),
                new ApiCallback<ExhibitionListModel>() {
                    @Override
                    public void onSuccess(ExhibitionListModel cultrueListModel) {
                        mvpView.SubmitResultSuccess(cultrueListModel);
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
