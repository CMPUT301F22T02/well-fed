package com.example.wellfed.common;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.wellfed.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmQuitDialog {
    private final AlertDialog dialog;

    public ConfirmQuitDialog(Context context, OnQuitListener onQuitListener) {
        this.dialog = new MaterialAlertDialogBuilder(context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setIcon(R.drawable.ic_baseline_cancel_24)
                .setTitle("Quit editing?")
                .setMessage("Changes that you have made are not saved")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Quit",
                        (dialog, which) -> onQuitListener.onQuit())
                .create();
    }

    public void show() {
        this.dialog.show();
    }
}
