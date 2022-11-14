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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class StorageIngredientAdapter
        extends DBAdapter<StorageIngredientAdapter.ViewHolder> {
    private static final String TAG = "SIAdapter";
    private final StorageIngredientDB db;
    private OnItemClickListener listener;

    public StorageIngredientAdapter(StorageIngredientDB db) {
        super(db.getQuery());
        Log.d(TAG, "StorageIngredientAdapter:");
        this.db = db;
    }

    public StorageIngredientAdapter(String query, StorageIngredientDB db) {
        super(db.getQuery(query));
        Log.d(TAG, "StorageIngredientAdapter:");
        this.db = db;
    }

    public StorageIngredientAdapter(String field, boolean ascending, StorageIngredientDB db) {
        super(db.getQuery());
        db.getQuery(field, ascending, ((query, success) -> {
            if (success) {
                changeQuery(query);
            } else {
                System.out.println("failure");
            }
        }));

        Log.d(TAG, "StorageIngredientAdapter:");
        this.db = db;
    }

    public interface OnItemClickListener {
        void onItemClick(StorageIngredient storageIngredient);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        Log.d(TAG, "onCreateViewHolder:");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storage_ingredient_view_holder, parent,
                        false);
        return new ViewHolder(view);
    }

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


    protected void sortString(String field, boolean ascending) {
        // load all the data
        db.getAllStorageIngredients((storageIngredients, success) -> {
            if (!success) return;
            Collections.sort(storageIngredients, (o1, o2) -> {
                switch (field) {
                    case "description":
                        return compareByDescription(o1, o2);
                    case "category":
                        return compareByCategory(o1, o2);
                    case "best-before":
                        return compareByBestBefore(o1, o2);
                    default:
                        return 0;
                }
            });

            if (!ascending) {
                Collections.reverse(storageIngredients);
            }

            HashMap<String, Integer> pos = new HashMap<>();
            ArrayList<DocumentSnapshot> snaps = new ArrayList<>();
            for (int i = 0; i < getSnapshots().size(); i++) {
                pos.put(getSnapshots().get(i).getId(), i);
            }
            for (int i = 0; i < storageIngredients.size(); i++) {
                String id = storageIngredients.get(i).getStorageId();
                int storedAt = pos.get(id);
                snaps.add(getSnapshots().get(storedAt));
            }
            setSnapshots(snaps);
        });
    }

    public int compareByDescription(StorageIngredient o1, StorageIngredient o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }

    public int compareByCategory(StorageIngredient o1, StorageIngredient o2) {
        return o1.getCategory().compareTo(o2.getDescription());
    }

    public int compareByBestBefore(StorageIngredient o1, StorageIngredient o2) {
        return o1.getBestBeforeDate().compareTo(o2.getBestBeforeDate());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // todo why is it static?
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private final TextView textView;
        private final TextView subTextView;
        private final View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textView = view.findViewById(R.id.textView);
            this.subTextView = view.findViewById(R.id.subTextView);
        }

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
