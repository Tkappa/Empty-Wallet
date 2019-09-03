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

    private int budgetAmount;

    public Category(String pName){
        name=pName;
        budgetAmount=-1;
    }
    public Category(String pName,int pBudgetAmount){
        name=pName;
        budgetAmount=pBudgetAmount;
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

    public int getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(int budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

}
