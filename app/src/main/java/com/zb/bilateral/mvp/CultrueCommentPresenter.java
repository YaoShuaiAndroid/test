package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.SignModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class CultrueCommentPresenter extends BasePresenter<CultrueCommentView> {
    Context context;

    public CultrueCommentPresenter(CultrueCommentView view, Context context) {
        attachView(view);
        this.context = context;
    }

    public void antComs(String id,String currentPage,boolean isLoad) {
        if(isLoad){
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("antId", id);
        map.put("currentPage",currentPage);

        addSubscription(apiBaseStores.antComs(id,currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitListModel>() {
                    @Override
                    public void onSuccess(CommitListModel commitListModel) {
                        mvpView.CommentListSuccess(commitListModel);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
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
        map.put("antiquesId",actId);
        map.put("token",token);
        map.put("content",text);

        addSubscription(apiBaseStores.comAntiques(actId, token,text,AppUtil.getMD5Str(map)),
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
                        mvpView.SendResultFail(msg);
                    }

                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }
                });
    }

}
