package com.androidapp.televisorapp.activity.map;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.item.ItemActivity;
import com.androidapp.televisorapp.activity.settings.SettingsActivity;
import com.androidapp.televisorapp.models.Television;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<Television> televisionList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();

    private String theme = "";

    private void startSettingsActivity() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra("TELEVISION_LIST", televisionList);
        intent.putExtra("ID_LIST", idList);

        if (!this.theme.equals("")) {
            intent.putExtra("THEME", this.theme);
        }

        startActivity(intent);
    }

    private void initializeBottomNavView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.ic_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.ic_home) {
                finish();
                overridePendingTransition(0 ,0);

                return true;
            } else if (id == R.id.ic_map) {
                return true;
            } else {
                startSettingsActivity();

                finish();
                overridePendingTransition(0 ,0);

                return true;
            }
        });
    }

    private void initializeLists(Intent intent) {
        idList = intent.getStringArrayListExtra("ID_LIST");
        televisionList = (ArrayList<Television>) intent.getSerializableExtra("TELEVISION_LIST");
    }

    private void initializeMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initializeTheme(Intent intent) {
        String result = intent.getStringExtra("THEME");

        if (result == null) {
            this.theme = "";
        } else {
            if (result.equals("light")) {
                this.theme = "light";
                setTheme(R.style.themeLight);
            } else {
                this.theme = "night";
                setTheme(R.style.themeNight);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeTheme(getIntent());
        setContentView(R.layout.activity_maps);

        initializeLists(getIntent());
        initializeBottomNavView();
        initializeMapFragment();
    }

    private void addMarkerOnGoogleMap(GoogleMap googleMap) {
        for (int i = 0; i < televisionList.size(); i++) {
            LatLng point = new LatLng(
                    Double.parseDouble(televisionList.get(i).getFirstPoint()),
                    Double.parseDouble(televisionList.get(i).getSecondPoint())
            );

            Marker marker = googleMap.addMarker(
                    new MarkerOptions().position(point).
                            title(televisionList.get(i).getBrand() + " " + televisionList.get(i).getModel()));

            marker.setTag(idList.get(i));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }
    }

    private int getIndex(String id) {
        for (int i = 0; i < idList.size(); i++) {
            if (idList.get(i).equals(id))
                return i;
        }

        return 0;
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        addMarkerOnGoogleMap(googleMap);

        googleMap.setOnInfoWindowClickListener(marker -> {
            int index = getIndex((String) marker.getTag());

            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
            intent.putExtra("ITEM_TYPE", "EDIT");
            intent.putExtra("TELEVISION", televisionList.get(index));
            intent.putExtra("ID", idList.get(index));

            if (!this.theme.equals("")) {
                intent.putExtra("THEME", this.theme);
            }

            startActivity(intent);
        });
    }
}