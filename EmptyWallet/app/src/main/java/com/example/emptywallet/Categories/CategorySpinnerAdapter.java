package com.example.emptywallet.Categories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.emptywallet.R;

import java.util.ArrayList;
import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category>{

    private Context context;
    private int layoutResourceId;
    private List<Category> myCategories;

    // We use this one to add a new category
    private Category fakeCategory;

    public  CategorySpinnerAdapter(@NonNull Context context, @LayoutRes int layoutResourceId){
        super(context,layoutResourceId);
        fakeCategory= new Category(context.getResources().getString(R.string.newcategory));
        fakeCategory.setId(-1);
        myCategories= new ArrayList<>();
        myCategories.add(fakeCategory);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        //If we selected the last element of the list we want to create a new category
        if(position>= myCategories.size()){
            label.setText(fakeCategory.getName());
        }
        else{
            label.setText(myCategories.get(position).getName());
        }
        return label;
    }

    @Override
    public Category getItem(int position){
        //If we selected the last element of the list we want to create a new category
        if(myCategories==null || position>=myCategories.size()) {
            Log.d("CategorySelection", "Triggered the fakeCategoryItem , position "+ position + ", mycategories ==null? " + new Boolean(myCategories==null).toString());
            if(myCategories!=null){
                Log.d("CategorySelection", "size is "+myCategories.size());
            }
            return fakeCategory;
        }
        else{

            return myCategories.get(position);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        //If we selected the last element of the list we want to create a new category
        if(myCategories==null || position>= myCategories.size()){
            label.setText(fakeCategory.getName());
        }
        else{
            label.setText(myCategories.get(position).getName());
        }
        return label;
    }


    public void setCategories(List<Category> pCategories){
        myCategories=pCategories;
        myCategories.add(fakeCategory);
    }

    static class CategoryHolder {
        TextView categoryName;
    }

    @Override
    public int getCount(){
        if(myCategories==null){

            Log.d("CategorySelection", "Triggered the fakeCategoryCount ");
            return 1;
        }
        return myCategories.size();
    }

    public int getPositionFromId(int id){
        Log.d("CategorySelection", "I'm begginning to search id=" + id);
        if(myCategories!=null){
            int position=myCategories.size();
            for(int i=0;i<myCategories.size();i++){
                Category c = myCategories.get(i);
                if(c.getId()==id){
                    Log.d("CategorySelection", "getPositionFromId: YA, FOUND! " + c.getName());
                    position=i;
                    Log.d("CategorySelection", "getPositionFromId: YA, Position " + position);

                }
            }
            return position;
        }
        else {
            Log.d("CategorySelection", "getPositionFromId: Brah, è null!! ");
            return 0;
        }
    }
}
