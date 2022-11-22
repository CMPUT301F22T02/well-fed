/*
 * DeleteButton
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
import android.view.View;
import android.widget.Button;

/**
 * The DeleteButton class attaches an OnClickListener to a Button that
 * opens a deletion confirmation dialog.
 */
public class DeleteButton implements View.OnClickListener {
    /**
     * Holds the DeleteDialog
     */
    private final ConfirmDeleteDialog confirmDeleteDialog;

    /**
     * Constructs a DeleteButton
     *
     * @param context           the context
     * @param view              the view
     * @param title             the title
     * @param onConfirmListener the OnConfirmListener
     */
    public DeleteButton(Context context, View view, String title,
                        ConfirmDialog.OnConfirmListener onConfirmListener) {
        view.setOnClickListener(this);
        this.confirmDeleteDialog =
                new ConfirmDeleteDialog(context, onConfirmListener, title);
    }

    /**
     * Shows the DeleteDialog when the button is clicked
     *
     * @param view the view
     */
    @Override public void onClick(View view) {
        this.confirmDeleteDialog.show();
    }
}
