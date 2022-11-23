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
package com.xffffff.wellfed.common;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.xffffff.wellfed.R;
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
     * The OnQuitListener interface defines the onConfirm handler that is
     * called when the user confirms.
     */
    public interface OnConfirmListener {
        /**
         * Handler for when the user confirms.
         */
        void onConfirm();
    }

    /**
     * Constructs a ConfirmQuitDialog
     *
     * @param context           the context
     * @param title             the title of the dialog
     * @param message           the message of the dialog
     * @param confirmButtonText the text of the confirm button
     * @param onConfirmListener the OnConfirmListener
     */
    public ConfirmDialog(Context context, String title, String message,
                         String confirmButtonText,
                         ConfirmDialog.OnConfirmListener onConfirmListener) {
        this.dialog = new MaterialAlertDialogBuilder(context,
                style.ThemeOverlay_Material3_MaterialAlertDialog_Centered).setIcon(
                        R.drawable.ic_baseline_cancel_24).setTitle(title)
                .setMessage(message).setNeutralButton("Cancel", null)
                .setPositiveButton(confirmButtonText,
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
