package com.xffffff.wellfed.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.DBAdapter;

import java.util.Objects;


/**
 * Adapter for the recipes in the {@link RecipeBookFragment}
 * Binds the view for each recipe to the data
 *
 * @version 1.0.0
 */
public class RecipeAdapter extends DBAdapter<RecipeAdapter.ViewHolder> {

    /**
     * Launcher to launch the new recipe activity
     */
    private RecipeLauncher recipeLauncher;

    /**
     * Creates a new RecipeAdapter given the RecipeDB.
     *
     * @param db the Recipe database that the adapter is connected to
     */
    public RecipeAdapter(RecipeDB db) {
        super(db.getQuery());
    }

    /**
     * Sets the RecipeLauncher for the recipe adapter
     *
     * @param recipeLauncher the recipe launcher
     */
    public void setRecipeLauncher(RecipeLauncher recipeLauncher) {
        this.recipeLauncher = recipeLauncher;
    }

    /**
     * Inflate the layout of a single View into the parent ViewGroup
     *
     * @param parent   the parent ViewGroup to inflate the layout in
     * @param viewType an identifier for the view type
     * @return the new ViewHolder for a Recipe created
     */
    @NonNull @Override public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View recipeView =
                layoutInflater.inflate(R.layout.activity_recipe_list, parent,
                        false);

        return new ViewHolder(recipeView);
    }

    /**
     * Bind the data with the views in the RecyclerView
     *
     * @param holder   the ViewHolder which holds Views to be bound to the
     *                 recipe
     * @param position the position of the View in the ViewHolder to bind the
     *                data to
     */
    @Override public void onBindViewHolder(@NonNull ViewHolder holder,
                                           int position) {
        DocumentSnapshot recipeSnapshot = getSnapshot(position);
        Recipe recipe = new Recipe(recipeSnapshot.getString("title"));
        recipe.setId(recipeSnapshot.getId());
        recipe.setPhotograph(recipeSnapshot.getString("photograph"));


        TextView recipeTitle = holder.recipeTitleTextView;
        TextView prepTime = holder.prepTime;
        TextView descriptionView = holder.description;
        TextView servings = holder.servings;
        TextView category = holder.category;
        ImageView img = holder.recipeImg;

        String description = recipeSnapshot.getString("comments");
        assert description != null;
        description = description.length() > 100 ?
                description.substring(0, 100) + "..." : description;
        descriptionView.setText(description);
        String prepTimeText = Objects.requireNonNull(recipeSnapshot.getData())
                .get("preparation-time") + " mins";
        prepTime.setText(prepTimeText);
        recipeTitle.setText(recipe.getTitle());
        category.setText(recipeSnapshot.getString("category"));
        String servingsText = "Serves: " + (Objects.requireNonNull(
                recipeSnapshot.getData().get("servings")));
        servings.setText(servingsText);
        Picasso.get().load(recipe.getPhotograph()).rotate(90).into(img);
        holder.view.setOnClickListener(v -> {

            if (recipeLauncher != null) {
                recipeLauncher.launch(recipe);
            }
        });

    }

    /**
     * Interface for launching a new recipe activity.
     */
    public interface RecipeLauncher {
        void launch(Recipe recipe);
    }

    /**
     * Holds the views for the recipes
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitleTextView;
        public TextView prepTime;
        public TextView description;
        public TextView category;
        public TextView servings;
        public ImageView recipeImg;
        public View view;

        /**
         * Creates a new ViewHolder
         *
         * @param itemView the view to be used for each item in the ViewHolder
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitleTextView =
                    itemView.findViewById(R.id.recipe_title_TextView);
            prepTime = itemView.findViewById(R.id.prep_time_TextView);
            recipeImg = itemView.findViewById(R.id.recipe_ImageView);
            view = itemView;
            description =
                    itemView.findViewById(R.id.recipe_description_TextView);
            category = itemView.findViewById(R.id.recipe_category_TextView);
            servings = itemView.findViewById(R.id.recipe_servings_TextView);
        }
    }

}
