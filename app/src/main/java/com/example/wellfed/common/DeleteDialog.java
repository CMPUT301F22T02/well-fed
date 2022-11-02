package com.example.wellfed.common;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.wellfed.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeleteDialog {
    private final AlertDialog dialog;

    public DeleteDialog(Context context, OnDeleteListener onDeleteListener,
                        String title) {
        this.dialog = new MaterialAlertDialogBuilder(context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setTitle(title)
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Delete",
                        (dialog, which) -> onDeleteListener.onDelete())
                .create();
    }

    public void show() {
        this.dialog.show();
    }
}
