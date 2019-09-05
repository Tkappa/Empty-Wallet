package com.example.emptywallet.Transactions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryActivity;
import com.example.emptywallet.Categories.CategorySpinnerAdapter;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.Constants;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.SelectTagActivity;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    private EditText mEditTitleView;
    private EditText mEditAmountView;
    private EditText mEditDateView;
    private EditText mEditDescriptionView;
    private ToggleButton mIsPurchaseView;
    private LinearLayout mTagsLayout;
    private Spinner mCategorySpinner;
    private CategorySpinnerAdapter adapter;

    private Button addTagBtn;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");

    private int myID;
    private Transaction myTransaction;
    private List<Tag> currentTags;
    private List<Tag> tagsToRemove;

    private TransactionsViewModel myTransViewModel;
    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;

    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();

        myTransViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        myTagsViewModel = ViewModelProviders.of(this).get(TagsViewModel.class);
        myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        setContentView(R.layout.activity_transaction);

        mEditTitleView = findViewById(R.id.edit_title);
        mEditAmountView = findViewById(R.id.edit_amount);
        mEditDateView = findViewById(R.id.edit_date);
        mEditDescriptionView=findViewById(R.id.edit_description);
        mIsPurchaseView=findViewById(R.id.isPurchaseToggler);
        addTagBtn = findViewById(R.id.addTag);
        mTagsLayout = findViewById(R.id.transaction_history_tagsLayout);
        mCategorySpinner = findViewById(R.id.transaction_spinner);

        currentTags= new ArrayList<>();
        tagsToRemove = new ArrayList<>();

        myCategoryViewModel.getAllCategories().observe( this, categories -> {
            adapter.setCategories(categories);
            if(myTransaction!=null){
                mCategorySpinner.setSelection(adapter.getPositionFromId(myTransaction.getCategoryId()));
            }
        });

        adapter = new CategorySpinnerAdapter(this,
                android.R.layout.simple_spinner_item);

        mCategorySpinner.setAdapter(adapter);
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Category currCat = adapter.getItem(position);
                if (currCat.getId()==-1){
                    Intent intent = new Intent( view.getContext(), CategoryActivity.class);
                    intent.putExtra("fromTransaction",true);
                    startActivityForResult(intent, Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE);
                }
                else {

                    int spinnerPosition = adapter.getPositionFromId(currCat.getId());
                    mCategorySpinner.setSelection(spinnerPosition);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) { }
        });

        //Button to save/update the Transaction
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            //If the transaction is not null it means we are updating an old transaction
            if(myTransaction!=null){

                myTransaction.setAmount(Integer.parseInt(mEditAmountView.getText().toString()));
                myTransaction.setTitle(mEditTitleView.getText().toString());
                myTransaction.setDescription(mEditDescriptionView.getText().toString());
                myTransaction.setPurchase(mIsPurchaseView.isChecked());

                Date date;
                try {
                    date = dateFormat.parse((mEditDateView.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = Calendar.getInstance().getTime();
                }

                myTransaction.setDate(date);
                if(adapter.getItem(mCategorySpinner.getSelectedItemPosition()).getId()!=-1){
                    myTransaction.setCategoryId(adapter.getItem(mCategorySpinner.getSelectedItemPosition()).getId());
                    Log.d("CategorySelection", "onCreate: I've set id to "+ myTransaction.getCategoryId());
                }
                myTransViewModel.update(myTransaction);

                //Removes from the DB all the old tags that have been removed
                for(int i = 0; i<tagsToRemove.size();i++){
                    Tag currTag = tagsToRemove.get(i);
                    myTagsViewModel.deleteTagFromTransaction(myTransaction,currTag);
                }

                //Adds all the tags that are displayed to the DB , the redundant ones will generate a confict that will be ignored
                for(int i = 0; i<currentTags.size();i++){
                    Tag currTag = currentTags.get(i);
                    myTagsViewModel.insertTransTagRelation(myTransaction,currTag);
                }

                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
            //If not it means we are creating a new Transaction
            else{
                Log.d("CategorySelection", "onCreate: "+ adapter.getItem(mCategorySpinner.getSelectedItemPosition()).getId() + " position = "+mCategorySpinner.getSelectedItemPosition() );
                if(!mEditAmountView.getText().toString().isEmpty() && !mEditTitleView.getText().toString().isEmpty() && !(adapter.getItem(mCategorySpinner.getSelectedItemPosition()).getId()==-1)){


                    Transaction toInsert = new Transaction(Integer.parseInt(mEditAmountView.getText().toString())
                            ,mEditTitleView.getText().toString()
                            ,mEditDescriptionView.getText().toString()
                            ,mIsPurchaseView.isChecked()
                            ,adapter.getItem(mCategorySpinner.getSelectedItemPosition()).getId());

                    Date date;
                    try {
                        date = dateFormat.parse((mEditDateView.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        date = Calendar.getInstance().getTime();
                    }

                    toInsert.setDate(date);

                    //Inserts the transaction with the appropriate tags
                    myTransViewModel.insertTransactionAndTags(toInsert,currentTags);
                    Intent replyIntent = new Intent();
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.empty_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        //Checks whether is a new Transaction or we are editing a old one
        if(myIntent.hasExtra("ID")){
            myID= myIntent.getIntExtra("ID",0);
            // If it's an old one we get it from the DB
            new updateViewFromDataAsyncTask().execute(myID);
        }
        else{
            myID=-1;
            mEditDateView.setText(dateFormat.format(new Date()));
        }

        if(myID!=-1){
            button.setText(R.string.updateTransaction);
        }


        mEditDateView.setOnClickListener(v -> {
            new DatePickerDialog(TransactionActivity.this, (view, year, monthOfYear, dayOfMonth) -> {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(TransactionActivity.this, (view1, hourOfDay, minute) -> {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateLabel();
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();

            }, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        addTagBtn.setOnClickListener(view -> {
            Intent intent = new Intent( view.getContext(), SelectTagActivity.class);
            startActivityForResult(intent, Constants.SELECT_TAG_ACTIVITY_REQUEST_CODE);
        });

    }

    private void updateLabel() {
        mEditDateView.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void addNewTagButton(Tag pTag,boolean isOldTag){
        currentTags.add(pTag);

        Button tagBtn = new Button(mTagsLayout.getContext());

        Drawable newD = ContextCompat.getDrawable(this, R.drawable.tag_shape);
        newD.setColorFilter(new PorterDuffColorFilter(pTag.getColor(), PorterDuff.Mode.SRC_ATOP));
        tagBtn.setBackground(newD);

        tagBtn.setText(pTag.getName());

        tagBtn.setOnClickListener(view -> {
            removeFromTagList(pTag);
            //If it's an old tag we need to remove it from the DB, if it's a new one it hasn't been added yet so there's no need
            if(isOldTag){
                tagsToRemove.add(pTag);
            }
            mTagsLayout.removeView(tagBtn);
        });
        mTagsLayout.addView(tagBtn);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_TAG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int tempID=data.getIntExtra("tagID",-1);
            new updateTagsFromTagIDAsyncTask().execute(tempID);
        } else {
            //No tag was selected
        }
        if(requestCode == Constants.NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            // 1 offset because the List starts at 0
            mCategorySpinner.setSelection(adapter.getCount()-1);
        }
    }


    private void removeFromTagList(Tag key){
        int toRemove=-1;
        for(int i=0; i<currentTags.size();i++){
            Tag currTag = currentTags.get(i);
            if (currTag==key){
                toRemove=i;
            }
        }
        if(toRemove!=-1){
            currentTags.remove(toRemove);
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
            myTransaction=result;
            if(myTransaction!=null){
                mEditTitleView.setText(myTransaction.getTitle());
                mEditAmountView.setText(myTransaction.getAmount()+"");
                mEditDateView.setText(dateFormat.format(myTransaction.getDate()));
                mEditDescriptionView.setText(myTransaction.getDescription());
                mIsPurchaseView.setChecked(myTransaction.getIsPurchase());

                new updateTagsFromDataAsyncTask().execute(myTransaction.getId());

            }
            else{
                //The retrival from the DB has had an error
                Intent replyIntent = new Intent();
                setResult(RESULT_CANCELED, replyIntent);
                finish();
            }
        }
    }
    private class updateTagsFromDataAsyncTask extends AsyncTask<Integer, Void, List<Tag>> {
        updateTagsFromDataAsyncTask() {

        }

        @Override
        protected List<Tag> doInBackground(final Integer... params) {
            return  myTagsViewModel.getTagsByTransactionID(params[0]);
        }

        @Override
        protected void onPostExecute (final List<Tag> result){
            if (result.size()>0){
                for(int i =0; i <result.size();i++){
                    final Tag myTag = result.get(i);
                    addNewTagButton(myTag,true);
                }
            }
        }
    }
    private class updateTagsFromTagIDAsyncTask extends AsyncTask<Integer, Void, Tag> {


        updateTagsFromTagIDAsyncTask() {

        }

        @Override
        protected Tag doInBackground(final Integer... params) {
            return  myTagsViewModel.getTagByID(params[0]);
        }

        @Override
        protected void onPostExecute (final Tag result){
            if (result!=null ){
                for (Tag i:currentTags) {
                    if(i.getId()==result.getId()){
                        return;
                    }
                }
                addNewTagButton(result,false);
            }
        }
    }
}
