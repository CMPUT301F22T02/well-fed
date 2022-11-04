package com.example.wellfed.recipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

// todo create sample data for recipes
// todo setup recipe edit button
// todo setup recipe add button


// recipe object that has been modified
// update our controller here -> to reflect these changes

/**
 * MealBookFragment contains a list Recipes {@link Recipe}
 *
 * @version 1.0.0
 */
public class RecipeBookFragment extends Fragment implements Launcher {
    /**
     * Recipes contains a list of Recipes {@link Recipe}
     */
    ArrayList<Recipe> recipes;
    /**
     * Stores the value of selected recipe in recipes
     */
    private int selected;
    /**
     * Instance of FirebaseFirestore {@link FirebaseFirestore}
     */
    private FirebaseFirestore db;


    /**
     * Controller class that handles the business logic for recipes
     */
    private RecipeController recipeController;

    /**
     * Adapter for list of recipes
     */
    RecipeAdapter adapter;

    /**
     * Launcher that launches an RecipeActivity {@link RecipeActivity}
     */
    ActivityResultLauncher<Recipe> recipeLauncher =
            registerForActivityResult(new RecipeContract(), result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        Recipe recipe = result.second;
                        switch (type) {
                            case "delete":
                                new DeleteRecipeTask().execute(recipe);
                                break;
                            default:
                                new IllegalArgumentException();
                        }
                    }
            );

    /**
     * Asynchronous task to delete a recipe in the bg
     */
    private class DeleteRecipeTask extends
            AsyncTask<Recipe, Void, Void> {
        protected Void doInBackground(Recipe... recipes) {
            for (Recipe recipe : recipes) {
                RecipeBookFragment.this.recipeController.deleteRecipe(recipe.getId());
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            new RecipeBookFragment.GetRecipesTask().execute();
        }
    }

    /**
     * Launcher that launches RecipeEditActivity {@link RecipeEditActivity}
     */
    ActivityResultLauncher<Recipe> recipeEditLauncher = registerForActivityResult(
            new RecipeEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                Recipe recipe = result.second;
                switch (type) {
                    case "save":
                        new AddRecipeTask().execute(recipe);
                }
                return;
            }
    );


    /**
     * method that is called upon creation of view
     * initializes the variables such as
     * recipes {@link RecipeBookFragment#recipes}
     * recipeController {@link RecipeBookFragment#recipeController}
     * db {@link RecipeBookFragment#db}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        recipeController = new RecipeController();
        db = FirebaseFirestore.getInstance();

        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    /**
     * Asynchronous task to add recipe to db in the bg
     */
    private class AddRecipeTask extends
            AsyncTask<Recipe, Void, Void> {
        protected Void doInBackground(Recipe... recipes) {
            for (Recipe recipe : recipes) {
                RecipeBookFragment.this.recipeController.addRecipe(recipe);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            new RecipeBookFragment.GetRecipesTask().execute();
        }
    }

    /**
     * Asynchronous task that gets list of recipes in the bg
     */
    protected class GetRecipesTask extends
            AsyncTask<Void, Void, ArrayList<Recipe>> {
        protected ArrayList<Recipe> doInBackground(Void... voids) {
            try {
                return RecipeBookFragment.this.recipeController.getRecipes();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(ArrayList<Recipe> recipes) {
            RecipeBookFragment.this.recipes.clear();
            RecipeBookFragment.this.recipes.addAll(recipes);
            RecipeBookFragment.this.adapter.notifyDataSetChanged();
        }
    }

    /**
     * method that is called when the view is created
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.recipe_rv);

        adapter = new RecipeAdapter(getActivity(), recipes, this);
        recipeController.setRecipes(recipes);
        recipeController.setRecipeAdapter(adapter);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetRecipesTask().execute();
    }

    /**
     * launches activity to create new recipe
     */
    @Override
    public void launch() {
        recipeEditLauncher.launch(null);
    }

    /**
     * launches activity for a Recipe{@link Recipe} in
     * the recipes at pos.
     * @param pos
     */
    @Override
    public void launch(int pos) {
        selected = pos;
        recipeLauncher.launch(recipes.get(pos));
    }
}
