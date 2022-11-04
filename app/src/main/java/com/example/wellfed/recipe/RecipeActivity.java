package com.example.wellfed.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.ingredient.Ingredient;

import com.example.wellfed.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends ActivityBase implements ConfirmDialog.OnConfirmListener {
    private List<Ingredient> ingredientList;
    private Recipe recipe;
    private RecyclerView ingredientRv;
    private RecipeIngredientAdapter recipeIngredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // intialize variables
        ingredientList = new ArrayList<>();
        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("Recipe");

        // add ingredients to the recipe
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientList.add(ingredient);
        }

        // initialize the views
        TextView title = findViewById(R.id.recipe_title_textView);
        TextView prepTime = findViewById(R.id.recipe_prep_time_textView);
        TextView servings = findViewById(R.id.recipe_no_of_servings_textView);
        title.setText(recipe.getTitle());
        prepTime.setText("Prepartion time: " + Integer.toString(recipe.getPrepTimeMinutes()));
        servings.setText("Servings: " + Integer.toString(recipe.getServings()));

        // ingredient recycle view
        ingredientRv = (RecyclerView) findViewById(R.id.recipe_ingredient_recycleViewer);
        recipeIngredientAdapter = new RecipeIngredientAdapter(ingredientList,
                R.layout.recipe_ingredient);
        ingredientRv.setAdapter(recipeIngredientAdapter);
        ingredientRv.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));


        DeleteButton deleteBtn = new DeleteButton(
                this,
                findViewById(R.id.recipe_delete_btn),
                "Delete Recipe",
                this);
    }


    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
