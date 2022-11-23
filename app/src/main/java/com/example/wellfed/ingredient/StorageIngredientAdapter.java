package com.example.wellfed.ingredient;

import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
	/**
	 * The listener for the adapter.
	 */
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
	 * The on click listener interface for the adapter.
	 */
	public interface OnItemClickListener {
		void onItemClick(StorageIngredient storageIngredient);
	}

	/**
	 * onCreateViewHolder is called when the view holder is created.
	 *
	 * @param parent   the parent view group
	 * @param viewType the view type
	 * @return the view holder
	 */
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

	/**
	 * onBindViewHolder is called when the view holder is bound.
	 *
	 * @param holder   the view holder
	 * @param position the position of the storage ingredient in the list
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
	 * Sorts the list of storage ingredients by the given field.
	 *
	 * @param query the query to sort
	 */
	protected void sortString(Query query) {
		changeQuery(query);
	}

	protected void search(String query) {
		if (query.isEmpty() || query.isBlank()) {
			clearSnapshots();
			changeQuery(db.getQuery());
		}

		// Get sorted search results
		db.getAllStorageIngredients((storageIngredients, success) -> {
			if (!success) return;
			Collections.sort(storageIngredients, this::compareByDescription);

			ArrayList<DocumentSnapshot> snaps = new ArrayList<>();
			for (int i = 0; i < storageIngredients.size(); i++) {
				StorageIngredient si = storageIngredients.get(i);
				if (si.getDescription().toLowerCase().contains(query.toLowerCase())) {
					String id = si.getStorageId();
					for (int j = 0; j < getSnapshots().size(); j++) {
						if (getSnapshots().get(j).getId().equals(id)) {
							snaps.add(getSnapshots().get(j));
							break;
						}
					}
				}
			}
			setSnapshots(snaps);
		});
	}

	/**
	 * Compare two storage ingredients by their description.
	 *
	 * @param o1 the first storage ingredient
	 * @param o2 the second storage ingredient
	 * @return the comparison result
	 */
	public int compareByDescription(StorageIngredient o1, StorageIngredient o2) {
		return o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase());
	}

	/**
	 * Compare two storage ingredients by their category.
	 *
	 * @param o1 the first storage ingredient
	 * @param o2 the second storage ingredient
	 * @return the comparison result
	 */
	public int compareByCategory(StorageIngredient o1, StorageIngredient o2) {
		return o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase());
	}

	/**
	 * Compare two storage ingredients by their expiration date.
	 *
	 * @param o1 the first storage ingredient
	 * @param o2 the second storage ingredient
	 * @return the comparison result
	 */
	public int compareByBestBefore(StorageIngredient o1, StorageIngredient o2) {
		return o1.getBestBefore().compareTo(o2.getBestBefore());
	}

	/**
	 * Compare two storage ingredients by their location.
	 *
	 * @param o1 the first storage ingredient
	 * @param o2 the second storage ingredient
	 * @return the comparison result
	 */
	public int compareByLocation(StorageIngredient o1, StorageIngredient o2) {
		return o1.getLocation().toLowerCase().compareTo(o2.getLocation().toLowerCase());
	}

	/**
	 * Set the on click listener for the adapter.
	 *
	 * @param listener the listener to set for the adapter
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
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
			this.bestBeforeTextView.setText(new SimpleDateFormat(
				"yyyy-MM-dd", Locale.US).format(bestBefore));
			if (storageIngredient.getBestBefore().before(new Date())) {
				this.bestBeforeTextView.setTextColor(Color.RED);
				this.subTextView.setTextColor(Color.RED);
				this.textView.setTextColor(Color.RED);
			}
			this.view.setOnClickListener(view -> {
				if (listener != null) {
					listener.onItemClick(storageIngredient);
				}
			});
		}
	}
}
