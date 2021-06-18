package com.androidapp.televisorapp.activity.image;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.models.Image;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    private String theme = "";

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.image_view));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadImage() {
        Image image = (Image) getIntent().getSerializableExtra("IMAGE");
        ImageView imageView = findViewById(R.id.imageView_image);

        Picasso.get().load(image.getImageURL()).into(imageView);
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
        setContentView(R.layout.activity_image);

        initializeToolbar();
        loadImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}