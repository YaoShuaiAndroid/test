package com.zb.bilateral.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.view.CustomProgressDialog;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.retrofit.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseNewFragment<P extends BasePresenter> extends Fragment {
    protected BaseNewActivity activity;

    protected View rootView;

    private List<Call> calls;

    protected Context mContext;

    protected P mvpPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (this.rootView == null) {
            this.rootView = inflater.inflate(this.getLayoutId(), container, false);
        }

        mContext = getActivity();
        ButterKnife.bind(this,rootView);

        this.initViewsAndEvents(this.rootView, savedInstanceState);

        mvpPresenter = createPresenter();

        this.initData();

        return this.rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (BaseNewActivity) context;
    }

    protected abstract int getLayoutId();

    protected abstract void initViewsAndEvents(View self, Bundle savedInstanceState);

    /**
     * Initialize the Activity data
     */
    protected abstract void initData();

    protected abstract P createPresenter();

    public void toastShow(String resId) {
        AppToast.makeShortToast(mContext,resId);
    }

    public CustomProgressDialog progressDialog;

    public CustomProgressDialog showProgressDialog() {
        progressDialog = new CustomProgressDialog(mContext,"加载中");
        progressDialog.show();
        return progressDialog;
    }

    public CustomProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new CustomProgressDialog(mContext,""+message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        callCancel();
        onUnsubscribe();

        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }

        //监听内存泄漏
        //MyApplication.getRefWatcher().watch(this);
    }

    public void showLoading() {
        showProgressDialog();
    }

    public void hideLoading() {
        dismissProgressDialog();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null&& mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
                .subscribe(subscriber));
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void addCalls(Call call) {
        if (calls == null) {
            calls = new ArrayList<>();
        }
        calls.add(call);
    }

    private void callCancel() {
        if (calls != null && calls.size() > 0) {
            for (Call call : calls) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
            }
            calls.clear();
        }
    }

}
