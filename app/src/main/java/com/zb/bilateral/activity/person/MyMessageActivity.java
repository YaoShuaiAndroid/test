package com.zb.bilateral.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.model.MyMessageModel;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.PlatformAndMuseumDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyMessageActivity extends BaseNewActivity<PublicPresenter<CommitModel>> implements PublicView<CommitModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_text)
    TextView topRightText;
    @Bind(R.id.message_content)
    EditText messageContent;
    @Bind(R.id.message_name)
    EditText messageName;
    @Bind(R.id.message_phone)
    EditText messagePhone;
    @Bind(R.id.mymuseum_text)
    TextView mymuseumText;
    @Bind(R.id.message_lin)
    LinearLayout messageLin;

    private String museum_id;

    private static final int SELECT_PLATFORM=1;
    private static final int SELECT_MUSUEM=2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topCenterText.setText("用户留言");
        topRightText.setText("查看留言");
    }

    @Override
    protected void initData() {

    }

    /**
     * 上传留言
     * @param content    留言内容
     * @param name  姓名
     * @param phone 电话
     */
    public void leaveWord(String content,String name,String phone) {
        String token=AppUtil.getToken(mCctivity);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        String museumText=mymuseumText.getText().toString();
        if (AppUtil.checkNetWork(mCctivity)) {
            if(TextUtils.isEmpty(museum_id)){
                if("平台".equals(museumText)){
                    mvpPresenter.leaveWord(content,name,phone,token,"0","");
                }else{
                    AppToast.makeToast(mCctivity,"请选择需要上传的位置");
                }
            }else{
                mvpPresenter.leaveWord(content,name,phone,token,"1",museum_id);
            }

        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000&&resultCode==100){
             museum_id=data.getStringExtra("museum_id");
            String name=data.getStringExtra("museum_name");

            mymuseumText.setText(name);
        }
    }

    @OnClick({R.id.top_left_img,R.id.top_right_rel,R.id.message_commit,R.id.museum_select_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel://查看留言
                Intent intent=new Intent(mCctivity,LookMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.message_commit:
                String content=messageContent.getText().toString();
                String name=messageName.getText().toString();
                String phone=messagePhone.getText().toString();

                if(TextUtils.isEmpty(content)){
                    AppToast.makeToast(mCctivity,"请输入留言内容");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    AppToast.makeToast(mCctivity,"请输入您的姓名");
                    return;
                }

                if(!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确联系方式");
                    return;
                }

                leaveWord(content,name,phone);
                break;
            case R.id.museum_select_rel://选择博物馆
                new PlatformAndMuseumDialog((Activity) mCctivity,messageLin,handler);
                break;
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(CommitModel commitModel) {
        AppToast.makeToast(mCctivity,"留言成功");
        finish();
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SELECT_MUSUEM:
                    Intent intent1=new Intent(mCctivity,MyseumListActivity.class);
                    startActivityForResult(intent1,1000);
                    break;
                case SELECT_PLATFORM:
                    mymuseumText.setText("平台");
                    break;
            }
        }
    };
}
