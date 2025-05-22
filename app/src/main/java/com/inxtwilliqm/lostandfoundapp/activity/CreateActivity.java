package com.inxtwilliqm.lostandfoundapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.database.AppDatabase;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

public class CreateActivity extends AppCompatActivity {
    private RadioGroup typeGroup;
    private EditText name, phone, description, date, location;
    private Button currentLocation, save;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // ActivityResultLauncher for ChooseLocationActivity
    @SuppressLint("SetTextI18n")
    private ActivityResultLauncher<Intent> locationLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                double selectedLatitude = result.getData().getDoubleExtra("selected_latitude", 0);
                double selectedLongitude = result.getData().getDoubleExtra("selected_longitude", 0);

                location.setText(selectedLatitude + ", " + selectedLongitude);
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        typeGroup = findViewById(R.id.typeGroup);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        currentLocation = findViewById(R.id.btnCurrentLocation);
        save = findViewById(R.id.btnSave);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        location.setOnClickListener(v -> chooseLocation());
        currentLocation.setOnClickListener(v -> getCurrentLocation());
        save.setOnClickListener(v -> addItem());
    }

    private void chooseLocation() {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        locationLauncher.launch(intent);
    }

    @SuppressLint("SetTextI18n")
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(this, l -> {
            if (l != null) {
                double latitude = l.getLatitude();
                double longitude = l.getLongitude();
                location.setText(latitude + ", " + longitude);
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItem() {
        Item item = new Item();
        item.type = ((RadioButton) findViewById(typeGroup.getCheckedRadioButtonId())).getText().toString();
        item.name = name.getText().toString();
        item.phone = phone.getText().toString();
        item.description = description.getText().toString();
        item.date = date.getText().toString();
        item.location = location.getText().toString();

        AppDatabase.getInstance(this).itemDao().insert(item);
        finish();
    }
}
