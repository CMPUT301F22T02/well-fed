package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.UTCDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.Objects;

/**
 * Represents an Ingredient's information page.
 */
public class IngredientActivity extends ActivityBase
        implements ConfirmDialog.OnConfirmListener {
    /**
     * The StorageIngredient object for the ingredient.
     */
    private StorageIngredient ingredient;

    /**
     * ActivityResultLauncher for the IngredientEditActivity to edit an
     * ingredient.
     */
    private final ActivityResultLauncher<StorageIngredient> launcher =
            registerForActivityResult(new IngredientEditContract(), result -> {
                String type = result.first;
                StorageIngredient ingredient = result.second;
                switch (type) {
                    case "quit":
                        onQuitEdit();
                        break;
                    case "edit":
                        onEdit(ingredient);
                        break;
                    default:
                        break;
                }
            });

    /**
     * onCreate method for the activity.
     *
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_activity);

        // Get ingredient from intent
        Intent intent = getIntent();
        ingredient =
                (StorageIngredient) intent.getSerializableExtra("ingredient");

        // Set ingredient name
        TextView ingredientName = findViewById(R.id.ingredient_name);
        ingredientName.setText(ingredient.getDescription());

        // Set ingredient best before with id=ingredient_best_before_date_value
        TextView ingredientBestBefore =
                findViewById(R.id.ingredient_best_before_date_value);
        UTCDate bestBefore = UTCDate.from(ingredient.getBestBeforeDate());
        ingredientBestBefore.setText(bestBefore.format("yyyy-MM-dd"));

        // Set ingredient quantity with id=ingredient_quantity_value
        TextView ingredientQuantity =
                findViewById(R.id.ingredient_quantity_value);
        ingredientQuantity.setText(ingredient.getAmountAndUnit());

        // Set ingredient location with id=ingredient_location_value
        TextView ingredientLocation =
                findViewById(R.id.ingredient_location_value);
        ingredientLocation.setText(ingredient.getLocation());

        // Set ingredient category with id=ingredient_categories_recycler_view
        TextView ingredientCategories = findViewById(R.id.ingredient_category);
        // Hide ingredient category if there is no category
        if (ingredient.getCategory() == null) {
            ingredientCategories.setVisibility(TextView.GONE);
        } else {
            ingredientCategories.setText(ingredient.getCategory());
        }


        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        // Set delete button
        new DeleteButton(this, findViewById(R.id.ingredient_delete_button),
                "Delete " + "ingredient?", this);

        // Set edit button
        FloatingActionButton editButton =
                findViewById(R.id.ingredient_edit_button);
        editButton.setOnClickListener(v -> launcher.launch(ingredient));
    }

    /**
     * onBackButtonPressed method for the activity. This method is called
     * when the back button is pressed.
     */
    @Override public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", ingredient);
        intent.putExtra("type", "back");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    /**
     * onConfirm method for the activity. This method is called when the user
     * confirms the delete action.
     */
    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("type", "launch");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onEdit method for the activity. This method is called when the user
     * edits an ingredient.
     *
     * @param ingredient The edited ingredient.
     */
    public void onEdit(StorageIngredient ingredient) {
        Intent intent = new Intent();
        intent.putExtra("type", "edit");
        intent.putExtra("ingredient", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onActivityResults method for the activity. This method is called when
     * the user confirms the delete action.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The data.
     */
    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            ingredient =
                    (StorageIngredient) data.getSerializableExtra("Ingredient");
            //            controller.updateIngredient(ingredient);
        }
    }

    /**
     * onConfirm method for the activity. This method is called when the user
     * confirms the delete action.
     */
    @Override public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", ingredient);
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}