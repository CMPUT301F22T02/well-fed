/*
 * ConfirmQuitDialog
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
