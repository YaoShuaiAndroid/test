package com.zb.bilateral.activity.person;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.RegisterPresenter;
import com.zb.bilateral.mvp.RegisterView;
import com.zb.bilateral.util.AppUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseNewActivity<RegisterPresenter> implements RegisterView {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.bind_phone)
    EditText bindPhone;
    @Bind(R.id.bind_code)
    EditText bindCode;
    @Bind(R.id.bind_send_code)
    TextView bindSendCode;

    boolean is_code = true;//当前是否可以获取验证码
    private int code_min = 60;

    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("绑定手机号码");

        token=getIntent().getStringExtra("token");
    }

    @Override
    protected void initData() {
    }

    /**
     * 获取手机验证码
     */
    public void getCode(String phone) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.getBindCode(phone);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     *绑定手机号码
     */
    public void register(String phone,String code) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.bindPhone(phone,code,token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.bind_send_code,R.id.bind_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.bind_send_code://发送验证码
                String phone=bindPhone.getText().toString();
                if(TextUtils.isEmpty(phone)&&!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确的手机号码");
                    return;
                }

                getCode(phone);
                break;
            case R.id.bind_commit://注册
                phone=bindPhone.getText().toString();
                String code=bindCode.getText().toString();

                if(TextUtils.isEmpty(phone)&&!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(code)){
                    AppToast.makeToast(mCctivity,"请输入验证码");
                    return;
                }

                register(phone,code);
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
        PersonModel personModel=new PersonModel();
        personModel.setToken(token);

        AppUtil.save(personModel, FileUtils.configPath(mCctivity));

        Intent intent=new Intent(mCctivity, MainActivity.class);
        startActivity(intent);
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
                            bindSendCode.setText("获取验证码");
                            bindSendCode.setEnabled(true);

                            is_code = true;
                            code_min = 60;

                            hanler.removeMessages(1);
                            hanler.removeMessages(2);
                        } else {
                            bindSendCode.setText(code_min + "s");
                            bindSendCode.setEnabled(false);
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