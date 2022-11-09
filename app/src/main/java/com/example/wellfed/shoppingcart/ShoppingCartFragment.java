package com.example.wellfed.shoppingcart;

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

import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment implements ShoppingCartIngredientAdapter.ShoppingCartIngredientLauncher, Launcher {
    /**
     * ShoppingCart is a singleton class that stores all ShoppingCartIngredient objects.
     */
    private ShoppingCart shoppingCart;

    /**
     * Adapter for the recycler view.
     */
    private ShoppingCartIngredientAdapter shoppingCartIngredientAdapter;

    /**
     * Controller for the ingredients.
     */
    private ShoppingCartIngredientController shoppingCartIngredientController;

    /**
     * Recycler view for the ingredients.
     */
    RecyclerView recyclerView;

    int position;

    /**
     * ActivityResultLauncher for the ShoppingCartIngredientEditActivity to edit an ingredient.
     * The result is a ShoppingCartIngredient.
     * The result is null if the user cancels the edit.
     */
    ActivityResultLauncher<ShoppingCartIngredient> shoppingCartIngredientLauncher = registerForActivityResult(new )

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}