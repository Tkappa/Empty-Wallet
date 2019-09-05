package com.example.emptywallet.Transactions;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.emptywallet.Categories.Category;

import java.util.Date;

@Entity(tableName= "transaction_table")
public class Transaction {

    private String title;

    //@PrimaryKey
    @NonNull
    private int amount;

    @NonNull
    private Date date;

    private String description;

    @NonNull
    private boolean isPurchase;

    @NonNull
    @ForeignKey(entity = Category.class,parentColumns = {"id"},childColumns = {"categoryID"},onDelete = ForeignKey.NO_ACTION)
    private int categoryId;

    //TODO: Photos
    //TODO: Geolocalization

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public Transaction(@NonNull int pAmount,String pTitle, String pDescription, boolean pIsPurchase,int pCategoryId){
        this.title=pTitle;
        this.amount=pAmount;
        this.date=new Date();
        this.id=0;
        this.description=pDescription;
        this.isPurchase=pIsPurchase;
        this.categoryId=pCategoryId;
    }
    public Transaction(){

    }

    public String getTitle(){
        return this.title;
    }
    public int getAmount(){
        return this.amount;
    }
    public Date getDate(){return this.date; }
    public int getId(){
        return this.id;
    }
    public String getDescription(){ return this.description;}
    public Boolean getIsPurchase(){return this.isPurchase;}
    public void setId(int pId){
        this.id=pId;
    }
    public void setAmount(int pAmount){
        this.amount=pAmount;
    }
    public void setTitle(String pTitle){
        this.title=pTitle;
    }
    public void setDate(Date pDate){this.date=pDate;}
    public void setDescription(String pDescription){this.description=pDescription;}
    public void setPurchase(boolean pIsPurchase){this.isPurchase=pIsPurchase;}
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
