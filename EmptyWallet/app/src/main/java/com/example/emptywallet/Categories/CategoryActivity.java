package com.example.emptywallet.Categories;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.example.emptywallet.R;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Transactions.TransactionActivity;


public class CategoryActivity extends AppCompatActivity {

    private EditText categoryTitleWidget;
    private Button cancelBtn;
    private Button addBtn;
    private EditText budgetAmount;
    private CategoryViewModel myCategoryViewModel;
    private ConstraintLayout myLayout;
    private Category myCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        myCategoryViewModel= ViewModelProviders.of(this).get(CategoryViewModel.class);
        myCategory= new Category();
        myCategory.setId(-1);

        setContentView(R.layout.activity_new_category);
        budgetAmount=findViewById(R.id.new_category_budgetamount);
        categoryTitleWidget = findViewById(R.id.category_activity_name);
        cancelBtn = findViewById(R.id.category_activity_cancel);
        myLayout = findViewById(R.id.new_category_layoutholder);
        addBtn = findViewById(R.id.category_activity_save);


        cancelBtn.setOnClickListener(view -> {
            //Go back without doing nothing
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });


        addBtn.setOnClickListener(view -> {
            String name = categoryTitleWidget.getText().toString();

            //The name is a required field
            if(!name.isEmpty()){
                if(myCategory.getId()==-1) {
                    Category tmp = new Category(name);
                    int amount = -1;
                    if (budgetAmount != null && !budgetAmount.getText().toString().isEmpty()) {
                        try {

                            amount = Integer.parseInt(budgetAmount.getText().toString());
                            Log.d("NewCategory", "Amount is " + amount);
                            tmp.setBudgetAmount(amount);
                            myCategoryViewModel.insert(tmp);
                            Intent replyIntent = new Intent();
                            setResult(RESULT_OK, replyIntent);
                            finish();
                        } catch (NumberFormatException ex) {
                            amount = -1;
                            Log.d("NewCategory", "Amount is " + amount);
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.newcatwrongamount,
                                    Toast.LENGTH_LONG).show();

                        }
                    } else {
                        myCategoryViewModel.insert(tmp);
                        Intent replyIntent = new Intent();
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                }
                else{
                    myCategory.setName(name);
                    int amount = -1;
                    if (budgetAmount != null && !budgetAmount.getText().toString().isEmpty()) {
                        try {
                            amount = Integer.parseInt(budgetAmount.getText().toString());
                            Log.d("NewCategory", "Amount is " + amount);
                            myCategory.setBudgetAmount(amount);
                            myCategoryViewModel.updateCategory(myCategory);
                            Intent replyIntent = new Intent();
                            setResult(RESULT_OK, replyIntent);
                            finish();
                        } catch (NumberFormatException ex) {
                            amount = -1;
                            Log.d("NewCategory", "Amount is " + amount);
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.newcatwrongamount,
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                }
            }
            else{
                Toast.makeText(
                        getApplicationContext(),
                        R.string.categorynameempty,
                        Toast.LENGTH_LONG).show();
            }
        });

        if(myIntent.hasExtra("fromTransaction")){
            //If i'm coming from a transaction i don't want to add the budget
            myLayout.removeView(budgetAmount);
        }
        if(myIntent.hasExtra("ID")){
            //If I'm passing an ID I'm checking an old one.
            int tempID=myIntent.getIntExtra("ID",-1);
            new updateCategoryViewFromDataAsyncTask().execute(tempID);
        }
    }

    private class updateCategoryViewFromDataAsyncTask extends AsyncTask<Integer, Void, Category> {


        updateCategoryViewFromDataAsyncTask() {

        }

        @Override
        protected Category doInBackground(final Integer... params) {
            Log.d("Hello", "doInBackground: "+ params[0]);
            return  myCategoryViewModel.getCategoryById(params[0]);
        }

        @Override
        protected void onPostExecute (Category result){
            myCategory=result;
            if(myCategory.getId()!=-1){
                budgetAmount.setText(myCategory.getBudgetAmount()+"");
                categoryTitleWidget.setText(myCategory.getName());
                addBtn.setText(R.string.updateTransaction);

            }
            else{
                //The retrival from the DB has had an error
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }
}
