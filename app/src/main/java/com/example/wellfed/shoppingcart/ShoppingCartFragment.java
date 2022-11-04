package com.example.wellfed.shoppingcart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;

import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment
        implements Launcher,
                    RecyclerView.AdapterDataObserver.OnAdapterDataChangedListener {
    ShoppingCartIngredientAdapter shoppingCartIngredientAdapter;
    private RecyclerView shoppingCartRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ShoppingCartController controller;
    private int selected;

    ActivityResultLauncher<ShoppingCartIngredient> launcher =
            registerForActivityResult(new ShoppingCartContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                ShoppingCartIngredient shoppingCartIngredient = result.second;
                switch (type) {
                    case "delete":
                        controller.deleteShoppingCartIngredient(this.selected);
                        break;
                    case "edit":
                        controller.editShoppingCartIngredient(selected, shoppingCartIngredient);
                        break;
                    case "launch":
                        this.launch(this.selected);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });

    ActivityResultLauncher<ShoppingCartIngredient> editLauncher =
            registerForActivityResult(new ShoppingCartContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                ShoppingCartIngredient shoppingCartIngredient = result.second;
                switch (type) {
                    case "add":
                        controller.addShoppingCartIngredient(shoppingCartIngredient);
                        break;
                    case "quit":
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.shoppingCartRecyclerView = view.findViewById(R.id.shopping_cart_list);
        this.linearLayoutManager = new LinearLayoutManager(getContext());
        this.shoppingCartRecyclerView.setLayoutManager(linearLayoutManager);

        controller = new ShoppingCartController();

        this.shoppingCartIngredientAdapter =
                new ShoppingCartIngredientAdapter(this, this.controller.getShoppingCartIngredients());
        this.controller.setAdapter(this.shoppingCartIngredientAdapter);
        this.shoppingCartRecyclerView.setAdapter(this.shoppingCartIngredientAdapter);
    }
}