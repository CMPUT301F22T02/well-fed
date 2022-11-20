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
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.Launcher;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

// todo create sample data for recipes
// todo setup recipe edit button
// todo setup recipe add button


// recipe object that has been modified
// update our controller here -> to reflect these changes

/**
 * RecipeBookFragment displays a list of Recipes {@link Recipe}
 *
 * @version 1.0.0
 */
public class RecipeBookFragment extends Fragment implements Launcher<Recipe>,
        RecipeAdapter.RecipeLauncher {
    /**
     * Recipes contains a list of Recipes {@link Recipe}
     */
    ArrayList<Recipe> recipes;
    /**
     * Stores the value of selected recipe in recipes
     */
    private int selected;

    /**
     * Controller class that handles the business logic for recipes
     */
    private RecipeController recipeController;

    /**
     * Adapter for list of recipes
     */
    RecipeAdapter adapter;

    RecipeDB recipeDB;

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
                                recipeController.deleteRecipe(recipe.getId());
                                break;
                            case "edit":
                                recipeController.editRecipe(recipe);
                                break;
                            default:
                                new IllegalArgumentException();
                        }
                    }
            );
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
                        recipeController.addRecipe(recipe);
                        break;
                    default:
                        break;
                }
                return;
            }
    );


    /**
     * method that is called upon creation of view
     * initializes the variables such as
     * recipes {@link RecipeBookFragment#recipes}
     * recipeController {@link RecipeBookFragment#recipeController}
     *
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
        recipeController = new RecipeController(getActivity());
        recipeController.getRecipeAdapter().setRecipeLauncher(this);
        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    /**
     * method that is called when the view is created
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.recipe_rv);
        rvRecipes.setAdapter(recipeController.getRecipeAdapter());
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

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

    //        TODO: fix view recipes using onItemClick listener instead of
    //         this one @manpreet
}
