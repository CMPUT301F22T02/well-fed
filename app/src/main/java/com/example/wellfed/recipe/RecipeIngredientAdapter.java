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

/**
 * Adapter that manages the view and data for the ingredients
 * in the {@link Recipe}
 *
 * @version 1.0.0
 */
public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {

    /**
     * list of {@link Ingredient}
     */
    List<Ingredient> recipeIngredientList;

    /**
     * layout id that specifies which layout to use
     */
    int layoutId;

    OnIngredientClick listener;

    public interface OnIngredientClick {
        void onEditClick(String reason, int pos);
    }


    /**
     * @param recipeIngredientList helps initialize our list
     * @param layoutId             id of the layout
     */
    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList, int layoutId, OnIngredientClick listener) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
        this.listener = listener;
    }

    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList, int layoutId) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
    }


    /**
     * inflates the view
     *
     * @param parent   activity that handles the ingredients
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recipeView = layoutInflater.inflate(layoutId, parent, false);
        RecipeIngredientViewHolder viewHolder = new RecipeIngredientViewHolder(recipeView, layoutId);
        return viewHolder;
    }


    /**
     * Binds the data in our list to the views
     *
     * @param holder   holds the inflated view
     * @param position gives the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {

        /**
         * Ingredient at the position
         */
        Ingredient recipeIngredient = recipeIngredientList.get(position);

        /**
         * view that holds the quantity of the ingredients
         */
        TextView ingredientQuantity = holder.ingredientQuantity;

        /**
         * view that holds the ingredient name
         */
        TextView ingredientName = holder.ingredientName;

        // set buttons based on layout
        if (this.layoutId == R.layout.recipe_ingredient_edit) {
            // ingredient edit btn
            ImageView editImgView = holder.editImgView;
            if (listener != null) {

                editImgView.setOnClickListener(view -> {
                    listener.onEditClick("edit", holder.getAdapterPosition());
                });

                // ingredient delete btn
                ImageView deleteImgView = holder.deleteImgView;
                deleteImgView.setOnClickListener(view -> {
                    listener.onEditClick("delete", holder.getAdapterPosition());
                });
            }
        }

        ingredientName.setText(recipeIngredient.getDescription());
        ingredientQuantity.setText(
                recipeIngredient.getAmount() + " " + recipeIngredient.getUnit());
    }


    /**
     * @return count of the number of ingredients in our list
     */
    @Override
    public int getItemCount() {
        return this.recipeIngredientList.size();
    }


}