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

public class MainActivity extends ActivityBase {
    final String TAG = "MainActivity";
    Stack<Integer> history;
    NavigationCollectionAdapter navigationCollectionAdapter;
    ViewPager2 viewPager;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override public void onBackPressed() {
        if (history.size() < 2) {
            super.onBackPressed();
        } else {
            history.pop();
            viewPager.setCurrentItem(history.peek());
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
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

        int colorPrimary = MaterialColors.getColor(bottomAppBar,
                com.google.android.material.R.attr.colorPrimary);
        int colorOutline = MaterialColors.getColor(bottomAppBar,
                com.google.android.material.R.attr.colorOutline);
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override public void onPageSelected(int position) {
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

                        // show/hide fab
                        // todo: AP
                    }
                });

        viewPager.setCurrentItem(2, false);
    }

    @Override public void makeSnackbar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text,
                Snackbar.LENGTH_SHORT).setAnchorView(bottomAppBar).show();
    }
}
