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

package com.xffffff.wellfed.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * The RequiredTextInputLayout class extends the TextInputLayout
 * class with methods for performing data validation.
 * Citation:
 * TextInputLayout. Android Developers. (n.d.). Retrieved September 26, 2022,
 * from https://developer.android.com/reference/com/google/android/material/
 * textfield/TextInputLayout
 * TextWatcher. Android Developers. (n.d.). Retrieved October 29, 2022,
 * from https://developer.android.com/reference/android/text/TextWatcher/
 *
 * @author Steven Tang
 * @version v1.2.0 2022-11-23
 **/
public class RequiredTextInputLayout extends TextInputLayout
        implements TextInputLayout.OnEditTextAttachedListener, TextWatcher {
    /**
     * Holds the EditText associated with the TextInputLayout
     */
    private EditText editText;

    /**
     * Holds the placeholder
     */
    private String placeholder;

    /**
     * Constructs a RequiredTextInputLayout object
     *
     * @param context the context
     */
    public RequiredTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructs a RequiredTextInputLayout object
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addOnEditTextAttachedListener(this);
    }

    /**
     * Constructs a RequiredTextInputLayout object
     *
     * @param context      the context
     * @param attrs        the attributes
     * @param defStyleAttr the default style attribute
     */
    public RequiredTextInputLayout(@NonNull Context context,
                                   @Nullable AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addOnEditTextAttachedListener(this);
    }

    /**
     * Binds the EditText and sets its TextChangedListener to this.
     *
     * @param textInputLayout the TextInputLayout
     */
    @Override public void onEditTextAttached(
            @NonNull TextInputLayout textInputLayout) {
        this.editText = Objects.requireNonNull(textInputLayout.getEditText());
        this.editText.addTextChangedListener(this);
        this.placeholder = this.editText.getText().toString();
    }

    /**
     * Returns the value of the EditTExt
     *
     * @return the value of the EditText
     */
    public String getText() {
        return this.editText.getText().toString();
    }

    /**
     * Sets the text of the EditText
     *
     * @param text the text
     */
    public void setText(String text) {
        this.editText.setText(text);
    }

    /**
     * Setter for placeholder
     *
     * @param placeholder the placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Set the placeholder text of the EditText
     *
     * @param placeholder the placeholder text
     */
    public void setPlaceholderText(String placeholder) {
        this.setPlaceholder(placeholder);
        this.editText.setText(placeholder);
    }

    /**
     * Check for whether the EditText has changes, i.e. the EditText's
     * current text differs from the placeholder text
     *
     * @return true if the EditText has changes
     */
    public Boolean hasChanges() {
        return !this.placeholder.equals(this.getText());
    }

    /*
     * Validates that the EditText is non-empty.
     * If it is empty, it displays an error message.
     * @return true if the EditText is non-empty
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

    /**
     * Validates that the EditText is valid. Valid refers to the EditText
     * is non-empty. If it is invalid, it displays an error message.
     *
     * @return true if the EditText is valid.
     */
    public Boolean isValid() {
        return this.isNonEmpty();
    }

    /**
     * The beforeTextChanged handler is called when within s, the count
     * characters beginning at start are about to be replaced by new text
     * with length after
     *
     * @param s     the CharSequence text
     * @param start start index
     * @param count count of characters about to be replaced
     * @param after length of new text
     */
    @Override public void beforeTextChanged(CharSequence s, int start,
                                            int count, int after) {
    }

    /**
     * The onTextChanged handler is called when within s, count characters
     * beginning at start have just replaced old text that had length before.
     *
     * @param s      the CharSequence text
     * @param start  start index
     * @param before length of old text
     * @param count  count of characters about to be replaced
     */
    @Override public void onTextChanged(CharSequence s, int start, int before,
                                        int count) {
    }

    /**
     * The afterTextChanged handler is called when somewhere within s, the text
     * has changed, it is checks if the EditText text is valid after
     * changes.
     *
     * @param s the Editable text
     */
    @Override public void afterTextChanged(Editable s) {
        this.isValid();
    }
}
