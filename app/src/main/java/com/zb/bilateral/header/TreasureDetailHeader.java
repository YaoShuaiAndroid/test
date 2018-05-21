package com.zb.bilateral.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.liji.imagezoom.util.ImageZoom;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.activity.home_page.ImageDetailActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.CollectModel;
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
public class TreasureDetailHeader extends RelativeLayout{
    private static final int COLLECT=1;

    View view;
    Intent intent;
    Context context;
    Handler handler;

    private TextView collect_year,treasure_title,treasure_detail_content,treasure_musume_title;
    private ImageView treasure_isColl_img;
    private RelativeLayout treasure_collect_rel;
    private Banner collect_banner;

    private RelativeLayout treasure_detail_message_rel;

    public TreasureDetailHeader(Context context,Handler handler) {
        super(context);
        this.context=context;
        this.handler=handler;
        init(context);
    }

    public TreasureDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public TreasureDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        view=inflate(context, R.layout.header_treasure_detail_item, this);
        collect_year= (TextView) view.findViewById(R.id.collect_year);
        treasure_title= (TextView) view.findViewById(R.id.treasure_title);
        treasure_detail_content= (TextView) view.findViewById(R.id.treasure_detail_content);
        collect_banner= (Banner) view.findViewById(R.id.collect_banner);
        treasure_musume_title= (TextView) view.findViewById(R.id.treasure_musume_title);
        treasure_collect_rel= (RelativeLayout) view.findViewById(R.id.treasure_collect_rel);
        treasure_isColl_img= (ImageView) view.findViewById(R.id.treasure_isColl_img);
        treasure_detail_message_rel= (RelativeLayout) view.findViewById(R.id.treasure_detail_message_rel);

        treasure_detail_message_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.getToken(context).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) context,collect_year);
                    return;
                }

                new CommentDialog((Activity) context,treasure_detail_message_rel,handler);
            }
        });

        treasure_collect_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(COLLECT);
                treasure_collect_rel.setClickable(false);
            }
        });
    }

    public void setCollectAndLike(CollectModel collectModel){
        if("1".equals(collectModel.getIsColl())){
            treasure_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            treasure_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        treasure_collect_rel.setClickable(true);
    }


    public void setData(CollectModel collectModel){
        if(!TextUtils.isEmpty(collectModel.getYears())){
            collect_year.setVisibility(View.VISIBLE);
            collect_year.setText(collectModel.getYears());
        }

        if("1".equals(collectModel.getIsColl())){
            treasure_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            treasure_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        treasure_title.setText(collectModel.getName());
        treasure_detail_content.setText(collectModel.getIntroduce());

        if(collectModel.getMuseumName()!=null){
            treasure_musume_title.setText(collectModel.getMuseumName());
        }

        setBanner(collectModel.getImgList());
    }

    public void setBanner(final List<BannerModel> listBanner) {
        final List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP+listBanner.get(i).getPath());
        }

        collect_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ImageZoom.show(context,images.get(position), images);
            }
        });

        collect_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        collect_banner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        collect_banner.setImages(images);
        //设置banner动画效果
        collect_banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        collect_banner.isAutoPlay(false);
        //设置轮播时间
        collect_banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        collect_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        collect_banner.start();
    }
}