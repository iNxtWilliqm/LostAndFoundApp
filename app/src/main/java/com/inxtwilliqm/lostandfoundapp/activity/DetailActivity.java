package com.inxtwilliqm.lostandfoundapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.database.AppDatabase;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

public class DetailActivity extends AppCompatActivity {
    private Item item;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_detail);

        int itemId = getIntent().getIntExtra("item_id", -1);
        item = AppDatabase.getInstance(this).itemDao().getAll().stream().filter(i -> i.id == itemId).findFirst().orElse(null);

        if (item != null) {
            ((TextView) findViewById(R.id.textDetails)).setText(
                item.type + " " + item.name + "\n" +
                    item.date + " days ago\n" +
                    "At " + item.location
            );

            findViewById(R.id.btnRemove).setOnClickListener(v -> {
                AppDatabase.getInstance(this).itemDao().delete(item);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            });
        }
    }
}
