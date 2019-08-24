package com.example.emptywallet.Tags;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emptywallet.R;

import petrov.kristiyan.colorpicker.ColorPicker;

public class NewTagActivity extends AppCompatActivity {

    private EditText tagTitleWidget;
    private TextView tagColourWidget;
    private Button cancelBtn;
    private Button addBtn;
    private TagsViewModel myTagsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myTagsViewModel= ViewModelProviders.of(this).get(TagsViewModel.class);

        setContentView(R.layout.activity_new_tag);

        tagColourWidget = findViewById(R.id.new_tag_tagColor);
        tagTitleWidget = findViewById(R.id.new_tag_tagName);
        cancelBtn = findViewById(R.id.cancel);
        addBtn = findViewById(R.id.addTag);

        tagColourWidget.setOnClickListener(v -> {
            ColorPicker colorPicker = new ColorPicker((Activity) v.getContext());
            colorPicker.show();
            colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                @Override
                public void onChooseColor(int position, int color) {

                    //Store the color in the text but hides it, and sets the background to that color so that the user can see it
                    tagColourWidget.setText(color+"");
                    tagColourWidget.setTextScaleX(0);
                    tagColourWidget.setBackgroundColor(color);
                }

                @Override
                public void onCancel() {
                    //Do Nothing!
                }
            });
        });

        cancelBtn.setOnClickListener(view -> {
            //Go back without doing nothing
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });

        addBtn.setOnClickListener(view -> {
            String name = tagTitleWidget.getText().toString();
            String colourTxt = tagColourWidget.getText().toString();
            int colourInt;

            //The name is a required field
            if(!name.isEmpty()){
                if (colourTxt.isEmpty()) {
                    //Default value
                    colourInt= Color.WHITE;
                }
                else {
                    colourInt = Integer.parseInt(colourTxt);
                }

                Tag tmp = new Tag(name,colourInt);
                myTagsViewModel.insert(tmp);

                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
            }
            else{
                Toast.makeText(
                        getApplicationContext(),
                        R.string.tagemptyname,
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}
