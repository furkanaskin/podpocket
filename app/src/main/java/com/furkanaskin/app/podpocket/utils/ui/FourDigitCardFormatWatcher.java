package com.furkanaskin.app.podpocket.utils.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Formats the watched EditText to a credit card number
 */
public class FourDigitCardFormatWatcher implements TextWatcher {

    public FourDigitCardFormatWatcher(EditText text) {
        this.text = text;
    }

    private EditText text;
    private String a;
    private int keyDel;

    // Change this to what you want... ' ', '-' etc..
    private static final char space = ' ';

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Remove spacing char
        boolean flag = true;
        String eachBlock[] = text.getText().toString().split("-");
        for (int i = 0; i < eachBlock.length; i++) {
            if (eachBlock[i].length() > 4) {
                flag = false;
            }
        }
        if (flag) {

            text.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                        keyDel = 1;
                    return false;
                }
            });

            if (keyDel == 0) {

                if (((text.getText().length() + 1) % 5) == 0) {

                    if (text.getText().toString().split("-").length <= 3) {
                        text.setText(text.getText() + " ");
                        text.setSelection(text.getText().length());
                    }
                }
                a = text.getText().toString();
            } else {
                a = text.getText().toString();
                keyDel = 0;
            }

        } else {
            text.setText(a);
        }
    }
}