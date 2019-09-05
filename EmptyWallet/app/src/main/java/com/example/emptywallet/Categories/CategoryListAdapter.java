package com.example.emptywallet.Categories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emptywallet.R;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Transactions.TransactionHistoryFilter;
import com.example.emptywallet.Transactions.TransactionsListAdapter;
import com.example.emptywallet.Transactions.TransactionsViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private final LayoutInflater myInflater;
    private List<Transaction> myTransactions; // Cached copy of words
    private List<Category> myCategories;

    private OnCategoryClickListener myOnCategotyClickListener;
    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;

    private TransactionHistoryFilter myFilter;

    CategoryListAdapter(Context context, OnCategoryClickListener pOnCategoryClickListener) {
        myInflater = LayoutInflater.from(context);
        myTransactions = new ArrayList<Transaction>();
        myCategories = new ArrayList<Category>();

        Log.d("CategoryBudget", "hello!5");
        this.myOnCategotyClickListener = pOnCategoryClickListener;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tTitle;
        private final TextView tAmount;
        private final TextView tBudgetAmount;
        private final View myItemView;


        OnCategoryClickListener myOnCategoryClickListener;

        private CategoryViewHolder(View itemView, OnCategoryClickListener pOnCategoryClickListener) {
            super(itemView);

            Log.d("CategoryBudget", "hello4!");
            tTitle = itemView.findViewById(R.id.budget_single_categoryTitle);
            tBudgetAmount = itemView.findViewById(R.id.budget_single_budgetAmount);
            tAmount = itemView.findViewById(R.id.budget_single_spentAmount);
            myItemView=itemView;

            this.myOnCategoryClickListener = pOnCategoryClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myOnCategoryClickListener.onCategoryClick(getAdapterPosition());
        }
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = myInflater.inflate(R.layout.recyclerview_singlecategory, parent, false);

        Log.d("CategoryBudget", "hello!");
        return new CategoryViewHolder(itemView, myOnCategotyClickListener);
    }

    // We need these two because we can't access the View Models from here, so we get them from the Fragment that handles us
    public void setMyTransViewModel(TransactionsViewModel pmyTransViewModel) {
        myTransViewModel = pmyTransViewModel;

    }

    public void setTagsViewModel(TagsViewModel pTagsViewModel) {
        myTagsViewModel = pTagsViewModel;
    }

    public void setCategoryViewModel(CategoryViewModel pCategoryViewModel) {
        myCategoryViewModel = pCategoryViewModel;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        if (myCategories != null) {

            Category current = myCategories.get(position);

            holder.tTitle.setText(current.getName());

            if (current.getBudgetAmount()<=0){
                holder.tBudgetAmount.setText("no budget set");
            }
            else {
                holder.tBudgetAmount.setText(current.getBudgetAmount()+"");
            }

            holder.itemView.setClipToOutline(true);

            List<Transaction> values = getAllTransactionsByCategoryID(current.getId());

            Log.d("CategoryBudget", "hello!" + current.getName() + ", size is "+values.size());

            int totalAmount=0;

            for (Transaction t:values){
                int curramount = t.getAmount();
                if(t.getIsPurchase()) curramount=curramount*-1;
                totalAmount+=curramount;
            }


            holder.tAmount.setText((-1*totalAmount)+"");
            Drawable tDraw = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.tag_shape);

            totalAmount = totalAmount*-1;
            float percentile;

            Log.d("CategoryBudget", "hello!" + current.getName() + ","+totalAmount);

            if(totalAmount>current.getBudgetAmount()){
                percentile= (float) (((totalAmount-current.getBudgetAmount()))/current.getBudgetAmount());
                if(percentile>1){
                    percentile=1;
                }
                tDraw.setColorFilter(new PorterDuffColorFilter(interpolateColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red),ContextCompat.getColor(holder.itemView.getContext(), R.color.white),percentile), PorterDuff.Mode.SRC_ATOP));
            }
            else
            {
                percentile= (float) ((totalAmount * 1.0)/current.getBudgetAmount());
                tDraw.setColorFilter(new PorterDuffColorFilter(interpolateColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white),ContextCompat.getColor(holder.itemView.getContext(), R.color.blue),percentile), PorterDuff.Mode.SRC_ATOP));
            }


            holder.myItemView.setBackground(tDraw);
            Log.d("CategoryBudget", "hello!" + current.getName() + ","+totalAmount + ", %" + percentile + " of " + current.getBudgetAmount());

        } else {
            // Covers the case of data not being ready yet.
            holder.tTitle.setText("No transaction found");
        }
    }

    void setTransactions(List<Transaction> transactions) {
        myTransactions=transactions;
        notifyDataSetChanged();
    }

    void setCategories(List<Category> categories) {
        myCategories=categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myCategories != null)
            return myCategories.size();
        else return 0;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    private void populateAllCategoriesValues() {


    }

    private List<Transaction> getAllTransactionsByCategoryID(int id){
        if(myTransactions==null) return new ArrayList<Transaction>();
        List<Transaction> returnArray = new ArrayList<>();
        for (Transaction t : myTransactions){
            if(t.getCategoryId()==id) returnArray.add(t);
        }
        return returnArray;
    }


    public int  interpolateColor(int  pX, int pY, float amount){
        int x=pX;
        int y= pY;
        float blending= amount;

        float inverse_blending = 1 - blending;


        int red = (int) ((float)(Color.red(x)   * blending   +   Color.red(y)   * inverse_blending));
        int green = (int) ((float)(Color.green(x) * blending   +   Color.green(y) * inverse_blending));
        int blue = (int) ((float)(Color.blue(x)  * blending   +   Color.blue(y)  * inverse_blending));
        Log.d("CategoryBudget", "Red x vs red y "+ Color.red(x) + ","+ Color.red(y) +"= " + red + ",Green x vs red y "+ Color.green(x) + ","+ Color.green(y) +"= " + green +",Blue x vs red y "+ Color.blue(x) + ","+ Color.blue(y) +"= " + blue );

        int blended = Color.rgb(red, green, blue);
        return  blended;
    }
}

