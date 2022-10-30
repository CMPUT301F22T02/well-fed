/*
 * RequiredDateTextInputLayout
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
import android.util.AttributeSet;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * The RequiredDateTextInputLayout class extends the RequiredTextInputLayout
 * class such that it can show a DatePickerDialog to allow the user to
 * input dates.
 * <p>
 * Citation:
 * DatePickerDialog. Android Developers. (n.d.). Retrieved September 26,
 * 2022, from
 * https://developer.android.com/reference/android/app/DatePickerDialog
 *
 * @author Steven Tang
 * @version v1.0.0 2022-09-26
 **/
public class RequiredDateTextInputLayout extends RequiredTextInputLayout
        implements View.OnFocusChangeListener, View.OnClickListener,
                   MaterialPickerOnPositiveButtonClickListener<Long>,
                   TextInputLayout.OnEditTextAttachedListener {
    private final DateFormat DATE_FORMAT;
    private Date date;

    public RequiredDateTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs,
                                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /*
     * The onEditTextAttached method sets an OnClickListener on the
     * TextInputLayout.
     */
    @Override
    public void onEditTextAttached(@NonNull TextInputLayout textInputLayout) {
        super.onEditTextAttached(textInputLayout);
        Objects.requireNonNull(textInputLayout.getEditText())
                .setOnClickListener(this);
    }

    /*
     * The showDatePickerDialog method opens the DatePickerDialog with
     * the current date.
     */
    private void showDatePickerDialog() {
        Long selectedTime;
        if (this.date == null) {
            selectedTime = MaterialDatePicker.todayInUtcMilliseconds();
        } else {
            selectedTime = this.date.getTime();
        }
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(selectedTime)
                .build();
        ContextThemeWrapper wrapper = (ContextThemeWrapper) getContext();
        AppCompatActivity activity =
                (AppCompatActivity) wrapper.getBaseContext();
        picker.show(activity.getSupportFragmentManager(), null);
        picker.addOnPositiveButtonClickListener(this);
    }

    /*
     * The onFocusChange shows the Date Picker Dialog when the
     * TextInputLayout is
     * focused.
     */
    @Override
    public void onFocusChange(View view, boolean focused) {
        if (focused) {
            this.showDatePickerDialog();
        }
    }

    /*
     * The onClick shows the Date Picker Dialog when the TextInputLayout is
     * clicked.
     */
    @Override
    public void onClick(View view) {
        this.showDatePickerDialog();
    }

    public Date getDate() {
        return date;
    }

    /*
     * The setDate method updates the TextInputLayout with a
     * formatted representation of the date.
     */
    private void setDate(Date date) {
        this.date = date;
        Objects.requireNonNull(this.getEditText())
                .setText(this.DATE_FORMAT.format(this.date));
    }

    /*
     * The setDate method updates the TextInputLayout with a
     * formatted representation of the date.
     */
    public void setPlaceholderDate(Date date) {
        this.date = date;
        this.setPlaceholderText(this.DATE_FORMAT.format(this.date));
    }

    /**
     * Called with the current {@code MaterialCalendar<S>} selection.
     *
     * @param selection
     */
    @Override
    public void onPositiveButtonClick(Long selection) {
        Date date = new Date(selection);
        this.setDate(date);
    }
}
