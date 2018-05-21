package com.zb.bilateral.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zb.bilateral.activity.dynamic.AnnouncementListActivity;
import com.zb.bilateral.activity.dynamic.DynamicListActivity;
import com.zb.bilateral.activity.dynamic.InformateListActivity;
import com.zb.bilateral.activity.person.collect.MuseumFragment;
import com.zb.bilateral.config.DynamicDataType;
import com.zb.bilateral.config.MyCollectDataType;

import java.util.ArrayList;
import java.util.List;


public class DynamicAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private String museum_id;

    private String[] mTabNames = {DynamicDataType.ACTIVITY.getCategory(), DynamicDataType.INFOMATE.getCategory(),
            DynamicDataType.ANNOUNCEM.getCategory()};

    public DynamicAdapter(FragmentManager fm,String museum_id) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(DynamicListActivity.newInstance(DynamicDataType.ACTIVITY.getCategory(), DynamicDataType.getMultiType(DynamicDataType.ACTIVITY),museum_id));
        mFragmentList.add(InformateListActivity.newInstance(DynamicDataType.INFOMATE.getCategory(), DynamicDataType.getMultiType(DynamicDataType.INFOMATE),museum_id));
        mFragmentList.add(AnnouncementListActivity.newInstance(DynamicDataType.ANNOUNCEM.getCategory(), DynamicDataType.getMultiType(DynamicDataType.ANNOUNCEM),museum_id));
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
