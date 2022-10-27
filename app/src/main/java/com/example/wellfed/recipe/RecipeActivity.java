package com.example.wellfed.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;

public class RecipeActivity extends ActivityBase implements DeleteDialogFragment.DeleteRecipe {
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
        showDeleteDialog();
    }

    private void showDeleteDialog() {
        FragmentManager fm = getSupportFragmentManager();
        fm.findFragmentById(R.id.recipe_book);
        new DeleteDialogFragment(RecipeActivity.this).show(fm, "Delete Recipe");
    }

    public void setRecipeController(RecipeController recipeController){
        this.recipeController = recipeController;
    }

    @Override
    public void deleteRecipe(){
        recipeController.deleteRecipe(position);
    }


}