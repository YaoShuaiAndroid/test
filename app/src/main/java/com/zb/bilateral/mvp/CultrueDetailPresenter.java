package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.CultrueListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class CultrueDetailPresenter extends BasePresenter<CultrueDetailView> {
    Context context;
    public CultrueDetailPresenter(CultrueDetailView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void cultrue_Detail(String gleaId,String token) {
        Map map=new HashMap();
        map.put("gleaId",gleaId);
        map.put("token",token);

        addSubscription(apiStores.cultrue_Detail(gleaId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<CultrueListModel>() {
                    @Override
                    public void onSuccess(CultrueListModel cultrueListModel) {
                        mvpView.SendResultSuccess(cultrueListModel.getGlea());
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

    public void gleaComs(String gleaId,String currentPage,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("gleaId",gleaId);
        map.put("currentPage",currentPage);

        addSubscription(apiStores.gleaComs(gleaId, currentPage,AppUtil.getMD5Str(map)),
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

    public void gleaColl(String gleaId,String token ) {
            mvpView.showLoading();

        Map map=new HashMap();
        map.put("gleaId",gleaId);
        map.put("token",token);

        addSubscription(apiStores.gleaColl(gleaId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitCollectSuccess(commitListModel.getStatus());
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

    public void comGlea(String actId,String token,String text ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("gleaId",actId);
        map.put("token",token);
        map.put("content",text);

        addSubscription(apiBaseStores.comGlea(actId, token,text,AppUtil.getMD5Str(map)),
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

    public void gleaLike(String gleaId,String token ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("gleaId",gleaId);
        map.put("token",token);

        addSubscription(apiStores.gleaLike(gleaId, token,AppUtil.getMD5Str(map)),
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
}
