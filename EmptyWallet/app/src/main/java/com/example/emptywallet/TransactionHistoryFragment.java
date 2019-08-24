package com.example.emptywallet;


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



public class TransactionHistoryFragment extends Fragment implements TransactionsListAdapter.OnTransactionClickListener {

    private TransactionsViewModel myTransViewModel;

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
        adapter.setMyTransViewModel(myTransViewModel);
        adapter.setTagsViewModel(ViewModelProviders.of(this).get(TagsViewModel.class));

        myTransViewModel.getAllTransactions().observe( this, transactions -> adapter.setTransactions(transactions));

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
}