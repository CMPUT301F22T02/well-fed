package com.example.wellfed;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends FragmentActivity {
    final String TAG = "Sample";
    NavigationCollectionAdapter navigationCollectionAdapter;
    ViewPager2 viewPager;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationCollectionAdapter = new NavigationCollectionAdapter(this);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(navigationCollectionAdapter);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            Menu menu = bottomAppBar.getMenu();
            for (int i = 0; i < menu.size(); ++i) {
                menu.getItem(i).getIcon().setTint(
                        getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral10)
                );
            }
            int j;
            switch (menuItem.getItemId()) {
                case R.id.ingredient_storage:
                    j = 0;
                    break;
                case R.id.recipe_book:
                    j = 1;
                    break;
                case R.id.meal_book:
                    j = 2;
                    break;
                case R.id.shopping_cart:
                    j = 3;
                    break;
                default:
                    return false;
            }
            viewPager.setCurrentItem(j);
            menu.getItem(j).getIcon().setTint(
                    getResources().getColor(com.google.android.material.R.color.m3_sys_color_dynamic_light_primary)
            );
            return true;
        });
        viewPager.setCurrentItem(2);
        bottomAppBar.getMenu().getItem(2).setChecked(true);
    }
}

