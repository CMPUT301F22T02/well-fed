package com.example.wellfed.recipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.wellfed.R;

import javax.annotation.Nullable;

public class DeleteDialogFragment extends DialogFragment {
    private DeleteRecipe recipe;

    public interface DeleteRecipe{
        public void delete();
    }

    public DeleteDialogFragment(DeleteRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_recipe_delete, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Dialog dialog =  builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recipe.delete();
                    }
                }).create();

        return dialog;

    }

    public interface DeleteIngredient {
        public void delete();
    }
}
