package com.example.wellfed.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.ArrayList;

public class RecipeBookFragment extends Fragment {
    Button startRecipeBtn;
    ArrayList<Recipe> recipes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.recipe_rv);
        String[] titles = {"taco", "bell", "apple","taco", "bell", "apple","taco",
                "bell", "apple","taco", "bell", "apple","taco", "bell", "apple2"};

        for(String t: titles){
            recipes.add(new Recipe(t));
        }

        RecipeAdapter adapter = new RecipeAdapter(getActivity(),recipes);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));


    }
}