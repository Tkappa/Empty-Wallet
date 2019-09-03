package com.example.emptywallet.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emptywallet.Constants;
import com.example.emptywallet.R;
import com.example.emptywallet.Stats.MainStatsActivity;
import com.example.emptywallet.Tags.TagsViewModel;
import com.example.emptywallet.Transactions.TransactionActivity;
import com.example.emptywallet.Transactions.TransactionFilterActivity;
import com.example.emptywallet.Transactions.TransactionHistoryFilter;
import com.example.emptywallet.Transactions.TransactionsListAdapter;
import com.example.emptywallet.Transactions.TransactionsViewModel;

import static android.app.Activity.RESULT_OK;

public class BudgetFragment  extends Fragment implements CategoryListAdapter.OnCategoryClickListener {

        private TransactionsViewModel myTransViewModel;
        private CategoryViewModel myCategoryViewModel;
        private Button newCategory;
        private CategoryListAdapter adapter;
        private Button statsButton;

        public BudgetFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_budget, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.budget_recyclerview);

            adapter = new CategoryListAdapter(getActivity(),this);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            Log.d("CategoryBudget", "hello3!");
            /*setFiltersButton = view.findViewById(R.id.transaction_history_set_filter_button);
            setFiltersButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), TransactionFilterActivity.class);
                startActivityForResult(intent, Constants.SET_FILTER_ACTIVITY_REQUEST_CODE);
            });*/

            myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
            myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
            adapter.setMyTransViewModel(myTransViewModel);
            adapter.setCategoryViewModel(myCategoryViewModel);
            adapter.setTagsViewModel(ViewModelProviders.of(this).get(TagsViewModel.class));

            myTransViewModel.getAllTransactions().observe( this, transactions -> adapter.setTransactions(transactions));
            myCategoryViewModel.getAllCategories().observe(this,categories -> {
                adapter.setCategories(categories);
                Log.d("Category","observer triggered");
            });

            newCategory=view.findViewById(R.id.budget_addcategoryButton);
            newCategory.setOnClickListener(v->{
                Intent intent = new Intent( view.getContext(), CategoryActivity.class);
                startActivityForResult(intent, Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE);
            });


            /*statsButton = view.findViewById(R.id.transaction_history_statsbutton);
            statsButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), MainStatsActivity.class);
                startActivity(intent);
            });*/

            return view;
        }

        @Override
        public void onCategoryClick(int position) {
            //Go to the corresponding transaction
            Log.d("TransactionsSelection","You've clicked on the "+position);

            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            intent.putExtra("ID",myCategoryViewModel.getAllCategories().getValue().get(position).getId());
            getActivity().startActivityForResult(intent, Constants.MODIFY_CATEGORY_ACTIVITY_REQUEST_CODE);
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("TransFiler","sono arrivato qui");
            if (requestCode == Constants.MODIFY_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged();
            }
            //if(requestCode == Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            //    // 1 offset because the List starts at 0
            //    mCategorySpinner.setSelection(adapter.getCount()-1);
            //}
        }

    }
