package com.example.wellfed.ingredient;

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

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    /**
     * The list of ingredients to display
     */
    private final ArrayList<StorageIngredient> ingredients;
    /**
     * The fragment that this adapter is being used in
     */
    private final IngredientLauncher ingredientLauncher;

    /**
     * The interface that is used to launch the ingredient fragment
     */
    public interface IngredientLauncher {
        public void launch(int pos);
    }

    /**
     * The constructor for the adapter
     *
     * @param parent           The parent activity
     * @param ingredients      The list of ingredients to display
     * @param ingredientLauncher The fragment that this adapter is being used in
     */
    public IngredientAdapter(FragmentActivity parent,
                             ArrayList<StorageIngredient> ingredients,
                             IngredientStorageFragment ingredientLauncher) {
        /**
         * The parent activity
         */
        this.ingredients = ingredients;
        this.ingredientLauncher = (IngredientLauncher) ingredientLauncher;
    }

    /**
     * The view holder for the adapter
     */
    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientNameTextView = (TextView) itemView.findViewById(R.id.ingredient_name_textView);

        }
    }

    /**
     * Creates the view holder
     *
     * @param parent   The parent view
     * @param viewType The type of view
     * @return The view holder
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View ingredientView = layoutInflater.inflate(R.layout.fragment_ingredient_storage, parent, false);

        return new IngredientViewHolder(ingredientView);
    }

    /**
     * Binds the view holder to the data
     *
     * @param holder   The view holder
     * @param position The position of the data
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        StorageIngredient ingredient = ingredients.get(position);

        TextView textView = holder.ingredientNameTextView;
        textView.setText(ingredient.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientLauncher.launch(position);
            }
        });
    }

    /**
     * Gets the number of items in the list
     *
     * @return The number of items in the list
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
