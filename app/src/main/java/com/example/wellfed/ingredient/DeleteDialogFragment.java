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
    private DeleteIngredient ingredient;

    public interface DeleteIngredient{
        public void delete();
    }

    public DeleteDialogFragment(DeleteIngredient ingredient) {
        this.ingredient = ingredient;
    }

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
