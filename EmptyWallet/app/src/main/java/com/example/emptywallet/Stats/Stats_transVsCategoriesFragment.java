package com.example.emptywallet.Stats;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Transactions.TransactionsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class Stats_transVsCategoriesFragment extends Fragment {

    private PieChart myPieChart;
    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;
    private ToggleButton incometoggler;
    private LinearLayout textLayout;


    private List<Category> myCategories;
    private List<Transaction> myTransactions;

    public Stats_transVsCategoriesFragment() {
        // Required empty public constructor
    }


    public static Stats_transVsCategoriesFragment newInstance(String param1, String param2) {
        Stats_transVsCategoriesFragment fragment = new Stats_transVsCategoriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_stats_transvscategories, container, false);

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        textLayout = view.findViewById(R.id.stats_transpurcase_textlayout);

        myPieChart= view.findViewById(R.id.stats_transcatpiechart);
        incometoggler= view.findViewById(R.id.stats_transpurcase_toggleincome);
        incometoggler.setOnClickListener(view1 -> {
            populatePieChart();
        });

        myCategoryViewModel.getAllCategories().observe(this, categories -> {
                myCategories=categories;

            Log.d("StatsView", "getallcat");
                if(myTransactions!=null && myCategories!=null){
                    Log.d("StatsView", "getallcatpop");
                populatePieChart();
            }
            });

        myTransViewModel.getAllTransactions().observe(this, transactions -> {
            myTransactions=transactions;
            Log.d("StatsView", "getalltra");
            if(myTransactions!=null && myCategories!=null){
                Log.d("StatsView", "getalltrapop");
                populatePieChart();
            }

        });
        //populatePieChart();
        return view;
    }

    private void populatePieChart(){
        List<Integer> amounts = new ArrayList<>();
        for (Category ignored : myCategories){
            amounts.add(new Integer(0));
        }
        for (Transaction t : myTransactions){
            int position = getCategoryPositionById(t.getCategoryId());
            if(t.getIsPurchase()==incometoggler.isChecked()){
                amounts.set(position,amounts.get(position)+t.getAmount());
            }
        }

        PieData piedata = new PieData();

        List<PieEntry> entries = new ArrayList<>();
        PieDataSet dataset = new PieDataSet(entries,"Categories");
        for(int i = 0;i<amounts.size();i++){
            entries.add(new PieEntry(amounts.get(i),myCategories.get(i).getName()));
            Random rnd = new Random();
            int tmp=Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            dataset.addColor(tmp);
        }
        concurrentSort(amounts,myCategories,amounts);
        textLayout.removeAllViews();
        for(int i = (myCategories.size()-1);i>=0;i--){
            TextView temp = new TextView(this.getContext());
            temp.setText(myCategories.get(i).getName() + "-" + amounts.get(i).toString());
            textLayout.addView(temp);
        }


        dataset.setValueTextColor(Color.BLACK);
        piedata.addDataSet(dataset);
        myPieChart.setData(piedata);
        myPieChart.invalidate();
        myPieChart.spin( 500,0,-360f, Easing.EaseInOutQuad);
    }

    private int getCategoryPositionById(int id){
        for (int i=0;i<myCategories.size();i++){
            if (myCategories.get(i).getId()==id) return i;
        }
        return -1;
    }

    public static <T extends Comparable<T>> void concurrentSort(
            final List<T> key, List<?>... lists){
        // Create a List of indices
        List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < key.size(); i++)
            indices.add(i);

        // Sort the indices list based on the key
        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                return key.get(i).compareTo(key.get(j));
            }
        });

        // Create a mapping that allows sorting of the List by N swaps.
        // Only swaps can be used since we do not know the type of the lists
        Map<Integer,Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
        List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                swapTo   = new ArrayList<Integer>(indices.size());
        for(int i = 0; i < key.size(); i++){
            int k = indices.get(i);
            while(i != k && swapMap.containsKey(k))
                k = swapMap.get(k);

            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }

        // use the swap order to sort each list by swapping elements
        for(List<?> list : lists)
            for(int i = 0; i < list.size(); i++)
                Collections.swap(list, swapFrom.get(i), swapTo.get(i));
    }


}
