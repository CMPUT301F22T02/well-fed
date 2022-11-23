package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.SortingFragment;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;

import java.util.Arrays;

/**
 * Activity which allows user to search for an existing ingredient
 * in the user's ingredient storage, or previously added recipe ingredients
 * to add to a recipe
 */
public class RecipeIngredientSearch extends ActivityBase
        implements RecipeIngredientSearchAdapter.OnItemClickListener, SortingFragment.OnSortClick {
    private RecyclerView ingredientRecycleView;
    private RecipeIngredientSearchAdapter adapter;

	/**
	 * The onCreate method for the ingredient search screen.
	 *
	 * @param savedInstanceState Bundle object for the activity.
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ingredient_storage);

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setListener(this);
        sortingFragment.setOptions(Arrays.asList(new String[]{"description", "category"}));
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_sort_container, sortingFragment)
                .commit();


        DBConnection connection = new DBConnection(getApplicationContext());
        IngredientDB db = new IngredientDB(connection);
        adapter = new RecipeIngredientSearchAdapter(db);
        adapter.setListener(this);
        ingredientRecycleView = findViewById(R.id.ingredient_storage_list);
        ingredientRecycleView.setAdapter(adapter);
        ingredientRecycleView.setLayoutManager(new LinearLayoutManager(this));

	}

	/**
	 * Listener for when an item is clicked within the RecyclerView of ingredients
	 *
	 * @param recipeIngredient The item that was clicked on
	 */
	@Override
	public void onItemClick(Ingredient recipeIngredient) {
		Intent intent = new Intent();
		intent.putExtra("type", "add");
		intent.putExtra("item", recipeIngredient);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

    @Override
    public void onClick(String field) {
        this.adapter.sortByField(field);
    }
}
