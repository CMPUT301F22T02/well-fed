package com.example.wellfed.shoppingcart;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

public class ShoppingCartIngredientViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView subtext;

    public TextView getName() { return name; }
    public TextView getSubtext() { return subtext; }

    public ShoppingCartIngredientViewHolder(@NonNull View itemView) {
        super(itemView);

        this.name = itemView.findViewById(R.id.shopping_cart_ingredient_name);
        this.subtext = itemView.findViewById(R.id.shopping_cart_ingredient_subtext);
    }
}
