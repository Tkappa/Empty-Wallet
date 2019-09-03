package com.example.emptywallet.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Transactions.Transaction;

import java.util.List;

@Dao
public interface RoomDAO {

    /* TAGS */
    @Insert
    void insertTag(Tag tag);

    @Query("DELETE FROM tag_table")
    void deleteAllTags();

    @Query("SELECT * FROM tag_table ORDER BY id ASC")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT * FROM tag_table WHERE id=:id LIMIT 1")
    Tag getTagByID(int id);

    @Update
    void updateTag(Tag... tags);


    /* TRANSACTIONS */

    @Insert
    long insertTransaction(Transaction transaction);

    @Query("DELETE FROM transaction_table")
    void deleteAllTransactions();

    @Query("SELECT * FROM transaction_table ORDER BY id ASC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE id=:id LIMIT 1")
    Transaction getTransactionByID(int id);

    @Update
    void updateTransactions(Transaction... transactions);

    /* TRANSACTIONS <-> TAGS RELATIONS */

    @Query("SELECT * FROM tagTransactionRelation")
    LiveData<List<tagTransactionRelation>> getAllTagsTransactionRelations();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTagTransactionRelation(tagTransactionRelation rel);

    @Query("DELETE FROM tagTransactionRelation WHERE tagTransactionRelation.TransactionKey=:transid AND tagTransactionRelation.tagKey=:tagid")
    void deleteTagFromTransaction(int transid, int tagid);

    @Query("SELECT * FROM tagTransactionRelation INNER JOIN tag_table ON tagTransactionRelation.tagKey = tag_table.id WHERE tagTransactionRelation.TransactionKey=:transid ")
    List<Tag> getAllTagsFromTransaction(int transid);

    @Query("SELECT * FROM tagTransactionRelation INNER JOIN transaction_table ON tagTransactionRelation.TransactionKey = transaction_table.id WHERE tagTransactionRelation.tagKey=:tagid ")
    LiveData<List<Transaction>> getAllTransactionFromTag(int tagid);


    /* CATEGORIES */
    @Insert
    long insertCategory(Category category);

    @Query("DELETE FROM category_table")
    void deleteAllCategories();

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM category_table WHERE id=:id LIMIT 1")
    Category getCategoryByID(int id);

    @Update
    void updateCategories(Category... categories);



}
