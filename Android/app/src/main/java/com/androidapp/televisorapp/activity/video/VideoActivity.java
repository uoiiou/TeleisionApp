package com.androidapp.televisorapp.activity.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidapp.televisorapp.R;

import java.util.Objects;

public class VideoActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private String theme = "";

    private void initializeProgressBar() {
        progressBar = findViewById(R.id.progressBar_video);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.video));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareVideoView(String videoLink) {
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(videoLink));
        videoView.start();

        videoView.setOnErrorListener((mediaPlayer, i, i1) -> {
            progressBar.setVisibility(View.GONE);
            finish();
            Toast.makeText(getApplicationContext(), getString(R.string.cant_play_this_video), Toast.LENGTH_LONG).show();

            return false;
        });

        videoView.setOnPreparedListener(mediaPlayer -> progressBar.setVisibility(View.GONE));
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
        setContentView(R.layout.activity_video);

        initializeProgressBar();
        initializeToolbar();
        prepareVideoView(getIntent().getStringExtra("VIDEO_LINK"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}