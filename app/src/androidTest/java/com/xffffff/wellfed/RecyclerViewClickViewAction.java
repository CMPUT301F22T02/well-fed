package com.xffffff.wellfed;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

/**
 * Used to click on a button inside of a Recyclerview item for testing purposes.
 * <p>
 * Citation:
 * URL: https://stackoverflow.com/questions/28476507/using-espresso-to-click
 * -view-inside-recyclerview-item
 * Title: Using Espresso to click view inside RecyclerView Item
 * Author: Blade
 * License: Creative Commons
 */
public class RecyclerViewClickViewAction {

    /**
     * The child view to click inside of.
     * @param id the ID of the child view
     * @return the ViewAction to execute
     */
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return null;
            }

            @Override public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}