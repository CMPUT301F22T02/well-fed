package com.example.wellfed.shoppingcart;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;

public class ShoppingCartIngredientEditActivity extends AppCompatActivity implements ConfirmDialog.OnConfirmListener {
    /**
     * EditText for the ingredient's description.
     */
    private EditText description;

    /**
     * EditText for the ingredient's amount.
     */
    private EditText amount;

    /**
     * EditText for the ingredient's unit.
     */
    private EditText unit;

    /**
     * EditText for the ingredient's location.
     */
    private EditText location;

    /**
     * EditText for the ingredient's expiration date.
     */
    private EditText bestBefore;

    /**
     * EditText for category of the ingredient.
     */
    private EditText category;

    /**
     * ShoppingCartIngredient object for the ingredient.
     */
    private ShoppingCartIngredient ingredient;

    /**
     * OnCreate method for the activity.
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_shopping_cart_ingredient);

        description = findViewById(R.id.shopping_cart_ingredient_description);
        amount = findViewById(R.id.shopping_cart_ingredient_amount);
    }
}
