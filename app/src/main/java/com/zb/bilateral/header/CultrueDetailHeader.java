package com.zb.bilateral.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CultrueModel;
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
public class CultrueDetailHeader extends RelativeLayout{
    private static final int COLLECT=1;
    private static final int LIKE=2;

    private View view;

    private Context context;

    private Handler handler;

    Banner mBanner;
    TextView cultrue_detail_title,cultrue_detail_adderss,cultrue_detail_date,
            cultrue_detail_like_num,cultrue_detail_distest;
    ImageView cultrue_detail_isColl_img,cultrue_dianzan_img;
    RelativeLayout cultrue_collect_rel,cultrue_detail_message_rel;
    LinearLayout cultrue_like_lin,cultrue_detail_lin;
    WebView cultrue_detail_content;

    public CultrueDetailHeader(Context context,Handler handler) {
        super(context);
        this.context=context;
        this.handler=handler;
        init(context);
    }

    public CultrueDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public CultrueDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        view=inflate(context, R.layout.header_cultrue_detail_item, this);

        mBanner= (Banner) view.findViewById(R.id.cultrue_banner);
        cultrue_detail_title= (TextView) view.findViewById(R.id.cultrue_detail_title);
        cultrue_detail_adderss= (TextView) view.findViewById(R.id.cultrue_detail_adderss);
        cultrue_detail_date= (TextView) view.findViewById(R.id.cultrue_detail_date);
        cultrue_detail_content= (WebView) view.findViewById(R.id.cultrue_detail_content);
        cultrue_detail_like_num= (TextView) view.findViewById(R.id.cultrue_detail_like_num);
        cultrue_detail_isColl_img= (ImageView) view.findViewById(R.id.cultrue_detail_isColl_img);
        cultrue_dianzan_img= (ImageView) view.findViewById(R.id.cultrue_dianzan_img);
        cultrue_detail_distest= (TextView) view.findViewById(R.id.cultrue_detail_distest);
        cultrue_collect_rel= (RelativeLayout) view.findViewById(R.id.cultrue_collect_rel);
        cultrue_like_lin= (LinearLayout) view.findViewById(R.id.cultrue_like_lin);
        cultrue_detail_lin= (LinearLayout) view.findViewById(R.id.cultrue_detail_lin);
        cultrue_detail_message_rel= (RelativeLayout) view.findViewById(R.id.cultrue_detail_message_rel);

        cultrue_collect_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(COLLECT);
                cultrue_collect_rel.setClickable(false);
            }
        });

        cultrue_like_lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(LIKE);
                cultrue_like_lin.setClickable(false);
            }
        });

        cultrue_detail_message_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.getToken(context).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) context,cultrue_detail_title);
                    return;
                }

                new CommentDialog((Activity) context,cultrue_detail_message_rel,handler);
            }
        });
    }

    public void setData(CultrueModel cultrueModel){
        cultrue_detail_lin.setVisibility(View.VISIBLE);

        cultrue_detail_title.setText(cultrueModel.getTitle());
        cultrue_detail_like_num.setText(""+cultrueModel.getLikeCount());
        cultrue_detail_content.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,
                AppUtil.css(cultrueModel.getContent()), "text/html", "utf-8", null);
        cultrue_detail_adderss.setText(cultrueModel.getCreateBy());
        cultrue_detail_date.setText(AppUtil.getStrTime(cultrueModel.getUpdateDate()));
        if(cultrueModel.getDigest().length()==0){
            cultrue_detail_distest.setVisibility(View.GONE);
        }
        cultrue_detail_distest.setText(cultrueModel.getDigest());

        if("1".equals(cultrueModel.getIsColl())){
            cultrue_detail_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            cultrue_detail_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        if("1".equals(cultrueModel.getIsLike())){
            cultrue_dianzan_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            cultrue_dianzan_img.setBackgroundResource(R.mipmap.like_false);
        }

        setBanner(cultrueModel.getImgList());
    }

    public void setCollectAndLike(CultrueModel cultrueModel){
        if("1".equals(cultrueModel.getIsColl())){
            cultrue_detail_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            cultrue_detail_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        if("1".equals(cultrueModel.getIsLike())){
            cultrue_dianzan_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            cultrue_dianzan_img.setBackgroundResource(R.mipmap.like_false);
        }
        cultrue_detail_like_num.setText(""+cultrueModel.getLikeCount());

        cultrue_collect_rel.setClickable(true);
        cultrue_like_lin.setClickable(true);
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