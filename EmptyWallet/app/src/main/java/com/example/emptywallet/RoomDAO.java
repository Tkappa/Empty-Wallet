package com.example.emptywallet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface RoomDAO {
    @Insert
    void insertTransaction(Transaction transaction);

    @Query("DELETE FROM transaction_table")
    void deleteAllTransactions();

    @Query("SELECT * FROM transaction_table ORDER BY id ASC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE id=:id LIMIT 1")
    Transaction getTransactionByID(int id);

    @Update
    public void updateTransactions(Transaction...transactions);


}
