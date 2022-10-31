/*
 * MealPlanViewHolder
 *
 * Version: v1.0.0
 *
 * Date: 2022-10-24
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

package com.example.wellfed.mealplan;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The MealPlanViewHolder class contains the layout for each individual
 * item in the RecyclerView.
 * <p>
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * @author Steven Tang
 * @version v1.0.0 2022-10-24
 **/
public class MealPlanViewHolder extends RecyclerView.ViewHolder {
    private final SimpleDateFormat circleDateFormat;
    private final SimpleDateFormat weekDayDateFormat;
    private final MaterialCardView materialCardView;
    private final TextView weekTextView;
    private final TextView weekDayTextView;
    private final TextView dayTextView;
    private final TextView titleTextView;
    private final TextView categoryTextView;

    public MaterialCardView getMaterialCardView() {
        return materialCardView;
    }

    public TextView getWeekTextView() {
        return weekTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getCategoryTextView() {
        return categoryTextView;
    }

    public void setDateCircle(Date date, int backgroundTint, int textColor) {
        dayTextView.setText(circleDateFormat.format(date));
        weekDayTextView.setText(weekDayDateFormat.format(date));
        dayTextView.setBackgroundTintList(ColorStateList.valueOf(
                backgroundTint));
        dayTextView.setTextColor(textColor);
    }

    public MealPlanViewHolder(@NonNull View itemView) {
        super(itemView);
        this.circleDateFormat = new SimpleDateFormat("d", Locale.US);
        this.circleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.weekDayDateFormat = new SimpleDateFormat("E",
                Locale.US);
        this.weekDayDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.materialCardView = itemView.findViewById(R.id.materialCardView);
        this.weekTextView = itemView.findViewById(R.id.weekTextView);
        this.weekDayTextView = itemView.findViewById(R.id.weekDayTextView);
        this.dayTextView = itemView.findViewById(R.id.dayTextView);
        this.titleTextView = itemView.findViewById(R.id.titleTextView);
        this.categoryTextView = itemView.findViewById(R.id.categoryTextView);
    }
}