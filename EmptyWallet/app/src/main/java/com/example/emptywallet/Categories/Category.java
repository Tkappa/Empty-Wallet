package com.example.emptywallet.Categories;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "category_table")
public class Category {
    @NonNull
    private String name;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public Category(String pName){
        name=pName;
    }
    public Category(){ }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
