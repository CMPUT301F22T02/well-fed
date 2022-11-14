package com.example.wellfed.shoppingcart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wellfed.R;
import com.google.android.material.textfield.TextInputEditText;

public class ShoppingCartDialog extends DialogFragment {
    private TextInputEditText bestBeforeDate;
    private TextInputEditText actualAmount;
    private TextInputEditText unit;
    private TextInputEditText location;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onCompletePressed(ShoppingCartIngredient shoppingCartIngredient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement onFragmentInteractionListener");
        }
    }

    static ShoppingCartDialog newInstance(ShoppingCartIngredient shoppingCartIngredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", shoppingCartIngredient);

        ShoppingCartDialog fragment = new ShoppingCartDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_shopping_cart, null);
        bestBeforeDate = view.findViewById(R.id.date);
        actualAmount = view.findViewById(R.id.actualAmount);
        unit = view.findViewById(R.id.unit);
        location = view.findViewById(R.id.location);

        Bundle args = getArguments();
        if (args != null) {
            ShoppingCartIngredient shoppingCartIngredient = (ShoppingCartIngredient) args.getSerializable("ingredient");

            // TODO: include description & other fields and set values here
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Complete Details")
                .setNegativeButton("Cancel", null)
                // define behavior when Complete is selected
                .setPositiveButton("Complete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // TODO: define behavior after clicking Complete
                        listener.onCompletePressed(new ShoppingCartIngredient(""));
                    }
                }).create();
    }
}
