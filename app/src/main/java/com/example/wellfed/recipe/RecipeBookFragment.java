package com.example.wellfed.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.MainActivity;
import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;
import com.example.wellfed.ingredient.Ingredient;

import java.util.ArrayList;

// todo create sample data for recipes
// todo setup recipe edit button
// todo setup recipe add button


// recipe object that has been modified
// update our controller here -> to reflect these changes

public class RecipeBookFragment extends Fragment implements Launcher {
    Button startRecipeBtn;
    ArrayList<Recipe> recipes;
    private int selected;

    private RecipeController recipeController;
    RecipeAdapter adapter;

    ActivityResultLauncher<Recipe> recipeLauncher =
            registerForActivityResult(new RecipeContract(), result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        Recipe recipe = result.second;
                        switch (type) {
                            case "Delete":
                                recipeController.deleteRecipe(this.selected);
                                break;
                            default:
                                new IllegalArgumentException();
                        }
                    }
            );

    ActivityResultLauncher<Recipe> recipeEditLauncher = registerForActivityResult(
            new RecipeEditContract(), result -> {
                if (result == null){
                    return;
                }
                String type = result.first;
                Recipe recipe = result.second;
                switch (type){
                    case "save":
                        recipeController.addRecipe(recipe);
                }
                return;
            }
    );


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        recipeController = new RecipeController();

        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.recipe_rv);

        adapter = new RecipeAdapter(getActivity(), recipes, this);
        recipeController.setRecipes(recipes);
        recipeController.setRecipeAdapter(adapter);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    /**
     * launches activity to create new recipe
     */
    @Override
    public void launch() {
        recipeEditLauncher.launch(null);
    }

    @Override
    public void launch(int pos) {
        selected = pos;
        recipeLauncher.launch(recipes.get(pos));
    }
}