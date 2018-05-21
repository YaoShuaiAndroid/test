package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class EditPwdActivity extends BaseNewActivity<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.old_pwd)
    EditText old_pwd;
    @Bind(R.id.confirm_pwd)
    EditText confirm_pwd;
    @Bind(R.id.new_pwd)
    EditText new_pwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_pwd;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("修改密码");
    }

    @Override
    protected void initData() {

    }

    public void register(String new_pwd,String old_pwd) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.updatePassword(new_pwd,old_pwd,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.edit_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.edit_commit:
                String oldPwd=old_pwd.getText().toString();
                String newPwd=new_pwd.getText().toString();
                String confirmPwd=confirm_pwd.getText().toString();

                if(TextUtils.isEmpty(oldPwd)){
                    AppToast.makeToast(mCctivity,"请输入原始密码");
                    return;
                }

                if(TextUtils.isEmpty(newPwd)||newPwd.length()<8){
                    AppToast.makeToast(mCctivity,"请输入8位以上新密码");
                    return;
                }

                if(!confirmPwd.equals(newPwd)){
                    AppToast.makeToast(mCctivity,"确认密码与新密码不一致");
                    return;
                }

                register(newPwd,oldPwd);
                break;
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"修改密码成功");
        finish();
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }
}
