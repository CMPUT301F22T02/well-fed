package com.example.wellfed;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class ActivityBase extends AppCompatActivity {

    // helps to go back to the previous active fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void deleteIngredient();

    public abstract void delete();
}