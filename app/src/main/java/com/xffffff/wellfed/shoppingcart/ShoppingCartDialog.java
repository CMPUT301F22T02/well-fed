//package com.xffffff.wellfed.shoppingcart;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.xffffff.wellfed.R;
//import com.google.android.material.textfield.TextInputEditText;
//
//public class ShoppingCartDialog extends DialogFragment {
//    private TextInputEditText bestBeforeDate;
//    private TextInputEditText actualAmount;
//    private TextInputEditText unit;
//    private TextInputEditText location;
//    private OnFragmentInteractionListener listener;
//
//    public interface OnFragmentInteractionListener {
//        void onCompletePressed(ShoppingCartIngredient shoppingCartIngredient);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof OnFragmentInteractionListener) {
//            listener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() +
//                    "must implement onFragmentInteractionListener");
//        }
//    }
//
//    static ShoppingCartDialog newInstance(ShoppingCartIngredient
//    shoppingCartIngredient) {
//        Bundle args = new Bundle();
//        args.putSerializable("ingredient", shoppingCartIngredient);
//
//        ShoppingCartDialog fragment = new ShoppingCartDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout
//        .dialog_shopping_cart, null);
//        bestBeforeDate = view.findViewById(R.id.date);
//        actualAmount = view.findViewById(R.id.actualAmount);
//        unit = view.findViewById(R.id.unit);
//        location = view.findViewById(R.id.location);
//
//        Bundle args = getArguments();
//        if (args != null) {
//            ShoppingCartIngredient shoppingCartIngredient =
//            (ShoppingCartIngredient) args.getSerializable("ingredient");
//
//            // TODO: include description & other fields and set values here
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        return builder
//                .setView(view)
//                .setTitle("Complete Details")
//                .setNegativeButton("Cancel", null)
//                // define behavior when Complete is selected
//                .setPositiveButton("Complete", new DialogInterface
//                .OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        // TODO: define behavior after clicking Complete
//                        listener.onCompletePressed(new
//                        ShoppingCartIngredient(""));
//                    }
//                }).create();
//    }
//}

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
package com.xffffff.wellfed.shoppingcart;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.R.style;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.xffffff.wellfed.R;

/**
 * The ConfirmDialog class provides a dialog for confirming user intentions.
 */
public class ShoppingCartDialog {

    /**
     * Constructs a ConfirmQuitDialog
     *
     * @param context           the context
     * @param title             the title of the dialog
     * @param message           the message of the dialog
     * @param confirmButtonText the text of the confirm button
     * @param onConfirmListener the OnConfirmListener
     */
    public ShoppingCartDialog(Context context, String title, String message,
                              String confirmButtonText,
                              ShoppingCartDialog.OnConfirmListener onConfirmListener) {
        AlertDialog dialog1 = new MaterialAlertDialogBuilder(context,
                style.ThemeOverlay_Material3_MaterialAlertDialog_Centered).setIcon(
                        R.drawable.ic_baseline_cancel_24).setTitle(title)
                .setMessage(message).setNeutralButton("Cancel", null)
                .setPositiveButton(confirmButtonText,
                        (dialog, which) -> onConfirmListener.onConfirm())
                .setView(R.layout.dialog_shopping_cart).create();
    }

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
}

