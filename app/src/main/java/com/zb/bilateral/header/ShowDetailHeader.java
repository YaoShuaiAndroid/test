package com.zb.bilateral.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.example.mycommon.util.LogUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.model.ShowModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.view.CommentDialog;
import com.zb.bilateral.view.PromptLoginDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yaos on 2017/5/3.
 */
public class ShowDetailHeader extends RelativeLayout{
    View view;
    Context context;
    private Handler handler;

    private static final int LIKE=1;

    private ImageView show_detail_like_img;

    private TextView show_detail_title,show_detail_num,show_musume_title;

    private LinearLayout show_detail_like_lin,show_detail_lin;
    
    private Banner show_banner;

    private WebView show_detail_content;

    private RelativeLayout show_detail_message_rel;
    
    public ShowDetailHeader(Context context,Handler handler) {
        super(context);
        this.handler=handler;
        this.context=context;
        init(context);
    }

    public ShowDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public ShowDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        view=inflate(context, R.layout.header_show_detail_item, this);
        show_banner= (Banner) view.findViewById(R.id.show_banner);
        show_detail_title= (TextView) view.findViewById(R.id.show_detail_title);
        show_detail_content= (WebView) view.findViewById(R.id.show_detail_content);
        show_detail_like_img= (ImageView) view.findViewById(R.id.show_detail_like_img);
        show_detail_like_lin= (LinearLayout) view.findViewById(R.id.show_detail_like_lin);
        show_detail_num= (TextView) view.findViewById(R.id.show_detail_num);
        show_detail_lin= (LinearLayout) view.findViewById(R.id.show_detail_lin);
        show_musume_title= (TextView) view.findViewById(R.id.show_musume_title);
        show_detail_message_rel= (RelativeLayout) view.findViewById(R.id.show_detail_message_rel);

        show_detail_like_lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(LIKE);
                show_detail_like_lin.setClickable(false);
            }
        });

        show_detail_message_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.getToken(context).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) context,show_detail_like_img);
                    return;
                }

                new CommentDialog((Activity) context,show_detail_message_rel,handler);
            }
        });
    }

    public void setData(ShowModel showModel){
        show_detail_lin.setVisibility(View.VISIBLE);
        show_detail_title.setText(showModel.getTitle());

        LogUtil.i("tag->"+AppUtil.getWidth(context));

        show_detail_content.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,
                AppUtil.css(showModel.getContent()), "text/html", "utf-8", null);
        show_detail_num.setText(""+showModel.getLikeCount());
        if(showModel.getIsLike()!=null&& "1".equals(showModel.getIsLike())){
            show_detail_like_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            show_detail_like_img.setBackgroundResource(R.mipmap.like_false);
        }

        if(showModel.getMuseumName()!=null){
            show_musume_title.setText(showModel.getMuseumName());
        }

        setBanner(showModel.getImgList());
    }

    public void setCollectAndLike(ShowModel showModel){

        if("1".equals(showModel.getIsLike())){
            show_detail_like_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            show_detail_like_img.setBackgroundResource(R.mipmap.like_false);
        }
        show_detail_num.setText(""+showModel.getLikeCount());

        show_detail_like_lin.setClickable(true);
    }

    public void setBanner(final List<BannerModel> listBanner) {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(ApiStores.IMG_URL_TOP+listBanner.get(i).getPath());
        }

        show_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        show_banner.setImageLoader(new GlideCircleImageLoader());
        //设置图片集合
        show_banner.setImages(images);
        //设置banner动画效果
        show_banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        show_banner.isAutoPlay(true);
        //设置轮播时间
        show_banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        show_banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        show_banner.start();

        show_banner.setOnBannerListener(new OnBannerListener() {
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