package com.inxtwilliqm.lostandfoundapp.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.database.AppDatabase;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

public class CreateActivity extends AppCompatActivity {
    private EditText name, phone, description, date, location;
    private RadioGroup typeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        typeGroup = findViewById(R.id.typeGroup);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Item item = new Item();
            item.name = name.getText().toString();
            item.phone = phone.getText().toString();
            item.description = description.getText().toString();
            item.date = date.getText().toString();
            item.location = location.getText().toString();
            item.type = ((RadioButton) findViewById(typeGroup.getCheckedRadioButtonId())).getText().toString();

            AppDatabase.getInstance(this).itemDao().insert(item);
            finish();
        });
    }
}
