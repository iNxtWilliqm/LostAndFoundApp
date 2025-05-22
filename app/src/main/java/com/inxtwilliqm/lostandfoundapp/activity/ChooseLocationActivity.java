package com.inxtwilliqm.lostandfoundapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.inxtwilliqm.lostandfoundapp.BuildConfig;
import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.databinding.ActivityChooseLocationBinding;

import java.util.Arrays;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityChooseLocationBinding binding;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);

        binding = ActivityChooseLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeMap();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupAutoComplete();
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.mapPlaces);
        mapFragment.getMapAsync(this);
    }

    private void setupAutoComplete() {
        ActivityResultLauncher<Intent> startAutoComplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(place.getAddress()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        Toast.makeText(this, "Selected: " + place.getName(), Toast.LENGTH_SHORT).show();

                        // Return the selected place to CreateActivity
                        Intent intent = new Intent();
                        intent.putExtra("selected_latitude", latLng.latitude);
                        intent.putExtra("selected_longitude", latLng.longitude);
                        intent.putExtra("selected_location", place.getName());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        );

        binding.pickPlaceButton.setOnClickListener(v -> {
            Place.Field[] fields = new Place.Field[]{Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS};
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(fields))
                .build(this);
            startAutoComplete.launch(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}
