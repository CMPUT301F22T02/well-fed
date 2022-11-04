package com.example.wellfed.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.Ingredient;

import java.util.List;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    List<Ingredient> recipeIngredientList;

    RecipeIngredientAdapter(List<Ingredient> recipeIngredientList){
        this.recipeIngredientList = recipeIngredientList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientQuantity;
        public TextView ingredientName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity_textView);
            ingredientName = (TextView) itemView.findViewById(R.id.ingredient_name_textView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View recipeView = layoutInflater.inflate(R.layout.recipe_ingredient, parent, false);

        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Ingredient recipeIngredient = recipeIngredientList.get(position);
            TextView ingredientQuantity = holder.ingredientQuantity;
            TextView ingredientName = holder.ingredientName;

            ingredientName.setText(recipeIngredient.getDescription());
            ingredientQuantity.setText(Float.toString(recipeIngredient.getAmount()) + " "  + recipeIngredient.getUnit());
    }

    @Override
    public int getItemCount() {
        return this.recipeIngredientList.size();
    }


}
