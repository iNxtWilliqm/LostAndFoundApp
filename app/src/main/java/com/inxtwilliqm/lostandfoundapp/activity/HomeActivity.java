package com.inxtwilliqm.lostandfoundapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.inxtwilliqm.lostandfoundapp.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnCreate).setOnClickListener(v ->
            startActivity(new Intent(this, CreateActivity.class)));

        findViewById(R.id.btnShow).setOnClickListener(v ->
            startActivity(new Intent(this, ListActivity.class)));

        findViewById(R.id.btnMap).setOnClickListener(v ->
            startActivity(new Intent(this, MapActivity.class)));
    }
}