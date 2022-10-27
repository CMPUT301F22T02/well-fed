package com.example.wellfed.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

// todo create sample data for recipes
// todo setup recycleview for recipe-ingredients
// todo setup recipe delete button
// todo setup recipe edit button


// recipe object that has been modified
// update our controller here -> to reflect these changes

public class RecipeBookFragment extends Fragment implements RecipeAdapter.RecipeLauncher {
    Button startRecipeBtn;
    ArrayList<Recipe> recipes;
    int position;

    private RecipeController recipeController;
    RecipeAdapter adapter;

    ActivityResultLauncher<Recipe> recipeLauncher = registerForActivityResult(new
                    RecipeContract(),
            new ActivityResultCallback<Recipe>() {
                @Override
                public void onActivityResult(Recipe result) {
                    if (result == null) {
                        recipeController.deleteRecipe(position);
                        return;
                    }
                }
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
        String[] titles = {
                "Banana Cake                 ",
                "Blueberry Coffee Cake       ",
                "Chocolate Cake              ",
                "Chocolate Mayonaise Cake    ",
                "Crazy Cake                  ",
                "Fresh Apple Cake            ",
                "Fresh Pear Cake             ",
                "Graham Cracker Cake         ",
                "Hot Water Chocolate Cake    ",
                "Hungry Bear Cheese Cake     ",
                "Lemon Poppy Cake            ",
                "Light Old Fashioned Fruit Cake",
                "My Best Gingerbread         ",
                "Oatmeal Cake                ",
                "Orange Angel Food Cake      ",
                "Orange-Poppy Seed Pound Cake",
                "Pineapple Cake              ",
                "Pineapple-Carrot Cake*      ",
                "Potatoe Cake                ",
                "Pumpkin Swirl Cheesecake    ",
                "Refrigerator Cheesecake     ",
                "Sherry Wine Cake            ",
                "Special Prune Cake*         ",
                "Spicy Fruit and Nut Cake*   ",
                "Strawberry Spice Loaf       ",
                "Three Layer Chocolate Mayonnaise Cake",
                "Upside Down Cake            ",
                "Blue Chip Cookies           ",
                "Bourbon Balls               ",
                "Chocolate Crisp Bran Cookies",
                "Chocolate Peanut Brunch Bars"
        };

        for (String t : titles) {
            recipes.add(new Recipe(t));
        }

        adapter = new RecipeAdapter(getActivity(), recipes, this);
        recipeController.setRecipes(recipes);
        recipeController.setRecipeAdapter(adapter);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void launch(int pos) {
        position = pos;
        recipeLauncher.launch(recipes.get(pos));
    }
}