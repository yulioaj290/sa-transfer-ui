package com.surfacesoft.yaj.satransferui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Yulio on 05/08/2015.
 */
public class CustomEditTextPreference extends EditTextPreference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextPreference(Context context) {
        super(context);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            String value = this.getEditText().getText().toString();
            if (callChangeListener(value)) {
                if (value.length() != 4) {
                    setText("");
                    Toast.makeText(this.getContext(), "La clave debe poseer 4 dígitos.", Toast.LENGTH_SHORT).show();
                } else {
                    setText(value);
                }
            }
        }
    }

    @Override
    public void setText(String text) {
        super.setText(Cryptography.encryptIt(text));
    }

    @Override
    public String getText() {
        return Cryptography.decryptIt(super.getText());
    }
}
