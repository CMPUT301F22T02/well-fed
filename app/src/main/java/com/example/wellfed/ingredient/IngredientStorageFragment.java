package com.example.wellfed.ingredient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.FoodStorage;
import com.example.wellfed.ingredient.IngredientAdapter;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.recipe.RecipeAdapter;
import com.example.wellfed.recipe.RecipeController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class IngredientStorageFragment extends Fragment implements IngredientAdapter.IngredientLauncher {
    private FoodStorage foodStorage;
    private IngredientAdapter ingredientAdapter;
    private IngredientController ingredientController;
    RecyclerView recyclerView;
    int position;

    ActivityResultLauncher<StorageIngredient> ingredientLauncher = registerForActivityResult(new IngredientContract(),
            result -> {
                if (result == null) {
                    ingredientController.deleteIngredient(position);
                    return;
                }
                ingredientController.updateIngredient(position, result);
            }
    );


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        foodStorage = new FoodStorage();
        ingredientController = new IngredientController();

        return inflater.inflate(R.layout.fragment_ingredient_storage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredient_storage_list);

        // Add ingredients to food storage
        foodStorage.addIngredient(new StorageIngredient("Milk", 1.0f, "l",
                "Freezer",
                new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Eggs", 12.0f, "count",
                "Fridge",
                new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Bread", 1.0f, "loaf",
                "Pantry",
                new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Butter", 1.0f, "stick",
                "Fridge",
                new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Cheese", 1.0f, "block",
                "Fridge",
                new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Chicken", 1.0f, "lb",
                "Freezer",
                new Date(2021, 12, 12)));


        // Add ingredients to the list
        ingredientAdapter = new IngredientAdapter(getActivity(),
                foodStorage.getIngredients(),
                this);
        ingredientController.setIngredients(foodStorage.getIngredients());
        ingredientController.setIngredientAdapter(ingredientAdapter);
        recyclerView.setAdapter(ingredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageButton addIngredientButton = view.findViewById(R.id.image_filter_button);
    }

    @Override
    public void launch(int pos) {
        position = pos;
        ingredientLauncher.launch(foodStorage.getIngredients().get(pos));
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}