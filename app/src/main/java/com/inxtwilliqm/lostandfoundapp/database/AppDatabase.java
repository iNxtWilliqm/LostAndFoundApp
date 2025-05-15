package com.inxtwilliqm.lostandfoundapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.inxtwilliqm.lostandfoundapp.dao.ItemDao;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "item_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}