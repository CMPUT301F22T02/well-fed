package com.xffffff.wellfed.ingredient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.DBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The adapter class for the storage ingredient list.
 */
public class StorageIngredientAdapter
        extends DBAdapter<StorageIngredientAdapter.ViewHolder> {
    /**
     * The tag for the log.
     */
    private static final String TAG = "SIAdapter";
    /**
     * The storage ingredient DB.
     */
    private final StorageIngredientDB db;
    private OnItemClickListener listener;

    /**
     * The constructor for the adapter.
     *
     * @param db the storage ingredient DB
     */
    public StorageIngredientAdapter(StorageIngredientDB db) {
        super(db.getQuery());
        Log.d(TAG, "StorageIngredientAdapter:");
        this.db = db;
    }

    /**
     * onCreateViewHolder is called when the view holder is created.
     *
     * @param parent   the parent view group
     * @param viewType the view type
     * @return the view holder
     */
    @NonNull @Override public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder:");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storage_ingredient_view_holder, parent,
                        false);
        return new ViewHolder(view);
    }

    /**
     * onBindViewHolder is called when the view holder is bound.
     *
     * @param holder   the view holder
     * @param position the position of the storage ingredient in the list
     */
    @Override public void onBindViewHolder(@NonNull ViewHolder holder,
                                           int position) {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * The on click listener interface for the adapter.
     */
    public interface OnItemClickListener {
        void onItemClick(StorageIngredient storageIngredient);
    }

    /**
     * The view holder class for the storage ingredient list.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private final TextView textView;
        private final TextView subTextView;
        private final TextView bestBeforeTextView;
        private final View view;

        /**
         * The constructor for the view holder.
         *
         * @param view the view to hold the data
         */
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.textView);
            this.subTextView = view.findViewById(R.id.subTextView);
            this.bestBeforeTextView = view.findViewById(R.id.dateTextView);
        }

        /**
         * Bind the storage ingredient to the view holder.
         *
         * @param storageIngredient the storage ingredient to bind
         * @param listener          the listener to set for the view holder
         */
        public void bind(StorageIngredient storageIngredient,
                         OnItemClickListener listener) {
            Log.d(TAG, "bind:");
            this.textView.setText(storageIngredient.getDescription());
            // Limit subtext to 15 characters for both category and location
            String category = storageIngredient.getCategory().length() > 15 ?
                    storageIngredient.getCategory().substring(0, 15) + "..." :
                    storageIngredient.getCategory();
            String location = storageIngredient.getLocation().length() > 15 ?
                    storageIngredient.getLocation().substring(0, 15) + "..." :
                    storageIngredient.getLocation();
            String subText = category + " | " + location;
            this.subTextView.setText(subText);
            Date bestBefore = storageIngredient.getBestBefore();
            this.bestBeforeTextView.setText(
                    new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(
                            bestBefore));
            int colorError = MaterialColors.getColor(this.view,
                    com.google.android.material.R.attr.colorError);
            int colorOnSurface = MaterialColors.getColor(this.view,
                    com.google.android.material.R.attr.colorOnSurface);

            if (storageIngredient.getBestBefore().before(new Date())) {
                this.bestBeforeTextView.setTextColor(colorError);
                this.subTextView.setTextColor(colorError);
                this.textView.setTextColor(colorError);
            } else {
                this.bestBeforeTextView.setTextColor(colorOnSurface);
                this.subTextView.setTextColor(colorOnSurface);
                this.textView.setTextColor(colorOnSurface);
            }
            this.view.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(storageIngredient);
                }
            });
        }
    }
}
