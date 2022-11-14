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

import java.util.ArrayList;

// This class is used to display the list of ingredients in the shopping cart
public class ShoppingCartIngredientAdapter extends RecyclerView.Adapter<ShoppingCartIngredientAdapter.ViewHolder> {
    private ArrayList<ShoppingCartIngredient> shoppingCartIngredients;

    /**
     * ShoppingCartIngredientLauncher object for the adapter.
     */
//    private ShoppingCartIngredientLauncher shoppingCartIngredientLauncher;

    private ShoppingCartFragment context;

    /**
     * Constructor for the launcher.
     */
//    public interface ShoppingCartIngredientLauncher {
//        public void launch(int pos);
//    }

    /**
     * Constructor for the IngredientAdapter.
     * @param shoppingCartIngredients ArrayList of ShoppingCartIngredient objects for the adapter.
     * @param shoppingCartFragment ShoppingCartFragment object for the adapter.
     */
    public ShoppingCartIngredientAdapter(ArrayList<ShoppingCartIngredient> shoppingCartIngredients,
                                         ShoppingCartFragment shoppingCartFragment) {
        this.shoppingCartIngredients = shoppingCartIngredients;
        this.context = shoppingCartFragment;
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
        ShoppingCartIngredient ingredient = shoppingCartIngredients.get(position);

        // Allow user to change pickup status when checking or unchecking checkbox
        CheckBox checkBox = holder.checkBox;

        // set checkbox to checked if ingredient already picked up
        checkBox.setChecked(ingredient.isPickedUp);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    ingredient.setPickedUp(true);
                    Toast.makeText(context.getContext(), "Picked up status set to " + String.valueOf(ingredient.isPickedUp), Toast.LENGTH_LONG).show();
                } else {
                    ingredient.setPickedUp(false);
                    Toast.makeText(context.getContext(), "Picked up status set to " + String.valueOf(ingredient.isPickedUp), Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.description.setText(ingredient.getDescription());
        holder.subtext.setText(String.valueOf(ingredient.getAmount()) + " " +
                ingredient.getUnit() + " | " + ingredient.getCategory());

        holder.itemView.setOnClickListener(view -> context.launch(ingredient));
    }

    /**
     * getItemCount method for the ShoppingCartIngredientAdapter.
     * @return int for the adapter.
     */
    @Override
    public int getItemCount() {return shoppingCartIngredients.size();}
}