package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.ShowListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class ShowDetailPresenter extends BasePresenter<ShowDetailView> {
    Context context;
    public ShowDetailPresenter(ShowDetailView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void show_Detail(String exhId,String token) {
        Map map=new HashMap();
        map.put("exhId",exhId);
        map.put("token",token);

        addSubscription(apiStores.show_Detail(exhId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<ShowListModel>() {
                    @Override
                    public void onSuccess(ShowListModel showListModel) {
                        mvpView.SendResultSuccess(showListModel.getExh());
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

    public void exhComs(String exhId,String currentPage,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("exhId",exhId);
        map.put("currentPage",currentPage);

        addSubscription(apiStores.exhComs(exhId, currentPage,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitListModel>() {
                    @Override
                    public void onSuccess(CommitListModel commitListModel) {
                        mvpView.CommentListSuccess(commitListModel);
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

    public void exhLike(String exhId,String token ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("exhId",exhId);
        map.put("token",token);

        addSubscription(apiStores.exhLike(exhId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitLikeSuccess(commitListModel.getStatus());
                    }
                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SubmitResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

    public void comExh(String exhId,String token,String text ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("exhId",exhId);
        map.put("token",token);
        map.put("content",text);

        addSubscription(apiBaseStores.comExh(exhId, token,text,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitCommitSuccess(commitListModel.getStatus());
                    }
                    @Override
                    public void onLogin() {
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SubmitResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }
}
