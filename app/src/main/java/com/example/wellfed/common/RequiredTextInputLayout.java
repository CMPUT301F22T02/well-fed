/*
 * RequiredTextInputLayout
 *
 * Version: v1.0.0
 *
 * Date: 2022-11-03
 *
 * Copyright notice:
 * This file is part of well-fed.
 *
 * well-fed is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * well-fed is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with well-fed. If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.wellfed.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

// TODO: https://developer.android.com/reference/android/text/TextWatcher

/**
 * The RequiredTextInputLayout class extends the TextInputLayout
 * class to provide methods for performing data validation.
 * <p>
 * Citation:
 * TextInputLayout. Android Developers. (n.d.). Retrieved September 26, 2022,
 * from https://developer.android.com/reference/com/google/android/material/
 * textfield/TextInputLayout
 *
 * @author Steven Tang
 * @version v1.0.0 2022-09-26
 **/
public class RequiredTextInputLayout extends TextInputLayout
        implements TextInputLayout.OnEditTextAttachedListener, TextWatcher {
    protected EditText editText;
    protected String initialText;

    public RequiredTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addOnEditTextAttachedListener(this);
    }

    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addOnEditTextAttachedListener(this);
    }

    /*
     * The onEditTextAttached method sets an OnClickListener on the
     * TextInputLayout.
     */
    @Override
    public void onEditTextAttached(
            @NonNull TextInputLayout textInputLayout) {
        this.editText = Objects.requireNonNull(textInputLayout.getEditText());
        this.editText.addTextChangedListener(this);
        this.initialText = this.editText.getText().toString();
    }

    public void setPlaceholderText(String initialText) {
        this.initialText = initialText;
        this.editText.setText(initialText);
    }

    public String getText() {
        return this.editText.getText().toString();
    }

    public Boolean hasChanges() {
        return !this.initialText.equals(this.getText());
    }

    /*
     * The isNonEmpty method validates that the EditText is non-empty.
     * If it is empty, it displays an error message.
     */
    private Boolean isNonEmpty() {
        if (this.editText.getText().toString().isEmpty()) {
            this.setError(this.getHint() + " is required");
            return false;
        } else {
            this.setError(null);
            return true;
        }
    }

    public Boolean isValid() {
        return this.isNonEmpty();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                  int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1,
                              int i2) {
    }

    @Override public void afterTextChanged(Editable editable) {
        this.isValid();
    }
}
