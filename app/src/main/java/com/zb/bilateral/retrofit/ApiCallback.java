package com.zb.bilateral.retrofit;


import com.zb.bilateral.model.BaseModel;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class ApiCallback<M> extends Subscriber<M> {
    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onLogin();

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }else if (code == 502 || code == 404||code == 503) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            if(e.getMessage() == null) {
                onFailure("网络异常");
                return;
            }

            if(e.getMessage().contains("resolve host")){
                onFailure("请检查网络设置");
            }else if(e.getMessage().contains("Failed to connect")){
                onFailure("网络异常，请稍后再试");
            }else if(e.getMessage().contains("timeout")){
                onFailure("加载超时");
            }else{
                onFailure("网络异常");
            }
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        if(model == null) {
            return;
        }

        BaseModel base= (BaseModel) model;

        //判断1为成功,其他未失败
        if("0".equals(base.getCode())){
            M m= (M) base.getData();
            if(base.getData()!=null){
                onSuccess(m);
            }else{
                onSuccess(m);
            }
        }else if("2".equals(base.getCode())){
           //表示未登录
            onLogin();
        }else{
            onFailure(base.getMsg());
        }
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
