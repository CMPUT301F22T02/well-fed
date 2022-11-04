package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wellfed.R;
import com.example.wellfed.mealplan.MealPlan;

public class ShoppingCartActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_ingredient_edit);
    }

    public void onDelete() {
        Intent intent = new Intent();
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onEdit(MealPlan mealPlan) {
        Intent intent = new Intent();
        intent.putExtra("type", "edit");
        intent.putExtra("mealPlan", mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("type", "launch");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
