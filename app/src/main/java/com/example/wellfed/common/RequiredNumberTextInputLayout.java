/*
 * RequiredNumberTextInputLayout
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
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


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
public class RequiredNumberTextInputLayout extends RequiredTextInputLayout {
    private Boolean requireDouble = true;
    private Boolean requirePositiveNumber = false;
    private Boolean requireInteger = false;


    public RequiredNumberTextInputLayout(@NonNull Context context) {
        super(context);
    }

    public RequiredNumberTextInputLayout(@NonNull Context context,
                                         @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RequiredNumberTextInputLayout(@NonNull Context context,
                                         @Nullable AttributeSet attrs,
                                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void requireDouble() {
        this.requireDouble = true;
        this.requireInteger = false;
    }

    public void requireInteger() {
        this.requireInteger = true;
        this.requireDouble = false;
    }

    public void setRequirePositiveNumber(Boolean requirePositiveNumber) {
        this.requirePositiveNumber = requirePositiveNumber;
    }

    public Double getDouble() throws NumberFormatException {
        String text = this.getText();
        return Double.parseDouble(text);
    }

    public Integer getInteger() throws NumberFormatException {
        String text = this.getText();
        return Integer.parseInt(text);
    }

    /*
     * The isValidInteger method validates that the EditText is
     * a valid integer. If it is not a integer, it displays
     * an error message.
     */
    private Boolean isValidDouble() {
        try {
            this.getDouble();
            this.setError(null);
            return true;
        } catch (NumberFormatException exception) {
            this.setError(this.getHint() + " must be a double");
            return false;
        }
    }

    /*
     * The isValidInteger method validates that the EditText is
     * a valid integer. If it is not a integer, it displays
     * an error message.
     */
    private Boolean isValidInteger() {
        try {
            this.getInteger();
            this.setError(null);
            return true;
        } catch (NumberFormatException exception) {
            this.setError(this.getHint() + " must be a integer");
            return false;
        }
    }

    /*
     * The isValidPositiveNumber method validates that the EditText is
     * a valid positive number. If it is not a positive number, it displays
     * an error message.
     */
    private Boolean isValidPositiveNumber() {
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
    }

    @Override
    public Boolean isValid() {
        super.isValid();
        if (this.requireDouble) {
            if (!this.isValidDouble()) {
                return false;
            }
        }
        if (this.requireInteger) {
            if (!this.isValidInteger()) {
                return false;
            }
        }
        if (this.requirePositiveNumber) {
            if (!this.isValidPositiveNumber()) {
                return false;
            }
        }
        return true;
    }
}
