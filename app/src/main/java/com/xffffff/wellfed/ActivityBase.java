package com.xffffff.wellfed;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * ActivityBase is the base class for all activities in the application
 * <p>
 * It contains the common functionality for all activities
 * such as the snackbar and back button
 * </p>
 */
public abstract class ActivityBase extends AppCompatActivity {
    /**
     * onOptionsItemSelect is called if a user wants to go back to the previous
     * fragment
     *
     * @param item the item that was selected
     * @return true if the item was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * makeSnackbar makes a snackbar with the given message
     * @param text the text to display in the snackbar
     */
    public void makeSnackbar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text,
            Snackbar.LENGTH_LONG).show();
    }
}
