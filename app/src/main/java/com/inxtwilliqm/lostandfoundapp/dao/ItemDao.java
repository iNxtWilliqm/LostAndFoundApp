package com.inxtwilliqm.lostandfoundapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.inxtwilliqm.lostandfoundapp.entity.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM items")
    List<Item> getAll();

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);
}
