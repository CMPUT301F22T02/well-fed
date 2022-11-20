package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.EditItemContract;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredient;

public class RecipeIngredientSearch extends ActivityBase
        implements RecipeIngredientSearchAdapter.OnItemClickListener {
    private RecyclerView ingredientRecycleView;

    // launch the edit recipeIngredientActivity
//    ActivityResultLauncher<Pair<Ingredient, Class>> editIngredientLauncher =
//            registerForActivityResult(new EditItemContract<>(),
//            result -> {
//                if (result == null){
//                    return;
//                }
//                String type = result.first;
//                Ingredient ingredient = result.second;
//
//                if (type.equals("edit")) {
//                    Intent intent = new Intent();
//                    intent.putExtra("type", type);
//                    intent.putExtra("ingredient", ingredient);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
//                }
//            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredient_storage);

        DBConnection connection = new DBConnection(getApplicationContext());
        IngredientDB db = new IngredientDB(connection);
        RecipeIngredientSearchAdapter adapter = new RecipeIngredientSearchAdapter(db);
        adapter.setListener(this);
        ingredientRecycleView = findViewById(R.id.ingredient_storage_list);
        ingredientRecycleView.setAdapter(adapter);
        ingredientRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(Ingredient recipeIngredient) {
        StorageIngredient storageIngredient = new StorageIngredient(recipeIngredient.getDescription());
        storageIngredient.setCategory(recipeIngredient.getCategory());
//        editIngredientLauncher.launch(new Pair<Ingredient, Class>(storageIngredient,
//                RecipeIngredientEditActivity.class));
    }

}
