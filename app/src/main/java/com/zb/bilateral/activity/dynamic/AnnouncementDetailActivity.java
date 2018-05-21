package com.zb.bilateral.activity.dynamic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.mvp.AnnouncementDetailPresenter;
import com.zb.bilateral.mvp.AnnouncementDetailView;

import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.ShareUtil;
import com.zb.bilateral.view.PromptLoginDialog;

public class AnnouncementDetailActivity
        extends BaseNewActivity<AnnouncementDetailPresenter>
        implements AnnouncementDetailView {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.announcement_title)
    TextView announcementTitle;
    @Bind(R.id.announcement_time)
    TextView announcementTime;
    @Bind(R.id.announcement_content)
    WebView announcementContent;
    @Bind(R.id.announcement_like_lin)
    LinearLayout announcementLikeLin;
    @Bind(R.id.announcement_like_img)
    ImageView announcementLikeImg;
    @Bind(R.id.announcement_like_num)
    TextView announcementLikeNum;

    private String activityId;
    private String token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_announcement_detail;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        activityId = getIntent().getStringExtra("activity_id");

        token = AppUtil.getToken(mCctivity);

        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.policy_share);
        topCenterText.setText("公告详情");
    }

    @Override
    protected void initData() {
        notice();
    }


    /**
     * 公告详情
     */
    public void notice() {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.notice(activityId, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 文物拾遗点赞/取消赞
     */
    public void noticeLike() {
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mCctivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.noticeLike(activityId, token);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img, R.id.announcement_like_lin,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.announcement_like_lin:
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mCctivity,topCenterText);
                    return;
                }

                noticeLike();

                announcementLikeLin.setClickable(false);
                break;
            case R.id.top_right_rel:
                //分享
                 publicMode= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

                if(activityModel==null||publicMode==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }

                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else {
                    new ShareUtil().share(activityModel.getTitle(), activityModel.getContent(),
                            publicMode, mCctivity);
                }
                break;
            default:
                break;
        }
    }

    PublicModel publicMode=null;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        new ShareUtil().share(activityModel.getTitle(), activityModel.getContent(),
                publicMode, mCctivity);
    }


    @Override
    protected AnnouncementDetailPresenter createPresenter() {
        return new AnnouncementDetailPresenter(this, mCctivity);
    }

    ActivityModel activityModel;

    @Override
    public void SendResultSuccess(ActivityModel activity) {
        activityModel = activity;

        announcementLikeNum.setText("" + activity.getLikeCount());
        announcementContent.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,
                AppUtil.css(activity.getContent()), "text/html", "utf-8", null);
        announcementTitle.setText(activity.getTitle());
        announcementTime.setText(AppUtil.getStrTime(activity.getCreateDate()));
        if ("1".equals(activity.getIsLike())) {
            announcementLikeImg.setBackgroundResource(R.mipmap.cultrue_dianzan);
        } else {
            announcementLikeImg.setBackgroundResource(R.mipmap.like_false);
        }
    }

    @Override
    public void SubmitLikeSuccess(String result) {
        if ("1".equals(result)) {
            announcementLikeImg.setBackgroundResource(R.mipmap.cultrue_dianzan);

            activityModel.setLikeCount(activityModel.getLikeCount() + 1);

            announcementLikeNum.setText("" + activityModel.getLikeCount());
        } else {
            announcementLikeImg.setBackgroundResource(R.mipmap.like_false);

            activityModel.setLikeCount(activityModel.getLikeCount() - 1);

            announcementLikeNum.setText("" + activityModel.getLikeCount());
        }

        announcementLikeLin.setClickable(true);
    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    public void SubmitResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);

        announcementLikeLin.setClickable(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
