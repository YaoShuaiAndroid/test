package com.zb.bilateral.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.FileUtils;
import com.example.mycommon.view.GlideCircleTransform;
import com.example.mycommon.view.GlideRoundImage;
import com.zb.bilateral.activity.person.MessageActivity;
import com.zb.bilateral.activity.person.PersonMessageActivity;
import com.zb.bilateral.activity.person.RecommendedActivity;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.util.StatusBarCompat;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.person.AppointActivity;
import com.zb.bilateral.activity.person.MuseumEentranceActivity;
import com.zb.bilateral.activity.person.MyActivityActivity;
import com.zb.bilateral.activity.person.MyAppointmentActivity;
import com.zb.bilateral.activity.person.MyMessageActivity;
import com.zb.bilateral.activity.person.SetActivity;
import com.zb.bilateral.activity.person.collect.MyCollectActivity;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.PersonModel;
import com.zb.bilateral.mvp.PublicPresenter;
import com.zb.bilateral.mvp.PublicView;
import com.zb.bilateral.view.PromptLoginDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class PersonFragment extends BaseNewFragment<PublicPresenter<PersonModel>> implements PublicView<PersonModel>{
    @Bind(R.id.person_appoint_text)
    TextView personAppointText;
    @Bind(R.id.person_name)
    TextView personName;
    @Bind(R.id.person_img)
    ImageView personImg;

    private PersonModel personModel;
    private String token;

    @Override
    protected int getLayoutId() {
        StatusBarCompat.compat((Activity) getActivity(),2);
        return R.layout.activity_person_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
         token=AppUtil.getToken(mContext);
    }

    @Override
    protected void initData() {
    }

    public void getData() {
        String token=AppUtil.getToken(mContext);
        if(TextUtils.isEmpty(token)){
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mContext)) {
            mvpPresenter.main(token);
        } else {
            AppToast.makeToast(mContext, "网络异常");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        personModel= (PersonModel) AppUtil.load(FileUtils.configPath(mContext));
        if(personModel!=null&&!TextUtils.isEmpty(personModel.getName())){
            if(personModel.getName()!=null){
                personName.setText(personModel.getName());
            }
            if(personModel.getPoint()!=null){
                personAppointText.setText(personModel.getPoint());
            }

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.no_img)
                    .transform(new GlideCircleTransform(mContext));

            Glide.with(mContext)
                    .load(personModel.getPhoto().contains("http")?""+personModel.getPhoto():ApiStores.IMG_URL_TOP + personModel.getPhoto())
                    .apply(options)
                    .into(personImg);
        }else{
            getData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        personModel= (PersonModel) AppUtil.load(FileUtils.configPath(mContext));
        if(personModel!=null){
            if(personModel.getName()!=null){
                personName.setText(personModel.getName());
            }

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.no_img)
                    .transform(new GlideCircleTransform(mContext));

            Glide.with(mContext)
                    .load(personModel.getPhoto().contains("http")?""+personModel.getPhoto():ApiStores.IMG_URL_TOP + personModel.getPhoto())
                    .apply(options)
                    .into(personImg);
        }
    }

    @Override
    protected PublicPresenter createPresenter() {
        return new PublicPresenter(this,mContext);
    }

    @OnClick({R.id.person_acitvity_rel,R.id.person_yuyue_rel,R.id.person_message_rel,R.id.person_set_rel,R.id.person_appoint_lin,
    R.id.person_entrance,R.id.person_museum_lin,R.id.person_collect_lin,R.id.person_activity_lin,R.id.person_cultrue_lin,
    R.id.person_message1_rel,R.id.person_img,R.id.person_shard_rel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_acitvity_rel://我的活动
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                Intent intent=new Intent(mContext, MyActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.person_yuyue_rel://我的预约讲解
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                 intent=new Intent(mContext, MyAppointmentActivity.class);
                startActivity(intent);
                break;
            case R.id.person_message_rel:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //用户留言
                intent=new Intent(mContext, MyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.person_set_rel:
                //设置
                if(TextUtils.isEmpty(token)){
                     intent=new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                intent=new Intent(mContext, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.person_entrance:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //新馆入住
                intent=new Intent(mContext, MuseumEentranceActivity.class);
                startActivity(intent);
                break;
            case R.id.person_museum_lin:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //我的收藏-博物馆
                Bundle bundle=new Bundle();
                bundle.putInt("item",0);

                intent=new Intent(mContext, MyCollectActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.person_collect_lin:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //我的收藏-馆藏
                 bundle=new Bundle();
                bundle.putInt("item",1);

                intent=new Intent(mContext, MyCollectActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.person_activity_lin:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //我的收藏-活动
                bundle=new Bundle();
                bundle.putInt("item",2);

                intent=new Intent(mContext, MyCollectActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.person_cultrue_lin:
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }
                //我的收藏-文物拾遗
                bundle=new Bundle();
                bundle.putInt("item",3);

                intent=new Intent(mContext, MyCollectActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.person_appoint_lin:
                //我的积分
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }

                intent=new Intent(mContext, AppointActivity.class);
                startActivity(intent);
                break;
            case R.id.person_message1_rel:
                //消息
                token=AppUtil.getToken(mContext);
                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }

                intent=new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.person_img:
                //跳转个人资料
                 token=AppUtil.getToken(mContext);
                if(TextUtils.isEmpty(token)){
                    intent=new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if(token.equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) mContext,personImg);
                    return;
                }

                intent=new Intent(mContext, PersonMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.person_shard_rel:
                intent=new Intent(mContext, RecommendedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void SendResultSuccess(PersonModel personModels) {
        personModel=personModels;
        if(TextUtils.isEmpty(personModel.getPoint())){
            personAppointText.setText("0");
        }else{
            personAppointText.setText(""+personModel.getPoint());
        }
        personName.setText(personModel.getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideCircleTransform(mContext));

        Glide.with(mContext)
                .load(personModels.getPhoto().contains("http")?""+personModels.getPhoto():ApiStores.IMG_URL_TOP + personModel.getPhoto())
                .apply(options)
                .into(personImg);

        PersonModel person= (PersonModel) AppUtil.load(FileUtils.configPath(mContext));
        if(person!=null&&person.getToken()!=null){
            personModel.setToken(person.getToken());
        }
        AppUtil.save(personModel,FileUtils.configPath(mContext));
    }

    @Override
    public void SendBannerSuccess(List<BannerModel> bannerModels) {

    }

    @Override
    public void SendResultFail(String msg) {
        AppToast.makeToast(mContext,msg);
    }
}
