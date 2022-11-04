package com.example.wellfed.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.Ingredient;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {

    List<Ingredient> recipeIngredientList;
    int layoutId;

    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList, int layoutId) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
    }


    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recipeView = layoutInflater.inflate(layoutId, parent, false);
        RecipeIngredientViewHolder viewHolder = new RecipeIngredientViewHolder(recipeView, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        Ingredient recipeIngredient = recipeIngredientList.get(position);
        TextView ingredientQuantity = holder.ingredientQuantity;
        TextView ingredientName = holder.ingredientName;

        // set buttons based on layout
        if (this.layoutId == R.layout.recipe_ingredient_edit) {
            // ingredient edit btn
            ImageView editImgView = holder.editImgView;
            editImgView.setOnClickListener(view -> {

            });

            // ingredient delete btn
            ImageView deleteImgView = holder.deleteImgView;
            deleteImgView.setOnClickListener(view -> {

            });
        }

        ingredientName.setText(recipeIngredient.getDescription());
        ingredientQuantity.setText(Float.toString(recipeIngredient.getAmount()) + " " + recipeIngredient.getUnit());
    }

    @Override
    public int getItemCount() {
        return this.recipeIngredientList.size();
    }


}