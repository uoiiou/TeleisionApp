package com.androidapp.televisorapp.activity.item.validation;

import android.content.Context;
import android.widget.EditText;

import com.androidapp.televisorapp.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

public class InputValidationItem {

    private final Context context;

    public InputValidationItem(Context context) {
        this.context = context;
    }

    private void showError(Techniques techniques, EditText editText, String error) {
        YoYo.with(techniques).duration(700).repeat(0).playOn(editText);
        editText.setError(error);
        editText.requestFocus();
    }

    private boolean isEmpty(List<EditText> editTextList) {
        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);

            if (editText.getText().toString().isEmpty())
            {
                showError(Techniques.RubberBand, editText, context.getString(R.string.fill_fields));
                return true;
            }
        }

        return false;
    }

    private boolean isYearCorrect(EditText editText) {
        if (Integer.parseInt(editText.getText().toString()) < 2010) {
            showError(Techniques.RubberBand, editText, context.getString(R.string.incorrect_year));
            return false;
        }

        return true;
    }

    private EditText findEditTextById(List<EditText> editTextList, int id) {
        EditText result = null;

        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);

            if (editText.getId() == id) {
                result = editText;
                break;
            }
        }

        return result;
    }

    private boolean isResolutionCorrect(EditText editText) {
        if (editText.getText().toString().contains(" x ")) {
            return true;
        }

        showError(Techniques.RubberBand, editText, context.getString(R.string.invalid_input_format));
        return false;
    }

    private boolean isLocationCorrect(EditText firstCoordinate, EditText secondCoordinate) {
        boolean first = true;
        boolean second = true;

        if (!firstCoordinate.getText().toString().contains(".")) {
            first = false;
            showError(Techniques.RubberBand, firstCoordinate, context.getString(R.string.incorrect_data_entered));
        }

        if (!secondCoordinate.getText().toString().contains(".")) {
            second = false;
            showError(Techniques.RubberBand, secondCoordinate, context.getString(R.string.incorrect_data_entered));
        }

        return first && second;
    }

    public boolean validationInput(List<EditText> editTextList) {
        boolean isEmpty = isEmpty(editTextList);
        if (isEmpty)
            return false;

        boolean isYearCorrect = isYearCorrect(findEditTextById(editTextList, R.id.editText_yearOfIssue));
        if (!isYearCorrect)
            return false;

        boolean isResolutionCorrect = isResolutionCorrect(findEditTextById(editTextList, R.id.editText_resolution));
        if (!isResolutionCorrect)
            return false;

        return isLocationCorrect(
                findEditTextById(editTextList, R.id.editText_firstCoordinate),
                findEditTextById(editTextList, R.id.editText_secondCoordinate)
        );
    }

    public boolean validationImageList(List<String> imageList, EditText editText) {
        if (imageList.size() < 2) {
            showError(Techniques.Swing, editText, context.getString(R.string.two_links_image));
            return false;
        }

        return true;
    }
}