package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.ingredient.StorageIngredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ShoppingCartIngredientActivity extends ActivityBase implements
        ConfirmDialog.OnConfirmListener {
    /**
     * The ShoppingCartIngredientContract is the contract for the ingredient.
     */
    private ShoppingCartIngredientContract contract;

    /**
     * The ShoppingCartIngredient object for the ingredient.
     */
    private ShoppingCartIngredient shoppingCartIngredient;

    /**
     * The ShoppingCartIngredientController is the controller for the ingredient.
     */
    private ShoppingCartIngredientController controller;

    /**
     * ActivityResultLauncher for the ShoppingCartIngredientEditActivity to edit an ingredient.
     */
    private final ActivityResultLauncher<ShoppingCartIngredient> editShoppingCartIngredient
            = registerForActivityResult(new ShoppingCartIngredientEditContract(),
            result -> {
                String type = result.first;
                ShoppingCartIngredient shoppingCartIngredient = result.second;
                switch(type) {
                    case "quit":
                        onQuitEdit();
                        break;
                    case "edit":
                        onEdit(shoppingCartIngredient);
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_ingredient_activity);

        // Get ingredient from intent
        Intent intent = getIntent();
        shoppingCartIngredient = (ShoppingCartIngredient) intent.getSerializableExtra("ingredient");

        // Set ingredient description
        TextView description = findViewById(R.id.shopping_cart_ingredient_description);
        description.setText(shoppingCartIngredient.getDescription());

        // Set ingredient amount
        TextView amount = findViewById(R.id.shopping_cart_ingredient_amount);
        amount.setText(String.valueOf(shoppingCartIngredient.getAmount()));

        // Set ingredient unit
        TextView unit = findViewById(R.id.shopping_cart_ingredient_unit);
        unit.setText(shoppingCartIngredient.getUnit());

        TextView category = findViewById(R.id.shopping_cart_ingredient_category);
        // Hide ingredient category if there is no category
        if (shoppingCartIngredient.getCategory() == null) {
            category.setVisibility(TextView.GONE);
        } else {
            category.setText(shoppingCartIngredient.getCategory());
        }

        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set delete button
        DeleteButton deleteButton = new DeleteButton(this, findViewById(R.id.shopping_cart_ingredient_delete_button),
                "Delete" + "ingredient?", this);

        // Set edit button
        FloatingActionButton editButton = findViewById(R.id.shopping_cart_ingredient_edit_button);
        editButton.setOnClickListener(v -> {
            editShoppingCartIngredient.launch(shoppingCartIngredient);
        });
    }

    /**
     * onBackButtonPressed method for the activity. This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", shoppingCartIngredient);
        intent.putExtra("type", "back");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onConfirm method for the activity. This method is called when the user confirms the delete action.
     */
    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", shoppingCartIngredient);
        intent.putExtra("type", "quit");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onEdit method for the activity. This method is called when the user edits an ingredient.
     *
     * @param shoppingCartIngredient
     */
    public void onEdit(ShoppingCartIngredient shoppingCartIngredient) {
        Intent intent = new Intent();
        intent.putExtra("ingredient", shoppingCartIngredient);
        intent.putExtra("type", "edit");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onActivityResults method for the activity. This method is called when the user confirms the delete action.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            shoppingCartIngredient = (ShoppingCartIngredient) data.getSerializableExtra("Ingredient");
            controller.updateIngredient(shoppingCartIngredient);
        }
    }

    /**
     * onPointerCaptureChanged method for the activity. This method is called when the user confirms the delete action.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    /**
     * onConfirm method for the activity. This method is called when the user confirms the delete action.
     */
    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", shoppingCartIngredient);
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
