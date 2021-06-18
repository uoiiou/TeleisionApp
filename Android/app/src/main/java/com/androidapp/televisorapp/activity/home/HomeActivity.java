package com.androidapp.televisorapp.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.home.adapter.HomeAdapter;
import com.androidapp.televisorapp.activity.map.MapsActivity;
import com.androidapp.televisorapp.activity.item.ItemActivity;
import com.androidapp.televisorapp.activity.settings.SettingsActivity;
import com.androidapp.televisorapp.models.Item;
import com.androidapp.televisorapp.models.Television;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private HomeAdapter homeAdapter;

    private final ArrayList<Television> televisionList = new ArrayList<>();
    private final ArrayList<String> idList = new ArrayList<>();
    private final ArrayList<Item> itemList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private boolean isDelete = false;
    private String theme = "";

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_home));
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(itemList);
        homeAdapter.setFullList();
        recyclerView.setAdapter(homeAdapter);
    }

    private void startNewBottomActivity(Intent intent) {
        intent.putExtra("TELEVISION_LIST", televisionList);
        intent.putExtra("ID_LIST", idList);

        if (!this.theme.equals("")) {
            intent.putExtra("THEME", this.theme);
        }

        startActivity(intent);

        overridePendingTransition(0 ,0);
    }

    private void initializeBottomNavView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.ic_home) {
                return true;
            } else if (id == R.id.ic_map) {
                startNewBottomActivity(new Intent(getApplicationContext(), MapsActivity.class));
                return true;
            } else {
                startNewBottomActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            }
        });
    }

    private String getImageURL(String imageLinks) {
        String [] strings = imageLinks.split(" ");
        return strings[0];
    }

    private void castTelevisionToItem(Television television) {
        itemList.add(new Item(
                getImageURL(television.getImageLinks()),
                television.getBrand(),
                television.getModel(),
                television.getYear(),
                television.getPrice())
        );
    }

    private void clearLists() {
        televisionList.clear();
        itemList.clear();
        idList.clear();
    }

    private void updateLists(Television television, String id) {
        televisionList.add(television);
        castTelevisionToItem(Objects.requireNonNull(television));
        idList.add(id);
    }

    private void startNewEditActivity(int position) {
        Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
        intent.putExtra("ITEM_TYPE", "EDIT");
        intent.putExtra("TELEVISION", televisionList.get(position));
        intent.putExtra("ID", idList.get(position));

        if (!this.theme.equals("")) {
            intent.putExtra("THEME", this.theme);
        }

        startActivity(intent);
    }

    private void deleteItemFromRecyclerView(DatabaseReference databaseReference, int position) {
        itemList.remove(position);
        homeAdapter.notifyItemRemoved(position);
        homeAdapter.setFullList();

        televisionList.remove(position);

        isDelete = true;
        databaseReference.child(idList.get(position)).removeValue();

        idList.remove(position);
    }

    private void initializeDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("televisions");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearLists();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    updateLists(dataSnapshot.getValue(Television.class), dataSnapshot.getKey());
                }

                if (!isDelete) {
                    homeAdapter.notifyDataSetChanged();
                    homeAdapter.setFullList();
                } else
                    isDelete = false;

                homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        startNewEditActivity(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        deleteItemFromRecyclerView(databaseReference, position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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
        setContentView(R.layout.activity_home);

        initializeToolbar();
        initializeRecyclerView();
        initializeBottomNavView();
        initializeDatabase();
    }

    @Override
    protected void onResume() {
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_toolbar, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_add) {
            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
            intent.putExtra("ITEM_TYPE", "ADD");

            if (!this.theme.equals("")) {
                intent.putExtra("THEME", this.theme);
            }

            startActivity(intent);
        }

        return true;
    }
}