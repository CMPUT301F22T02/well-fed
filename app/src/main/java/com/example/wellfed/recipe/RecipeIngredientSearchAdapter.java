package com.example.wellfed.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.DBAdapter;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientAdapter;
import com.google.firebase.firestore.Query;

public class RecipeIngredientSearchAdapter extends DBAdapter<RecipeIngredientSearchAdapter.ViewHolder> {
    private static final String TAG = "RIAdapter";
    private final IngredientDB db;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Ingredient ingredient);
    }

    public RecipeIngredientSearchAdapter(IngredientDB db) {
        super(db.getQuery());
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storage_ingredient_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = db.snapshotToIngredient(getSnapshot(position));
        holder.bind(ingredient, this.listener);

    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView subTextView;
        private final View view;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.textView);
            this.subTextView = view.findViewById(R.id.subTextView);
        }

        public void bind(Ingredient ingredient, OnItemClickListener listener) {
            this.textView.setText(ingredient.getDescription());
            this.subTextView.setText(ingredient.getCategory());
            if (listener != null) {
                this.view.setOnClickListener(v->{
                    listener.onItemClick(ingredient);
                });
            }
        }

    }

}
