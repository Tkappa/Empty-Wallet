package com.example.emptywallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class SelectTagActivity extends AppCompatActivity implements TagSelectorListAdapter.OnTagClickListener {

    private TagsViewModel myTagsViewModel;
    private RecyclerView myRecyclerView;
    private LinearLayoutManager myLayoutManager;
    private TagSelectorListAdapter myAdapter;
    private Button cancelButton;
    private Button newTagButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_select_tag);
        myRecyclerView = findViewById(R.id.select_tag_RecyclerView);
        cancelButton= findViewById(R.id.select_tag_cancelButton);
        cancelButton.setOnClickListener(view -> {

            //Go to old intent without doing anything
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });

        newTagButton= findViewById(R.id.select_tag_newTagButton);
        newTagButton.setOnClickListener(view -> {

            //Go to the create new tag activity
            Intent intent = new Intent(view.getContext(), NewTagActivity.class);
            startActivityForResult(intent, Constants.NEW_TRANSACTION_ACTIVITY_REQUEST_CODE);
        });

         myRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        myRecyclerView.setLayoutManager(myLayoutManager);

        // specify an adapter (see also next example)
        myAdapter = new TagSelectorListAdapter(this,this);
        myRecyclerView.setAdapter(myAdapter);
        myTagsViewModel= ViewModelProviders.of(this).get(TagsViewModel.class);
        myTagsViewModel.getAllTags().observe(this, tags -> myAdapter.setTags(tags));
    }
    @Override
    public void onTagClick(int position) {

        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);
        replyIntent.putExtra("tagID", myTagsViewModel.getAllTags().getValue().get(position).getId());
        Log.d("TagsSelection", "onTagClick: You've clicked ID " + myTagsViewModel.getAllTags().getValue().get(position).getId());
        finish();
    }

}
