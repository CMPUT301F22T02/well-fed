package com.example.wellfed.recipe;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import com.example.wellfed.common.Launcher;
import com.example.wellfed.common.SortingFragment;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * RecipeBookFragment displays a list of Recipes {@link Recipe}
 *
 * @version 1.0.0
 */
public class RecipeBookFragment extends Fragment implements Launcher<Recipe>, RecipeAdapter.RecipeLauncher, SortingFragment.OnSortClick {
	/**
	 * Recipes contains a list of Recipes {@link Recipe}
	 */
	ArrayList<Recipe> recipes;

	/**
	 * Controller class that handles the business logic for recipes
	 */
	private RecipeController recipeController;

	/**
	 * Launcher that launches an RecipeActivity {@link RecipeActivity}
	 */
	ActivityResultLauncher<Recipe> recipeLauncher = registerForActivityResult(new RecipeContract(), result -> {
		if (result == null) {
			return;
		}
		String type = result.first;
		Recipe recipe = result.second;
		switch (type) {
			case "delete":
				recipeController.deleteRecipe(recipe);
				break;
			case "edit":
				recipeController.editRecipe(recipe);
				break;
			default:
				break;
		}
	});
	/**
	 * Launcher that launches RecipeEditActivity {@link RecipeEditActivity}
	 */
	ActivityResultLauncher<Recipe> recipeEditLauncher = registerForActivityResult(new RecipeEditContract(), result -> {
		if (result == null) {
			return;
		}
		String type = result.first;
		Recipe recipe = result.second;
		if ("add".equals(type)) {
			recipeController.addRecipe(recipe);
		}
	});


	/**
	 * method that is called upon creation of view
	 * initializes the variables such as
	 * recipes {@link RecipeBookFragment#recipes}
	 * recipeController {@link RecipeBookFragment#recipeController}
	 *
	 * @param inflater           the LayoutInflater object that can be used
	 *                           to  inflate any views in the fragment
	 * @param container          if non-null, this is the parent view that
	 *                           the  fragment's UI should be attached to.
	 *                           The fragment should not add the view
	 *                           itself,  but this can be used to generate
	 *                           the LayoutParams of the view.
	 * @param savedInstanceState if non-null, this fragment is being
	 *                           re-constructed from a previous saved state
	 *                           as given here.
	 * @return inflated view of the fragment {@link View}
	 */
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		recipes = new ArrayList<>();
		recipeController = new RecipeController(requireActivity());
		recipeController.getRecipeAdapter().setRecipeLauncher(this);
		return inflater.inflate(R.layout.fragment_recipe_book, container, false);
	}

	/**
	 * method that is called when the view is created
	 *
	 * @param view               the View returned by onCreateView
	 *                           (LayoutInflater,  ViewGroup, Bundle)
	 * @param savedInstanceState if non-null, this fragment is being
	 *                           re-constructed from a previous saved
	 *                           state as given here.
	 */
	@Override
	public void onViewCreated(@NonNull View view,
							  @Nullable Bundle savedInstanceState) {

		RecyclerView rvRecipes = view.findViewById(R.id.recipe_rv);
		rvRecipes.setAdapter(recipeController.getRecipeAdapter());
		rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

		SortingFragment sortingFragment = new SortingFragment();
		sortingFragment.setOptions(Arrays.asList("title", "preparation-time", "servings", "category"));
		sortingFragment.setListener(this);
		requireActivity().getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_sort_container2, sortingFragment).commit();


	}

	/**
	 * launches activity for a Recipe{@link Recipe} in
	 * the recipes at pos.
	 */
	@Override
	public void launch(Recipe recipe) {
		if (recipe == null) {
			recipeEditLauncher.launch(null);
		} else {
			recipeLauncher.launch(recipe);
		}
	}

	/**
	 * onClick method for the RecipeAdapter {@link RecipeAdapter}
	 *
	 * @param field the field that was clicked
	 */
	@Override
	public void onClick(String field) {
		recipeController.sort(field);
	}
}
