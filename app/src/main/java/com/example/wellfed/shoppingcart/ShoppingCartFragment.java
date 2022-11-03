package com.example.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment {
    ArrayList<ShoppingCartIngredient> shoppingCartIngredients;

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

        ShoppingCartIngredientAdapter adapter = new ShoppingCartIngredientAdapter(shoppingCartIngredients);
        rvShoppingCart.setAdapter(adapter);
        rvShoppingCart.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}