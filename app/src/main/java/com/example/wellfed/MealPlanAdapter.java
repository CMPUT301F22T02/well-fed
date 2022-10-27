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

package com.example.wellfed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.mealplan.MealPlan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The MealPlanAdapter class binds ArrayList<MealPlan> to RecyclerView.
 *
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * @version v1.0.0 2022-10-24
 * @author Steven Tang
 **/
public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanViewHolder> {
    private final Fragment CONTEXT;
    private final ArrayList<MealPlan> mealPlans;
    private final DateFormat DATE_FORMAT;

    public MealPlanAdapter(Fragment context,
                           ArrayList<MealPlan> mealPlans) {
        this.CONTEXT = context;
        this.mealPlans = mealPlans;
        this.DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.notifyItemRangeInserted(0, this.mealPlans.size());
    }

    /*
     * The onCreateViewHolder method returns a new MealPlanViewHolder object using
     * the viewholder_MealPlan.xml view.
     */
    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_meal_plan, parent, false);
        return new MealPlanViewHolder(view);
    }

    /*
     * The onBindViewHolder method binds a MealPlan object and a MealPlanViewHolder,
     * taking the data in the MealPlan object and mapping it into a human
     * readable format to be presented in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        MealPlan mealPlan = this.mealPlans.get(position);
        holder.getTitleTextView().setText(mealPlan.getTitle());
        holder.getCategoryTextView().setText(mealPlan.getCategory());
        View itemView = holder.getItemView();
        itemView.setOnClickListener((view) -> {
            Fragment mealPlanFragment =
                    MealPlanFragment.newInstance(mealPlan);
//            start mealPlanFragment
            ((FragmentActivity) this.CONTEXT.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.pager, mealPlanFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }

    @Override
    public int getItemCount() {
        return this.mealPlans.size();
    }

}