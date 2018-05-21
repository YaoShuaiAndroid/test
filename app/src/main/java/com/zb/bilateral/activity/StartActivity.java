package com.zb.bilateral.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.model.PublicListModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.util.SPUtils;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.BaseModel;
import com.zb.bilateral.mvp.BasePresenter;

import java.util.List;

public class StartActivity extends BaseNewActivity<PublicPresenter<PublicListModel>> implements PublicView<PublicListModel> {
    Runnable runnable;
    @Override
    protected int getLayoutId() {
        //设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        return R.layout.activity_start;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        boolean isFirstRun = (boolean) SPUtils.get(StartActivity.this, "isFirstRun", true);
        PersonModel personModel = (PersonModel) AppUtil.load(FileUtils.configPath(mCctivity));

        if(isFirstRun) {
            startActivity(new Intent(StartActivity.this, GuideActivity.class));
            finish();
        }else if(personModel==null){
            runnable=new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(mCctivity, LoginActivity.class));
                    finish();
                }
            };

            handler.postDelayed(runnable,3000);
        }else{
            runnable=new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(mCctivity, MainActivity.class));
                    finish();
                }
            };

            handler.postDelayed(runnable,3000);
        }
    }

    @Override
    protected void initData() {
        other();
    }

    public void other() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.other(mCctivity);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(PublicListModel publicListModel) {

    }

    @Override
    public void SendResultFail(String msg) {

    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null&&runnable!=null){
            handler.removeCallbacks(runnable);
            handler=null;
        }
    }

    Handler handler=new Handler();
}
