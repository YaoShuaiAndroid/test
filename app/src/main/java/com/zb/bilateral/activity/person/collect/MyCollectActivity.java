package com.zb.bilateral.activity.person.collect;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.bilateral.R;
import com.zb.bilateral.base.BaseNewActivity;
import com.zb.bilateral.config.MyCollectDataType;
import com.zb.bilateral.mvp.BasePresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class MyCollectActivity extends BaseNewActivity {
    @Bind(R.id.process_thing_layout)
    TabLayout tabLayout;
    @Bind(R.id.process_thing_view_pager)
    ViewPager viewPager;
    @Bind(R.id.top_center_text)
    TextView mTitleText;
    @Bind(R.id.top_left_img)
    ImageView topLeftRel;

    private MyCollectAdapter myCollectAdapter;
    private int item;//当前选中第几个

    public static void start(Context context) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        mTitleText.setText("我的收藏");
        topLeftRel.setBackgroundResource(R.mipmap.left_back);

        item=getIntent().getIntExtra("item",0);

        initData();
    }

    @Override
    protected void initData() {
        myCollectAdapter = new MyCollectAdapter(getSupportFragmentManager());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(myCollectAdapter.getCount() - 1);
        viewPager.setAdapter(myCollectAdapter);
        viewPager.setCurrentItem(item);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(MyCollectDataType.MUSEUM.getCategory());
        tabLayout.getTabAt(1).setText(MyCollectDataType.COLLECT.getCategory());
        tabLayout.getTabAt(2).setText(MyCollectDataType.ACTIVITY.getCategory());
        tabLayout.getTabAt(3).setText(MyCollectDataType.CULTRUE.getCategory());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.top_left_rel})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.top_left_rel:
                finish();
                break;
        }
    }
}