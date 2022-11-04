/*
 * ShoppingCartIngredientAdapter
 *
 * Version: v1.0.0
 *
 * Date: 2022-10-28
 *
 * Copyright notice:
 * This file is part of well-fed.
 *
 * well-fed is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * well-fed is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with well-fed. If not, see <https://www.gnu.org/licenses/>.
 */

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
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 *
 * @author Weiguo Jiang
 * @version v1.0.0 2022-10-28
 **/
public class ShoppingCartIngredientAdapter extends RecyclerView.Adapter<ShoppingCartIngredientViewHolder> {
    private final ShoppingCartFragment context;
    private final ArrayList<ShoppingCartIngredient> ingredients;

    public ShoppingCartIngredientAdapter(ShoppingCartFragment context,
                                         ArrayList<ShoppingCartIngredient> ingredients) {
        this.ingredients = ingredients;
        this.context = context;
    }

    /*
     * The onCreateViewHolder method returns a new ShoppingCartIngredientViewHolder
     * object using the shopping_cart_ingredient.xml view.
     */
    @NonNull @Override public ShoppingCartIngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_cart_ingredient, parent, false);
        return new ShoppingCartIngredientViewHolder(view);
    }

    /*
     * The onBindViewHolder method binds a ShoppingCartIngredient object
     * and a ShoppingCartIngredientViewHolder, taking the data in the
     * ShoppingCartIngredient object and mapping it into a human
     * readable format to be presented in the RecyclerView.
     */
    @Override public void onBindViewHolder(@NonNull ShoppingCartIngredientViewHolder holder,
                                           int position) {
        // Fetch the current binding object and create a view for it
        ShoppingCartIngredient shoppingCartIngredient = this.ingredients.get(position);
        holder.getName().setText(shoppingCartIngredient.getDescription());
        holder.getSubtext().setText(String.valueOf(shoppingCartIngredient.getAmount()) + " " + shoppingCartIngredient.getUnit());
    }

    @Override public int getItemCount() { return this.ingredients.size(); }
}