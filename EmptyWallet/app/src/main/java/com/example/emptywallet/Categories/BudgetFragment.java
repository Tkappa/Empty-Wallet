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
import com.example.emptywallet.Tags.TagsViewModel;
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

            myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
            myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

            adapter.setMyTransViewModel(myTransViewModel);
            adapter.setCategoryViewModel(myCategoryViewModel);
            adapter.setTagsViewModel(ViewModelProviders.of(this).get(TagsViewModel.class));

            myTransViewModel.getAllTransactions().observe( this, transactions -> adapter.setTransactions(transactions));
            myCategoryViewModel.getAllCategories().observe(this,categories -> {
                adapter.setCategories(categories);
            });

            newCategory=view.findViewById(R.id.budget_addcategoryButton);
            newCategory.setOnClickListener(v->{
                Intent intent = new Intent( view.getContext(), CategoryActivity.class);
                startActivityForResult(intent, Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE);
            });

            return view;
        }

        @Override
        public void onCategoryClick(int position) {
            //Open the corresponding category

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
        }
    }
