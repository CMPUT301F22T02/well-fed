/*
 * ConfirmDialog
 *
 * Version: v1.0.0
 *
 * Date: 2022-11-03
 *
 * Copyright notice:
 * This file is part of well-fed.
 *
 * well-fed is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * well-fed is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with well-fed. If not, see <https://www.gnu.org/licenses/>.
 */
package com.example.wellfed.common;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.wellfed.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.R.style;

/**
 * The ConfirmDialog class provides a dialog for confirming user intentions.
 */
public class ConfirmDialog {
    /**
     * Holds the AlertDialog dialog
     */
    private final AlertDialog dialog;

    /**
     * The OnQuitListener interface defines the onConfirm method that is
     * called when the user confirms.
     */
    public interface OnConfirmListener {
        void onConfirm();
    }

    /**
     * Constructs a ConfirmQuitDialog
     *
     * @param context           the context
     * @param onConfirmListener the OnConfirmListener
     */
    public ConfirmDialog(Context context, String title, String message,
                         String positiveButtonText,
                         ConfirmDialog.OnConfirmListener onConfirmListener) {
        this.dialog = new MaterialAlertDialogBuilder(context,
                style.ThemeOverlay_Material3_MaterialAlertDialog_Centered).setIcon(
                        R.drawable.ic_baseline_cancel_24).setTitle(title)
                .setMessage(message).setNeutralButton("Cancel", null)
                .setPositiveButton(positiveButtonText,
                        (dialog, which) -> onConfirmListener.onConfirm())
                .create();
    }

    /**
     * Shows the dialog
     */
    public void show() {
        this.dialog.show();
    }
}
