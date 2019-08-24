package com.example.emptywallet;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Transaction.class,Tag.class,tagTransactionRelation.class}, version = 6)
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
