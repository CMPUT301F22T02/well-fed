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

package com.xffffff.wellfed.mealplan;

import android.content.res.ColorStateList;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.xffffff.wellfed.R;

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
    /**
     * circleDateFormat is the format for the date in the circle
     */
    private final SimpleDateFormat circleDateFormat;
    /**
     * weekDayDateFormat is the format for the week day in the circle
     */
    private final SimpleDateFormat weekDayDateFormat;
    /**
     * materialCartView is the card view for the item
     */
    private final MaterialCardView materialCardView;
    /**
     * weekTextView is the text view for the week day
     */
    private final TextView weekTextView;
    /**
     * weekDayTextView is the text view for the date
     */
    private final TextView weekDayTextView;
    /**
     * dayTextView is the text view for the day
     */
    private final TextView dayTextView;
    /**
     * titleTextView is the text view for the title
     */
    private final TextView titleTextView;
    /**
     * categoryTextView is the text view for the category
     */
    private final TextView categoryTextView;
    /**
     * colorPrimary is the primary color of the app
     */
    private final int colorPrimary;
    /**
     * colorOnPrimary is the on primary color of the app
     */
    private final int colorOnPrimary;
    /**
     * colorSuface is the surface color of the app
     */
    private final int colorSurface;
    /**
     * colorSuface is the variant surface color of the app
     */
    private final int colorSurfaceVariant;
    /**
     * colorOnSurface is the on surface color of the app
     */
    private final int colorOnSurface;

    /**
     * MealPlanViewHolder is the constructor for the MealPlanViewHolder
     *
     * @param itemView the view
     */
    public MealPlanViewHolder(@NonNull View itemView) {
        super(itemView);
        this.circleDateFormat = new SimpleDateFormat("d", Locale.US);
        this.circleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.weekDayDateFormat = new SimpleDateFormat("E", Locale.US);
        this.weekDayDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.materialCardView = itemView.findViewById(R.id.materialCardView);
        this.weekTextView = itemView.findViewById(R.id.weekTextView);
        this.weekDayTextView = itemView.findViewById(R.id.weekDayTextView);
        this.dayTextView = itemView.findViewById(R.id.dayTextView);
        this.titleTextView = itemView.findViewById(R.id.titleTextView);
        this.categoryTextView = itemView.findViewById(R.id.categoryTextView);
        this.colorPrimary = MaterialColors.getColor(itemView,
                com.google.android.material.R.attr.colorPrimary);
        this.colorOnPrimary = MaterialColors.getColor(itemView,
                com.google.android.material.R.attr.colorOnPrimary);
        this.colorSurface = MaterialColors.getColor(itemView,
                com.google.android.material.R.attr.colorSurface);
        this.colorSurfaceVariant = MaterialColors.getColor(itemView,
                com.google.android.material.R.attr.colorSurfaceVariant);
        this.colorOnSurface = MaterialColors.getColor(itemView,
                com.google.android.material.R.attr.colorOnSurface);
    }

    /**
     * getMaterialCardView is the getter for materialCardView
     *
     * @return the materialCardView
     */
    public MaterialCardView getMaterialCardView() {
        return materialCardView;
    }

    /**
     * getWeekTextView is the getter for weekTextView
     *
     * @return the weekTextView
     */
    public TextView getWeekTextView() {
        return weekTextView;
    }

    /**
     * getTitleTextView is the getter for titleTextView
     *
     * @return the titleTextView
     */
    public TextView getTitleTextView() {
        return titleTextView;
    }

    /**
     * getCategoryTextView is the getter for categoryTextView
     *
     * @return the categoryTextView
     */
    public TextView getCategoryTextView() {
        return categoryTextView;
    }

    /**
     * setDateCircle method sets the date circle
     *
     * @param date      the date
     * @param isPrimary whether the date circle is primary
     */
    public void setDateCircle(Date date, Boolean isPrimary) {
        dayTextView.setText(circleDateFormat.format(date));
        weekDayTextView.setText(weekDayDateFormat.format(date));
        if (isPrimary) {
            dayTextView.setBackgroundTintList(
                    ColorStateList.valueOf(colorPrimary));
            dayTextView.setTextColor(colorOnPrimary);
        } else {
            dayTextView.setBackgroundTintList(
                    ColorStateList.valueOf(colorSurface));
            dayTextView.setTextColor(colorOnSurface);
        }
    }

    /**
     * Set card style method sets the card style for filled and outlined
     * variants
     *
     * @param variant "filled" or "outlined"
     */
    public void setCardStyle(String variant) {
        if (variant.equals("filled")) {
            materialCardView.setCardBackgroundColor(colorSurfaceVariant);
            materialCardView.setStrokeWidth(0);
        } else if (variant.equals("outlined")) {
            materialCardView.setCardBackgroundColor(colorSurface);
            float densityDPI =
                    this.itemView.getContext().getResources().getDisplayMetrics().densityDpi;
            materialCardView.setStrokeWidth((int) densityDPI / DisplayMetrics.DENSITY_DEFAULT);
        }
    }

}