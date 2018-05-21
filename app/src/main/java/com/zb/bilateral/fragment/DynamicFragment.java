package com.zb.bilateral.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.adapter.DynamicAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.config.DynamicDataType;
import com.zb.bilateral.mvp.BasePresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class DynamicFragment extends BaseNewFragment {
    @Bind(R.id.dynamic_layout)
    TabLayout tabLayout;
    @Bind(R.id.dynamic_view_pager)
    ViewPager viewPager;

    private DynamicAdapter dynamicAdapter;
    private MuseumDetailActivity museumActivity;

    private String museum_id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        museumActivity= (MuseumDetailActivity) activity;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, DynamicFragment.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        //获取Activity传递过来的数据并且显示到
        Bundle args=getArguments();
        museum_id=args.getString("museum_id");

        initData();
    }

    @Override
    protected void initData() {
        dynamicAdapter = new DynamicAdapter(getActivity().getSupportFragmentManager(),museum_id);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(dynamicAdapter.getCount() - 1);
        viewPager.setAdapter(dynamicAdapter);

        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText(DynamicDataType.ACTIVITY.getCategory());
        tabLayout.getTabAt(1).setText(DynamicDataType.INFOMATE.getCategory());
        tabLayout.getTabAt(2).setText(DynamicDataType.ANNOUNCEM.getCategory());
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

    @OnClick({R.id.top_left_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                museumActivity.close();
                break;
        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
