package com.zb.bilateral.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zb.bilateral.activity.person.BindPhoneActivity;
import com.zb.bilateral.mvp.LoginPresenter;
import com.zb.bilateral.mvp.LoginView;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.util.FileUtils;
import com.zb.bilateral.MainActivity;
import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.CommitModel;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseNewActivity<LoginPresenter> implements LoginView {
    @Bind(R.id.login_phone)
    EditText loginPhone;
    @Bind(R.id.login_pwd)
    EditText loginPwd;

    private UMShareAPI umShareAPI;

    @Override
    protected int getLayoutId(){
        //设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        umShareAPI=UMShareAPI.get(mCctivity);
    }

    @Override
    protected void initData() {
    }

    /**
     * 登录
     */
    public void main(String phone,String pwd) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.main(phone,pwd);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }

    /**
     * 三方登录
     */
    public void thirdPartyLogin(String openId,String name,String photo,String type) {
        if (AppUtil.checkNetWork(mCctivity)) {
            mvpPresenter.thirdPartyLogin(openId,name,photo,type);
        } else {
            AppToast.makeToast(mCctivity, "网络异常");
        }
    }


    @OnClick({R.id.login_commit,R.id.login_registered,R.id.login_findpwd,R.id.login_wx_lin,R.id.login_qq_lin,
    R.id.login_youke})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_commit:
                String phone=loginPhone.getText().toString();
                String pwd=loginPwd.getText().toString();

                if(!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(mCctivity,"请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(pwd)||pwd.length()<8){
                    AppToast.makeToast(mCctivity,"请输入8位数以上密码");
                    return;
                }

                main(phone,pwd);
                break;
            case R.id.login_registered:
                Intent intent=new Intent(mCctivity, RegisteredActivity.class);
                startActivity(intent);
                break;
            case R.id.login_findpwd:
                intent=new Intent(mCctivity, FindPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.login_wx_lin:
                //微信登录
                umShareAPI.doOauthVerify((Activity) mCctivity, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.login_qq_lin:
                //QQ登录
                umShareAPI.doOauthVerify((Activity) mCctivity, SHARE_MEDIA.QQ, authListener);
                break;
            case R.id.login_youke:
                //游客登录
                PersonModel personModel=new PersonModel();
                personModel.setToken(ApiStores.TOURIST_TOKEN);
                LogUtil.i(personModel.getToken()+";"+ApiStores.TOURIST_TOKEN);
                AppUtil.save(personModel,FileUtils.configPath(mCctivity));

                intent=new Intent(mCctivity, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this,mCctivity);
    }


    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                umShareAPI.getPlatformInfo(LoginActivity.this, platform, userinfoListener);
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        //微博返回--》{urank=10, screen_name=秋冬美韵, story_read_state=-1, mbrank=0, weihao=, province=44, statuses_count=26824, following=false, class=1, follow_me=false, verified_type=-1, id=3237207062, iconurl=http://tva1.sinaimg.cn/crop.0.0.180.180.50/c0f3dc16jw1e8qgp5bmzyj2050050aa8.jpg, verified_reason_url=, accessToken=2.00I8zEXDijIjxCc3ebe6b250xvZjuC, domain=, avatar_hd=http://tva1.sinaimg.cn/crop.0.0.180.180.1024/c0f3dc16jw1e8qgp5bmzyj2050050aa8.jpg, friends_count=1643, bi_followers_count=99, location=广东 深圳, verified_source=, name=秋冬美韵, pagefriends_count=0, verified_reason=, mbtype=0, verified_source_url=, insecurity={"sexual_content":false}, remark=, url=, city=3, refreshToken=null, gender=男, block_app=0, ptype=0, block_word=0, star=0, status={"created_at":"Mon Sep 04 10:28:46 +0800 2017","id":4148202993174779,"mid":"4148202993174779","idstr":"4148202993174779","text":"周期天数：21天，今天第1天\n月经情况：月经不痛,出血量正常\n基础体温：20.00\n身体症状：1123\n检测项目：暂无\n检测日期：\n检测时间：暂无\n检测结果：暂无","textLength":142,"source_allowclick":0,"source_type":1,"source":"<a href=\"http:\/\/app.weibo.com\/t\/feed\/4nkfWi\" rel=\"nofollow\">未通过审核应用<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"is_vip_paid_status":false,"is_paid":false,"mblog_vip_type":0,"reposts_count":0,"comments_count":0,"attitudes_count":0,"isLongText":false,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"hasActionTypeCard":0,"darwin_tags":[],"hot_weibo_tags":[],"text_tag_tips":[],"userType":0,"more_info_type":0,"positive_recom_flag":0,"gif_ids":"","is_show_bulletin":2,"comment_manage_info":{"comment_permission_type":-1}}, verified=false, allow_all_act_msg=false, lang=zh-cn, expiration=1504638757890, idstr=3237207062, vclub_member=0, profile_url=u/3237207062, credit_score=80, avatar_large=http://tva1.sinaimg.cn/crop.0.0.180.180.180/c0f3dc16jw1e8qgp5bmzyj2050050aa8.jpg, user_ability=0, online_status=0, geo_enabled=true, followers_count=997, profile_image_url=http://tva1.sinaimg.cn/crop.0.0.180.180.50/c0f3dc16jw1e8qgp5bmzyj2050050aa8.jpg, description=, access_token=2.00I8zEXDijIjxCc3ebe6b250xvZjuC, verified_trade=, uid=3237207062, created_at=Mon Mar 25 21:08:36 +0800 2013, favourites_count=0, allow_all_comment=true, expires_in=1504638757890}

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mCctivity, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mCctivity, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    private String openid;
    //获取用户信息
    private UMAuthListener userinfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                if(platform.equals(SHARE_MEDIA.WEIXIN)){
                    openid=data.get("openid");
                    mvpPresenter.thirdPartyLogin(data.get("openid"),data.get("screen_name"),data.get("iconurl"),"1");
                }else if(platform.equals(SHARE_MEDIA.QQ)){
                    openid=data.get("openid");
                    mvpPresenter.thirdPartyLogin(data.get("openid"),data.get("screen_name"),data.get("iconurl"),"0");
                }
            }
        }
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "操作失败1", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "操作取消", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void sendResultSuccess(CommitModel commitModel) {
        PersonModel personModel=new PersonModel();
        personModel.setToken(commitModel.getToken());

        AppUtil.save(personModel,FileUtils.configPath(mCctivity));

        Intent intent=new Intent(mCctivity, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void sendThirdResultSuccess(CommitModel commitModel) {
        String token=commitModel.getToken();

        if(TextUtils.isEmpty(token)){
            AppToast.makeToast(mCctivity,"数据异常");
        }

        if(commitModel.getStatus()!=null&&commitModel.getStatus().equals("1")){
            //(0：未绑定手机，1：已绑定手机)
            PersonModel personModel=new PersonModel();
            personModel.setToken(commitModel.getToken());

            AppUtil.save(personModel,FileUtils.configPath(mCctivity));

            Intent intent=new Intent(mCctivity, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent=new Intent(mCctivity, BindPhoneActivity.class);
            intent.putExtra("token",token);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void sendResultFail(String msg) {
        AppToast.makeToast(mCctivity,msg);
    }
}
