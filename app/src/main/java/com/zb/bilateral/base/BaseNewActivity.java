package com.zb.bilateral.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.example.mycommon.util.StatusBarCompat;
import com.example.mycommon.view.CustomProgressDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.zb.bilateral.MyApplication;
import com.zb.bilateral.R;
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

public abstract class BaseNewActivity<P extends BasePresenter> extends AppCompatActivity {
    public Context mCctivity;
    private CompositeSubscription mCompositeSubscription;
    private List<Call> calls;

    protected P mvpPresenter;

    private boolean isAutoSize = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());

        mCctivity = this;

        //一定要在 setContentView 之后哈，不然你就等着玩空指针吧
        ButterKnife.bind(this);

        //状态栏兼容处理
        StatusBarCompat.compat((Activity) mCctivity, 1);

        this.initViewsAndEvents(savedInstanceState);

        mvpPresenter = createPresenter();

        this.initData();
    }


    protected abstract int getLayoutId();

    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        callCancel();
        onUnsubscribe();

        ButterKnife.unbind(this);
        mCctivity = null;

        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }

        //监听内存泄漏
        //MyApplication.getRefWatcher().watch(this);

        super.onDestroy();
    }

    public void setAutoSize(boolean autoSize) {
        isAutoSize = autoSize;
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

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showLoading() {
        showProgressDialog();
    }

    public void hideLoading() {
        dismissProgressDialog();
    }

    public void toastShow(String resId) {
        AppToast.makeShortToast(mCctivity, resId);
    }

    public CustomProgressDialog progressDialog;

    public CustomProgressDialog showProgressDialog() {
        progressDialog = new CustomProgressDialog(mCctivity, "加载中");
        progressDialog.show();
        return progressDialog;
    }

    public CustomProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new CustomProgressDialog(mCctivity, "" + message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }

    //    ---------------------------------start 点击空白处隐藏键盘-------------------------------------------------
    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判断是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    ---------------------------------end 点击空白处隐藏键盘-------------------------------------------------


}
