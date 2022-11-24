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

package com.xffffff.wellfed.common;

import android.content.Context;

/**
 * The ConfirmQuitDialog class provides a dialog for confirming whether the
 * user intends to quit the an activity where they have unsaved changes.
 */
public class ConfirmQuitDialog extends ConfirmDialog {
    /**
     * Constructs a ConfirmQuitDialog
     *
     * @param context           the context
     * @param onConfirmListener the OnConfirmListener
     */
    public ConfirmQuitDialog(Context context,
                             OnConfirmListener onConfirmListener) {
        super(context, "Quit editing?",
                "Changes that you have made are not " + "saved?", "Quit",
                onConfirmListener);
    }
}
