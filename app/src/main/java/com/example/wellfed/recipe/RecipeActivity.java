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

public class RecipeActivity extends ActivityBase {
    private ListView ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");

        TextView title = findViewById(R.id.recipe_title_textView);
        title.setText(recipe.getTitle());
        showDeleteDialog();
    }

    private void showDeleteDialog() {
        FragmentManager fm = getSupportFragmentManager();
        new DeleteDialogFragment().show(fm, "Delete Recipe");
    }


}