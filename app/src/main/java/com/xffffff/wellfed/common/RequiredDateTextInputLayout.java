/*
 * RequiredDateTextInputLayout
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/**
 * The RequiredDateTextInputLayout class extends the RequiredTextInputLayout
 * class such that it can show a MaterialDatePicker to allow the user to
 * input dates.
 * Citation:
 * MaterialDatePicker. Android Developers. (n.d.). Retrieved November 3,
 * 2022, from https://developer.android.com/reference/com/google/android/
 * material/datepicker/MaterialDatePicker
 *
 * @author Steven Tang
 * @version v1.1.0 2022-10-29
 **/
public class RequiredDateTextInputLayout extends RequiredTextInputLayout
        implements View.OnFocusChangeListener, View.OnClickListener,
                   MaterialPickerOnPositiveButtonClickListener<Long>,
                   TextInputLayout.OnEditTextAttachedListener {
    /**
     * Holds the date format used to display the date
     */
    private final DateFormat DATE_FORMAT;
    /**
     * Holds the time zone offset
     */
    private final int timeZoneOffset;
    /**
     * Holds the date currently selected
     */
    private Date date;

    /**
     * Constructs a RequiredDateTextInputLayout object
     *
     * @param context the context
     */
    public RequiredDateTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Constructs a RequiredDateTextInputLayout object
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        this.timeZoneOffset = tz.getOffset(cal.getTimeInMillis());
    }

    /**
     * Constructs a RequiredDateTextInputLayout object
     *
     * @param context      the context
     * @param attrs        the attributes
     * @param defStyleAttr the default style attribute
     */
    public RequiredDateTextInputLayout(@NonNull Context context,
                                       @Nullable AttributeSet attrs,
                                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        this.timeZoneOffset = tz.getOffset(cal.getTimeInMillis());
    }

    /**
     * Sets the EditText's OnClickListener to this
     *
     * @param textInputLayout the TextInputLayout
     */
    @Override public void onEditTextAttached(
            @NonNull TextInputLayout textInputLayout) {
        super.onEditTextAttached(textInputLayout);
        Objects.requireNonNull(textInputLayout.getEditText())
                .setOnClickListener(this);
    }

    /**
     * Opens the MaterialDatePicker dialog with the selected date.
     */
    private void showDatePickerDialog() {
        long selectedTime;

        if (this.date == null) {
            selectedTime = MaterialDatePicker.todayInUtcMilliseconds();
        } else {
            selectedTime = this.date.getTime() + this.timeZoneOffset;
        }
        MaterialDatePicker<Long> picker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date").setSelection(selectedTime)
                        .build();
        ContextThemeWrapper wrapper = (ContextThemeWrapper) getContext();
        AppCompatActivity activity =
                (AppCompatActivity) wrapper.getBaseContext();
        picker.show(activity.getSupportFragmentManager(), null);
        picker.addOnPositiveButtonClickListener(this);
    }

    /**
     * Opens the MaterialDatePicker dialog when the TextInputLayout is focused.
     *
     * @param view    the view that has changed focus.
     * @param focused true if the view is focused.
     */
    @Override public void onFocusChange(View view, boolean focused) {
        if (focused) {
            this.showDatePickerDialog();
        }
    }

    /**
     * Opens the MaterialDatePicker dialog when the TextInputLayout is clicked.
     *
     * @param view the view that was clicked
     */
    @Override public void onClick(View view) {
        this.showDatePickerDialog();
    }

    /**
     * Gets the date selected by the user.
     *
     * @return the date selected by the user
     */
    public Date getDate() {
        return date;
    }

    /**
     * Updates the text with the formatted representation of the date.
     *
     * @param date the date to be set
     */
    public void setDate(Date date) {
        this.date = date;
        this.setText(this.DATE_FORMAT.format(this.date));
    }

    /**
     * Updates the placeholder text with the formatted representation of the
     * date.
     *
     * @param date the date to set
     */
    public void setPlaceholderDate(Date date) {
        this.date = date;
        this.setPlaceholderText(this.DATE_FORMAT.format(this.date));
    }

    /**
     * Handler for MaterialDatePicker positive button click.
     *
     * @param selection the selected date in milliseconds since epoch
     */
    @Override public void onPositiveButtonClick(Long selection) {
        Date date = new Date(selection - this.timeZoneOffset);
        this.setDate(date);
    }
}
