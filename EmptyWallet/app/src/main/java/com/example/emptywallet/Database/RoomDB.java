package com.example.emptywallet.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.emptywallet.Categories.Category;
import com.example.emptywallet.Tags.Tag;
import com.example.emptywallet.Transactions.Transaction;

@Database(entities = {Transaction.class, Tag.class,tagTransactionRelation.class, Category.class}, version = 9)
@TypeConverters({Converters.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract RoomDAO roomDAO();

    private static volatile RoomDB INSTANCE;

    static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
