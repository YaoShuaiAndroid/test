package com.zb.bilateral.activity.person.collect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zb.bilateral.config.MyCollectDataType;

import java.util.ArrayList;
import java.util.List;


public class MyCollectAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    private String[] mTabNames = {MyCollectDataType.MUSEUM.getCategory(), MyCollectDataType.COLLECT.getCategory(),
            MyCollectDataType.ACTIVITY.getCategory(), MyCollectDataType.CULTRUE.getCategory()};

    public MyCollectAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(MuseumFragment.newInstance(MyCollectDataType.MUSEUM.getCategory(), MyCollectDataType.getMultiType(MyCollectDataType.MUSEUM)));
        mFragmentList.add(MyCollectFragment.newInstance(MyCollectDataType.COLLECT.getCategory(), MyCollectDataType.getMultiType(MyCollectDataType.COLLECT)));
        mFragmentList.add(ActivityFragment.newInstance(MyCollectDataType.ACTIVITY.getCategory(), MyCollectDataType.getMultiType(MyCollectDataType.ACTIVITY)));
        mFragmentList.add(MyCultrueFragment.newInstance(MyCollectDataType.CULTRUE.getCategory(), MyCollectDataType.getMultiType(MyCollectDataType.CULTRUE)));
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
