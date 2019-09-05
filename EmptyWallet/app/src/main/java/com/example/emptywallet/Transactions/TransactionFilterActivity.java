package com.example.emptywallet.Transactions;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Categories.CategoryActivity;
import com.example.emptywallet.Categories.CategorySpinnerAdapter;
import com.example.emptywallet.Categories.CategoryViewModel;
import com.example.emptywallet.Constants;
import com.example.emptywallet.R;
import com.example.emptywallet.Tags.SelectTagActivity;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Tags.TagsViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionFilterActivity extends AppCompatActivity {

    private ToggleButton toggleCategory;
    private ToggleButton toggleCategoryExclude;
    private Button addSpinner;
    private List<Spinner> categorySpinners;
    private LinearLayout spinnerLayout;


    private ToggleButton toggleTags;
    private ToggleButton toggleTagsExclude;
    private Button addTag;
    private List<Tag> tagList;
    private LinearLayout tagsLayout;


    private ToggleButton fromDateToggl;
    private EditText fromDateSelector;
    private Calendar fromDateCalendar;

    private ToggleButton toDateToggl;
    private EditText toDateSelector;
    private Calendar toDateCalendar;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");

    private TagsViewModel myTagsViewModel;
    private CategoryViewModel myCategoryViewModel;
    private CategorySpinnerAdapter adapter;

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_filter);
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();

        myTagsViewModel = ViewModelProviders.of(this).get(TagsViewModel.class);
        myCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);


        toggleCategoryExclude=findViewById(R.id.transaction_filter_category_holder_includeexclude);
        addSpinner=findViewById(R.id.transaction_filter_category_holder_addspinner);
        spinnerLayout = findViewById(R.id.transaction_filter_category_holder);

        toggleCategory=findViewById(R.id.transaction_filter_category_holder_toggler);
        toggleCategory.setOnClickListener(v->{
            for(Spinner i : categorySpinners){
                i.setEnabled(toggleCategory.isChecked());
            }
            addSpinner.setEnabled(toggleCategory.isChecked());
        });

        addSpinner.setEnabled(toggleCategory.isChecked());
        addSpinner.setOnClickListener(v->{
            Spinner temp = new Spinner(v.getContext());
            temp.setAdapter(adapter);
            temp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    // TODO Auto-generated method stub
                    categorySpinners.remove(temp);
                    spinnerLayout.removeView(temp);
                    return true;
                }
            });
            temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    Category currCat = adapter.getItem(position);
                    Log.d("CategorySelection", "onItemSelected:  " + currCat.getName()+ ", "+ currCat.getId());
                    int spinnerPosition = adapter.getPositionFromId(currCat.getId());
                    temp.setSelection(spinnerPosition);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) { }
            });
            spinnerLayout.addView(temp);
            categorySpinners.add(temp);

        });
        categorySpinners=new ArrayList<>();

        adapter = new CategorySpinnerAdapter(this,
                android.R.layout.simple_spinner_item);
        myCategoryViewModel.getAllCategories().observe( this, categories -> adapter.setCategories(categories));


        toggleTagsExclude=findViewById(R.id.transaction_filter_tag_holder_includeexclude);
        toggleTags=findViewById(R.id.transaction_filter_tag_holder_toggle);

        addTag=findViewById(R.id.transaction_filter_tag_holder_addtag);
        addTag.setOnClickListener(view -> {
            Intent intent = new Intent( view.getContext(), SelectTagActivity.class);
            startActivityForResult(intent, Constants.SELECT_TAG_ACTIVITY_REQUEST_CODE);
        });

        tagList=new ArrayList<>();
        tagsLayout=findViewById(R.id.transaction_filter_tags_holder_taglayout);
        toggleTags.setOnClickListener(v->{
            addTag.setEnabled(toggleTags.isChecked());
        });

        addTag.setEnabled(toggleTags.isChecked());

        fromDateToggl=findViewById(R.id.transaction_filter_date_holder_fromToggle);
        fromDateToggl.setOnClickListener(v->{
            fromDateSelector.setEnabled(fromDateToggl.isChecked());
        });

        fromDateSelector=findViewById(R.id.transaction_filter_date_holder_fromdatetime);
        fromDateSelector.setOnClickListener(v -> {
            new DatePickerDialog(TransactionFilterActivity.this, (view, year, monthOfYear, dayOfMonth) -> {

                fromDateCalendar.set(Calendar.YEAR, year);
                fromDateCalendar.set(Calendar.MONTH, monthOfYear);
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDateSelector.setText(dateFormat.format(fromDateCalendar.getTime()));


            }, fromDateCalendar
                    .get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH),
                    fromDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        fromDateSelector.setText(dateFormat.format(fromDateCalendar.getTime()));

        toDateToggl=findViewById(R.id.transaction_filter_date_holder_toToggle);
        toDateToggl.setOnClickListener(v->{
            toDateSelector.setEnabled(toDateToggl.isChecked());
        });
        toDateSelector=findViewById(R.id.transaction_filter_date_holdertodatetime);
        toDateSelector.setOnClickListener(v -> {
            new DatePickerDialog(TransactionFilterActivity.this, (view, year, monthOfYear, dayOfMonth) -> {

                toDateCalendar.set(Calendar.YEAR, year);
                toDateCalendar.set(Calendar.MONTH, monthOfYear);
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDateSelector.setText(dateFormat.format(toDateCalendar.getTime()));


            }, toDateCalendar
                    .get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH),
                    toDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        toDateSelector.setText(dateFormat.format(toDateCalendar.getTime()));

        submitButton= findViewById(R.id.transaction_filter_submitbutton);
        submitButton.setOnClickListener(v->{
            Intent replyIntent = new Intent();
            setResult(RESULT_OK, replyIntent);
            //Puts the filters in the intent, the acrivity will pick it up and set it as his filter
            replyIntent.putExtra("categoryFilter",toggleCategory.isChecked());
            replyIntent.putExtra("categoryFilterExclude",toggleCategoryExclude.isChecked());

            List<Integer> categoryIds = new ArrayList<>();
            for(Spinner s: categorySpinners){
                categoryIds.add(adapter.getItem(s.getSelectedItemPosition()).getId());
            }
            replyIntent.putExtra("categoryFilteredList", convertIntegers(categoryIds));


            replyIntent.putExtra("tagFilter",toggleTags.isChecked());
            replyIntent.putExtra("tagFilterExclude",toggleTagsExclude.isChecked());

            List<Integer> TagIds = new ArrayList<>();
            for(Tag t: tagList){
                TagIds.add(t.getId());
            }
            replyIntent.putExtra("tagFilterList", convertIntegers(TagIds));


            replyIntent.putExtra("fromDateFilter",fromDateToggl.isChecked());
            replyIntent.putExtra("fromDateDate",fromDateCalendar.getTime().getTime());


            replyIntent.putExtra("toDateFilter",toDateToggl.isChecked());
            replyIntent.putExtra("toDateDate",toDateCalendar.getTime().getTime());

            finish();
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_TAG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int tempID=data.getIntExtra("tagID",-1);
            new updateTagsFromTagIDAsyncTask().execute(tempID);
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
                for (Tag i:tagList) {
                    if(i.getId()==result.getId()){
                        return;
                    }
                }
                addNewTagButton(result);
            }
        }
    }
    private void addNewTagButton(Tag pTag){
        tagList.add(pTag);

        Button tagBtn = new Button(tagsLayout.getContext());

        Drawable newD = ContextCompat.getDrawable(this, R.drawable.tag_shape);
        newD.setColorFilter(new PorterDuffColorFilter(pTag.getColor(), PorterDuff.Mode.SRC_ATOP));
        tagBtn.setBackground(newD);

        tagBtn.setText(pTag.getName());

        tagBtn.setOnClickListener(view -> {
            removeFromTagList(pTag);
            tagsLayout.removeView(tagBtn);
        });
        tagsLayout.addView(tagBtn);

    }

    private void removeFromTagList(Tag key){
        int toRemove=-1;
        for(int i=0; i<tagList.size();i++){
            Tag currTag = tagList.get(i);
            if (currTag==key){
                toRemove=i;
            }
        }
        if(toRemove!=-1){
            tagList.remove(toRemove);
        }
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}
