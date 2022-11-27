package com.xffffff.wellfed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.xffffff.wellfed.common.ConfirmDialog;
import com.xffffff.wellfed.common.ConfirmQuitDialog;

/**
 * EditActivityBase is the base class for all activities that edit data in the
 * application
 */
public abstract class EditActivityBase extends ActivityBase
    implements ConfirmDialog.OnConfirmListener {
    private ConfirmQuitDialog confirmQuitDialog;

    /**
     * onBackPress is called when the back button is pressed
     * <p>
     * It shows a dialog to confirm that the user wants to quit editing
     * the data
     * </p>
     */
    @Override
    public void onBackPressed() {
        if (this.hasUnsavedChanges()) {
            this.confirmQuitDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * onCreated is called when the activity is created
     * <p>
     * It sets the content view and initializes the confirm quit dialog
     * and the confirm dialog
     * </p>
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.confirmQuitDialog = new ConfirmQuitDialog(this, this);
    }

    /**
     * onOptionsItemSelected is called when an item in the options menu is
     * selected by the user
     *
     * @param item the item that was selected
     * @return true if the item was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.hasUnsavedChanges() && item.getItemId() == android.R.id.home) {
            this.confirmQuitDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * hasUnsavedChanges is called to check if the activity has unsaved changes
     *
     * @return true if the activity has unsaved changes, false otherwise
     */
    abstract public Boolean hasUnsavedChanges();

    /**
     * onConfirm is called when the user confirms the dialog
     * <p>
     * It returns the intent to the calling activity
     * </p>
     */
    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}