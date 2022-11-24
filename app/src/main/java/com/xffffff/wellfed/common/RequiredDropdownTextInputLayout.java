/*
 * RequiredDropdownTextInputLayout
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

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * The RequiredDropdownTextInputLayout class extends the RequiredTextInputLayout
 * class to provide methods for compatibility with Dropdowns.
 * Citation:
 * MaterialAutoCompleteTextView. Android Developers. (n.d.).
 * Retrieved November 3, 2022, from https://developer.android.com/reference/com/
 * google/android/material/textfield/MaterialAutoCompleteTextView
 *
 * @author Steven Tang
 * @version v1.0.0 2022-11-03
 **/
public class RequiredDropdownTextInputLayout extends RequiredTextInputLayout {
    /**
     * Holds the MaterialAutoCompleteTextView associated with the
     * TextInputLayout
     */
    private MaterialAutoCompleteTextView autoCompleteTextView;

    /**
     * Constructs a RequiredDropdownTextInputLayout object
     *
     * @param context the context
     */
    public RequiredDropdownTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructs a RequiredDropdownTextInputLayout object
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public RequiredDropdownTextInputLayout(@NonNull Context context,
                                           @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a RequiredDropdownTextInputLayout object
     *
     * @param context      the context
     * @param attrs        the attributes
     * @param defStyleAttr the default style attribute
     */
    public RequiredDropdownTextInputLayout(@NonNull Context context,
                                           @Nullable AttributeSet attrs,
                                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set the placeholder text of the MaterialAutoCompleteTextView
     *
     * @param placeholder the placeholder text
     */
    @Override public void setPlaceholderText(String placeholder) {
        super.setPlaceholder(placeholder);
        this.autoCompleteTextView.setText(placeholder, false);
    }

    /**
     * Set the dropdown items of the MaterialAutoCompleteTextView
     *
     * @param items the items to populate the dropdown
     */
    public void setSimpleItems(String[] items) {
        this.autoCompleteTextView.setSimpleItems(items);
    }

    /**
     * Sets autoCompleteTextView to the MaterialAutoCompleteTextView
     *
     * @param textInputLayout the TextInputLayout
     */
    @Override public void onEditTextAttached(
            @NonNull TextInputLayout textInputLayout) {
        super.onEditTextAttached(textInputLayout);
        this.autoCompleteTextView =
                (MaterialAutoCompleteTextView) Objects.requireNonNull(
                        textInputLayout.getEditText());
    }
}
