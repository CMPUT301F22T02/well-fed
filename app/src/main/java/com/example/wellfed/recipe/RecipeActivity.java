package com.example.wellfed.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.ingredient.StoredIngredient;
import com.example.wellfed.ingredient.StoredIngredientDB;

import java.util.Date;

public class RecipeActivity extends ActivityBase {
    private ListView ingredientList;
    private RecipeController recipeController;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getParent();
        Intent intent = getIntent();
        position = intent.getIntExtra("Position", -1);
        Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");

        TextView title = findViewById(R.id.recipe_title_textView);
        title.setText(recipe.getTitle());

        StoredIngredientDB db = new StoredIngredientDB();
        StoredIngredient storedIngredient = new StoredIngredient("Broccoli");
        storedIngredient.setCategory("Vegetable");
        Date bestBefore = new Date(2022, 10, 1);
        storedIngredient.setBestBefore(bestBefore);
        storedIngredient.setLocation("Fridge");
        storedIngredient.setAmount(5);
        storedIngredient.setUnit("kg");

        try {
            db.addStoredIngredient(storedIngredient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Recipe", recipe);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

//        showDeleteDialog();
    }

//    private void showDeleteDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        fm.findFragmentById(R.id.recipe_book);
//        new DeleteDialogFragment(RecipeActivity.this).show(fm, "Delete Recipe");
//    }

    public void setRecipeController(RecipeController recipeController) {
        this.recipeController = recipeController;
    }

}