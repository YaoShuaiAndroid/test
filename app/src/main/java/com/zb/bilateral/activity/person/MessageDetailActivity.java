package com.zb.bilateral.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MessageDetailActivity extends BaseNewActivity<PublicPresenter<MessageModel>> implements PublicView<MessageModel>{
    @Bind(R.id.message_content)
    TextView messageContent;
    private String messageId;
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        messageId=getIntent().getStringExtra("messageId");

        topLeftImg.setBackgroundResource(R.mipmap.left_back);
    }

    @Override
    protected void initData() {
        message();
    }

    /**
     * 获取消息详情
     */
    public void message() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.message(messageId);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected PublicPresenter<MessageModel> createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @Override
    public void SendResultSuccess(MessageModel messageModel) {
        messageContent.setText(messageModel.getContent());
        topCenterText.setText(messageModel.getTitle());
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {

    }
}
