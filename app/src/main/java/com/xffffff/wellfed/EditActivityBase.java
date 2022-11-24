package com.xffffff.wellfed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.xffffff.wellfed.common.ConfirmDialog;
import com.xffffff.wellfed.common.ConfirmQuitDialog;

public abstract class EditActivityBase extends ActivityBase
        implements ConfirmDialog.OnConfirmListener {
    private ConfirmQuitDialog confirmQuitDialog;

    @Override public void onBackPressed() {
        if (this.hasUnsavedChanges()) {
            this.confirmQuitDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.confirmQuitDialog = new ConfirmQuitDialog(this, this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (this.hasUnsavedChanges() && item.getItemId() == android.R.id.home) {
            this.confirmQuitDialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    abstract public Boolean hasUnsavedChanges();

    @Override public void onConfirm() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}