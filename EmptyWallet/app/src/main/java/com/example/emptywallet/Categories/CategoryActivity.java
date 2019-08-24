package com.example.emptywallet.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.emptywallet.R;


public class CategoryActivity extends AppCompatActivity {

    private EditText categoryTitleWidget;
    private Button cancelBtn;
    private Button addBtn;
    private CategoryViewModel myCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myCategoryViewModel= ViewModelProviders.of(this).get(CategoryViewModel.class);

        setContentView(R.layout.activity_new_category);

        categoryTitleWidget = findViewById(R.id.category_activity_name);
        cancelBtn = findViewById(R.id.category_activity_cancel);
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

                Category tmp = new Category(name);
                myCategoryViewModel.insert(tmp);
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
            else{
                Toast.makeText(
                        getApplicationContext(),
                        R.string.categorynameempty,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}
