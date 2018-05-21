package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.CollectListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class TreasureDetailPresenter extends BasePresenter<TreasureDetailView> {
    Context context;
    public TreasureDetailPresenter(TreasureDetailView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void treasure_Detail(String treaId,String token) {
        Map map=new HashMap();
        map.put("treaId",treaId);
        map.put("token",token);

        addSubscription(apiStores.treasure_Detail(treaId, token,AppUtil.getMD5Str(map)),
                new ApiCallback<CollectListModel>() {
                    @Override
                    public void onSuccess(CollectListModel collectListModel) {
                       mvpView.SendResultSuccess(collectListModel.getTrea());
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

    public void treaComs(String treaId,String currentPage,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("treaId",treaId);
        map.put("currentPage",currentPage);

        addSubscription(apiStores.treaComs(treaId, currentPage,AppUtil.getMD5Str(map)),
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

    public void comTrea(String treaId,String token,String content ) {
            mvpView.showLoading();

        Map map=new HashMap();
        map.put("treaId",treaId);
        map.put("content",content);
        map.put("token",token);

        addSubscription(apiStores.comTrea(treaId, content,token,AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SubmitCommitSuccess("");
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

    public void gleaColl(String treaId,String token ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("treaId",treaId);
        map.put("token",token);

        addSubscription(apiStores.treaColl(treaId, token,AppUtil.getMD5Str(map)),
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

}
