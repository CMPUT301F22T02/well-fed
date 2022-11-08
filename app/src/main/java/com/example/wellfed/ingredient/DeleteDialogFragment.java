package com.example.wellfed.ingredient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wellfed.R;

public class DeleteDialogFragment extends DialogFragment {
    /**
     * DeleteIngredient object that is passed in to the dialog.
     */
    private final DeleteIngredient ingredient;

    /**
     * Constructor for the DeleteDialogFragment.
     */
    public interface DeleteIngredient{
        public void delete();
    }

    /**
     * Constructor for the DeleteDialogFragment.
     * @param ingredient DeleteIngredient object that is passed in to the dialog.
     */
    public DeleteDialogFragment(DeleteIngredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * Creates the dialog.
     * @param savedInstanceState Bundle object for the dialog.
     * @return Dialog object for the dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ingredient_delete, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Dialog dialog =  builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialogInterface, i) -> ingredient.delete()).create();
        return dialog;
    }
}
