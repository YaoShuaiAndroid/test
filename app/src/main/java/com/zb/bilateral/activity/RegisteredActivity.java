package com.zb.bilateral.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.mvp.RegisterPresenter;
import com.zb.bilateral.mvp.RegisterView;

import butterknife.Bind;
import butterknife.OnClick;

public class RegisteredActivity extends BaseNewActivity<RegisterPresenter> implements RegisterView {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.register_phone)
    EditText register_phone;
    @Bind(R.id.register_code)
    EditText register_code;
    @Bind(R.id.register_confirm_pwd)
    EditText register_confirm_pwd;
    @Bind(R.id.register_pwd)
    EditText register_pwd;
    @Bind(R.id.register_send_code)
    TextView register_send_code;
    //当前是否可以获取验证码
    boolean is_code = true;
    private int code_min = 60;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registered;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("注册");
    }

    @Override
    protected void initData() {
    }

    /**
     * 获取手机验证码
     */
    public void getCode(String phone) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.getCode(phone);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     *注册
     */
    public void register(String phone,String code ,String pwd) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.register(phone,code,pwd);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.register_send_code,R.id.register_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.register_send_code://发送验证码
                String phone=register_phone.getText().toString();
                if(TextUtils.isEmpty(phone)&&!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确的手机号码");
                    return;
                }

                getCode(phone);
                break;
            case R.id.register_commit://注册
                 phone=register_phone.getText().toString();
                String code=register_code.getText().toString();
                String pwd=register_pwd.getText().toString();
                String confirm_pwd=register_confirm_pwd.getText().toString();

                if(TextUtils.isEmpty(phone)&&!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(code)){
                    AppToast.makeToast(mCctivity,"请输入验证码");
                    return;
                }

                if(TextUtils.isEmpty(pwd)||pwd.length()<8){
                    AppToast.makeToast(mCctivity,"请输入8位数以上密码!");
                    return;
                }

                if(!pwd.equals(confirm_pwd)){
                    AppToast.makeToast(mCctivity,"密码与确认密码不一致");
                    return;
                }

                register(phone,code,pwd);
                break;
        }
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this,mCctivity);
    }

    @Override
    public void SendCodeSuccess(String code) {
        hanler.sendEmptyMessage(2);
    }

    @Override
    public void SendSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"注册成功");
        finish();
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hanler.removeMessages(1);
        hanler.removeMessages(2);
    }

    Handler hanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //读完秒数后恢复获取验证码功能
                    try {
                        if (code_min == 0) {
                            register_send_code.setText("获取验证码");
                            register_send_code.setEnabled(true);

                            is_code = true;
                            code_min = 60;

                            hanler.removeMessages(1);
                            hanler.removeMessages(2);
                        } else {
                            register_send_code.setText(code_min + "s");
                            register_send_code.setEnabled(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    if (is_code) {
                        code_min--;
                        hanler.sendEmptyMessage(1);
                        hanler.sendEmptyMessageDelayed(2, 1000);
                    }
                    break;
            }
        }
    };
}
