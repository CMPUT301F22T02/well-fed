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

// This class is used to display the list of ingredients in the ingredient storage
// It displays the name of the ingredient and one of its attributes depending
// on user's choice
// The choices are: expiration date, quantity, and location

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private FragmentActivity parent;
    private final ArrayList<StorageIngredient> ingredients;
    private final IngredientLauncher ingredientLauncher;

    public interface IngredientLauncher {
        public void launch(int pos);
    }

    public IngredientAdapter(FragmentActivity parent, ArrayList<StorageIngredient> ingredients,
                             IngredientStorageFragment ingredientStorageFragment) {
        this.parent = parent;
        this.ingredients = ingredients;
        this.ingredientLauncher = (IngredientLauncher) ingredientStorageFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView;
        public TextView ingredientAttributeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name_textView);
            ingredientAttributeTextView =
                    itemView.findViewById(R.id.ingredient_subtext);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View ingredientView = inflater.inflate(R.layout.ingredient_element_subtext, parent, false);

        ViewHolder viewHolder = new ViewHolder(ingredientView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageIngredient ingredient = ingredients.get(position);

        TextView ingredientNameTextView = holder.ingredientNameTextView;
        ingredientNameTextView.setText(ingredient.getDescription());

        TextView ingredientAttributeTextView = holder.ingredientAttributeTextView;
        ingredientAttributeTextView.setText(ingredient.getBestBefore());

        holder.itemView.setOnClickListener(v -> ingredientLauncher.launch(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
