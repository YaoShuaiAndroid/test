package com.zb.bilateral.header;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yaos on 2017/5/3.
 */
public class PolicyHeader extends RelativeLayout{
    View view;
    Intent intent;
    Context context;
    Banner mBanner;

    public PolicyHeader(Context context) {
        super(context);
        this.context=context;
        init(context);
    }

    public PolicyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public PolicyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(Context context) {
        view=inflate(context, R.layout.header_policy_item, this);
        mBanner= (Banner) view.findViewById(R.id.banner);
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
                try {
                    Uri uri = Uri.parse(listBanner.get(position).getLink());
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}