package com.example.emptywallet.Stats;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class StatsFragmentPagerAdapter extends FragmentPagerAdapter{

    public StatsFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Stats_transVsCategoriesFragment();
            case 1:
                return new Stats_transVsCategoriesVsTagsFragment();
            case 2:
                return new Stats_transVsDaysFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Categories Pie";
            case 1:
                return "Tags per Categories";
            case 2:
                return "Days";
            default:
                return "Doesn't Exist";
        }
    }
}
