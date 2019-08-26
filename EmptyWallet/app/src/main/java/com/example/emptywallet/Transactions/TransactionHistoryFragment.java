package com.example.emptywallet.Transactions;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.Constants;
import com.example.emptywallet.R;
import com.example.emptywallet.Stats.MainStatsActivity;
import com.example.emptywallet.Tags.TagsViewModel;

import static android.app.Activity.RESULT_OK;


public class TransactionHistoryFragment extends Fragment implements TransactionsListAdapter.OnTransactionClickListener {

    private TransactionsViewModel myTransViewModel;
    private Button setFiltersButton;
    private TransactionsListAdapter adapter;
    private Button statsButton;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        adapter = new TransactionsListAdapter(getActivity(),this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setFiltersButton = view.findViewById(R.id.transaction_history_set_filter_button);
        setFiltersButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), TransactionFilterActivity.class);
            startActivityForResult(intent, Constants.SET_FILTER_ACTIVITY_REQUEST_CODE);
        });

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        adapter.setMyTransViewModel(myTransViewModel);
        adapter.setCategoryViewModel(ViewModelProviders.of(this).get(CategoryViewModel.class));
        adapter.setTagsViewModel(ViewModelProviders.of(this).get(TagsViewModel.class));

        myTransViewModel.getAllTransactions().observe( this, transactions -> adapter.setTransactions(transactions));


        statsButton = view.findViewById(R.id.transaction_history_statsbutton);
        statsButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MainStatsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onTransactionClick(int position) {
        //Go to the corresponding transaction
        Log.d("TransactionsSelection","You've clicked on the "+position);

        Intent intent = new Intent(getActivity(), TransactionActivity.class);
        intent.putExtra("ID",myTransViewModel.getAllTransactions().getValue().get(position).getId());

        getActivity().startActivityForResult(intent, Constants.MODIFY_TRANSACTION_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TransFiler","sono arrivato qui");
        if (requestCode == Constants.SET_FILTER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            TransactionHistoryFilter filter = new TransactionHistoryFilter();

            filter.setCheckCategories(data.getBooleanExtra("categoryFilter",false));
            filter.setExcludeCategories(data.getBooleanExtra("categoryFilterExclude",false));
            int[] catToFilter = data.getIntArrayExtra("categoryFilteredList");
            Log.d("TransFiler", "C'è int array ?" + data.hasExtra("categoryFilteredList"));

            filter.setCategoriesToFilter(catToFilter);

            filter.setTagFilter(data.getBooleanExtra("tagFilter",false));
            filter.setTagFilterExclude(data.getBooleanExtra("tagFilterExclude",false));
            filter.setTagFilterList(data.getIntArrayExtra("tagFilterList"));

            filter.setToDateFilter(data.getBooleanExtra("fromDateFilter",false));
            filter.setToDateDate(data.getLongExtra("fromDateDate",0));

            filter.setToDateFilter(data.getBooleanExtra("toDateFilter",false));
            filter.setToDateDate(data.getLongExtra("toDateDate",0));
            Log.d("TransFiler","sono arrivato qui" +  new Boolean(filter.getCategoriesToFilter()==null).toString());

            adapter.setTransactionFilter(filter);
            adapter.setTransactions(myTransViewModel.getAllTransactions().getValue());
            adapter.checkFilter();

        }
        //if(requestCode == Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
        //    // 1 offset because the List starts at 0
        //    mCategorySpinner.setSelection(adapter.getCount()-1);
        //}
    }

}