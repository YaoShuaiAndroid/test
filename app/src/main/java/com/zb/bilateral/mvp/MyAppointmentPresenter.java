package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class MyAppointmentPresenter extends BasePresenter<MyAppointmentView> {
    Context context;
    public MyAppointmentPresenter(MyAppointmentView view, Context context) {
        attachView(view);
        this.context=context;
    }

    public void myCommentary(String currentPage,String token,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("currentPage",currentPage);
        map.put("token",token);

        addSubscription(apiStores.myCommentary(token,currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<AppointmentListModel>() {
                    @Override
                    public void onSuccess(AppointmentListModel appointmentListModel) {
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
    //消息接口
    public void messages(String currentPage,String token,boolean is_load) {
        if(is_load){
            mvpView.showLoading();
        }

        Map map=new HashMap();
        map.put("currentPage",currentPage);
        map.put("token",token);

        addSubscription(apiStores.messages(token,currentPage, AppUtil.getMD5Str(map)),
                new ApiCallback<AppointmentListModel>() {
                    @Override
                    public void onSuccess(AppointmentListModel appointmentListModel) {
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

    public void delComm(String commId,String token ) {
            mvpView.showLoading();

        Map map=new HashMap();
        map.put("commId",commId);
        map.put("token",token);

        addSubscription(apiStores.delComm(token,commId, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.submitSuccess(commitModel);
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
