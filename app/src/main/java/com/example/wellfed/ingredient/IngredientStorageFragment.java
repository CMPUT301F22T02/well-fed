package com.example.wellfed.ingredient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.ArrayList;

public class IngredientStorageFragment extends Fragment {

    Button addIngredientBtn;
    private ArrayList<StorageIngredient> ingredients;
    private IngredientListController ingredientController;
    private IngredientAdapter adapter;
    int position;

    ActivityResultLauncher<StorageIngredient> ingredientLauncher = registerForActivityResult(new IngredientContract(),
            result -> {
                if (result == null) {
                    ingredientController.deleteIngredient(position);
                    return;
                }
                ingredientController.updateIngredient(position, result);
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_ingredient_storage, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();



    }
}