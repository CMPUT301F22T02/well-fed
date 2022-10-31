/*
 * RequiredTextInputLayout
 *
 * Version: v1.0.0
 *
 * Date: 2022-09-26
 *
 * Copyright notice:
 * This file is part of stang5-Foodbook.
 *
 * stang5-Foodbook is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * stang5-Foodbook is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with stang5-Foodbook. If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.wellfed.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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
public class RequiredDropdownTextInputLayout extends RequiredTextInputLayout {
    private AutoCompleteTextView autoCompleteTextView;

    public RequiredDropdownTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public RequiredDropdownTextInputLayout(@NonNull Context context,
                                           @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RequiredDropdownTextInputLayout(@NonNull Context context,
                                           @Nullable AttributeSet attrs,
                                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPlaceholderText(String initialText) {
        this.initialText = initialText;
        this.autoCompleteTextView.setText(initialText, false);
    }

    /*
     * The onEditTextAttached method sets an OnClickListener on the
     * TextInputLayout.
     */
    @Override
    public void onEditTextAttached(
            @NonNull TextInputLayout textInputLayout) {
        super.onEditTextAttached(textInputLayout);
        this.autoCompleteTextView =
                (AutoCompleteTextView) Objects.requireNonNull(
                        textInputLayout.getEditText());
    }
}
