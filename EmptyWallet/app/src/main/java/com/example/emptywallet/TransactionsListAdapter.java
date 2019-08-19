package com.example.emptywallet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.TransactionViewHolder> {
    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tTitle;
        private final TextView tAmount;
        private final TextView tDate;
        OnTransactionClickListener onTransactionClickListener;

        private TransactionViewHolder(View itemView, OnTransactionClickListener pOnTransactionClickListener) {
            super(itemView);
            tTitle = itemView.findViewById(R.id.transactionTitle);
            tDate= itemView.findViewById(R.id.transactionDate);
            tAmount=itemView.findViewById(R.id.transactionAmount);
            this.onTransactionClickListener = pOnTransactionClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTransactionClickListener.onTransactionClick(getAdapterPosition());
        }
    }

    private final LayoutInflater myInflater;
    private List<Transaction> myTransactions; // Cached copy of words
    private OnTransactionClickListener myOnTransactionClickListener;

    TransactionsListAdapter(Context context,OnTransactionClickListener pOnTransactionClickListener) {
        myInflater = LayoutInflater.from(context);
        this.myOnTransactionClickListener=pOnTransactionClickListener;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_singletransaction, parent, false);
        return new TransactionViewHolder(itemView,myOnTransactionClickListener);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (myTransactions != null) {
            Transaction current = myTransactions.get(position);
            holder.tTitle.setText(current.getTitle());
            holder.tAmount.setText(current.getAmount()+"");
            holder.tDate.setText(current.getDate().toString());
            Log.d("Hello", "onBindViewHolder: "+holder.tDate.getText());
        } else {
            // Covers the case of data not being ready yet.
            holder.tTitle.setText("No transaction found");
        }
    }

    void setTransactions(List<Transaction> transactions){
        myTransactions=transactions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myTransactions != null)
            return myTransactions.size();
        else return 0;
    }

    public interface OnTransactionClickListener{
        void onTransactionClick(int position);
    }
}
