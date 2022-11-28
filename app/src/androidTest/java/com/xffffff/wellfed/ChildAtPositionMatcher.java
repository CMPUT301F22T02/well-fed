package com.xffffff.wellfed;

import static kotlin.jvm.internal.Intrinsics.checkNotNull;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matches a child at a position in a view.
 * Citation: I let the Espresso test recorder create this method for me.
 */
public class ChildAtPositionMatcher {
    /**
     * Matches a child at a position.
     * @param parentMatcher the parent of the child
     * @param position the position of the child to match
     * @return the matcher for the child at a position
     */
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override public void describeTo(Description description) {
                description.appendText(
                        "Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup &&
                        parentMatcher.matches(parent) &&
                        view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * Matches a recyclerview item at a given position
     * IMPORTANT: Citation
     * This code was taken from Stack Overflow
     * URL: https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
     * Website Title: Stack Overflow
     * Author: riwnodennyk
     * Date Written: Jan 14th 2016 (Updated on Dec 21th 2017)
     * Date Used: November 27th 2022
     * @param position The position of the RecyclerView item in the RecyclerView
     * @param itemMatcher The matcher we check against the RecyclerView item
     * @return The matcher at the item position
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
