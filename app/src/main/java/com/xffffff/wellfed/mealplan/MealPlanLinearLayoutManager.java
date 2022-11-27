package com.xffffff.wellfed.mealplan;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * The linear layout manager for the meal book fragment.
 */
public class MealPlanLinearLayoutManager extends LinearLayoutManager {
    /**
     * Creates a vertical LinearLayoutManager
     *
     * @param context Current context, will be used to access resources.
     */
    public MealPlanLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * Returns special padding bottom that allows overscrolling past the
     * last item in the recycler view.
     * @return Padding bottom
     */
    @Override public int getPaddingBottom() {
        return getHeight() - 256;
    }
}
