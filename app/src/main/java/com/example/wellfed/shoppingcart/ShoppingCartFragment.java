package com.example.wellfed.shoppingcart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.ArrayList;

// TODO: switch to another activity to display details of an item when it's clicked
// TODO: add swipe delete feature
public class ShoppingCartFragment extends Fragment {
    ArrayList<ShoppingCartIngredient> shoppingCartIngredients;

    ShoppingCartIngredientAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        shoppingCartIngredients = new ArrayList<>();

        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvShoppingCart = view.findViewById(R.id.shopping_cart_list);

        ShoppingCartIngredient ingredient1 = new ShoppingCartIngredient("Banana");
        ingredient1.setUnit("1 banana");
        shoppingCartIngredients.add(ingredient1);

        ShoppingCartIngredient ingredient2 = new ShoppingCartIngredient("Apple");
        ingredient2.setUnit("2 apples");
        shoppingCartIngredients.add(ingredient2);

        ShoppingCartIngredient ingredient3 = new ShoppingCartIngredient("Salt");
        ingredient3.setUnit("500 g");
        shoppingCartIngredients.add(ingredient3);

        adapter = new ShoppingCartIngredientAdapter(shoppingCartIngredients);
        rvShoppingCart.setAdapter(adapter);
        rvShoppingCart.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageButton addIngredientButton = view.findViewById(R.id.shopping_cart_filter_button);
        addIngredientButton.setOnClickListener(this::addShoppingCartIngredient);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addShoppingCartIngredient(View view) {
        // Create an alert dialog to add an ingredient with the layout ingredient_add_dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_shopping_cart_add, null);
        // Add buttons to the dialog
        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, id) -> {
                    // Get the text from the text fields
                    TextView label = dialogView.findViewById(R.id.label);
                    TextView date = dialogView.findViewById(R.id.date);
                    TextView category = dialogView.findViewById(R.id.category);
                    TextView amount = dialogView.findViewById(R.id.amount);
                    TextView unit = dialogView.findViewById(R.id.unit);
                    TextView location = dialogView.findViewById(R.id.location);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}