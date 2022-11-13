package com.example.wellfed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.snackbar.Snackbar;

public abstract class ActivityBase extends AppCompatActivity {

    // helps to go back to the previous active fragment
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeSnackbar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text,
                Snackbar.LENGTH_SHORT).show();
    }
}