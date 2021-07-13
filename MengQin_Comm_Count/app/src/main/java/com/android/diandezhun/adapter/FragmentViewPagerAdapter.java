package com.android.diandezhun.adapter;

import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by zt on 2015/12/21.
 * viewpager填充器
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentsList;
    List<String> titleList;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList, List<String> titleList) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentsList.get(i);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null) {
            return titleList.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
