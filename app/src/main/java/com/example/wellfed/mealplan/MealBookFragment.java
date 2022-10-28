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
import com.example.wellfed.common.AdapterDataObserver;

public class MealBookFragment extends Fragment
        implements MealPlanAdapter.Launcher,
                   AdapterDataObserver.OnAdapterDataChangedListener {
    private TextView userFirstNameTextView;
    private TextView callToActionTextView;
    private RecyclerView mealPlanRecyclerView;
    private MealPlanAdapter mealPlanAdapter;
    private MealPlanController controller;
    private int selected;

    ActivityResultLauncher<MealPlan> launcher =
            registerForActivityResult(new MealPlanContract(),
                    result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        switch (type) {
                            case "delete":
                                controller.deleteMealPlan(this.selected);
                                break;
                            case "launch":
                                this.launch(this.selected);
                                break;
                            default:
                                break;
                        }
                    });

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_book, container, false);
    }

    @Override public void onViewCreated(@NonNull View view,
                                        @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        this.userFirstNameTextView =
                view.findViewById(R.id.userFirstNameTextView);
        this.callToActionTextView =
                view.findViewById(R.id.callToActionTextView);
        this.mealPlanRecyclerView =
                view.findViewById(R.id.mealPlanRecyclerView);
        this.mealPlanRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        controller = new MealPlanController();

        this.mealPlanAdapter =
                new MealPlanAdapter(this, this.controller.getMealPlans());
        this.mealPlanAdapter.registerAdapterDataObserver(
                new AdapterDataObserver(this));
        this.controller.setAdapter(this.mealPlanAdapter);
        this.mealPlanRecyclerView.setAdapter(this.mealPlanAdapter);

        this.mealPlanAdapter.notifyItemRangeChanged(0,
                this.controller.getMealPlans().size());

        this.userFirstNameTextView.setText(
                getString(R.string.greeting, "Akshat"));
    }

    @Override public void launch(int pos) {
        selected = selected;
        launcher.launch(this.controller.getMealPlans().get(pos));
    }

    private void updateCallToAction() {
        if (this.mealPlanAdapter.getItemCount() > 0) {
            MealPlan currentMealPlan = this.controller.getMealPlans().get(0);
            SpannableStringBuilder calltoAction =
                    new SpannableStringBuilder().append(
                                    getString(R.string.call_to_action_make_meal_plan))
                            .append(" ").append(currentMealPlan.getTitle(),
                                    new StyleSpan(Typeface.ITALIC), 0).append("?");
            this.callToActionTextView.setText(calltoAction);
        } else {
            this.callToActionTextView.setText(
                    R.string.call_to_action_add_meal_plan);
        }
    }

    @Override public void onAdapterDataChanged() {
        this.updateCallToAction();
    }
}
