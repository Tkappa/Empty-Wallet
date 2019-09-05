package com.example.emptywallet.Stats;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.TagsViewModel;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Transactions.TransactionsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Stats_transVsDaysFragment extends Fragment {

    private BarChart myBarChart;
    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;
    private ToggleButton incometoggler;
    private LinearLayout textLayout;


    private List<Category> myCategories;
    private List<Transaction> myTransactions;

    public Stats_transVsDaysFragment() {
        // Required empty public constructor
    }


    public static Stats_transVsDaysFragment newInstance(String param1, String param2) {
        Stats_transVsDaysFragment fragment = new Stats_transVsDaysFragment();
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
        View view =inflater.inflate(R.layout.fragment_stats_transvsdays, container, false);

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);


        myBarChart= view.findViewById(R.id.stats_transdaysbarchart);
        incometoggler= view.findViewById(R.id.stats_transdays_toggleincome);
        incometoggler.setOnClickListener(view1 -> {
            populateBarChart();
        });

        myTransViewModel.getAllTransactions().observe(this, transactions -> {
            myTransactions=transactions;
            Log.d("StatsView", "getalltra");
            if(myTransactions!=null){
                Log.d("StatsView", "getalltrapop");
                populateBarChart();
            }

        });
        return view;
    }

    private void populateBarChart(){
        List<Integer> amounts = new ArrayList<>();
        for(int i =0;i<7;i++){
            amounts.add(0);
        }

        for (Transaction t : myTransactions){
            if(t.getIsPurchase()==incometoggler.isChecked()){
                Calendar c= Calendar.getInstance();
                c.setTime(t.getDate());
                amounts.set(c.get(Calendar.DAY_OF_WEEK)-1,amounts.get(Calendar.DAY_OF_WEEK-1)+t.getAmount());
                Log.d("StatFilter", c.get(Calendar.DAY_OF_WEEK)+","+ amounts.get(c.get(Calendar.DAY_OF_WEEK)-1));
            }
        }

        BarData bardata = new BarData();

        String dayNames[] = {"a","b","c","d","e","f","g"};
        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0;i<7;i++){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK,i+1);
            String dayLabel = new SimpleDateFormat("EEEE").format(c.getTime());
            dayNames[i]=dayLabel;
            Log.d("StatFilter", dayLabel+","+ amounts.get(i));
            entries.add(new BarEntry(new Float(i),amounts.get(i),dayLabel));
            Random rnd = new Random();
            int tmp=Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        }

        BarDataSet dataset = new BarDataSet(entries,"Days");
        dataset.setStackLabels(dayNames);
        /*textLayout.removeAllViews();
        for(int i = (myCategories.size()-1);i>=0;i--){
            TextView temp = new TextView(this.getContext());
            temp.setText(myCategories.get(i).getName() + "-" + amounts.get(i).toString());
            textLayout.addView(temp);
        }*/


        dataset.setValueTextColor(Color.BLACK);
        bardata.addDataSet(dataset);

        myBarChart.setData(bardata);
        myBarChart.invalidate();

        Description really = new Description();
        really.setText("Amount spent on each day");
        myBarChart.setDescription(really);
        myBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames));
        myBarChart.animateXY( 500,500, Easing.EaseInOutQuad);
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
