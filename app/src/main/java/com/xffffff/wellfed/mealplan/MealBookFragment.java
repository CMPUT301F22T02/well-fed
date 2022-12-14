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
package com.xffffff.wellfed.mealplan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.Launcher;
import com.xffffff.wellfed.common.OnItemClickListener;

/**
 * The fragment for the meal book. This fragment shows the meals in the meal
 * book. The user can add meals to the meal book by clicking on the add button.
 */
public class MealBookFragment extends Fragment
        implements Launcher<MealPlan>, OnItemClickListener<MealPlan> {
    /**
     * CallToActionTextView displays if the meal book is empty and if it is
     * it displays info to add meals to the meal book.
     */
    private TextView callToActionTextView;
    /**
     * LinearLayoutManager for the RecyclerView
     */
    private MealPlanLinearLayoutManager layoutManager;
    /**
     * The meal plan controller.
     */
    private MealPlanController controller;
    /**
     * ActivityResultLauncher for the meal plan activity to launch an
     * activity to add a meal plan.
     */
    ActivityResultLauncher<MealPlan> editLauncher =
            registerForActivityResult(new MealPlanEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                MealPlan mealPlan = result.second;
                switch (type) {
                    case "add":
                        controller.addMealPlan(mealPlan);
                        break;
                    case "quit":
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });
    /**
     * The selected meal plan.
     */
    private MealPlan selectedMealPlan;
    /**
     * ActivityResultLauncher for the meal plan activity to launch, edit or
     * delete the meal plan.
     */
    ActivityResultLauncher<MealPlan> launcher =
            registerForActivityResult(new MealPlanContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                MealPlan mealPlan = result.second;
                switch (type) {
                    case "delete":
                        controller.deleteMealPlan(mealPlan, false);
                        break;
                    case "edit":
                        controller.updateMealPlan(mealPlan);
                        break;
                    case "launch":
                        this.launch(selectedMealPlan);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });

    /**
     * onCreateView is called when the fragment is created. It inflates the
     * layout.
     *
     * @param inflater           The layout inflater.
     * @param container          The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    @Nullable @Override public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_book, container, false);
    }

    /**
     * onViewCreated is called when the view is created. It sets up the
     * recycler view and the call to action text view.
     *
     * @param view               The view.
     * @param savedInstanceState The saved instance state.
     */
    @Override public void onViewCreated(@NonNull View view,
                                        @Nullable Bundle savedInstanceState) {
        TextView userFirstNameTextView =
                view.findViewById(R.id.userFirstNameTextView);
        this.callToActionTextView =
                view.findViewById(R.id.callToActionTextView);
        RecyclerView mealPlanRecyclerView =
                view.findViewById(R.id.mealPlanRecyclerView);
        this.layoutManager =
                new MealPlanLinearLayoutManager(getContext());
        mealPlanRecyclerView.setLayoutManager(layoutManager);

        controller = new MealPlanController(requireActivity());

        this.controller.getAdapter().setOnItemClickListener(this);
        this.controller.setOnAdapterChangedListener(this::updateCallToAction);
        mealPlanRecyclerView.setAdapter(this.controller.getAdapter());
        mealPlanRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mealPlanRecyclerView.setNestedScrollingEnabled(false);

        userFirstNameTextView.setText(getString(R.string.greeting));
        this.updateCallToAction(null, -1);
    }

    /**
     * launch launches the meal plan activity.
     *
     * @param mealPlan The meal plan to launch.
     */
    @Override public void launch(MealPlan mealPlan) {
        if (mealPlan == null) {
            editLauncher.launch(null);
        } else {
            selectedMealPlan = mealPlan;
            launcher.launch(mealPlan);
        }
    }

    /**
     * updateCallToAction updates the call to action text view.
     *
     * @param mealPlan The meal plan to update the call to action text view.
     */
    private void updateCallToAction(MealPlan mealPlan, int position) {
        if (mealPlan != null) {
            SpannableStringBuilder callToAction =
                    new SpannableStringBuilder().append(
                                    getString(R.string.call_to_action_make_meal_plan))
                            .append(" ").append(mealPlan.getTitle(),
                                    new StyleSpan(Typeface.ITALIC), 0).append("?");
            this.callToActionTextView.setText(callToAction);
            this.layoutManager.scrollToPosition(position);
        } else {
            this.callToActionTextView.setText(getString(R.string.default_call_to_action));
        }
    }

    /**
     * onItemClick is called when the user clicks on a meal plan. It launches
     * the meal plan activity.
     *
     * @param mealPlan The meal plan to launch.
     */
    @Override public void onItemClick(MealPlan mealPlan) {
        this.launch(mealPlan);
    }
}
