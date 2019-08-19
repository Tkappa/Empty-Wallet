package com.example.emptywallet;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

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
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        pager = findViewById(R.id.pager);
        adapter = new WalletFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout=findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        /*
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final TransactionsListAdapter adapter = new TransactionsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myTransViewModel.getAllTransactions().observe( this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
            }
        });*/
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.DEBUG,"Hello",Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE + "vs"+ requestCode + ","+RESULT_OK+"vs"+ resultCode);
        if (requestCode == Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Transaction word = new Transaction(Integer.parseInt(data.getStringExtra("AMOUNT")),data.getStringExtra("TITLE"),"",true);
            myTransViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
