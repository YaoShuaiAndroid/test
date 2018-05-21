package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AppointActivity extends BaseNewActivity<PublicPresenter<PersonModel>> implements PublicView<PersonModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.role_appoint_text)
    TextView roleAppointText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appoint;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("我的积分");
    }

    @Override
    protected void initData() {
        getData();
    }

    public void getData() {
        String token= AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.myPoint(token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }


    @OnClick({R.id.top_left_img,R.id.appoint_lin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.appoint_lin:
                PublicModel publicModel= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));
                if(publicModel!=null){
                    Intent intent=new Intent(mCctivity, AboutWeActivity.class);
                    intent.putExtra("content",publicModel.getPointRule());
                    intent.putExtra("title","积分规则");
                    startActivity(intent);
                }else{
                   AppToast.makeToast(mCctivity,"暂无积分规则");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void SendResultSuccess(PersonModel personModel) {
        roleAppointText.setText(personModel.getPoint());
    }

    @Override
    public void SendResultFail(String msg) {

    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}
