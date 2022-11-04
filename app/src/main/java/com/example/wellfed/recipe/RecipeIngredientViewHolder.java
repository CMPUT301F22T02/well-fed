package com.example.wellfed.recipe;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {
    public TextView ingredientQuantity;
    public TextView ingredientName;
    ImageView editImgView;
    ImageView deleteImgView;

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
