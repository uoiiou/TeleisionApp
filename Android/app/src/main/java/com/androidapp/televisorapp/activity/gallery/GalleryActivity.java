package com.androidapp.televisorapp.activity.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.gallery.adapter.GalleryAdapter;
import com.androidapp.televisorapp.activity.image.ImageActivity;
import com.androidapp.televisorapp.models.Image;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {

    private String theme = "";

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

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.gallery_title));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private ArrayList<Image> getImageURLs() {
        ArrayList<Image> imageList = new ArrayList<>();
        String[] urls = getIntent().getStringExtra("IMAGE_LIST").split(" ");

        for (String url : urls) {
            imageList.add(new Image(url));
        }

        return imageList;
    }

    private void initializeRecyclerView(ArrayList<Image> imageList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        GalleryAdapter adapter = new GalleryAdapter(imageList);
        adapter.setOnItemClickListener(position -> {
            Image image = imageList.get(position);

            Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
            intent.putExtra("IMAGE", image);

            if (!this.theme.equals("")) {
                intent.putExtra("THEME", this.theme);
            }

            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeTheme(getIntent());
        setContentView(R.layout.activity_gallery);

        initializeToolbar();
        ArrayList<Image> imageList = getImageURLs();
        initializeRecyclerView(imageList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}