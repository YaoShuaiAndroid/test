package com.zb.bilateral.activity.person;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.view.GlideRoundImage;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.ShareDetailModel;
import com.zb.bilateral.model.ShareModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RecommendedActivity extends BaseNewActivity<PublicPresenter<ShareDetailModel>> implements PublicView<ShareDetailModel>{
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.recommend_img)
    ImageView recommendImg;
    @Bind(R.id.recommend_content)
    TextView recommendContent;
    @Bind(R.id.recommend_title)
    TextView recommendTitle;

    private String content;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommended;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.policy_share);
        topCenterText.setText("邀请好友");
    }

    @Override
    protected void initData() {
        share();
    }


    /**
     * 分享
     */
    public void share() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.share();
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    @OnClick({R.id.top_left_rel,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                finish();
                break;
            case R.id.top_right_rel:
                //分享
                if(shareModel==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }


                 content=shareModel.getContent();
                //防止QQ太长无法分享
                if(!TextUtils.isEmpty(content)&& content.length()>30){
                    content=""+content.substring(0,29);
                }

                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else{
                    UMWeb web = new UMWeb(shareModel.getUrl());
                    web.setTitle(shareModel.getTitle());//标题
                    UMImage image = new UMImage(mCctivity,"http://www.zb0933.com/zbjj/userfiles/1/images/logo/logo.png");//网络图片
                    web.setThumb(image);
                    web.setDescription(content);//描述

                    new ShareAction((Activity) mCctivity)
                            .withMedia(web)
                            .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,
                                    SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(shareListener).open();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        UMWeb web = new UMWeb(shareModel.getUrl());
        web.setTitle(shareModel.getTitle());//标题
        UMImage image = new UMImage(mCctivity,"http://www.zb0933.com/zbjj/userfiles/1/images/logo/logo.png");//网络图片
        web.setThumb(image);
        web.setDescription(content);//描述

        new ShareAction((Activity) mCctivity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(shareListener).open();
    }

    ShareModel shareModel=null;

    @Override
    public void SendResultSuccess(ShareDetailModel shareDetailModel) {
        recommendTitle.setText(shareDetailModel.getShare().getTitle());
        recommendContent.setText(shareDetailModel.getShare().getContent());

        shareModel=shareDetailModel.getShare();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mCctivity));

        Glide.with(mCctivity)
                .load(shareDetailModel.getShare().getPicture())
                .apply(options)
                .into(recommendImg);
    }

    @Override
    public void SendResultFail(String msg) {

    }

    @Override
    public void SendBannerSuccess(List list) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            AppToast.makeToast(mCctivity,"分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            AppToast.makeToast(mCctivity,"分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AppToast.makeToast(mCctivity,"您取消了分享");
        }
    };

}
