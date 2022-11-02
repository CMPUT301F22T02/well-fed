package com.example.wellfed.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.FoodStorage;
import com.example.wellfed.ingredient.IngredientAdapter;
import com.example.wellfed.ingredient.StorageIngredient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class IngredientStorageFragment extends Fragment {
    FoodStorage foodStorage;
    IngredientAdapter ingredientAdapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ingredient_storage, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredient_storage_list);
        foodStorage = new FoodStorage();

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
        ingredientAdapter = new IngredientAdapter(foodStorage.getIngredients());
        recyclerView.setAdapter(ingredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        ImageButton addIngredientButton = view.findViewById(R.id.image_filter_button);
        addIngredientButton.setOnClickListener(this::addIngredient);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addIngredient(View view) {
        // Create an alert dialog to add an ingredient with the layout ingredient_add_dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.ingredient_add_dialog, null);
        // Add buttons to the dialog
        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, id) -> {
                    // Get the text from the text fields
                    TextView name = dialogView.findViewById(R.id.ingredient_name);
                    TextView quantity = dialogView.findViewById(R.id.ingredient_quantity);
                    TextView unit = dialogView.findViewById(R.id.ingredient_unit);
                    TextView location = dialogView.findViewById(R.id.ingredient_location);
                    TextView expiration = dialogView.findViewById(R.id.ingredient_expiration);

                    // Create a new ingredient with the text from the text fields
                    StorageIngredient ingredient = new StorageIngredient(name.getText().toString(),
                            Float.parseFloat(quantity.getText().toString()),
                            unit.getText().toString(),
                            location.getText().toString(),
                            new Date(expiration.getText().toString()));

                    // Add the ingredient to the food storage
                    foodStorage.addIngredient(ingredient);
                    ingredientAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}