package com.androidapp.televisorapp.activity.item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.gallery.GalleryActivity;
import com.androidapp.televisorapp.activity.item.validation.InputValidationItem;
import com.androidapp.televisorapp.activity.video.VideoActivity;
import com.androidapp.televisorapp.models.Television;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemActivity extends AppCompatActivity {

    private EditText editText_brand, editText_model, editText_year, editText_price,
            editText_diagonal, editText_resolution, editText_matrixFrequency,
            editText_localFirst, editText_localSecond, editText_videoLink, editText_imageURL;
    private CheckBox checkBox_wifi, checkBox_hdmi, checkBox_usb;
    private Spinner spinner_color;
    private ProgressBar progressBar;
    private Button button_addImage, button_saveItem;
    private ListView listView;

    private final List<EditText> editTextList = new ArrayList<>();
    private final List<String> imageURLList = new ArrayList<>();

    private boolean isAdd = true;
    private String id;

    private String theme = "";

    public void setItemsInListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, imageURLList);

        listView.setAdapter(adapter);
    }

    private void initializeActivityCommon() {
        editTextList.add(editText_brand = findViewById(R.id.editText_brand));
        editTextList.add(editText_model = findViewById(R.id.editText_model));
        editTextList.add(editText_year = findViewById(R.id.editText_yearOfIssue));
        editTextList.add(editText_price = findViewById(R.id.editText_price));
        editTextList.add(editText_diagonal = findViewById(R.id.editText_diagonal));
        editTextList.add(editText_resolution = findViewById(R.id.editText_resolution));
        editTextList.add(editText_matrixFrequency = findViewById(R.id.editText_matrixFrequency));
        editTextList.add(editText_localFirst = findViewById(R.id.editText_firstCoordinate));
        editTextList.add(editText_localSecond = findViewById(R.id.editText_secondCoordinate));
        editTextList.add(editText_videoLink = findViewById(R.id.editText_videoURL));
        editText_imageURL = findViewById(R.id.editText_imageURL);

        checkBox_wifi = findViewById(R.id.checkBox_wifi);
        checkBox_hdmi = findViewById(R.id.checkBox_hdmi);
        checkBox_usb = findViewById(R.id.checkBox_usb);

        spinner_color = findViewById(R.id.spinner_color);

        progressBar = findViewById(R.id.progressBar_item);

        button_saveItem = findViewById(R.id.button_saveItem);
        button_addImage = findViewById(R.id.button_addImageURL);

        listView = findViewById(R.id.listView_imageURL);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            imageURLList.remove(position);
            setItemsInListView();
        });
    }

    private void initializeToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private int getColorIndex(String color) {
        for (int i = 0; i < spinner_color.getCount(); i++) {
            if (spinner_color.getItemAtPosition(i).equals(color))
                return i;
        }

        return 0;
    }

    private void initializeActivityEdit(Television television) {
        editText_brand.setFocusable(false);
        editText_brand.setCursorVisible(false);
        editText_brand.setText(television.getBrand());

        editText_model.setFocusable(false);
        editText_model.setCursorVisible(false);
        editText_model.setText(television.getModel());

        editText_year.setFocusable(false);
        editText_year.setCursorVisible(false);
        editText_year.setText(television.getYear());

        editText_price.setFocusable(false);
        editText_price.setCursorVisible(false);
        editText_price.setText(television.getPrice());

        editText_diagonal.setFocusable(false);
        editText_diagonal.setCursorVisible(false);
        editText_diagonal.setText(television.getDiagonal());

        editText_resolution.setFocusable(false);
        editText_resolution.setCursorVisible(false);
        editText_resolution.setText(television.getResolution());

        editText_matrixFrequency.setFocusable(false);
        editText_matrixFrequency.setCursorVisible(false);
        editText_matrixFrequency.setText(television.getFrequency());

        editText_localFirst.setFocusable(false);
        editText_localFirst.setCursorVisible(false);
        editText_localFirst.setText(television.getFirstPoint());

        editText_localSecond.setFocusable(false);
        editText_localSecond.setCursorVisible(false);
        editText_localSecond.setText(television.getSecondPoint());

        editText_videoLink.setFocusable(false);
        editText_videoLink.setCursorVisible(false);
        editText_videoLink.setText(television.getVideoLink());

        editText_imageURL.setFocusable(false);
        editText_imageURL.setCursorVisible(false);
        editText_imageURL.setText("");

        checkBox_wifi.setEnabled(false);
        checkBox_wifi.setChecked((television.getWifi().equals("true")));

        checkBox_hdmi.setEnabled(false);
        checkBox_hdmi.setChecked((television.getHdmi().equals("true")));

        checkBox_usb.setEnabled(false);
        checkBox_usb.setChecked((television.getUsb().equals("true")));

        spinner_color.setEnabled(false);
        spinner_color.setSelection(getColorIndex(television.getColor()));

        button_saveItem.setEnabled(false);
        button_addImage.setEnabled(false);

        String[] strings = television.getImageLinks().split(" ");
        imageURLList.clear();

        imageURLList.addAll(Arrays.asList(strings));
        setItemsInListView();
        listView.setEnabled(false);
    }

    private void prepareEditActivity(Intent intent) {
        isAdd = false;
        id = intent.getStringExtra("ID");

        initializeToolbar(findViewById(R.id.toolbar), getString(R.string.edit_title_edit));
        initializeActivityEdit((Television) intent.getSerializableExtra("TELEVISION"));
    }

    private void prepareAddActivity() {
        isAdd = true;
        initializeToolbar(findViewById(R.id.toolbar),  getString(R.string.edit_title_add));
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
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        String item_type = intent.getStringExtra("ITEM_TYPE");
        initializeActivityCommon();

        if (item_type.equals("EDIT")) {
            prepareEditActivity(intent);
        } else {
            prepareAddActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isAdd) {
            getMenuInflater().inflate(R.menu.menu_edit_toolbar, menu);
        }
        return true;
    }

    private void enableAllFields() {
        editText_brand.setFocusable(true);
        editText_brand.setFocusableInTouchMode(true);
        editText_brand.setCursorVisible(true);

        editText_model.setFocusable(true);
        editText_model.setFocusableInTouchMode(true);
        editText_model.setCursorVisible(true);

        editText_year.setFocusable(true);
        editText_year.setFocusableInTouchMode(true);
        editText_year.setCursorVisible(true);

        editText_price.setFocusable(true);
        editText_price.setFocusableInTouchMode(true);
        editText_price.setCursorVisible(true);

        editText_diagonal.setFocusable(true);
        editText_diagonal.setFocusableInTouchMode(true);
        editText_diagonal.setCursorVisible(true);

        editText_resolution.setFocusable(true);
        editText_resolution.setFocusableInTouchMode(true);
        editText_resolution.setCursorVisible(true);

        editText_matrixFrequency.setFocusable(true);
        editText_matrixFrequency.setFocusableInTouchMode(true);
        editText_matrixFrequency.setCursorVisible(true);

        editText_localFirst.setFocusable(true);
        editText_localFirst.setFocusableInTouchMode(true);
        editText_localFirst.setCursorVisible(true);

        editText_localSecond.setFocusable(true);
        editText_localSecond.setFocusableInTouchMode(true);
        editText_localSecond.setCursorVisible(true);

        editText_videoLink.setFocusable(true);
        editText_videoLink.setFocusableInTouchMode(true);
        editText_videoLink.setCursorVisible(true);

        editText_imageURL.setFocusable(true);
        editText_imageURL.setFocusableInTouchMode(true);
        editText_imageURL.setCursorVisible(true);

        checkBox_wifi.setEnabled(true);
        checkBox_hdmi.setEnabled(true);
        checkBox_usb.setEnabled(true);

        spinner_color.setEnabled(true);

        button_saveItem.setEnabled(true);
        button_addImage.setEnabled(true);
        listView.setEnabled(true);
    }

    private void startVideoActivity() {
        String videoLink = editText_videoLink.getText().toString();

        if (!videoLink.equals("")) {
            Intent intent = new Intent(getApplicationContext(), VideoActivity.class);

            intent.putExtra("VIDEO_LINK", editText_videoLink.getText().toString());

            if (!this.theme.equals("")) {
                intent.putExtra("THEME", this.theme);
            }

            startActivity(intent);
        }
    }

    private void startGalleryActivity() {
        if (imageURLList.size() >= 2) {
            Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);

            StringBuilder str = new StringBuilder();

            for (int i = 0; i < imageURLList.size(); i++) {
                str.append(imageURLList.get(i)).append(" ");
            }

            intent.putExtra("IMAGE_LIST", str.toString());

            if (!this.theme.equals("")) {
                intent.putExtra("THEME", this.theme);
            }

            startActivity(intent);
        } else {
            InputValidationItem inputValidationItem = new InputValidationItem(getApplicationContext());
            inputValidationItem.validationImageList(imageURLList, editText_imageURL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.ic_edit) {
            YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(findViewById(R.id.ic_edit));
            enableAllFields();
        } else if (id == R.id.ic_video) {
            startVideoActivity();
        } else if (id == R.id.ic_gallery) {
            startGalleryActivity();
        }

        return true;
    }

    public void onClick_addImageURL(View view) {
        if (!editText_imageURL.getText().toString().equals("")) {
            imageURLList.add(editText_imageURL.getText().toString());

            setItemsInListView();
            editText_imageURL.setText("");
        } else {
            YoYo.with(Techniques.RubberBand).duration(700).repeat(0).playOn(editText_imageURL);
            editText_imageURL.setError(getString(R.string.field_must_be_filled));
            editText_imageURL.requestFocus();
        }
    }

    private String getString(EditText editText) {
        return editText.getText().toString();
    }

    private Television getNewTelevision() {
        return new Television(
                getString(editText_brand),
                getString(editText_model),
                getString(editText_year),
                getString(editText_price),
                getString(editText_diagonal),
                getString(editText_resolution),
                getString(editText_matrixFrequency),
                String.valueOf(checkBox_wifi.isChecked()),
                String.valueOf(checkBox_hdmi.isChecked()),
                String.valueOf(checkBox_usb.isChecked()),
                spinner_color.getSelectedItem().toString(),
                getString(editText_localFirst),
                getString(editText_localSecond),
                getString(editText_videoLink), imageURLList);
    }

    private void clearInputs() {
        editText_brand.setText("");
        editText_model.setText("");

        editText_year.setText("");
        editText_price.setText("");

        editText_diagonal.setText("");
        editText_resolution.setText("");
        editText_matrixFrequency.setText("");

        checkBox_wifi.setChecked(false);
        checkBox_hdmi.setChecked(false);
        checkBox_usb.setChecked(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.colors,
                android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_color.setAdapter(adapter);

        editText_localFirst.setText("");
        editText_localSecond.setText("");

        editText_videoLink.setText("");
        editText_imageURL.setText("");

        imageURLList.clear();
        listView.setAdapter(null);
    }

    private void clearFocus() {
        editText_brand.clearFocus();
        editText_model.clearFocus();

        editText_year.clearFocus();
        editText_price.clearFocus();

        editText_diagonal.clearFocus();
        editText_resolution.clearFocus();
        editText_matrixFrequency.clearFocus();

        checkBox_wifi.clearFocus();
        checkBox_hdmi.clearFocus();
        checkBox_usb.clearFocus();

        spinner_color.clearFocus();

        editText_localFirst.clearFocus();
        editText_localSecond.clearFocus();

        editText_videoLink.clearFocus();
        editText_imageURL.clearFocus();
    }

    private void commonOnClickSave() {
        progressBar.setVisibility(View.GONE);
        clearInputs();
        clearFocus();

        finish();
    }

    public void onClick_saveItem(View view) {
        InputValidationItem inputValidationItem = new InputValidationItem(getApplicationContext());
        if (!inputValidationItem.validationInput(editTextList))
            return;

        if (!inputValidationItem.validationImageList(imageURLList, editText_imageURL))
            return;

        YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(button_saveItem);
        progressBar.setVisibility(View.VISIBLE);

        Television television = getNewTelevision();

        if (isAdd) {
            FirebaseDatabase.getInstance().getReference("televisions").push().setValue(television).
                    addOnCompleteListener(taskCreateTelevision -> {
                if (taskCreateTelevision.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.television_added_success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.television_added_failed), Toast.LENGTH_LONG).show();
                }

                commonOnClickSave();
            });
        } else {
            FirebaseDatabase.getInstance().getReference("televisions").child(id).setValue(television).
                    addOnCompleteListener(taskCreateTelevision -> {
                if (taskCreateTelevision.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.television_update_success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.television_update_failed), Toast.LENGTH_LONG).show();
                }

                commonOnClickSave();
            });
        }
    }
}