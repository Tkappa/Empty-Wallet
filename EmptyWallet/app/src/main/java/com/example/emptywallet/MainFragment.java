package com.example.emptywallet;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private TransactionsViewModel myTransViewModel;
    Button num;
    Button newtransaction;
    TextView flavourText;
    int currentDisplayTime=Constants.DISPLAY_SHOWDAY;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        flavourText = view.findViewById(R.id.flavourText);
        num = view.findViewById(R.id.amount);
        num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDisplayTime= (currentDisplayTime%3)+1;
                num.setText(myTransViewModel.getTotalAmountSpent(currentDisplayTime).toString());
                    switch (currentDisplayTime) {
                        case Constants.DISPLAY_SHOWDAY:
                            flavourText.setText(R.string.spentday);
                            break;
                        case Constants.DISPLAY_SHOWWEEK:
                            flavourText.setText(R.string.spentweek);
                            break;
                        case Constants.DISPLAY_SHOWMONTH:
                            flavourText.setText(R.string.spentmonth);
                            break;
                        default:
                            break;
                    }
            }
        });
        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myTransViewModel.getAllTransactions().observe( this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                num.setText(myTransViewModel.getTotalAmountSpent(currentDisplayTime).toString());
            }
        });

        newtransaction=view.findViewById(R.id.newtransaction);
        newtransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransactionActivity.class);
                getActivity().startActivityForResult(intent, Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE);
            }
        });
        return view;
    }

}
