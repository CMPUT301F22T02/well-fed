package com.example.wellfed.ingredient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;


public class IngredientStorageFragment extends Fragment implements IngredientAdapter.IngredientLauncher, Launcher {
    /**
     * FoodStorage is a singleton class that stores all the ingredients.
     */
    private FoodStorage foodStorage;
    /**
     * The adapter for the recycler view.
     */
    private IngredientAdapter ingredientAdapter;
    /**
     * The ingredientController is the controller for the ingredient.
     */
    private IngredientController ingredientController;
    /**
     * The recycler view for the ingredients.
     */
    RecyclerView recyclerView;
    /**
     * The text input for the search bar.
     */
    int position;

    /**
     * ActivityResultLauncher for the IngredientEditActivity to edit an ingredient.
     * The result is a StorageIngredient.
     * The result is null if the user cancels the edit.
     */
    ActivityResultLauncher<StorageIngredient> ingredientLauncher = registerForActivityResult(new IngredientContract(), result -> {
        if (result == null) {
            return;
        }
        String type = result.first;
        StorageIngredient ingredient = result.second;
        switch (type) {
            case "delete":
                ingredientController.deleteIngredient(position);
                ingredientAdapter.notifyItemRangeChanged(position, foodStorage.getIngredients().size());
                break;
            case "edit":
                ingredientController.updateIngredient(position, ingredient);
                break;
            default:
                break;
        }
    });

    /**
     * ActivityResultLauncher for the IngredientAddActivity to add an ingredient.
     * The result is a StorageIngredient.
     * The result is null if the user cancels the add.
     */
    ActivityResultLauncher<StorageIngredient> addIngredientLauncher = registerForActivityResult(new IngredientEditContract(), result -> {
        if (result == null) {
            return;
        }
        String type = result.first;
        StorageIngredient ingredient = result.second;
        switch (type) {
            case "add":
                ingredientController.addIngredient(ingredient);
                break;
            case "quit":
                break;
            default:
                throw new IllegalArgumentException();
        }
    });

    /**
     * onCreate method for the IngredientStorageFragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        foodStorage = new FoodStorage();
        ingredientController = new IngredientController();

        return inflater.inflate(R.layout.fragment_ingredient_storage, container, false);
    }

    /**
     * onViewCreated method for the IngredientStorageFragment.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredient_storage_list);

        // Add ingredients to food storage
        foodStorage.addIngredient(new StorageIngredient("Milk", 1.0f, "l", "Freezer", new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Eggs", 12.0f, "count", "Fridge", new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Bread", 1.0f, "loaf", "Pantry", new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Butter", 1.0f, "stick", "Fridge", new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Cheese", 1.0f, "block", "Fridge", new Date(2021, 12, 12)));
        foodStorage.addIngredient(new StorageIngredient("Chicken", 1.0f, "lb", "Freezer", new Date(2021, 12, 12)));


        // Add ingredients to the list
        ingredientAdapter = new IngredientAdapter(getActivity(), foodStorage.getIngredients(), this);
        ingredientController.setIngredients(foodStorage.getIngredients());
        ingredientController.setIngredientAdapter(ingredientAdapter);
        recyclerView.setAdapter(ingredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Search bar
        TextInputEditText searchBar =
                view.findViewById(R.id.ingredient_storage_search);

        // On search bar text change show "Functionality not implemented yet" message
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getActivity(), "Functionality not implemented yet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Launches the IngredientEditActivity to edit an ingredient.
     * @param pos The position of the ingredient in the list.
     */
    @Override
    public void launch(int pos) {
        position = pos;
        ingredientLauncher.launch(foodStorage.getIngredients().get(pos));
    }

    /**
     * Launches the IngredientAddActivity to add an ingredient.
     */
    @Override
    public void launch() {
        addIngredientLauncher.launch(null);
    }

    /**
     * getDefaultViewModelProviderFactory method for the IngredientStorageFragment.
     * @return A default ViewModelProviderFactory.
     */
    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}