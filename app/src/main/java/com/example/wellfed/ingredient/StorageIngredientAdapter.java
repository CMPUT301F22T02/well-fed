package com.example.wellfed.ingredient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.DBAdapter;

/**
 * Acts as an adapter for the RecyclerView of stored ingredients.
 */
public class StorageIngredientAdapter
        extends DBAdapter<StorageIngredientAdapter.ViewHolder> {
    /**
     * Tag used for logging purposes
     */
    private static final String TAG = "SIAdapter";
    /**
     * The DB of all StorageIngredients
     */
    private final StorageIngredientDB db;
    /**
     * The listener for an item click in the RecyclerView
     */
    private OnItemClickListener listener;

    /**
     * Creates a StorageIngredientAdapter
     * @param db the StorageIngredientDB object that acts as a database for stored ingredients
     */
    public StorageIngredientAdapter(StorageIngredientDB db) {
        super(db.getQuery());
        Log.d(TAG, "StorageIngredientAdapter:");
        this.db = db;
    }

    /**
     * The listener for an item click in the RecyclerView
     */
    public interface OnItemClickListener {
        void onItemClick(StorageIngredient storageIngredient);
    }

    /**
     * Method called when the ViewHolder is created, that inflates the layout for all
     * storage ingredients
     * @param parent the parent ViewGroup
     * @param viewType the view type
     * @return the created ViewHolder
     */
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        Log.d(TAG, "onCreateViewHolder:");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storage_ingredient_view_holder, parent,
                        false);
        return new ViewHolder(view);
    }

    /**
     * Binds the ViewHolder to a state of the DB, to display stored ingredients
     * @param holder    the ViewHolder to bind
     * @param position  the position of the
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:");
        db.getStorageIngredient(getSnapshot(position),
                (storageIngredient, success) -> {
                    if (success) {
                        holder.bind(storageIngredient, listener);
                    } else {
                        Log.e(TAG, "failed to get storage ingredient");
                    }
                });
    }

    /**
     * Sets the listener for an item click in the Recyclerview
     * @param listener the listener to set
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Creates a ViewHolder for a specific View in the RecyclerView. This allows for a
     * StorageIngredient object to be bound to a View, and displayed on screen.
     */
    // todo why is it static?
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private final TextView textView;
        private final TextView subTextView;
        private final View view;

        /**
         * Creates a ViewHolder for a specific view
         * @param view the view to create a ViewHolder for
         */
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.textView);
            this.subTextView = view.findViewById(R.id.subTextView);
        }

        /**
         * Binds a view to a StorageIngredient and click listener to allow for clicking an ingredient
         * in the list
         * @param storageIngredient the storageIngredient to bind to the view
         * @param listener          the click listener for when an ingredient's view is clicked
         */
        public void bind(StorageIngredient storageIngredient,
                         OnItemClickListener listener) {
            Log.d(TAG, "bind:");
            this.textView.setText(storageIngredient.getDescription());
            this.subTextView.setText(storageIngredient.getCategory());
            this.view.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(storageIngredient);
                }
            });
        }
    }
}
