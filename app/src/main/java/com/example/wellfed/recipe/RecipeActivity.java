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
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.OnDeleteListener;
import com.example.wellfed.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends ActivityBase implements OnDeleteListener {
    private List<RecipeIngredient> ingredientList;
    private Recipe recipe;
    private RecyclerView ingredientRv;
    private RecipeIngredientAdapter recipeIngredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ingredientList = new ArrayList<>();


        getParent();
        Intent intent = getIntent();
        this.recipe = (Recipe) intent.getSerializableExtra("Recipe");
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            ingredientList.add(ingredient);
        }


        TextView title = findViewById(R.id.recipe_title_textView);
        title.setText(recipe.getTitle());

        ingredientRv = (RecyclerView) findViewById(R.id.recipe_ingredient_recycleViewer);
        recipeIngredientAdapter = new RecipeIngredientAdapter(ingredientList);
        ingredientRv.setAdapter(recipeIngredientAdapter);
        ingredientRv.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));


//        TODO: implement delete button
        DeleteButton deleteBtn = new DeleteButton(this,
                findViewById(R.id.recipe_delete_btn),
                "Delete Recipe",
                this);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Recipe", recipe);
        returnIntent.putExtra("Reason", "BackPressed");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDelete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("type", "Delete");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


}