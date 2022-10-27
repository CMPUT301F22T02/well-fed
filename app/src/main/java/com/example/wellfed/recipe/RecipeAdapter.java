package com.example.wellfed.recipe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

import java.io.Serializable;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private FragmentActivity parent;
    private RecipeLauncher recipeLauncher;


    public interface RecipeLauncher{
        public void launch(int pos);
    }


    public RecipeAdapter(FragmentActivity parent, List<Recipe> recipes, RecipeBookFragment recipeBookFragment) {
        this.parent = parent;
        this.recipes = recipes;
        this.recipeLauncher = (RecipeLauncher) recipeBookFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title_textView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View recipeView = layoutInflater.inflate(R.layout.activity_recipe_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        TextView recipeTitle = holder.recipeTitleTextView;
        recipeTitle.setText(recipe.getTitle());
        recipeTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeLauncher.launch(holder.getAdapterPosition());
                }
            });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


}
