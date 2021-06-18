package com.androidapp.televisorapp.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapp.televisorapp.R;
import com.androidapp.televisorapp.activity.home.HomeActivity;
import com.androidapp.televisorapp.activity.main.validation.InputValidationMain;
import com.androidapp.televisorapp.models.User;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewTitle, textViewHelp;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSign;
    private ProgressBar progressBar;

    private boolean isLogin = true;

    private String theme = "";

    private void initializeActivity() {
        textViewTitle = findViewById(R.id.textView_title);

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);

        buttonSign = findViewById(R.id.button_sign);

        progressBar = findViewById(R.id.progressBar_main);

        textViewHelp = findViewById(R.id.textView_help);
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
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initializeActivity();
    }

    private void clearInputs() {
        editTextEmail.setText("");
        editTextPassword.setText("");

        editTextEmail.clearFocus();
        editTextPassword.clearFocus();
    }

    private void animViews(Techniques techniques) {
        YoYo.with(Techniques.FlipInX).duration(700).repeat(0).playOn(textViewTitle);
        YoYo.with(Techniques.FlipInX).duration(700).repeat(0).playOn(buttonSign);
        YoYo.with(Techniques.FlipInX).duration(700).repeat(0).playOn(textViewHelp);

        YoYo.with(techniques).duration(300).repeat(0).playOn(editTextEmail);
        YoYo.with(techniques).duration(300).repeat(0).playOn(editTextPassword);
    }

    private void setTextForInput(String title, String button, String help) {
        textViewTitle.setText(title);
        buttonSign.setText(button);
        textViewHelp.setText(help);
    }

    public void onClick_helpShow(View v) {
        clearInputs();

        if (isLogin) {
            animViews(Techniques.SlideInLeft);
            setTextForInput(
                    getString(R.string.register),
                    getString(R.string.sign_up),
                    getString(R.string.already_have_an_account)
            );

            isLogin = false;
        } else {
            animViews(Techniques.SlideInRight);
            setTextForInput(
                    getString(R.string.login),
                    getString(R.string.sign_in),
                    getString(R.string.don_t_have_an_account)
            );

            isLogin = true;
        }
    }

    public void onClick_signButton(View v) {
        InputValidationMain inputValidation = new InputValidationMain(getApplicationContext(), editTextEmail, editTextPassword);
        if (!inputValidation.validationInput())
            return;

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.Pulse).duration(400).repeat(0).playOn(buttonSign);

        if (buttonSign.getText().toString().equals(getString(R.string.sign_in))) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(taskSignIn -> {
                if (taskSignIn.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                    if (!this.theme.equals("")) {
                        intent.putExtra("THEME", this.theme);
                    }

                    startActivity(intent);
                } else {
                    inputValidation.errorSign(getApplicationContext(), getString(R.string.failed_to_login));
                }

                progressBar.setVisibility(View.GONE);
                clearInputs();
            });
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(taskSignUp -> {
                if (taskSignUp.isSuccessful()) {
                    User user = new User(email, password);

                    FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance()
                            .getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(taskCreateUser -> {
                                if (taskCreateUser.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.user_reg_succes), Toast.LENGTH_LONG).show();
                                } else {
                                    inputValidation.errorSign(getApplicationContext(), getString(R.string.failed_to_register));
                                }
                        progressBar.setVisibility(View.GONE);
                        clearInputs();

                        onClick_helpShow(new View(getApplicationContext()));
                    });
                } else {
                    inputValidation.errorSign(getApplicationContext(), getString(R.string.failed_to_register));
                    progressBar.setVisibility(View.GONE);
                    clearInputs();
                }
            });
        }
    }
}