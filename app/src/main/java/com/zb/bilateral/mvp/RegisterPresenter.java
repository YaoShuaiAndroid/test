package com.zb.bilateral.mvp;

import android.content.Context;
import android.content.Intent;

import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.model.QuestionListModel;
import com.zb.bilateral.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yaos on 2017/5/10.
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
    Context context;
    public RegisterPresenter(RegisterView view, Context context) {
        attachView(view);
        this.context=context;
    }

    //获取验证码
    public void getCode(String phone ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);

        addSubscription(apiBaseStores.getCode(phone, AppUtil.getMD5Str(map)),
                new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String string) {
                        mvpView.SendCodeSuccess("获取验证码成功");
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

    //获取验证码
    public void getCode1(String phone ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);

        addSubscription(apiBaseStores.getCode1(phone, AppUtil.getMD5Str(map)),
                new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String string) {
                        mvpView.SendCodeSuccess("获取验证码成功");
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

    //绑定手机号码获取验证码
    public void getBindCode(String phone ) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);

        addSubscription(apiBaseStores.sendCodeForBinding(phone, AppUtil.getMD5Str(map)),
                new ApiCallback<String>() {
                    @Override
                    public void onSuccess(String string) {
                        mvpView.SendCodeSuccess("获取验证码成功");
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

    //注册
    public void register(String phone ,String code,String pwd) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);
        map.put("phoneCode",code);
        map.put("password",pwd);

        addSubscription(apiBaseStores.register(phone,code,pwd, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendSuccess(commitModel);
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

    //绑定手机号码
    public void bindPhone(String phone ,String code,String token) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);
        map.put("phoneCode",code);
        map.put("token",token);

        addSubscription(apiBaseStores.bindPhone(phone,code,token, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendSuccess(commitModel);
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

    //找回密码
    public void validateCode(String phone ,String code,String pwd) {
        mvpView.showLoading();

        Map map=new HashMap();
        map.put("phone",phone);
        map.put("smsgCode",code);
        map.put("newPwd",pwd);

        addSubscription(apiBaseStores.validateCode(phone,code,pwd, AppUtil.getMD5Str(map)),
                new ApiCallback<CommitModel>() {
                    @Override
                    public void onSuccess(CommitModel commitModel) {
                        mvpView.SendSuccess(commitModel);
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
