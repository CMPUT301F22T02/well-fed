package com.example.wellfed.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wellfed.R;

public class RecipeBookFragment extends Fragment {
    Button startRecipeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
                startRecipeBtn = (Button) getView().findViewById(R.id.recipe_start_btn);
        startRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("name", "manpreet");
                startActivity(intent);
            }
        });
    }
}