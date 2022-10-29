package com.example.wellfed.ingredient;

import android.content.Intent;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.recipe.DeleteDialogFragment;

public class IngredientActivity extends ActivityBase implements DeleteDialogFragment.DeleteIngredient {
    private StorageIngredient ingredient;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_activity);

        Intent intent = getIntent();
        this.ingredient = intent.getParcelableExtra("ingredient");

        TextView title = findViewById(R.id.ingredient_title_textView);
        title.setText(ingredient.getTitle());

        TextView quantity = findViewById(R.id.ingredient_quantity_textView);
        quantity.setText(ingredient.getQuantity());

        TextView unit = findViewById(R.id.ingredient_unit_textView);
        unit.setText(ingredient.getUnit());

        TextView expiration = findViewById(R.id.ingredient_expiration_textView);
        expiration.setText(ingredient.getExpiration());

        TextView location = findViewById(R.id.ingredient_location_textView);
        location.setText(ingredient.getLocation());

        TextView notes = findViewById(R.id.ingredient_notes_textView);
        notes.setText(ingredient.getNotes());

        showDeleteDialog();
    }

    private void showDeleteDialog() {
        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        deleteDialogFragment.show(getSupportFragmentManager(), "deleteDialog");
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void deleteIngredient() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void delete() {
        deleteIngredient();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
