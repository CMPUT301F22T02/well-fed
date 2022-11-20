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
     * List of {@link Ingredient}
     */
    List<Ingredient> recipeIngredientList;

    /**
     * Layout id that specifies which layout to use
     */
    int layoutId;

    /**
     * Listener for when an ingredient is clicked.
     */
    OnIngredientClick listener;

    /**
     * Interface for listeners that listen for clicks on an ingredient
     */
    public interface OnIngredientClick {
        void onEditClick(String reason, int pos);
    }

    /**
     * Creates a new RecipeIngredientAdapter
     *
     * @param recipeIngredientList the list of Recipes to adapt into a displayable form
     *                             for the RecyclerView
     * @param layoutId             id identifying the layout of a single ingredient in the list
     * @param listener             the listener to be notified when an ingredient
     *                             is clicked
     */
    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList, int layoutId, OnIngredientClick listener) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
        this.listener = listener;
    }

    /**
     * Creates a new RecipeIngredientAdapter
     *
     * @param recipeIngredientList the list of Recipes to adapt into a displayable form
     *                             for the RecyclerView
     * @param layoutId             id identifying the layout of a single ingredient in the list
     */
    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList, int layoutId) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
    }


    /**
     * Inflates the view, by creating a ViewHolder for views inside of the RecyclerView
     *
     * @param parent   activity that handles the ingredients
     * @param viewType the view type for the new View.
     * @return         the created ViewHolder
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
     * Binds the data in our list to the views associated with it.
     *
     * @param holder   holds the inflated view to bind to
     * @param position gives the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {

        Ingredient recipeIngredient = recipeIngredientList.get(position);
        TextView ingredientQuantity = holder.ingredientQuantity;
        TextView ingredientName = holder.ingredientName;

        // set buttons based on layout
        if (this.layoutId == R.layout.recipe_ingredient_edit) {
            // ingredient edit btn
            ImageView editImgView = holder.editImgView;
            ImageView deleteImgView = holder.deleteImgView;
            if (listener != null) {

                editImgView.setOnClickListener(view -> {
                    listener.onEditClick("edit", holder.getAdapterPosition());
                });

                // ingredient delete btn

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
     * Gets the number of items in our recipe's ingredient list.
     *
     * @return count of the number of ingredients in our list
     */
    @Override
    public int getItemCount() {
        return this.recipeIngredientList.size();
    }


}