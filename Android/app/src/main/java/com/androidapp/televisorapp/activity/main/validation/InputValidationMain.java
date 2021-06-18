package com.androidapp.televisorapp.activity.main.validation;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.androidapp.televisorapp.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class InputValidationMain {

    private final EditText editTextEmail;
    private final EditText editTextPassword;

    private final String email;
    private final String password;

    private final Context context;

    public InputValidationMain(Context context, EditText email, EditText password) {
        this.context = context;

        this.editTextEmail = email;
        this.editTextPassword = password;

        this.email = email.getText().toString();
        this.password = password.getText().toString();
    }

    private void showError(EditText editText, String error) {
        YoYo.with(Techniques.RubberBand).duration(700).repeat(0).playOn(editText);
        editText.setError(error);
        editText.requestFocus();
    }

    public boolean validationInput() {
        if (email.isEmpty()) {
            showError(editTextEmail, context.getString(R.string.email_required));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(editTextEmail, context.getString(R.string.valid_email));
            return false;
        }

        if (password.isEmpty()) {
            showError(editTextPassword, context.getString(R.string.password_required));
            return false;
        }

        if (password.length() < 6) {
            showError(editTextPassword, context.getString(R.string.min_password_len));
            return false;
        }

        return true;
    }

    public void errorSign(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        YoYo.with(Techniques.RubberBand).duration(700).repeat(0).playOn(editTextEmail);
        YoYo.with(Techniques.RubberBand).duration(700).repeat(0).playOn(editTextPassword);
    }
}