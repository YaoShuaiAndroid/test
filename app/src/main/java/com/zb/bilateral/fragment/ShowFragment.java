package com.zb.bilateral.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zb.bilateral.Constants;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.home_page.MuseumDetailActivity;
import com.zb.bilateral.adapter.DynamicAdapter;
import com.zb.bilateral.adapter.ShowAdapter;
import com.zb.bilateral.base.BaseNewFragment;
import com.zb.bilateral.mvp.BasePresenter;
import com.zb.bilateral.view.CommitDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ShowFragment extends BaseNewFragment {
    @Bind(R.id.show_view_pager)
    ViewPager viewPager;
    @Bind(R.id.show_temp_text)
    TextView showTempText;
    @Bind(R.id.show_base_text)
    TextView showBaseText;


    private ShowAdapter showAdapter;
    private MuseumDetailActivity museumActivity;

    private String museum_id;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        museumActivity= (MuseumDetailActivity) activity;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ShowFragment.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_fragment;
    }

    @Override
    protected void initViewsAndEvents(View self, Bundle savedInstanceState) {
        //获取Activity传递过来的数据并且显示到
        Bundle args=getArguments();
        museum_id=args.getString("museum_id");
    }

    @Override
    protected void initData() {
        showAdapter = new ShowAdapter(getActivity().getSupportFragmentManager(),museum_id);
        viewPager.setOffscreenPageLimit(showAdapter.getCount() - 1);
        viewPager.setAdapter(showAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    showTempText.setBackgroundResource(R.drawable.yellow_back_right_5dp);
                    showTempText.setTextColor(getResources().getColor(R.color.white_color));
                    showBaseText.setBackgroundResource(R.drawable.white_back_left_5dp);
                    showBaseText.setTextColor(getResources().getColor(R.color.top_color));
                }else{
                    showBaseText.setBackgroundResource(R.drawable.yellow_back_left_5dp);
                    showBaseText.setTextColor(getResources().getColor(R.color.white_color));
                    showTempText.setBackgroundResource(R.drawable.white_back_right_5dp);
                    showTempText.setTextColor(getResources().getColor(R.color.top_color));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.top_left_img,R.id.show_base_text,R.id.show_temp_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_img:
                museumActivity.close();
                break;
            case R.id.show_base_text://基本展览
                viewPager.setCurrentItem(0);
                break;
            case R.id.show_temp_text://临时展览
                viewPager.setCurrentItem(1);
                break;
        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
