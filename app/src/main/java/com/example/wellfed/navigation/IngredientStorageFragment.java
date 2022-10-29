package com.example.wellfed.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.IngredientAdapter;
import com.example.wellfed.ingredient.StorageIngredient;

import java.util.ArrayList;
import java.util.Date;


public class IngredientStorageFragment extends Fragment {

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
        RecyclerView recyclerView = view.findViewById(R.id.ingredient_storage_list);

        ArrayList<StorageIngredient> ingredients = new ArrayList<>();
        ingredients.add(new StorageIngredient("Milk", 1, "L", "Freezer",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Eggs", 12, "pcs", "Fridge",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Flour", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Sugar", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Salt", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Milk", 1, "L", "Freezer",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Eggs", 12, "pcs", "Fridge",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Flour", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Sugar", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Salt", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));

        ingredients.add(new StorageIngredient("Milk", 1, "L", "Freezer",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Eggs", 12, "pcs", "Fridge",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Flour", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Sugar", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));
        ingredients.add(new StorageIngredient("Salt", 1, "kg", "Pantry",
                new Date(2021, 12, 12)));


        // Add ingredients to the list
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        recyclerView.setAdapter(ingredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}