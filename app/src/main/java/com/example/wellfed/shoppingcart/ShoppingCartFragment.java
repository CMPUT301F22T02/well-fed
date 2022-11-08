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
    ShoppingCartController controller;
    ShoppingCartIngredientAdapter adapter;

    RecyclerView shoppingcartRecyclerView;
    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        shoppingcartRecyclerView = view.findViewById(R.id.shopping_cart_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        shoppingcartRecyclerView.setLayoutManager(linearLayoutManager);
        controller = new ShoppingCartController();
        adapter = new ShoppingCartIngredientAdapter(controller.getIngredients());

        controller.setAdapter(adapter);
        shoppingcartRecyclerView.setAdapter(adapter);
    }
}