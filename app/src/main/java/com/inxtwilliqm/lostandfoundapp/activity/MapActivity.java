package com.inxtwilliqm.lostandfoundapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inxtwilliqm.lostandfoundapp.R;
import com.inxtwilliqm.lostandfoundapp.database.AppDatabase;
import com.inxtwilliqm.lostandfoundapp.databinding.ActivityMapBinding;
import com.inxtwilliqm.lostandfoundapp.entity.Item;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapBinding binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Enable main thread queries for simplicity
        db = AppDatabase.getInstance(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Get items directly on UI thread (for demo only!)
        List<Item> items = db.itemDao().getAll();

        if (items.isEmpty()) {
            Toast.makeText(this, "No items to display", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add all items to map
        for (Item item : items) {
            addItemToMap(item);
        }

        // Zoom to first item
        if (!items.isEmpty()) {
            zoomToItem(items.get(0));
        }
    }

    private void addItemToMap(Item item) {
        try {
            // Parse "lat, lng" string
            String[] parts = item.location.split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lng = Double.parseDouble(parts[1].trim());

            LatLng position = new LatLng(lat, lng);

            mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(item.name)
                .snippet(item.type + "\n" + item.description));

        } catch (Exception e) {
            Toast.makeText(this, "Error showing: " + item.name, Toast.LENGTH_SHORT).show();
        }
    }

    private void zoomToItem(Item item) {
        try {
            String[] parts = item.location.split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lng = Double.parseDouble(parts[1].trim());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 12f));
        } catch (Exception e) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(0, 0), 2f)); // Default view if error
        }
    }
}