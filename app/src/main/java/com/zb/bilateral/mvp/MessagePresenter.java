package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.AppointmentListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.model.MyMessageModel;
import com.zb.bilateral.retrofit.ApiCallback;
import com.zb.bilateral.util.AppUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class MessagePresenter extends BasePresenter<MessageView> {
    Context context;
    public MessagePresenter(MessageView view, Context context) {
        attachView(view);
        this.context=context;
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
                new ApiCallback<MyMessageModel>() {
                    @Override
                    public void onSuccess(MyMessageModel messageModel) {
                        mvpView.SendResultSuccess(messageModel);
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

    //删除消息接口
    public void delMessages(String messageId,String token ) {
            mvpView.showLoading();

        Map map=new HashMap();
        map.put("messageId",messageId);
        map.put("token",token);

        addSubscription(apiStores.delMessages(token,messageId, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.delRsultSuccess("删除成功");
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
