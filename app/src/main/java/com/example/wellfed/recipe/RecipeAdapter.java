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
import com.example.wellfed.common.DBAdapter;
import com.example.wellfed.common.Launcher;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.List;

/**
 * Adapter for the recipes in the {@link RecipeBookFragment}
 * binds the view to the data
 *
 * @version 1.0.0
 */
public class RecipeAdapter extends DBAdapter<RecipeAdapter.ViewHolder> {


    private final RecipeDB recipeDB;

    public void setRecipeLauncher(RecipeLauncher recipeLauncher) {
        this.recipeLauncher = recipeLauncher;
    }

    /**
     * launcher to launch to a new activity
     */
    private RecipeLauncher recipeLauncher;

    public interface RecipeLauncher{
        void launch(Recipe recipe);
    }

    /**
     * constructor the adapter
     *
     */
    public RecipeAdapter(RecipeDB db) {
        super(db.getQuery());
        this.recipeDB = db;
    }

    /**
     * Stores the view for the recipes
     *
     * @version 1.0.0
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title_textView);
        }
    }

    /**
     * Inflate the layout with the parent context
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View recipeView = layoutInflater.inflate(R.layout.activity_recipe_list, parent, false);

        return new ViewHolder(recipeView);
    }

    /**
     * Bind the data with the views
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentSnapshot recipeSnapshot = getSnapshot(position);
        Recipe recipe = new Recipe(recipeSnapshot.getString("title"));
        recipe.setId(recipeSnapshot.getId());

        TextView recipeTitle = holder.recipeTitleTextView;
        recipeTitle.setText(recipe.getTitle());

        if (recipeLauncher != null) {
            recipeTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recipeLauncher.launch(recipe);
                }
            });
        }
    }

}
