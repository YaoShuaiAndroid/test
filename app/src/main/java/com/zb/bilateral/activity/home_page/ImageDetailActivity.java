package com.zb.bilateral.activity.home_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mycommon.util.SPUtils;
import com.example.mycommon.view.GlideRoundImage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.GuideActivity;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.model.BannerModel;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.GlideCircleImageLoader;
import com.zb.bilateral.util.GlideImageFitXYLoader;
import com.zb.bilateral.util.GlideImageLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImageDetailActivity extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager guide_viewpager;
    private LinearLayout guide_indicator_ll;
    private LinearLayout guide_btn_ll;
    private Context mActivity;

    private GuideAdapter adapter;
    private ImageView[] indicator = null;// 下面的导航指示器
    private List<View> views;
    private List<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        images=getIntent().getStringArrayListExtra("img");

        //设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_image_detail);
        mActivity = this;

        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        guide_indicator_ll = (LinearLayout) findViewById(R.id.guide_indicator_ll);
        guide_btn_ll = (LinearLayout) findViewById(R.id.guide_btn_ll);
        views = new ArrayList<View>();
        indicator = new ImageView[images.size()];
        // 循环加入图片的业务
        for (int i = 0; i < images.size(); i++) {
            ImageView mImageView = new ImageView(mActivity);
            views.add(mImageView);

            ImageView imageView = new ImageView(getApplication());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            indicator[i] = imageView;
            if (i == 0) {
                indicator[i].setBackgroundResource(R.mipmap.banner_dot_01);
            } else {
                indicator[i].setBackgroundResource(R.mipmap.banner_dot_03);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 18;
            layoutParams.rightMargin = 18;
            guide_indicator_ll.addView(imageView, layoutParams);
        }
        adapter = new GuideAdapter(views);
        // 为mViewPager绑定适配器
        guide_viewpager.setAdapter(adapter);
        guide_viewpager.setOnPageChangeListener(this);
        guide_viewpager.setCurrentItem(0);
        guide_viewpager.setOffscreenPageLimit(1);

        guide_btn_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    class GuideAdapter extends PagerAdapter {
        private List<View> views;
        private LinkedList<View> recyleBin = new LinkedList<View>();

        public GuideAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final View view;
            if (!recyleBin.isEmpty()) {
                view = recyleBin.pop();
            } else {
                view = views.get(position);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Glide.with(mActivity).asBitmap().load(images.get(position)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        BitmapDrawable bitmapDrawable=new BitmapDrawable(resource);
                        view.setBackgroundDrawable(bitmapDrawable);
                    }
                }); //方法中设置asBitmap可以设置回调类型

                view.setLayoutParams(params);
            }
            container.addView(view, 0);
            return views.get(position);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % views.size());

        if (position == images.size() - 1) {
            guide_btn_ll.setVisibility(View.VISIBLE);
        } else {
            guide_btn_ll.setVisibility(View.GONE);
        }

    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < indicator.length; i++) {
            if (i == selectItems) {
                indicator[i].setBackgroundResource(R.mipmap.banner_dot_01);
            } else {
                indicator[i].setBackgroundResource(R.mipmap.banner_dot_03);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}