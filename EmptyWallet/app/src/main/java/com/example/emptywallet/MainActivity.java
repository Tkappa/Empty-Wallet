package com.example.emptywallet;

import android.content.Intent;
import android.os.Bundle;

import com.example.emptywallet.Transactions.TransactionsViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private TransactionsViewModel myTransViewModel;
    private ViewPager pager;
    private WalletFragmentPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onStart(){
        super.onStart();
        pager.setCurrentItem(1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.pager);
        adapter = new WalletFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout=findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.DEBUG,"ActivityResults",Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE + "vs"+ requestCode + ","+RESULT_OK+"vs"+ resultCode);
        if (requestCode == Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //RESULT SUCCESFUL
        } else {
            //WAS NOT SUCCESFUL
        }
    }
}
