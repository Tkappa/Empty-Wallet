package com.example.emptywallet.Stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryActivity;
import com.example.emptywallet.Categories.CategorySpinnerAdapter;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.Constants;
import com.example.emptywallet.Database.tagTransactionRelation;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Transactions.TransactionsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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


public class Stats_transVsCategoriesVsTagsFragment extends Fragment {

    private PieChart myPieChart;
    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;
    private ToggleButton incometoggler;
    private LinearLayout textLayout;
    private Spinner categorySelector;
    private CategorySpinnerAdapter mySpinnerAdapter;


    private List<Category> myCategories;
    private List<Transaction> myTransactions;
    private List<Tag> myTags;
    private List<tagTransactionRelation> myTagTransactionRelations;

    private Integer selectedCategoryID;

    public Stats_transVsCategoriesVsTagsFragment() {
        // Required empty public constructor
    }


    public static Stats_transVsCategoriesVsTagsFragment newInstance(String param1, String param2) {
        Stats_transVsCategoriesVsTagsFragment fragment = new Stats_transVsCategoriesVsTagsFragment();
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
        View view =inflater.inflate(R.layout.fragment_stats_transvscategoriesvstags, container, false);
        selectedCategoryID= new Integer(-1);

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        myTagsViewModel = ViewModelProviders.of(this).get(TagsViewModel.class);



        textLayout = view.findViewById(R.id.stats_transpurcasetags_textlayout);
        categorySelector= view.findViewById(R.id.stats_transpurcasetags_categoryselector);


        myTagsViewModel.getAllTags().observe(this,tags->{
            myTags=tags;
            if(myTransactions!=null && myCategories!=null && myTags!=null && myTagTransactionRelations!=null){
                Log.d("StatsView", "getallcatpop");
                populatePieChart();
            }
        });
        myTagsViewModel.getAllTagTransactionRelations().observe(this,relations->{
            myTagTransactionRelations=relations;
            if(myTransactions!=null && myCategories!=null && myTags!=null && myTagTransactionRelations!=null){
                Log.d("StatsView", "getallcatpop");
                populatePieChart();
            }
        });
        myCategoryViewModel.getAllCategories().observe(this, categories -> {
            myCategories=categories;

            mySpinnerAdapter.setCategories(myCategories);

            Log.d("StatsView", "getallcat");
            if(myTransactions!=null && myCategories!=null && myTags!=null && myTagTransactionRelations!=null){
                Log.d("StatsView", "getallcatpop");
                populatePieChart();
            }
        });
        myTransViewModel.getAllTransactions().observe(this, transactions -> {
            myTransactions=transactions;
            Log.d("StatsView", "getalltra");
            if(myTransactions!=null && myCategories!=null && myTags!=null && myTagTransactionRelations!=null){
                Log.d("StatsView", "getallcatpop");
                populatePieChart();
            }

        });





        mySpinnerAdapter=new CategorySpinnerAdapter(view.getContext(),android.R.layout.simple_spinner_item);
        mySpinnerAdapter.setJollyName("See all categories");
        categorySelector.setAdapter(mySpinnerAdapter);
        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Category currCat = mySpinnerAdapter.getItem(position);
                Log.d("CategorySelection", "onItemSelected:  " + currCat.getName()+ ", "+ currCat.getId());
                selectedCategoryID=currCat.getId();
                populatePieChart();;

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) { }
        });

        myPieChart= view.findViewById(R.id.stats_transpurcasetagspiechart);
        incometoggler= view.findViewById(R.id.stats_transpurcasetags_toggleincome);
        incometoggler.setOnClickListener(view1 -> {
            populatePieChart();
        });



        return view;
    }

    private void populatePieChart(){
        List<Integer> amounts = new ArrayList<>();


        for (Tag t : myTags){
            amounts.add(new Integer(0));
        }




        for (Transaction t : myTransactions){
            if(t.getIsPurchase()==incometoggler.isChecked()&&(t.getCategoryId()==selectedCategoryID || selectedCategoryID==-1)){
                List<Tag> taglist;
                for(Tag tag :myTags){
                    if(transactionHasTag(t.getId(),tag.getId())){
                        int position = getTagPositionById(tag.getId());
                        amounts.set(position,amounts.get(position)+t.getAmount());
                    }
                }
            }
        }



        PieData piedata = new PieData();

        List<PieEntry> entries = new ArrayList<>();
        PieDataSet dataset = new PieDataSet(entries,"Tags");
        for(int i = 0;i<amounts.size();i++){
            if(amounts.get(i)>0){
                entries.add(new PieEntry(amounts.get(i),myTags.get(i).getName()));
                dataset.addColor(myTags.get(i).getColor());
            }
        }
        concurrentSort(amounts,myTags,amounts);
        textLayout.removeAllViews();

        for(int i = (myTags.size()-1);i>=0;i--){
            if(amounts.get(i)>0) {
                TextView temp = new TextView(this.getContext());
                temp.setText(myTags.get(i).getName() + " - " + amounts.get(i).toString());
                temp.setTextSize(20);
                textLayout.addView(temp);
            }
        }


        dataset.setValueTextColor(Color.BLACK);
        TextView temp = this.getView().findViewById(R.id.fasf);
        if(entries.size()>0) {
            temp.setText("Tags");
        }
        else {
            temp.setText("No Tags found for this category");
        }
        piedata.addDataSet(dataset);
        myPieChart.setData(piedata);
        myPieChart.setNoDataText("TestProva");
        myPieChart.invalidate();
        Description really = new Description();
        really.setText("Amount spent on Tags by category");
        myPieChart.setDescription(really);
        myPieChart.spin( 500,0,-360f, Easing.EaseInOutQuad);
    }

    private int getTagPositionById(int id){
        for (int i=0;i<myTags.size();i++){
            if (myTags.get(i).getId()==id) return i;
        }
        return -1;
    }

    private boolean transactionHasTag(int TransId,int Tagid){
        for (tagTransactionRelation rel : myTagTransactionRelations){
            if (rel.getTransactionKey()==TransId && rel.getTagKey() ==Tagid){
                return true;
            }
        }
        return false;
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
