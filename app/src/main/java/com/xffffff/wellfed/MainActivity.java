package com.xffffff.wellfed;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xffffff.wellfed.common.Launcher;
import com.xffffff.wellfed.navigation.NavigationCollectionAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * MainActivity is the main activity of the app
 * It contains the bottom navigation bar and the floating action button
 */
public class MainActivity extends ActivityBase {
    /**
     * Stack history of the fragments
     */
    Stack<Integer> history;
    /**
     * NavigationCollectionAdapter for the bottom navigation bar and the
     * viewpager that contains the fragments
     */
    NavigationCollectionAdapter navigationCollectionAdapter;
    /**
     * The viewpager that contains the fragments for the bottom navigation bar
     */
    ViewPager2 viewPager;
    /**
     * The bottom navigation bar that contains the tabs for the fragments in
     * the viewpager
     */
    BottomAppBar bottomAppBar;
    /**
     * The floating action button that is used to add new ingredients,
     * recipes, and meal plans
     */
    FloatingActionButton fab;

    /**
     * onActivityResult is called when an activity returns a result to this
     * activity
     *
     * @param requestCode the request code of the activity that returned the
     *                    result
     * @param resultCode  the result code of the activity that returned the
     *                    result
     * @param data        the data that was returned by the activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * onCreate is called when the activity is created and initializes the
     * activity with the history and the viewpager
     */
    @Override
    public void onBackPressed() {
        if (history.size() < 2) {
            super.onBackPressed();
        } else {
            history.pop();
            viewPager.setCurrentItem(history.peek());
        }
    }

    /**
     * onCreate method is called when the activity is created and initializes
     * the activity with the history and the viewpager
     *
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        history = new Stack<>();

        navigationCollectionAdapter = new NavigationCollectionAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(navigationCollectionAdapter);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        List<Integer> itemPageList = Arrays.asList(R.id.ingredient_storage_item,
            R.id.recipe_book_item, R.id.meal_book_item,
            R.id.shopping_cart_item);

        bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            int page = itemPageList.indexOf(menuItem.getItemId());
            viewPager.setCurrentItem(page);
            return true;
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            int i = viewPager.getCurrentItem();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Launcher<Object> launcher =
                (Launcher<Object>) fragmentManager.findFragmentByTag(
                    "f" + i);
            if (launcher != null) {
                launcher.launch(null);
            }
        });

        // fixes crashes related to nested fragments
        viewPager.setOffscreenPageLimit(3);

        int colorPrimary = MaterialColors.getColor(bottomAppBar,
            com.google.android.material.R.attr.colorPrimary);
        int colorOutline = MaterialColors.getColor(bottomAppBar,
            com.google.android.material.R.attr.colorOutline);
        viewPager.registerOnPageChangeCallback(
            new ViewPager2.OnPageChangeCallback() {
                /**
                 * onPageSelected is called when the page is selected and
                 * changes the color of the bottom navigation bar and the
                 * floating action button
                 * @param position the position of the page that was selected
                 *                 in the viewpager
                 */
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // update the bottom app bar selected icon
                    bottomAppBar = findViewById(R.id.bottomAppBar);

                    Menu menu = bottomAppBar.getMenu();
                    for (int i = 0; i < menu.size(); ++i) {
                        menu.getItem(i).getIcon().setTint(colorOutline);
                    }
                    menu.getItem(position).getIcon().setTint(colorPrimary);

                    // update history stack
                    if (history.size() == 0 || history.peek() != position) {
                        history.push(position);
                    }

                    // Hide the fab if the current page is the shopping
                    // cart page
                    if (position == 3) {
                        fab.hide();
                    } else {
                        fab.show();
                    }


                }
            });

        viewPager.setCurrentItem(2, false);
    }

    /**
     * makeSnackbar creates a snackbar with the given message
     *
     * @param text the message to display in the snackbar
     */
    @Override
    public void makeSnackbar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text,
            Snackbar.LENGTH_SHORT).setAnchorView(bottomAppBar).show();
    }
}
