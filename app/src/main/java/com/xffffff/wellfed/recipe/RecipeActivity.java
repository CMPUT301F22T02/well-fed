package com.xffffff.wellfed.recipe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.ConfirmDialog;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.DeleteButton;
import com.xffffff.wellfed.common.ItemDetailAdapter;
import com.xffffff.wellfed.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays the view for Recipe, showing all of a Recipe's
 * information
 *
 * @version 1.0.0
 */
public class RecipeActivity extends ActivityBase
    implements ConfirmDialog.OnConfirmListener {
    /**
     * Launcher that launches RecipeEditActivity {@link RecipeEditActivity}
     */
    ActivityResultLauncher<Recipe> recipeEditLauncher =
        registerForActivityResult(new RecipeEditContract(), result -> {
            if (result == null) {
                return;
            }
            String type = result.first;
            Recipe recipe = result.second;

            if (type.equals("edit")) {
                Intent intent = new Intent();
                intent.putExtra("item", recipe);
                intent.putExtra("type", "edit");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
    /**
     * stores the list of Ingredient{@link Ingredient}
     */
    private List<Ingredient> ingredientList;
    /**
     * Adapter for the RecyclerView that displays the list of Items
     */
    private ItemDetailAdapter adapter;
    /**
     * stores the recipe {@link Recipe}
     */
    private Recipe recipe;

    /**
     * method that is called when the activity is created
     *
     * @param savedInstanceState the saved instance state
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(
        Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Initialize the variables
        ingredientList = new ArrayList<>();
        adapter = new ItemDetailAdapter();
        adapter.setItems(ingredientList);
        Intent intent = getIntent();


        recipe = (Recipe) intent.getSerializableExtra("item");
        boolean viewonly = intent.getBooleanExtra("viewonly", false);

        FloatingActionButton fab = findViewById(R.id.save_fab);
        Button deleteButton = findViewById(R.id.recipe_delete_btn);

        if (recipe.getServings() == null) {
            fab.setVisibility(View.GONE);
            DBConnection connection = new DBConnection(getApplicationContext());
            RecipeDB recipeDB = new RecipeDB(connection);
            recipeDB.getRecipe(recipe.getId(), (foundRecipe, success) -> {
                recipe = foundRecipe;
                updateView(foundRecipe);
                fab.setVisibility(View.VISIBLE);
            });
        } else {
            updateView(recipe);
        }

        RecyclerView ingredientRv =
            findViewById(R.id.recipe_ingredient_recycleViewer);
        ingredientRv.setAdapter(adapter);
        ingredientRv.setLayoutManager(
            new LinearLayoutManager(RecipeActivity.this));


        if (viewonly) {
            fab.setVisibility(ImageView.GONE);
            deleteButton.setVisibility(ImageView.GONE);
        } else {
            new DeleteButton(this, deleteButton, "Delete Recipe", this);
        }

        fab.setOnClickListener(view -> recipeEditLauncher.launch(recipe));
    }

    /**
     * updateView - updates the view with the recipe information
     * @param recipe the recipe to update the view with
     */
    public void updateView(Recipe recipe) {
        // initialize the views
        TextView title = findViewById(R.id.recipe_title_textView);
        TextView prepTime = findViewById(R.id.recipe_prep_time_textView);
        TextView servings = findViewById(R.id.recipe_no_of_servings_textView);
        TextView category = findViewById(R.id.recipe_category);
        TextView description = findViewById(R.id.recipe_description_textView);

        ingredientList.addAll(recipe.getIngredients());
        adapter.notifyDataSetChanged();
        title.setText(recipe.getTitle());
        String recipePrepTime = recipe.getPrepTimeMinutes() + " mins";
        prepTime.setText(recipePrepTime);
        String servingsText = "Servings: " + recipe.getServings();
        servings.setText(servingsText);
        category.setText(recipe.getCategory());
        description.setText(recipe.getComments());
        updateImageView(recipe.getPhotograph());
    }

    /**
     * method that stops the activity with a result
     * when delete confirmation is complete
     */
    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("item", recipe);
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Update image view with image
     *
     * @param url url of image to display
     */
    public void updateImageView(String url) {
        if (url == null) {
            return;
        }
        ImageView recipeImg = findViewById(R.id.recipe_img);
        Glide.with(this).load(url).into(recipeImg);
        recipeImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        recipeImg.setImageTintList(null);
    }
}
