package com.example.wellfed.Recipe2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wellfed.R;

public class RecipeActivity extends AppCompatActivity {
    private ListView ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        String ingredients[] = {"1 lb Ground Beef", "1 table spoon chili powder", "1 teaspoon ground cumin"};
        ingredientList = findViewById(R.id.recipe_ingredient_listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_recipe_list, ingredients);

        ingredientList.setAdapter(arrayAdapter);

    }

    // helps to go back to the previous active fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}