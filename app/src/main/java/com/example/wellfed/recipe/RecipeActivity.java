package com.example.wellfed.recipe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.ingredient.Ingredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Activity that holds the view for Recipe showing all it's detail
 *
 * @version 1.0.0
 */
public class RecipeActivity extends ActivityBase implements ConfirmDialog.OnConfirmListener {
    /**
     * stores the list of Ingredient{@link Ingredient}
     */
    private List<Ingredient> ingredientList;

    /**
     * stores the recipe {@link Recipe}
     */
    private Recipe recipe;

    /**
     * Launcher that launches RecipeEditActivity {@link RecipeEditActivity}
     */
    ActivityResultLauncher<Recipe> recipeEditLauncher = registerForActivityResult(
            new RecipeEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                Recipe recipe = result.second;

                if (type.equals("edit")) {
                    Intent intent = new Intent();
                    intent.putExtra("Recipe", recipe);
                    intent.putExtra("type", "edit");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                return;
            }
    );

    /**
     * method that is called when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Initialize the variables
        ingredientList = new ArrayList<>();

        /**
         * Stores the intent the activity was created with
         */
        Intent intent = getIntent();


        recipe = (Recipe) intent.getSerializableExtra("Recipe");

        // initialize the views
        TextView title = findViewById(R.id.recipe_title_textView);
        TextView prepTime = findViewById(R.id.recipe_prep_time_textView);
        TextView servings = findViewById(R.id.recipe_no_of_servings_textView);
        TextView category = findViewById(R.id.recipe_category);
        TextView description = findViewById(R.id.recipe_description_textView);
        ImageView img = findViewById(R.id.recipe_img);
        FloatingActionButton fab = findViewById(R.id.save_fab);

        DBConnection connection = new DBConnection(getApplicationContext());
        RecipeDB recipeDB = new RecipeDB(connection);
        recipeDB.getRecipe(recipe.getId(), (foundRecipe, success) -> {
            recipe = foundRecipe;
            title.setText(recipe.getTitle());
            prepTime.setText("Preparation time: " + Integer.toString(recipe.getPrepTimeMinutes()));
            servings.setText("Servings: " + Integer.toString(recipe.getServings()));
            category.setText("Category: " + recipe.getCategory());
            description.setText(recipe.getComments());

            Picasso.get()
                    .load(recipe.getPhotograph())
                    .rotate(90)
                    .into(img);
            // add ingredients to the recipe
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredientList.add(ingredient);
//                recipeIngredientAdapter.notifyItemInserted(ingredientList.size());
            }
        });


//        // ingredient recycle view
//        ingredientRv = (RecyclerView) findViewById(R.id.recipe_ingredient_recycleViewer);
//        recipeIngredientAdapter = new RecipeIngredientAdapter(ingredientList,
//                R.layout.recipe_ingredient);
//        ingredientRv.setAdapter(recipeIngredientAdapter);
//        ingredientRv.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));

        /**
         * DeleteBtn to create a dialog asking for delete confirmation
         */
        DeleteButton deleteBtn = new DeleteButton(
                this,
                findViewById(R.id.recipe_delete_btn),
                "Delete Recipe",
                this);

        fab.setOnClickListener(view->{
            recipeEditLauncher.launch(recipe);
        });
    }

    /**
     * method that stops the activity with a result
     * when delete confirmation is complete
     */
    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("Recipe", recipe);
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
