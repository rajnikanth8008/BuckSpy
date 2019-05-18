package com.example.administrator.buckspy;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

class CommonUtils {
    static void addTextWatcher(final EditText... editText) {
        try {
            for (final EditText text : editText) {
                text.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String value = editable.toString();
                        switch (text.getId()){
//                            case (editText[0].getId()) :
//                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("Textwatcher", e.getMessage());
        }
    }
}
