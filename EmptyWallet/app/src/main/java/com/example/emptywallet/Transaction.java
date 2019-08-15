package com.example.emptywallet;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;

@Entity(tableName= "transaction_table")
public class Transaction {

    @NonNull
    private String title;

    //@PrimaryKey
    @NonNull
    private int amount;

    @NonNull
    private Date date;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public Transaction(@NonNull int pAmount,@NonNull String pTitle){
        this.title=pTitle;
        this.amount=pAmount;
        this.date=new Date();
        this.id=0;
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
}
