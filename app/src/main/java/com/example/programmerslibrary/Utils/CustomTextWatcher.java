package com.example.programmerslibrary.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {

    View v;
    View w;
    EditText[] edList;

    public CustomTextWatcher(EditText[] edList, Button v, Button w) {
        this.v = v;
        this.w = w;
        this.edList = edList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        for (EditText editText : edList) {
            if (editText.getText().toString().trim().length() <= 0) {
                v.setEnabled(false);
                w.setEnabled(false);
                break;
            }
            else {
                v.setEnabled(true);
                w.setEnabled(true);
            }
        }
    }
}