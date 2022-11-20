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
import com.example.wellfed.common.DBAdapter;
import com.example.wellfed.common.UTCDate;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientAdapter;
import com.google.android.material.color.MaterialColors;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * The MealPlanAdapter class binds ArrayList to RecyclerView.
 * <p>
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * @author Steven Tang
 * @version v1.0.0 2022-10-24
 **/
public class MealPlanAdapter extends DBAdapter<MealPlanViewHolder> {
    private final MealPlanDB db;

    /**
     * The listener for an item click in the RecyclerView
     */
    private OnItemClickListener listener;

    // TODO: keep the context for now, try to remove it later
    public MealPlanAdapter(MealPlanDB db) {
        super(db.getQuery());
        this.db = db;
    }

    /**
     * The listener for an item click in the RecyclerView
     */
    public interface OnItemClickListener {
        void onItemClick(MealPlan mealPlan);
    }

    /**
     * Sets the listener for an item click in the Recyclerview
     * @param listener the listener to set
     */
    // TODO: move this to superclass
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

        db.getMealPlan(getSnapshot(position), (mealPlan, success) -> {
            if (success) {
                holder.getTitleTextView().setText(mealPlan.getTitle());
                holder.getCategoryTextView().setText(mealPlan.getCategory());
                holder.getMaterialCardView().setOnClickListener(
                        view -> {
                            if (listener != null) {
                                listener.onItemClick(mealPlan);
                            }
                        });

                UTCDate today = new UTCDate();
                UTCDate eatDate = UTCDate.from(mealPlan.getEatDate());

                UTCDate eatDateFirstDayOfWeek = eatDate.getFirstDayOfWeek();
                if (position > 0) {
//                    // TODO: this is a hack, fix it later
//                    MealPlan priorMealPlan = this.mealPlans.get(position - 1);
//                    UTCDate priorEatDate = UTCDate.from(priorMealPlan.getEatDate());
//                    if (priorEatDate.equals(eatDate)) {
//                        return;
//                    }
//                    UTCDate priorEatDateFirstDayOfWeek =
//                            priorEatDate.getFirstDayOfWeek();
//                    if (eatDateFirstDayOfWeek.equals(priorEatDateFirstDayOfWeek)) {
//                        return;
//                    }
                }
                UTCDate eatDateLastDayOfWeek = eatDate.getLastDayOfWeek();
                String eatDateFirstDayOfWeekMonth = eatDateFirstDayOfWeek.format("MMMM ");
                String eatDateFirstDayOfWeekDay = eatDateFirstDayOfWeek.format("d");
                String weekLabel = eatDateFirstDayOfWeekMonth + eatDateFirstDayOfWeekDay + " - ";
                String eatDateLastDayOfWeekMonth = eatDateLastDayOfWeek.format("MMMM ");
                if (!eatDateLastDayOfWeekMonth.equals(eatDateFirstDayOfWeekMonth)) {
                    weekLabel += eatDateLastDayOfWeekMonth;
                }
                weekLabel += eatDateLastDayOfWeek.format("d");
                holder.getWeekTextView().setText(weekLabel);
                holder.getWeekTextView().setVisibility(View.VISIBLE);
                holder.setDateCircle(eatDate, today.equals(eatDate));
            }
        });
    }
}