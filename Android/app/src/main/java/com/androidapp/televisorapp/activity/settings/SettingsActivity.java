package com.androidapp.televisorapp.activity.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.home.HomeActivity;
import com.androidapp.televisorapp.activity.main.MainActivity;
import com.androidapp.televisorapp.activity.map.MapsActivity;
import com.androidapp.televisorapp.models.Television;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ArrayList<Television> televisionList = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();

    private String theme = "";

    private void initializeToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.settings));
    }

    private void startMapActivity() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("TELEVISION_LIST", televisionList);
        intent.putExtra("ID_LIST", idList);

        if (!this.theme.equals("")) {
            intent.putExtra("THEME", this.theme);
        }

        startActivity(intent);
    }

    private void initializeBottomNavView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.ic_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.ic_home) {
                finish();
                overridePendingTransition(0 ,0);

                return true;
            } else if (id == R.id.ic_map) {
                startMapActivity();

                overridePendingTransition(0 ,0);
                finish();

                return true;
            } else {
                return true;
            }
        });
    }

    private void initializeLists(Intent intent) {
        idList = intent.getStringArrayListExtra("ID_LIST");
        televisionList = (ArrayList<Television>) intent.getSerializableExtra("TELEVISION_LIST");
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
        setContentView(R.layout.activity_settings);

        initializeToolbar();
        initializeLists(getIntent());
        initializeBottomNavView();
    }

    private void setLocate(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang.toLowerCase()));
        res.updateConfiguration(conf, dm);

        Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
        Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);

        finishAffinity();

        if (!this.theme.equals("")) {
            intentHome.putExtra("THEME", this.theme);
            intentSettings.putExtra("THEME", this.theme);
        }

        startActivity(intentHome);
        startActivity(intentSettings);
    }

    public void onClick_radioButton_lang(View v) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup_lang);
        int radioID = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = findViewById(radioID);

        if (radioButton.getTag().toString().equals("english")) {
            setLocate("en");
        } else {
            setLocate("ru");
        }
    }

    private void setCustomTheme(String theme) {
        finishAffinity();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("THEME", theme);
        startActivity(intent);

        intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra("THEME", theme);
        startActivity(intent);
    }

    public void onClick_radioButton_theme(View v) {
        RadioGroup radioGroup = findViewById(R.id.radioGroup_theme);
        int radioID = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = findViewById(radioID);

        if (radioButton.getTag().toString().equals("light")) {
            setCustomTheme("light");
        } else {
            setCustomTheme("night");
        }
    }

    public void onClick_logout(View v) {
        Button button = findViewById(R.id.button_logOut);
        YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(button);

        finishAffinity();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if (!this.theme.equals("")) {
            intent.putExtra("THEME", this.theme);
        }

        startActivity(intent);
    }
}