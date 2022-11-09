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

// This class is used to display the list of ingredients in the shopping cart
public class ShoppingCartIngredientAdapter extends RecyclerView.Adapter<ShoppingCartIngredientAdapter.ViewHolder> {
    ArrayList<ShoppingCartIngredient> shoppingCartIngredients;

    /**
     * ShoppingCartIngredientLauncher object for the adapter.
     */
    ShoppingCartIngredientLauncher shoppingCartIngredientLauncher;

    /**
     * Constructor for the launcher.
     */
    public interface ShoppingCartIngredientLauncher {
        public void launch(int pos);
    }

    public ShoppingCartIngredientAdapter(ArrayList<ShoppingCartIngredient> shoppingCartIngredients,
                                         ShoppingCartFragment shoppingCartFragment) {
        this.shoppingCartIngredients = shoppingCartIngredients;
        this.shoppingCartIngredientLauncher = shoppingCartFragment;
    }

    /**
     * ViewHolder class for the ShoppingCartIngredientAdapter.
     * It contains the TextViews for the name and the attribute of the ingredient.
     */
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