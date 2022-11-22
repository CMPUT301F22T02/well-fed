package com.example.wellfed.shoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wellfed.R;
import com.example.wellfed.common.DBAdapter;
import com.example.wellfed.ingredient.Ingredient;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

// This class is used to display the list of ingredients in the shopping cart
public class ShoppingCartIngredientAdapter extends DBAdapter<ShoppingCartIngredientAdapter.ViewHolder> {
    /**
     * DB for the shopping cart
     */
    private final ShoppingCartDB db;
    /**
     * Listener for an item click in the RecyclerView
     */
    private OnItemClickListener listener;

    /**
     * Constructor for the adapter
     */
    public ShoppingCartIngredientAdapter(ShoppingCartDB db) {
        super(db.getQuery());
        this.db = db;

    }

    public interface OnItemClickListener {
        void onItemClick(ShoppingCartIngredient shoppingCartIngredient);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * ViewHolder class for the ShoppingCartIngredientAdapter.
     * It contains the TextViews for the description, amount,
     * unit and category of the ingredient.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView subtext;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.shopping_cart_ingredient_description);
            subtext = itemView.findViewById(R.id.shopping_cart_ingredient_subtext);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    /**
     * onCreateViewHolder method for the ShoppingCartIngredientAdapter.
     * @param parent ViewGroup for the adapter.
     * @param viewType int for the adapter.
     * @return ViewHolder object for the adapter.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View shoppingCartIngredientView = inflater.inflate(R.layout.shopping_cart_ingredient, parent,
            false);

        return new ViewHolder(shoppingCartIngredientView);
    }

    /**
     * onBindViewHolder method for the ShoppingCartIngredientAdapter.
     * @param holder ViewHolder object for the adapter.
     * @param position int for the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.getShoppingCart((mealCart, success) -> {
            if (success) {
                ShoppingCartIngredient ingredient = mealCart.getIngredient(position);
                holder.description.setText(ingredient.getDescription());
                String subtext = ingredient.getAmount() + " " + ingredient.getUnit() + " " +
                    ingredient.getCategory();
                holder.subtext.setText(subtext);
                holder.checkBox.setChecked(ingredient.isPickedUp());
            }
        });
    }

    /**
     * getItemCount method for the ShoppingCartIngredientAdapter.
     * @return int for the adapter.
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}