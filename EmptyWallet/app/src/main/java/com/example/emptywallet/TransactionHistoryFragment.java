package com.example.emptywallet;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionsListAdapter.OnTransactionClickListener {

    private TransactionsViewModel myTransViewModel;
    public static final int NEW_TRANSACTION_ACTIVITY_REQUEST_CODE = 1;

    public TransactionHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final TransactionsListAdapter adapter = new TransactionsListAdapter(getActivity(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myTransViewModel.getAllTransactions().observe( this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
            }
        });
        return view;
    }

    @Override
    public void onTransactionClick(int position) {
        //Vai a quell'activity
        Log.println(Log.DEBUG,"Hello",""+position);
        Intent intent = new Intent(getActivity(), TransactionActivity.class);
        intent.putExtra("ID",myTransViewModel.getAllTransactions().getValue().get(position).getId());
        getActivity().startActivityForResult(intent, Constants.MODIFY_TRANSACTION_ACTIVITY_REQUEST_CODE);
    }
}