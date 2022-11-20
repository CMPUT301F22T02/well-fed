package com.example.wellfed.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class EditRecyclerViewFragment<Item extends Serializable> extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Item> items;

    private ActivityResultLauncher<Item> editLauncher =
            registerForActivityResult(new EditItemContract<>(),
                    result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        Item ingredient = result.second;
                        switch (type) {
                            case "add":
                                items.add(ingredient);
//                                recipeIngredientAdapter.notifyItemInserted(
//                                        recipeIngredients.size());
                                break;
                            case "edit":
//                                type
//                                recipeIngredients.set(selectedIngredient,
//                                        ingredient);
//                                recipeIngredientAdapter.notifyItemChanged(
//                                        selectedIngredient);
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    });

    ActivityResultLauncher<Item> searchLauncher =
            registerForActivityResult(new SearchItemContract<>(),
                    result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        Item item = result.second;
                        switch (type) {
                            case "edit":
                                items.add(item);
//                                recipeIngredientAdapter.notifyItemInserted(
//                                        recipeIngredients.size());
                            case "quit":
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    });

    public EditRecyclerViewFragment() {

    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_recycler_view,
                container, false);
        super.onCreate(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(recipeIngredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView addButton = view.findViewById(R.id.addButton);
        ImageView searchButton = view.findViewById(R.id.searchButton);

        addButton.setOnClickListener(v -> {
            editLauncher.launch(null);
        });

        searchButton.setOnClickListener(v -> {
            searchLauncher.launch(null);
        });

        return view;
    }
}
