package com.example.emptywallet.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Transactions.Transaction;

public class DataBaseRelationHolder {
}


//Used for the relations between tags and transaction , it's N to N
@Entity(primaryKeys = {"tagKey", "TransactionKey"},
        foreignKeys = {
                @ForeignKey(entity = Tag.class,
                        parentColumns = {"id"},
                        childColumns = {"tagKey"},
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Transaction.class,
                        parentColumns = {"id"},
                        childColumns = {"TransactionKey"},
                        onDelete = ForeignKey.CASCADE
                )}
)
class tagTransactionRelation {
    @NonNull
    private int tagKey = 0;

    @NonNull
    private int TransactionKey = 0;

    public tagTransactionRelation(){

    }
    public tagTransactionRelation(int transID,int tagID){
        tagKey=tagID;
        TransactionKey=transID;
    }

    public int getTagKey() {
        return tagKey;
    }

    public void setTagKey(int tagKey) {
        this.tagKey = tagKey;
    }

    public int getTransactionKey() {
        return TransactionKey;
    }

    public void setTransactionKey(int transactionKey) {
        TransactionKey = transactionKey;
    }

}