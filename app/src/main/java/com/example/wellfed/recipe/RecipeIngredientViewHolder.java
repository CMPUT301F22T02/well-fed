package com.example.wellfed.recipe;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

/**
 * Viewholder for the ingredients in the recipe
 * @version 1.0.0
 */
public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
    /**
     * view that holds quantity of the ingredient
     */
    public TextView ingredientQuantity;

    /**
     * view that holds the name of the ingredient
     */
    public TextView ingredientName;

    /**
     * image that allows to edit the ingredient
     */
    ImageView editImgView;

    /**
     * image that allows to delete ingredient
     */
    ImageView deleteImgView;

    /**
     * constructor for creating the view
     * @param itemView
     * @param layoutId
     */
    public RecipeIngredientViewHolder(@NonNull View itemView, int layoutId) {
        super(itemView);
        if (layoutId == R.layout.recipe_ingredient_edit){
            editImgView = (ImageView) itemView.findViewById(R.id.ingredient_edit_imgView);
            deleteImgView = (ImageView) itemView.findViewById(R.id.ingredient_delete_imgView);
        }
        ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity_textView);
        ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name_textView);
    }

}
