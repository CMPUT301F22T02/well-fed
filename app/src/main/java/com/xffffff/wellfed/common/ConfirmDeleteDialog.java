/*
 * DeleteDialog
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

/**
 * The ConfirmDeleteDialog class provides a dialog for confirming whether the
 * user intends to delete an item.
 */
public class ConfirmDeleteDialog extends ConfirmDialog {

    /**
     * Constructs a ConfirmDeleteDialog
     *
     * @param context           the context
     * @param onConfirmListener the OnConfirmListener
     * @param title             the title of the dialog
     */
    public ConfirmDeleteDialog(Context context,
                               OnConfirmListener onConfirmListener,
                               String title) {
        super(context, title, "Are you sure you want to delete this item?",
                "Delete", onConfirmListener);
    }
}
