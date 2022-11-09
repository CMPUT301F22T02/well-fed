package com.example.wellfed.shoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wellfed.R;

import java.util.ArrayList;

/**
 * The ShoppingCartIngredientAdapter class binds ArrayList<ShoppingCartIngredient> to RecyclerView.
 * <p>
 * @author Weiguo Jiang
 * @version v1.0.0 2022-10-28
 **/
public class ShoppingCartIngredientAdapter extends RecyclerView.Adapter<ShoppingCartIngredientAdapter.ViewHolder> {
    ArrayList<ShoppingCartIngredient> ingredients;

    public ShoppingCartIngredientAdapter(ArrayList<ShoppingCartIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientDescription;
        public TextView ingredientAmount;
        public TextView ingredientUnit;
        public TextView ingredientCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientDescription = itemView.findViewById(R.id.shopping_cart_ingredient_description);
            ingredientAmount = itemView.findViewById(R.id.shopping_cart_ingredient_amount);
            ingredientUnit = itemView.findViewById(R.id.shopping_cart_ingredient_unit);
            ingredientCategory = itemView.findViewById(R.id.shopping_cart_ingredient_category);
        }
    }

    @NonNull
    @Override
    public ShoppingCartIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View ingredientView = layoutInflater.inflate(R.layout.shopping_cart_ingredient, parent,
                false);

        return new ViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartIngredient ingredient = ingredients.get(position);

        holder.ingredientDescription.setText(ingredient.getDescription());
        holder.ingredientAmount.setText(String.valueOf(ingredient.getAmount()));
        holder.ingredientUnit.setText(ingredient.getUnit());
        holder.ingredientCategory.setText(ingredient.getCategory());
    }

    @Override
    public int getItemCount() {return ingredients.size();}
}