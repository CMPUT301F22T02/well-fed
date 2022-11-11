package com.example.wellfed.recipe;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;

public class RecipeIngredientSearch extends ActivityBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredient_storage);
    }
}
