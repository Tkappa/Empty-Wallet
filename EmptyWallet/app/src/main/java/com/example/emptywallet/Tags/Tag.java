

package com.example.emptywallet.Tags;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "tag_table")
public class Tag {

    @NonNull
    private String name;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int color;

    public Tag(@NonNull String pName,int pColor){
        this.name=pName;
        this.id=0;
        if (pColor == 0){
            color = Color.LTGRAY;
        }
        else {
            color= pColor;
        }
    }
    public Tag(){}

    public String getName(){
        return this.name;
    }
    public void setName(String pName) {
        this.name = pName;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int pID){
        this.id=pID;
    }
    public int getColor(){
        return this.color;
    }
    public void setColor(int pColor){
        this.color=pColor;
    }
}
