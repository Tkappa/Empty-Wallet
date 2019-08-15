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

public class TransactionActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditTitleView;
    private EditText mEditAmountView;
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

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(myTransaction!=null){
                    myTransaction.setAmount(Integer.parseInt(mEditAmountView.getText().toString()));
                    myTransaction.setTitle(mEditTitleView.getText().toString());
                    myTransViewModel.update(myTransaction);

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
            new updateViewAsyncTask().execute(myID);
        }
        else{
            myID=-1;
        }
        if(myID!=-1){
            button.setText(R.string.updateTransaction);
        }
    }


    private class updateViewAsyncTask extends AsyncTask<Integer, Void, Transaction> {


        updateViewAsyncTask() {

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
            }
            else{
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }

}
