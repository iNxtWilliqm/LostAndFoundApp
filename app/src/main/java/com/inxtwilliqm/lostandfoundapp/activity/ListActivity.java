package com.inxtwilliqm.lostandfoundapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.database.AppDatabase;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);
        items = AppDatabase.getInstance(this).itemDao().getAll();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                items.stream().map(i -> i.type + " " + i.name).collect(Collectors.toList()));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("item_id", items.get(position).id);
            startActivity(intent);
        });
    }
}
