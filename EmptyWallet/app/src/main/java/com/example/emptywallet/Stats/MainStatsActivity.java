package com.example.emptywallet.Stats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.emptywallet.R;
import com.example.emptywallet.Transactions.TransactionsViewModel;
import com.example.emptywallet.WalletFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainStatsActivity extends AppCompatActivity {

    private ViewPager pager;
    private StatsFragmentPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stats);
        pager = findViewById(R.id.stats_pager);
        adapter = new StatsFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout=findViewById(R.id.stats_tab_layout);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    protected void onStart(){
        super.onStart();
        pager.setCurrentItem(1);
    }

}
