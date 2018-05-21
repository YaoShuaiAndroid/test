package com.zb.bilateral.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mycommon.util.StatusBarCompat;
import com.zb.bilateral.mvp.BasePresenter;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ys.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    private static int statusBarColor = -1;

    protected ProgressDialog progressDialog;

    protected boolean isStatusCompat = true;

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());

        context = this;

        BaseActivityManager.getInstance().addActivity(this);

        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarTranslucent(this,true);

        this.initToolbar(savedInstanceState);
        this.initViewsAndEvents(savedInstanceState);
        this.initData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
        BaseActivityManager.getInstance().removeActivity(this);
        context = null;
    }


    public void setStatusCompat(boolean statusCompat) {
        isStatusCompat = statusCompat;
    }

    protected abstract int getLayoutId();

    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initToolbar(Bundle savedInstanceState);

    protected <V extends View> V findView(int id) {
        return (V) this.findViewById(id);
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(String message) {
        showProgressDialog(message, true);
    }

    public void showProgressDialog(String message, boolean cancelable) {
        progressDialog = ProgressDialog.show(getContext(), "", message);
        progressDialog.setCancelable(cancelable);
    }

    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void showToast(int messageId) {
        showToast(getString(messageId));
    }

    public Context getContext() {
        return this;
    }
}
