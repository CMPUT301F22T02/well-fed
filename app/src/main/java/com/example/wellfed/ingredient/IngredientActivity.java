package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.OnDeleteListener;

public class IngredientActivity extends ActivityBase implements OnDeleteListener {
    private IngredientContract contract;
    private StorageIngredient ingredient;
    private IngredientController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_element_subtext);

        Intent intent = getIntent();
        this.ingredient = (StorageIngredient) intent.getSerializableExtra("Ingredient");
        this.contract = new IngredientContract();
        this.controller = new IngredientController();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Ingredient", ingredient);
        returnIntent.putExtra("Reason", "Update");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDelete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Ingredient", ingredient);
        returnIntent.putExtra("Reason", "Delete");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void onEdit() {
        Intent intent = contract.createIntent(this, ingredient);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            ingredient = (StorageIngredient) data.getSerializableExtra("Ingredient");
            controller.updateIngredient(ingredient);
        }
    }


    @Override
    public void deleteIngredient() {
        controller.deleteIngredient(ingredient);
    }

    @Override
    public void delete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Ingredient", ingredient);
        returnIntent.putExtra("Reason", "Delete");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
