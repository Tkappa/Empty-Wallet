package com.example.emptywallet.Transactions;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.TransactionViewHolder> {

    private final LayoutInflater myInflater;
    private List<Transaction> myTransactions; // Cached copy of words
    private OnTransactionClickListener myOnTransactionClickListener;

    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;

    private SimpleDateFormat dayDateFormat = new SimpleDateFormat("EEEE");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private TransactionHistoryFilter myFilter;

    TransactionsListAdapter(Context context,OnTransactionClickListener pOnTransactionClickListener) {
        myInflater = LayoutInflater.from(context);
        myFilter= new TransactionHistoryFilter();
        myTransactions= new ArrayList<Transaction>();
        this.myOnTransactionClickListener=pOnTransactionClickListener;
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tTitle;
        private final TextView tAmount;
        private final TextView tDate;
        private final TextView tCategory;
        private final LinearLayout tTagLayout;

        OnTransactionClickListener onTransactionClickListener;

        private TransactionViewHolder(View itemView, OnTransactionClickListener pOnTransactionClickListener) {
            super(itemView);

            tTitle = itemView.findViewById(R.id.transactionTitle);
            tDate= itemView.findViewById(R.id.transactionDate);
            tAmount=itemView.findViewById(R.id.transactionAmount);
            tTagLayout=itemView.findViewById(R.id.transaction_history_tagsLayout);
            tCategory=itemView.findViewById(R.id.transaction_history_transactionCategory);

            this.onTransactionClickListener = pOnTransactionClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTransactionClickListener.onTransactionClick(getAdapterPosition());
        }
    }



    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = myInflater.inflate(R.layout.recyclerview_singletransaction, parent, false);
        return new TransactionViewHolder(itemView,myOnTransactionClickListener);
    }

    // We need these two because we can't access the View Models from here, so we get them from the Fragment that handles us
    public void setMyTransViewModel(TransactionsViewModel pmyTransViewModel){
        myTransViewModel = pmyTransViewModel;
    }

    public void setTagsViewModel(TagsViewModel pTagsViewModel){
        myTagsViewModel = pTagsViewModel;
    }

    public void setCategoryViewModel(CategoryViewModel pCategoryViewModel){
        myCategoryViewModel = pCategoryViewModel;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (myTransactions != null) {

            Transaction current = myTransactions.get(position);

            holder.tTitle.setText(current.getTitle());
            holder.tAmount.setText(current.getAmount()+"");
            holder.tDate.setText(dayDateFormat.format(current.getDate())+" "+dateFormat.format(current.getDate()));

            Drawable temp;
            if(current.getIsPurchase()){
                temp= ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.transaction_purchase_shape);
            }
            else{
                temp= ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.transaction_income_shape);
            }

            holder.itemView.setClipToOutline(true);
            holder.itemView.setBackground(temp);

            //Remove all the old Tags
            holder.tTagLayout.removeAllViews();
            //And send a query to get an updated list
            new getAndPrintTagsFromTransaction(holder).execute(current);
            new getAndPrintCategory(holder).execute(current);

        } else {
            // Covers the case of data not being ready yet.
            holder.tTitle.setText("No transaction found");
        }
    }

    void setTransactions(List<Transaction> transactions){
        myTransactions.clear();
        for(Transaction i:transactions){
            if( myFilter.passesFilter(i)){
                myTransactions.add(i);
            }
        }
        //myTransactions=transactions;
        notifyDataSetChanged();
    }

    void checkFilter(){
        for (Transaction i:new ArrayList<Transaction>(myTransactions)){
            if(!myFilter.passesFilter(i)) myTransactions.remove(i);
        }
        notifyDataSetChanged();
    }

    void setTransactionFilter(TransactionHistoryFilter pFilter){
        myFilter=pFilter;
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

    private class getAndPrintTagsFromTransaction extends AsyncTask<Transaction, Void, List<Tag>> {

        private TransactionViewHolder holder;

        getAndPrintTagsFromTransaction(TransactionViewHolder pholder) {
            this.holder=pholder;
        }

        @Override
        protected List<Tag> doInBackground(final Transaction... params) {
            return myTagsViewModel.getTagsByTransactionID(params[0].getId());
        }

        @Override
        protected void onPostExecute (final List<Tag> result){

            for(Tag i : result){

                TextView tmp = new TextView(holder.itemView.getContext());

                Drawable tDraw = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tag_shape);
                tDraw.setColorFilter(new PorterDuffColorFilter(i.getColor(), PorterDuff.Mode.SRC_ATOP));
                tmp.setBackground(tDraw);
                tmp.setText(i.getName());

                holder.tTagLayout.addView(tmp);
            }
        }
    }

    private class getAndPrintCategory extends AsyncTask<Transaction, Void, Category> {

        private TransactionViewHolder holder;

        getAndPrintCategory(TransactionViewHolder pholder) {
            this.holder=pholder;
        }

        @Override
        protected Category doInBackground(final Transaction... params) {
            return myCategoryViewModel.getCategoryById(params[0].getCategoryId());
        }

        @Override
        protected void onPostExecute (final Category result){
            holder.tCategory.setText(result.getName());

        }
    }
}
