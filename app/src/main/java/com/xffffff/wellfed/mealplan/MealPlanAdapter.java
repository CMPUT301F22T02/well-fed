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

package com.xffffff.wellfed.mealplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import java.util.Date;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.DBAdapter;
import com.xffffff.wellfed.common.DateUtil;

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
    /**
     * The listener for an item click in the RecyclerView
     */
    private OnItemLoadListener itemLoadListener;
    private DateUtil dateUtil;

    // TODO: keep the context for now, try to remove it later
    public MealPlanAdapter(MealPlanDB db) {
        super(db.getQuery());
        this.db = db;
        dateUtil = new DateUtil();
    }

    /**
     * The listener for an item click in the RecyclerView
     */
    public interface OnItemClickListener {
        void onItemClick(MealPlan mealPlan);
    }

    /**
     * The listener for an item load in the RecyclerView
     */
    public interface OnItemLoadListener {
        void onItemLoad(MealPlan mealPlan);
    }

    /**
     * Sets the listener for an item click in the Recyclerview
     *
     * @param listener the listener to set
     */
    // TODO: move this to superclass
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the listener for an item load in the Recyclerview
     *
     * @param listener the listener to set
     */
    // TODO: move this to superclass
    public void setOnItemLoadListener(OnItemLoadListener listener) {
        this.itemLoadListener = listener;
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

    /**
     * The onBindViewHolder method binds a MealPlan object and a
     * MealPlanViewHolder,
     * taking the data in the MealPlan object and mapping it into a human
     * readable format to be presented in the RecyclerView.
     *
     * @param holder The MealPlanViewHolder object.
     * @param position The position of the MealPlan object in the ArrayList.
     */
    @Override public void onBindViewHolder(@NonNull MealPlanViewHolder holder,
                                           int position) {

        db.getMealPlan(getSnapshot(position), (mealPlan, success) -> {
            if (success) {
                holder.getTitleTextView().setText(mealPlan.getTitle());
                holder.getCategoryTextView().setText(mealPlan.getCategory());
                holder.getMaterialCardView().setOnClickListener(view -> {
                    if (listener != null) {
                        listener.onItemClick(mealPlan);
                    }
                });

                Date eatDate = mealPlan.getEatDate();
                Date eatDateFirstDayOfWeek = dateUtil.getFirstDayOfWeek(eatDate);
                if (position > 0) {
                    db.getMealPlan(getSnapshot(position - 1),
                            (priorMealPlan, success2) -> {
                                if (success2) {
                                    Date priorEatDate = priorMealPlan.getEatDate();
                                    if (dateUtil.equals(priorEatDate,eatDate)) {
                                        return;
                                    }
                                    renderDates(eatDate, holder);
                                    Date priorEatDateFirstDayOfWeek =
                                            dateUtil.getFirstDayOfWeek(priorEatDate);
                                    if (dateUtil.equals(eatDateFirstDayOfWeek,
                                            priorEatDateFirstDayOfWeek)) {
                                        return;
                                    }
                                    renderWeeks(eatDate, eatDateFirstDayOfWeek,
                                            holder);

                                }
                            });
                } else {
                    renderDates(eatDate, holder);
                    renderWeeks(eatDate, eatDateFirstDayOfWeek, holder);
                }
            }
            if (itemLoadListener != null) {
                itemLoadListener.onItemLoad(mealPlan);
            }
        });
    }

    /**
     * Renders the dates in the MealPlanViewHolder
     * @param eatDate the date to render
     * @param holder the MealPlanViewHolder to render the date in
     */
    private void renderDates(Date eatDate, @NonNull MealPlanViewHolder holder) {
        Date today = new Date();
        holder.setDateCircle(eatDate, today.equals(eatDate));
    }

    /**
     * Renders the weeks in the MealPlanViewHolder
     * @param eatDate the date to render
     * @param eatDateFirstDayOfWeek the first day of the week of the date to render
     * @param holder the MealPlanViewHolder to render the week in
     */
    private void renderWeeks(Date eatDate, Date eatDateFirstDayOfWeek,
                             @NonNull MealPlanViewHolder holder) {
        dateUtil = new DateUtil();
        Date eatDateLastDayOfWeek = dateUtil.getLastDayOfWeek(eatDate);
        String eatDateFirstDayOfWeekMonth =
                dateUtil.format(eatDateFirstDayOfWeek, "MMMM ");
        String eatDateFirstDayOfWeekDay =
                dateUtil.format(eatDateFirstDayOfWeek,"d");
        String weekLabel =
                eatDateFirstDayOfWeekMonth + eatDateFirstDayOfWeekDay + " - ";
        String eatDateLastDayOfWeekMonth =
                dateUtil.format(eatDateLastDayOfWeek,"MMMM ");
        if (!eatDateLastDayOfWeekMonth.equals(eatDateFirstDayOfWeekMonth)) {
            weekLabel += eatDateLastDayOfWeekMonth;
        }
        weekLabel += dateUtil.format(eatDateLastDayOfWeek, "d");
        holder.getWeekTextView().setText(weekLabel);
        holder.getWeekTextView().setVisibility(View.VISIBLE);
    }
}