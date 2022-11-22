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
import com.example.wellfed.common.SortingFragment;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredient;

import java.util.Arrays;

public class RecipeIngredientSearch extends ActivityBase
        implements RecipeIngredientSearchAdapter.OnItemClickListener, SortingFragment.OnSortClick {
    private RecyclerView ingredientRecycleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredient_storage);

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setListener(this);
        sortingFragment.setOptions(Arrays.asList(new String[]{"description", "best-before"}));
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_sort_container, sortingFragment)
                .commit();


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
        Intent intent = new Intent();
        intent.putExtra("type", "add");
        intent.putExtra("item", recipeIngredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(String field) {

    }
}
