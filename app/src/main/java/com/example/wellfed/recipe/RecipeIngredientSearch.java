package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientAdapter;

public class RecipeIngredientSearch extends ActivityBase
        implements RecipeIngredientSearchAdapter.OnItemClickListener {

    StorageIngredientAdapter storageIngredientAdapter;
    private RecyclerView ingredientRecycleView;

    // launch the edit recipeIngredientActivity
    ActivityResultLauncher<Ingredient> editIngredientLauncher = registerForActivityResult(new RecipeIngredientEditContract(),
            result -> {

                if (result == null){
                    return;
                }
                String type = result.first;
                Ingredient ingredient = result.second;

                if (type.equals("edit")) {
                    Intent intent = new Intent();
                    intent.putExtra("type", type);
                    intent.putExtra("ingredient", ingredient);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                return;
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredient_storage);

        RecipeIngredientSearchAdapter adapter = new RecipeIngredientSearchAdapter(new IngredientDB(getApplicationContext(), false));
        adapter.setListener(this);
        ingredientRecycleView = findViewById(R.id.ingredient_storage_list);
        ingredientRecycleView.setAdapter(adapter);
        ingredientRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(Ingredient recipeIngredient) {
        StorageIngredient storageIngredient = new StorageIngredient(recipeIngredient.getDescription());
        storageIngredient.setCategory(recipeIngredient.getCategory());
        editIngredientLauncher.launch(storageIngredient);
    }

}
