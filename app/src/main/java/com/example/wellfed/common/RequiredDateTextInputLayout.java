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

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

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
                   DatePickerDialog.OnDateSetListener,
                   TextInputLayout.OnEditTextAttachedListener {
    private final Context CONTEXT;
    private final DateFormat DATE_FORMAT;
    private Date date;

    public RequiredDateTextInputLayout(@NonNull Context context) {
        super(context);
        this.CONTEXT = context;
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.addOnEditTextAttachedListener(this);
    }

    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.CONTEXT = context;
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.addOnEditTextAttachedListener(this);
    }

    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs,
                                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.CONTEXT = context;
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.addOnEditTextAttachedListener(this);
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
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this.CONTEXT,
                this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
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

    /*
     * The onDateSet method calls setDate with the date from the data picker.
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Date date = new GregorianCalendar(year, month, day).getTime();
        this.setDate(date);
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
}
