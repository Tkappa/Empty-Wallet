package com.example.emptywallet;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WalletFragmentPagerAdapter extends FragmentPagerAdapter {

    public WalletFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new TransactionHistoryFragment();
            case 1:
                return new MainFragment();
                default:
                    return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "History";
            case 1:
                return "Main";
                default:
                    return "bho";
        }
    }
}
