package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.ActivityListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class MyActivityPresenter extends BasePresenter<MyActivityView> {
    Context context;
    public MyActivityPresenter(MyActivityView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void signUpAct(String currentPage,String token,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("currentPage",currentPage);
        map.put("token",token);

        addSubscription(apiStores.signUpAct(token,currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<ActivityListModel>() {
                    @Override
                    public void onSuccess(ActivityListModel appointmentListModel) {
                        mvpView.SendResultSuccess(appointmentListModel);
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
