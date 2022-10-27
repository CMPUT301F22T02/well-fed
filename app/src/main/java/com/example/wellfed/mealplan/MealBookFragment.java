/*
 * MealBookFragment
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

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeContract;
import com.example.wellfed.recipe.RecipeController;

import java.util.ArrayList;

public class MealBookFragment extends Fragment implements MealPlanAdapter.Launcher {
    private TextView userFirstNameTextView;
    private TextView callToActionTextView;
    private RecyclerView mealPlanRecyclerView;
    private MealPlanAdapter mealPlanAdapter;
    private MealPlanController controller;
    private int position;
//    TODO: isolate to controller
    ArrayList<MealPlan> mealPlans;

    ActivityResultLauncher<MealPlan> launcher = registerForActivityResult(new
                    MealPlanContract(),
            new ActivityResultCallback<MealPlan>() {
                @Override
                public void onActivityResult(MealPlan result) {
                    if (result == null) {
                        return;
                    }
                    controller.deleteMealPlan(position);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mealPlans = new ArrayList<>();
        controller = new MealPlanController();

        //        TODO: REMOVE THIS DEMO DATA
        MealPlan mealPlan = new MealPlan("Cereal and Banana");
        mealPlan.setCategory("Breakfast");
        mealPlan.setServings(2);
        mealPlans.add(mealPlan);
        MealPlan mealPlan2 = new MealPlan("Butter Chicken");
        mealPlan2.setCategory("Lunch");
        mealPlans.add(mealPlan2);
        mealPlan.setServings(6);

        return inflater.inflate(R.layout.fragment_meal_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();


        this.userFirstNameTextView =
                view.findViewById(R.id.userFirstNameTextView);
        this.callToActionTextView =
                view.findViewById(R.id.callToActionTextView);
        this.mealPlanRecyclerView =
                view.findViewById(R.id.mealPlanRecyclerView);
        this.mealPlanRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        this.mealPlanAdapter = new MealPlanAdapter(this, mealPlans);
        this.mealPlanRecyclerView.setAdapter(this.mealPlanAdapter);
        this.mealPlanAdapter.notifyItemRangeChanged(
                0, mealPlans.size()
        );

        this.userFirstNameTextView.setText(getString(
                R.string.greeting, "Akshat"));
        if (mealPlanAdapter.getItemCount() > 0) {
            MealPlan currentMealPlan = mealPlans.get(0);
            SpannableStringBuilder calltoAction = new SpannableStringBuilder()
                    .append(getString(R.string.call_to_action_make_meal_plan))
                    .append(" ")
                    .append(currentMealPlan.getTitle(), new StyleSpan(
                            Typeface.ITALIC), 0)
                    .append("?");
            this.callToActionTextView.setText(calltoAction);
        } else {
            this.callToActionTextView.setText(
                    R.string.call_to_action_add_meal_plan);
        }
    }

    @Override public void launch(int pos) {
        position = pos;
        launcher.launch(mealPlans.get(pos));
    }
}
