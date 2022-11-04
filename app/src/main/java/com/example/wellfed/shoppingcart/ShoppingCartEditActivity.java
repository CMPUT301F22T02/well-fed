package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmQuitDialog;
import com.example.wellfed.common.OnQuitListener;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class ShoppingCartEditActivity extends ActivityBase implements OnQuitListener {
    private RequiredTextInputLayout labelTextInput;
    private RequiredTextInputLayout dateTextInput;
    private RequiredDropdownTextInputLayout categoryTextInput;
    private RequiredNumberTextInputLayout amountTextInput;
    private RequiredTextInputLayout unitTextInput;
    private RequiredTextInputLayout locationTextInput;
    private FloatingActionButton fab;

    private ShoppingCartIngredient shoppingCartIngredient;
    private String type;

    @Override
    public void onBackPressed() {
        if (this.hasUnsavedChanges()) {
            new ConfirmQuitDialog(this, this).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.hasUnsavedChanges()) {
            if (item.getItemId() == android.R.id.home) {
                new ConfirmQuitDialog(this, this).show();
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_ingredient_edit);

        this.labelTextInput = findViewById(R.id.labelTextInput);
        this.dateTextInput = findViewById(R.id.dateTextInput);
        this.categoryTextInput = findViewById(R.id.categoryTextInput);
        this.amountTextInput = findViewById(R.id.amountTextInput);
        this.unitTextInput = findViewById(R.id.unitTextInput);
        this.locationTextInput = findViewById(R.id.locationTextInput);

        this.amountTextInput.requireDouble();
        this.amountTextInput.setRequirePositiveNumber(true);
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(view -> onSave());

        Intent intent = this.getIntent();
        this.shoppingCartIngredient = (ShoppingCartIngredient) intent.getSerializableExtra("shoppingCartIngredient");
        if (this.shoppingCartIngredient != null) {
            this.type = "edit";
            this.labelTextInput.setPlaceholderText(this.shoppingCartIngredient.getDescription());
            this.categoryTextInput.setPlaceholderText(
                    this.shoppingCartIngredient.getCategory());
            this.unitTextInput.setPlaceholderText(this.shoppingCartIngredient.getUnit());
            this.amountTextInput.setPlaceholderText(
                    String.format(Locale.US, "%d", shoppingCartIngredient.getAmount()));
        } else {
            this.type = "add";
        }
    }

    private Boolean hasUnsavedChanges() {
        if (this.labelTextInput.hasChanges()) {
            return true;
        }
        if (this.dateTextInput.hasChanges()) {
            return true;
        }
        if (this.categoryTextInput.hasChanges()) {
            return true;
        }
        if (this.amountTextInput.hasChanges()) {
            return true;
        }
        if (this.unitTextInput.hasChanges()) {
            return true;
        }
        if (this.locationTextInput.hasChanges()) {
            return true;
        }
    }
}
