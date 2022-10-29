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
import android.util.AttributeSet;

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
public class RequiredTextInputLayout extends TextInputLayout {

    public RequiredTextInputLayout(@NonNull Context context) {
        super(context);
    }

    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
     * The isNonEmpty method validates that the EditText is non-empty.
     * If it is empty, it displays an error message.
     */
    public Boolean isNonEmpty() {
        if (Objects.requireNonNull(this.getEditText()).getText().toString()
                .isEmpty()) {
            this.setError(this.getHint() + " is required");
            return false;
        } else {
            this.setError(null);
            return true;
        }
    }

    /*
     * The isValidPositiveNumber method validates that the EditText is
     * a valid positive number. If it is not a positive number, it displays
     * an error message.
     */
    public Boolean isValidPositiveNumber() {
        if (this.isNonEmpty()) {
            double value;
            try {
                value = Double.parseDouble(
                        Objects.requireNonNull(this.getEditText()).getText()
                                .toString());
            } catch (NumberFormatException exception) {
                value = 0;
            }
            if (value <= 0) {
                this.setError(this.getHint() + " must be a positive number");
                return false;
            } else {
                this.setError(null);
                return true;
            }
        } else {
            return false;
        }
    }
}
