package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.model.CommitListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.SignModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class ActivityDetailPresenter extends BasePresenter<ActivityDetailView> {
    Context context;

    public ActivityDetailPresenter(ActivityDetailView view, Context context) {
        attachView(view);
        this.context = context;
    }

    public void act_Detail(String id, String token) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("token", token);

        addSubscription(apiBaseStores.act_Detail(id, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel activityListModel) {
                        mvpView.SendResultSuccess(activityListModel.getAct());
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

    public void infoComs(String infoId, String currentPage, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("infoId", infoId);
        map.put("currentPage", currentPage);

        addSubscription(apiBaseStores.infoComs(infoId, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitListModel>() {
                    @Override
                    public void onSuccess(CommitListModel commitListModel) {
                        mvpView.CommentListSuccess(commitListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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


    public void actComs(String actId, String currentPage, boolean is_load) {
        if (is_load) {
            mvpView.showLoading();
        }

        Map map = new HashMap();
        map.put("actId", actId);
        map.put("currentPage", currentPage);

        addSubscription(apiBaseStores.actComs(actId, currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitListModel>() {
                    @Override
                    public void onSuccess(CommitListModel commitListModel) {
                        mvpView.CommentListSuccess(commitListModel);
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    //资讯详情
    public void info_Detail(String id, String token) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("token", token);

        addSubscription(apiBaseStores.info_Detail(id, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel activityModel) {
                        mvpView.SendResultSuccess(activityModel.getInfo());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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


    public void actLike(String actId, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("actId", actId);
        map.put("token", token);

        addSubscription(apiBaseStores.actLike(actId, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitLikeSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    public void infoLike(String infoId, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("infoId", infoId);
        map.put("token", token);

        addSubscription(apiBaseStores.infoLike(infoId, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitLikeSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    public void delAct(String actId, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("actId", actId);
        map.put("token", token);

        addSubscription(apiBaseStores.delAct(actId, token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.CancelSignSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    public void comAct(String actId, String token, String text) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("actId", actId);
        map.put("token", token);
        map.put("content", text);

        addSubscription(apiBaseStores.comAct(actId, token, text, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitCommitSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    public void comInfo(String actId, String token, String text) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("infoId", actId);
        map.put("token", token);
        map.put("content", text);

        addSubscription(apiBaseStores.comInfo(actId, token, text, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitCommitSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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


    public void signUpAct(String actId, String token, SignModel signModel) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("actId", actId);
        map.put("token", token);
        map.put("name", signModel.getName());
        map.put("phone", signModel.getPhone());
        map.put("number", signModel.getNumber());
        map.put("other", signModel.getOther());
        map.put("is_self", signModel.getIs_self());

        addSubscription(apiBaseStores.signUpAct(actId, token, signModel.getName(), signModel.getPhone(), signModel.getNumber(),
                signModel.getIs_self(), signModel.getOther(), AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitListModel) {
                        mvpView.SubmitSignSuccess(commitListModel.getStatus());
                    }

                    @Override
                    public void onLogin() {
                        Intent intent = new Intent(context, LoginActivity.class);
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

    public void actCollCommit(String gleaId,String token ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("actId",gleaId);
        map.put("token",token);

        addSubscription(apiStores.actCollCommit(gleaId, token,AppUtil.getMD5Str(map)),
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
