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

package com.xffffff.wellfed.common;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * The RequiredNumberTextInputLayout class extends the RequiredTextInputLayout
 * class with methods for performing number data validation.
 *
 * @author Steven Tang
 * @version v1.0.0 2022-11-03
 **/
public class RequiredNumberTextInputLayout extends RequiredTextInputLayout {
    /**
     * Holds the Boolean for whether to require a double, defaults to true
     */
    private Boolean requireDouble = true;

    /**
     * Holds the Boolean for whether to require an integer, defaults to false
     */
    private Boolean requireInteger = false;

    /**
     * Holds the Boolean for whether to require a positive number, defaults to
     * false
     */
    private Boolean requirePositiveNumber = false;

    /**
     * Constructs a RequiredNumberTextInputLayout object
     *
     * @param context the context
     */
    public RequiredNumberTextInputLayout(@NonNull Context context) {
        super(context);
    }

    /**
     * Constructs a RequiredNumberTextInputLayout object
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public RequiredNumberTextInputLayout(@NonNull Context context,
                                         @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a RequiredNumberTextInputLayout object
     *
     * @param context      the context
     * @param attrs        the attributes
     * @param defStyleAttr the default style attribute
     */
    public RequiredNumberTextInputLayout(@NonNull Context context,
                                         @Nullable AttributeSet attrs,
                                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Validate data to require a double
     */
    public void setRequireDouble() {
        this.requireDouble = true;
        this.requireInteger = false;
    }

    /**
     * Validate data to require a integer
     */
    public void setRequireInteger() {
        this.requireInteger = true;
        this.requireDouble = false;
    }

    /**
     * Validate data to require a positive number if requirePositiveNumber is
     * true.
     *
     * @param requirePositiveNumber the Boolean for whether to require a
     *                              positive number
     */
    public void setRequirePositiveNumber(Boolean requirePositiveNumber) {
        this.requirePositiveNumber = requirePositiveNumber;
    }

    /**
     * Returns the double value of the text
     *
     * @return double value of the text
     * @throws NumberFormatException if the text is not a double
     */
    public Double getDouble() throws NumberFormatException {
        String text = this.getText();
        return Double.parseDouble(text);
    }

    /**
     * Returns the integer value of the text
     *
     * @return double value of the text
     * @throws NumberFormatException if the text is not an integer
     */
    public Integer getInteger() throws NumberFormatException {
        String text = this.getText();
        return Integer.parseInt(text);
    }

    /**
     * Validates that the EditText is a valid double. If it is not a
     * double, it displays an error message.
     *
     * @return true if the EditText is a valid double, false otherwise
     */
    private Boolean isValidDouble() {
        try {
            this.getDouble();
            this.setError(null);
            return true;
        } catch (NumberFormatException exception) {
            this.setError(this.getHint() + " must be a number");
            return false;
        }
    }

    /**
     * Validates that the EditText is a valid integer. If it is not a
     * integer, it displays an error message.
     *
     * @return true if the EditText is a valid integer, false otherwise
     */
    private Boolean isValidInteger() {
        try {
            this.getInteger();
            this.setError(null);
            return true;
        } catch (NumberFormatException exception) {
            this.setError(this.getHint() + " must be an integer");
            return false;
        }
    }

    /**
     * Validates that the EditText is a valid positive number. If it is not a
     * positive number, it displays an error message.
     *
     * @return true if the EditText is a valid positive number, false otherwise
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

    /**
     * Validates that the EditText is valid. Valid refers to the EditText
     * is non-empty and satisfies the number validation requirements. If it is
     * invalid, it displays an error message.
     *
     * @return true if the EditText is valid.
     */
    @Override public Boolean isValid() {
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
