package com.inxtwilliqm.lostandfoundapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;
}
