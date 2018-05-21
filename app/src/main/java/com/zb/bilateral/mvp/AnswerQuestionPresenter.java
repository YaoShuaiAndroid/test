package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.AnsweiListModel;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.QuestionListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class AnswerQuestionPresenter extends BasePresenter<AnswerQuestionView> {
    Context context;
    public AnswerQuestionPresenter(AnswerQuestionView view, Context context) {
        attachView(view);
        this.context=context;
    }

    //答题-获取题目
    public void getAnswer(String museumId,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("museumId",museumId);
        map.put("token",token);

        addSubscription(apiBaseStores.getAnswer(museumId,token, AppUtil.getMD5Str(map)),
                new ApiCallback<AnsweiListModel>() {
                    @Override
                    public void onSuccess(AnsweiListModel answeiListModel) {
                        mvpView.SendResultSuccess(answeiListModel);
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

    //提交答案
    public void subAnswer(String answerList,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("answerList",answerList);
        map.put("token",token);

        addSubscription(apiBaseStores.subAnswer(answerList,token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SubmitResultSuccess(commitModel);
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
