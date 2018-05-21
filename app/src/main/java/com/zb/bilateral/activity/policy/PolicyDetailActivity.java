package com.zb.bilateral.activity.policy;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.umeng.socialize.UMShareAPI;
import com.zb.bilateral.model.PublicModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.PolicyListModel;
import com.zb.bilateral.model.PolicyModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.util.ShareUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
/*
* 政策法规详情
* */
public class PolicyDetailActivity extends BaseNewActivity<PublicPresenter<PolicyListModel>>  implements PublicView<PolicyListModel> {
    @Bind(R.id.top_left_img)
    ImageView topLeftImg;
    @Bind(R.id.top_center_text)
    TextView topCenterText;
    @Bind(R.id.top_right_img)
    ImageView topRightImg;
    @Bind(R.id.policy_detail_title)
    TextView policyDetailTtile;
    @Bind(R.id.policy_detail_date)
    TextView policyDetailDate;
    @Bind(R.id.policy_detail_digest)
    TextView policyDetailDigest;
    @Bind(R.id.policy_detail_content)
    WebView policyDetailContent;
    @Bind(R.id.policy_detail_lin)
    LinearLayout policyDetailLin;

    private String policy_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_policy_detail;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        topLeftImg.setBackgroundResource(R.mipmap.left_back);
        topRightImg.setBackgroundResource(R.mipmap.policy_share);
        topCenterText.setText("政策法规");


        policy_id=getIntent().getStringExtra("policy_id");
    }

    @Override
    protected void initData() {
        policy();
    }

    /**
     * 政策法规列表
     */
    public void policy() {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.policy(policy_id);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    @OnClick({R.id.top_left_img,R.id.top_right_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                finish();
                break;
            case R.id.top_right_rel:
                //分享
                PublicModel publicMode= (PublicModel) AppUtil.load(FileUtils.publicPath(mCctivity));

                if(policyModel1==null||publicMode==null){
                    AppToast.makeToast(mCctivity,"数据异常");
                    return;
                }

                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                    ActivityCompat.requestPermissions(this,mPermissionList,123);
                }else {
                    new ShareUtil().share(policyModel1.getTitle(), policyModel1.getContent(),
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
        new ShareUtil().share(policyModel1.getTitle(), policyModel1.getContent(),
                publicMode, mCctivity);
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mCctivity);
    }

    PolicyModel policyModel1=null;
    @Override
    public void SendResultSuccess(PolicyListModel policyListModel) {
         policyModel1=policyListModel.getPolicy();

        policyDetailLin.setVisibility(View.VISIBLE);
        policyDetailContent.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,
                AppUtil.css(policyModel1.getContent()), "text/html", "utf-8", null);
        policyDetailDigest.setText(policyModel1.getDigest());
        policyDetailTtile.setText(policyModel1.getTitle());
        policyDetailDate.setText(AppUtil.getStrTime(policyModel1.getCreateDate()));
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mCctivity, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
