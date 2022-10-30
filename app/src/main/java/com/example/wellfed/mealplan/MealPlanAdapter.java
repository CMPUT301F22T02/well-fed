/*
 * MealPlanAdapter
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The MealPlanAdapter class binds ArrayList<MealPlan> to RecyclerView.
 * <p>
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * @author Steven Tang
 * @version v1.0.0 2022-10-24
 **/
public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanViewHolder> {
    private final MealBookFragment context;
    private final ArrayList<MealPlan> mealPlans;

    public MealPlanAdapter(MealBookFragment context,
                           ArrayList<MealPlan> mealPlans) {
        this.context = context;
        this.mealPlans = mealPlans;
    }

    /*
     * The onCreateViewHolder method returns a new MealPlanViewHolder object
     * using
     * the viewholder_MealPlan.xml view.
     */
    @NonNull @Override public MealPlanViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_meal_plan, parent, false);
        return new MealPlanViewHolder(view);
    }

    /*
     * The onBindViewHolder method binds a MealPlan object and a
     * MealPlanViewHolder,
     * taking the data in the MealPlan object and mapping it into a human
     * readable format to be presented in the RecyclerView.
     */
    @Override public void onBindViewHolder(@NonNull MealPlanViewHolder holder,
                                           int position) {

        MealPlan mealPlan = this.mealPlans.get(position);
        holder.getTitleTextView().setText(mealPlan.getTitle());
        holder.getCategoryTextView().setText(mealPlan.getCategory());
        holder.getMaterialCardView().setOnClickListener(
                view -> context.launch(holder.getAdapterPosition()));

        Date today = new Date();
        Date eatDate = mealPlan.getEatDate();

        SimpleDateFormat hashDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                Locale.US);
        hashDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String todayHash = hashDateFormat.format(today);
        String eatDayHash = hashDateFormat.format(eatDate);

        int colorPrimary = MaterialColors.getColor(context.getView(),
                com.google.android.material.R.attr.colorPrimary);
        int colorOnPrimary = MaterialColors.getColor(context.getView(),
                com.google.android.material.R.attr.colorOnPrimary);
        int colorSurface = MaterialColors.getColor(context.getView(),
                com.google.android.material.R.attr.colorSurface);
        int colorOnSurface = MaterialColors.getColor(context.getView(),
                com.google.android.material.R.attr.colorOnSurface);

        if (position > 0) {
            MealPlan previousMealPlan = this.mealPlans.get(position - 1);
            if (!previousMealPlan.getEatDate().equals(mealPlan.getEatDate())) {
                if (todayHash.equals(eatDayHash)) {
                    holder.setDateCircle(eatDate, colorPrimary, colorOnPrimary);
                } else {
                    holder.setDateCircle(eatDate, colorSurface, colorOnSurface);
                }
            }
        } else {
            if (todayHash.equals(eatDayHash)) {
                holder.setDateCircle(eatDate, colorPrimary, colorOnPrimary);
            } else {
                holder.setDateCircle(eatDate, colorSurface, colorOnSurface);
            }
        }
    }

    @Override public int getItemCount() {
        return this.mealPlans.size();
    }

}