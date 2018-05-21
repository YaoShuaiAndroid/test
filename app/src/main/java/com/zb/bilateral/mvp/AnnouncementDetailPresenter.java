package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class AnnouncementDetailPresenter extends BasePresenter<AnnouncementDetailView> {
    Context context;

    public AnnouncementDetailPresenter(AnnouncementDetailView view, Context context) {
        attachView(view);
        this.context = context;
    }

    //公告详情
    public void notice(String id, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("id", id);
        map.put("token", token);

        addSubscription(apiBaseStores.notice(id, token, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityModel>() {
                    @Override
                    public void onSuccess(ActivityModel activityModel) {
                        mvpView.SendResultSuccess(activityModel);
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


    public void noticeLike(String noticeId, String token) {
        mvpView.showLoading();

        Map map = new HashMap();
        map.put("noticeId", noticeId);
        map.put("token", token);

        addSubscription(apiStores.noticeLike(noticeId, token, AppUtil.getMD5Str(map)),
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
}
