package com.example.wellfed.ingredient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.util.ArrayList;
import java.util.List;

// This class is used to display the list of ingredients in the ingredient storage
// It displays the name of the ingredient and one of its attributes depending
// on user's choice
// The choices are: expiration date, quantity, and location

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private ArrayList<StorageIngredient> ingredients;

    public IngredientAdapter(ArrayList<StorageIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView;
        public TextView ingredientAttributeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientNameTextView = (TextView) itemView.findViewById(R.id.ingredient_name);
            ingredientAttributeTextView = (TextView) itemView.findViewById(R.id.ingredient_subtext);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View ingredientView = layoutInflater.inflate(R.layout.ingredient_element_subtext, parent,
                false);

        return new ViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageIngredient ingredient = ingredients.get(position);

        TextView ingredientNameTextView = holder.ingredientNameTextView;
        ingredientNameTextView.setText(ingredient.getDescription());

        TextView ingredientAttributeTextView = holder.ingredientAttributeTextView;
        String quantity = String.valueOf(ingredient.getAmount());
        String unit = ingredient.getUnit();
        String quantityAndUnit = quantity + " " + unit;
        ingredientAttributeTextView.setText(quantityAndUnit);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
