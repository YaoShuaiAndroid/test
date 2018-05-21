package com.zb.bilateral.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.GlideImageLoader;
import com.zb.bilateral.view.CommentDialog;
import com.zb.bilateral.view.PromptLoginDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yaos on 2017/5/3.
 */
public class InformateDetailHeader extends RelativeLayout{
    private static final int LIKE=2;

    private View view;

    private Context context;

    private Handler handler;

    Banner mBanner;
    TextView informate_detail_title,informate_detail_date,
            informate_detail_like_num,informate_detail_distest;
    ImageView informate_dianzan_img;
    WebView informate_detail_content;
    LinearLayout informate_like_lin;

    RelativeLayout info_detail_message_rel;

    public InformateDetailHeader(Context context, Handler handler) {
        super(context);
        this.context=context;
        this.handler=handler;
        init(context);
    }

    public InformateDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public InformateDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        view=inflate(context, R.layout.header_informate_item, this);

        mBanner= (Banner) view.findViewById(R.id.informate_banner);
        informate_detail_title= (TextView) view.findViewById(R.id.informate_detail_title);
        informate_detail_date= (TextView) view.findViewById(R.id.informate_detail_date);
        informate_detail_content= (WebView) view.findViewById(R.id.informate_detail_content);
        informate_detail_like_num= (TextView) view.findViewById(R.id.informate_detail_like_num);
        informate_dianzan_img= (ImageView) view.findViewById(R.id.informate_dianzan_img);
        informate_like_lin= (LinearLayout) view.findViewById(R.id.informate_like_lin);
        info_detail_message_rel= (RelativeLayout) view.findViewById(R.id.info_detail_message_rel);


        informate_like_lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(LIKE);
                informate_like_lin.setClickable(false);
            }
        });

        info_detail_message_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.getToken(context).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) context,informate_detail_title);
                    return;
                }

                new CommentDialog((Activity) context,info_detail_message_rel,handler);
            }
        });
    }

    public void setData(ActivityModel informateModel){
        informate_detail_title.setText(informateModel.getTitle());
        informate_detail_like_num.setText(""+informateModel.getLikeCount());
        informate_detail_content.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,
                AppUtil.css(informateModel.getContent()), "text/html", "utf-8", null);
        informate_detail_date.setText(AppUtil.getStrTime(informateModel.getCreateDate()));

        if("1".equals(informateModel.getIsLike())){
            informate_dianzan_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            informate_dianzan_img.setBackgroundResource(R.mipmap.like_false);
        }

        setBanner(informateModel.getImgList());
    }

    public void setCollectAndLike(ActivityModel informateModel){
        if("1".equals(informateModel.getIsLike())){
            informate_dianzan_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            informate_dianzan_img.setBackgroundResource(R.mipmap.like_false);
        }
        informate_detail_like_num.setText(""+informateModel.getLikeCount());

        informate_like_lin.setClickable(true);
    }

    public void setBanner(final List<BannerModel> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP+listBanner.get(i).getPath());
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });
    }
}