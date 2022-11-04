package com.example.wellfed.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wellfed.ingredient.IngredientStorageFragment;
import com.example.wellfed.recipe.RecipeBookFragment;

import com.example.wellfed.mealplan.MealBookFragment;
import com.example.wellfed.shoppingcart.ShoppingCartFragment;

public class NavigationCollectionAdapter extends FragmentStateAdapter {
    public NavigationCollectionAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new IngredientStorageFragment();
                break;
            case 1:
                fragment = new RecipeBookFragment();
                break;
            case 2:
                fragment = new MealBookFragment();
                break;
            case 3:
                fragment = new ShoppingCartFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected position: " + position);
        }
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

