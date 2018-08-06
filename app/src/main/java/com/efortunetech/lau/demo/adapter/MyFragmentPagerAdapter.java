package com.efortunetech.lau.demo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.efortunetech.lau.demo.fragment.ProductFragment;
import com.efortunetech.lau.demo.bean.TabsBean;
import com.efortunetech.lau.demo.fragment.TestCarouselFragment;

import java.util.List;

/**
 * Created by yq06 on 2018/7/13.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<TabsBean> tabs_lists;
    private int type;

    public MyFragmentPagerAdapter(FragmentManager fm, List<TabsBean> tabs_lists, int type) {
        super(fm);
        this.tabs_lists = tabs_lists;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (type == 0) {
            if (tabs_lists.get(position).Id == 999) {
                f = new TestCarouselFragment();
            } else {
//                f = ProductFragment.newInstance(tabs_lists.get(position).Id);
//                f = NewsFragment_v2.newInstance(tabs_lists.get(position).Id);
                f = ProductFragment.newInstance(tabs_lists.get(position).Id);
            }
        } else if (type == 1) {
//            f = ProductFragment.newInstance(tabs_lists.get(position).Id);
//            f = ProductFragment_v2.newInstance(tabs_lists.get(position).Id);
        }
        return f;
    }


    @Override
    public int getCount() {
        return tabs_lists.size();
    }

    //  标题会自动设置到TabLayout中
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs_lists.get(position).name;
    }

}
