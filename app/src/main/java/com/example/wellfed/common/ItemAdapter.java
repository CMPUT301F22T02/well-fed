package com.example.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;

import java.util.List;

/**
 * Adapter that manages the view and data for the ingredients
 * in the {@link Recipe}
 *
 * @version 1.0.0
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

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
    ItemAdapter(List<Ingredient> recipeIngredientList, int layoutId, OnIngredientClick listener) {
        this.recipeIngredientList = recipeIngredientList;
        this.layoutId = layoutId;
        this.listener = listener;
    }

    ItemAdapter(List<Ingredient> recipeIngredientList, int layoutId) {
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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View recipeView = layoutInflater.inflate(layoutId, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(recipeView, layoutId);
        return viewHolder;
    }


    /**
     * Binds the data in our list to the views
     *
     * @param holder   holds the inflated view
     * @param position gives the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

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
     * @return count of the number of ingredients in our list
     */
    @Override
    public int getItemCount() {
        return this.recipeIngredientList.size();
    }


    /**
     * Viewholder for the items
     * @version 1.0.0
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
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
        public ItemViewHolder(@NonNull View itemView, int layoutId) {
            super(itemView);
            if (layoutId == R.layout.recipe_ingredient_edit){
                editImgView = (ImageView) itemView.findViewById(R.id.ingredient_edit_imgView);
                deleteImgView = (ImageView) itemView.findViewById(R.id.ingredient_delete_imgView);
            }
            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity_textView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name_textView);
        }

    }
}