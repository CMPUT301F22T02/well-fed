package com.example.wellfed.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;

import java.util.ArrayList;

/**
 * RecipeBookSelectFragment is a version of RecipeBookFragment that allows
 * the user to select a recipe
 *
 * @version 1.0.0
 */
public class RecipeBookSelectFragment extends RecipeBookFragment {
    /**
     * Stores the recipe selected listener
     */
    private OnSelectedListener listener;

    /**
     * Interface for recipe selected listener
     */
    public interface OnSelectedListener {
        void onSelected(Recipe recipe);
    }

    /**
     * Sets the recipe selected listener
     *
     * @param listener recipe selected listener
     */
    public void setListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Calls the recipe selected listener
     */
    @Override
    public void launch(Recipe recipe) {
        if (listener != null) {
            listener.onSelected(recipe);
        }
    }
}
