package com.xffffff.wellfed.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.xffffff.wellfed.ingredient.IngredientStorageFragment;
import com.xffffff.wellfed.mealplan.MealBookFragment;
import com.xffffff.wellfed.recipe.RecipeBookFragment;
import com.xffffff.wellfed.shoppingcart.ShoppingCartFragment;

/**
 * NavigationCollectionAdapter is the adapter for the navigation collection
 * that handles the fragments for each tab in the bottom navigation bar
 */
public class NavigationCollectionAdapter extends FragmentStateAdapter {
    /**
     * Constructor for the NavigationCollectionAdapter
     *
     * @param fa the FragmentActivity that contains the
     *           NavigationCollectionAdapter
     */
    public NavigationCollectionAdapter(FragmentActivity fa) {
        super(fa);
    }

    /**
     * createFragment creates a new fragment for the tab at the given position
     *
     * @param position the position of the tab
     * @return the fragment for the tab at the given position
     */
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
                throw new IllegalStateException(
                    "Unexpected position: " + position);
        }
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return fragment;
    }

    /**
     * getItemCount returns the number of tabs in the bottom navigation bar
     *
     * @return the number of tabs in the bottom navigation bar
     */
    @Override
    public int getItemCount() {
        return 4;
    }
}

