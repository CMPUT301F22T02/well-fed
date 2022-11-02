package com.example.wellfed.recipe;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
// todo create an xml file for this class

/**
 * Activity which allows user to edit an existing recipe
 * @version 1.0.0
 */
public class RecipeEditActivity extends ActivityBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);
    }
}
