package com.example.emptywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditTitleView;
    private EditText mEditAmountView;
    private EditText mEditDateView;
    private EditText mEditDescriptionView;
    private ToggleButton mIsPurchaseView;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");

    private int myID;
    private Transaction myTransaction;
    private TransactionsViewModel myTransViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);

        setContentView(R.layout.activity_transaction);
        mEditTitleView = findViewById(R.id.edit_title);
        mEditAmountView = findViewById(R.id.edit_amount);
        mEditDateView = findViewById(R.id.edit_date);
        mEditDescriptionView=findViewById(R.id.edit_description);
        mIsPurchaseView=findViewById(R.id.isPurchaseToggler);


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(myTransaction!=null){
                    myTransaction.setAmount(Integer.parseInt(mEditAmountView.getText().toString()));
                    myTransaction.setTitle(mEditTitleView.getText().toString());
                    myTransaction.setDescription(mEditDescriptionView.getText().toString());
                    myTransaction.setPurchase(mIsPurchaseView.isChecked());
                    Date date = null;
                    try {
                        date = dateFormat.parse((mEditDateView.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(date);
                    myTransaction.setDate(date);
                    myTransViewModel.update(myTransaction);

                    Intent replyIntent = new Intent();
                    finish();
                }
                else{
                    Transaction toInsert = new Transaction(Integer.parseInt(mEditAmountView.getText().toString()),mEditTitleView.getText().toString(),mEditDescriptionView.getText().toString(),mIsPurchaseView.isChecked());

                    Date date = null;
                    try {
                        date = dateFormat.parse((mEditDateView.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(date);
                    toInsert.setDate(date);
                    myTransViewModel.insert(myTransaction);
                    Intent replyIntent = new Intent();
                    finish();
                }
                Intent replyIntent = new Intent();
                Log.println(Log.DEBUG,"Hello","ciao1");
                if (TextUtils.isEmpty(mEditTitleView.getText())||TextUtils.isEmpty(mEditAmountView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);

                    Log.println(Log.DEBUG,"Hello","ciao2");
                } else {
                    String title = mEditTitleView.getText().toString();
                    replyIntent.putExtra("TITLE", title);
                    String amount = mEditAmountView.getText().toString();
                    replyIntent.putExtra("AMOUNT", amount);
                    setResult(RESULT_OK, replyIntent);

                    Log.println(Log.DEBUG,"Hello","ciao3");
                }

                Log.println(Log.DEBUG,"Hello","ciao4");
                finish();
            }
        });
        if(myIntent.hasExtra("ID")){

            Log.d("Hello", "hasExtra: "+ myID);
            myID= myIntent.getIntExtra("ID",0);
            Log.d("Hello", "hasExtra: "+ myID);
            new updateViewFromDataAsyncTask().execute(myID);
        }
        else{
            myID=-1;
        }
        if(myID!=-1){
            button.setText(R.string.updateTransaction);
        }
    }


    private class updateViewFromDataAsyncTask extends AsyncTask<Integer, Void, Transaction> {


        updateViewFromDataAsyncTask() {

        }

        @Override
        protected Transaction doInBackground(final Integer... params) {
            Log.d("Hello", "doInBackground: "+ params[0]);
            return  myTransViewModel.getTransactionByID(params[0]);
        }

        @Override
        protected void onPostExecute (Transaction result){
            myTransaction=result;//myTransViewModel.getGetTransactionByIDResult(params[0]);
            Log.d("Hello", "onPostExecute: "+ result.getTitle());
            if(myTransaction!=null){
                mEditTitleView.setText(myTransaction.getTitle());
                mEditAmountView.setText(myTransaction.getAmount()+"");
                mEditDateView.setText(dateFormat.format(myTransaction.getDate()));
                mEditDescriptionView.setText(myTransaction.getDescription());
                mIsPurchaseView.setChecked(myTransaction.getIsPurchase());

            }
            else{
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }

    /*private class insertDataAsyncTask extends AsyncTask<Transaction, Void, Void> {


        saveDataAsyncTask() {

        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            Log.d("Hello", "doInBackground: "+ params[0]);
            myTransViewModel.insert(params[0]);
            //return  myTransViewModel.getTransactionByID(params[0]);
        }

        @Override
        protected Void onPostExecute (){
            /*myTransaction=result;//myTransViewModel.getGetTransactionByIDResult(params[0]);
            Log.d("Hello", "onPostExecute: "+ result.getTitle());
            if(myTransaction!=null){
                mEditTitleView.setText(myTransaction.getTitle());
                mEditAmountView.setText(myTransaction.getAmount()+"");
                mEditDateView.setText(myTransaction.getDate().toString());
                mEditDescriptionView.setText(myTransaction.getDescription());
                mIsPurchaseView.setChecked(myTransaction.getIsPurchase());

            }
            else{
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }
    private class updateDataAsyncTask extends AsyncTask<Transaction, Void, Void> {


        saveDataAsyncTask() {

        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            Log.d("Hello", "doInBackground: "+ params[0]);
            myTransViewModel.insert(params[0]);
            //return  myTransViewModel.getTransactionByID(params[0]);
        }

        @Override
        protected Void onPostExecute (){
            /*myTransaction=result;//myTransViewModel.getGetTransactionByIDResult(params[0]);
            Log.d("Hello", "onPostExecute: "+ result.getTitle());
            if(myTransaction!=null){
                mEditTitleView.setText(myTransaction.getTitle());
                mEditAmountView.setText(myTransaction.getAmount()+"");
                mEditDateView.setText(myTransaction.getDate().toString());
                mEditDescriptionView.setText(myTransaction.getDescription());
                mIsPurchaseView.setChecked(myTransaction.getIsPurchase());

            }
            else{
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }*/
}
