package com.zb.bilateral.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.example.mycommon.util.SPUtils;
import com.zb.bilateral.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager guide_viewpager;
    private LinearLayout guide_indicator_ll;
    private LinearLayout guide_btn_ll;
    private Context mActivity;

    private GuideAdapter adapter;
    private ImageView[] indicator = null;// 下面的导航指示器
    private List<View> views;
    private int imgaes[] = {R.mipmap.start,R.mipmap.start1, R.mipmap.start2, R.mipmap.start3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_guide);
        mActivity = this;

        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        guide_indicator_ll = (LinearLayout) findViewById(R.id.guide_indicator_ll);
        guide_btn_ll = (LinearLayout) findViewById(R.id.guide_btn_ll);
        views = new ArrayList<View>();
        indicator = new ImageView[imgaes.length];
        // 循环加入图片的业务
        for (int i = 0; i < imgaes.length; i++) {
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
                SPUtils.put(mActivity, "isFirstRun", false);
                startActivity(new Intent(mActivity,LoginActivity.class));
                finish();
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
                view.setBackgroundResource(imgaes[position]);
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

        if (position == imgaes.length - 1) {
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
