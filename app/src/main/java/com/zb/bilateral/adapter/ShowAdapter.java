package com.zb.bilateral.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zb.bilateral.activity.dynamic.DynamicListActivity;
import com.zb.bilateral.activity.show.ShowActivity;
import com.zb.bilateral.config.DynamicDataType;
import com.zb.bilateral.config.ShowDataType;

import java.util.ArrayList;
import java.util.List;


public class ShowAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    private String[] mTabNames = {ShowDataType.BASE.getCategory(), ShowDataType.TEMP.getCategory()};

    public ShowAdapter(FragmentManager fm,String museum_id) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(ShowActivity.newInstance(ShowDataType.BASE.getCategory(), ShowDataType.getMultiType(ShowDataType.BASE),museum_id));
        mFragmentList.add(ShowActivity.newInstance(ShowDataType.TEMP.getCategory(), ShowDataType.getMultiType(ShowDataType.TEMP),museum_id));
    }

    @Override
    public int getCount() {
        return mTabNames.length;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
