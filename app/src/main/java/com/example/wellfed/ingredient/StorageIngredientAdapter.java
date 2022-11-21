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
import com.example.wellfed.common.UTCDate;
import com.example.wellfed.common.UTCDateFormat;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

	public int compareByDescription(StorageIngredient o1, StorageIngredient o2) {
		return o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase());
	}

	public int compareByCategory(StorageIngredient o1, StorageIngredient o2) {
		return o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase());
	}

	public int compareByBestBefore(StorageIngredient o1, StorageIngredient o2) {
		return o1.getBestBeforeDate().compareTo(o2.getBestBeforeDate());
	}

	public int compareByLocation(StorageIngredient o1, StorageIngredient o2) {
		return o1.getLocation().toLowerCase().compareTo(o2.getLocation().toLowerCase());
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	// todo why is it static?
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private static final String TAG = "ViewHolder";
		private final TextView textView;
		private final TextView subTextView;
		private final TextView bestBeforeTextView;
		private final View view;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			this.textView = view.findViewById(R.id.textView);
			this.subTextView = view.findViewById(R.id.subTextView);
			this.bestBeforeTextView = view.findViewById(R.id.dateTextView);
		}

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
			UTCDate bestBefore =
				UTCDate.from(storageIngredient.getBestBeforeDate());
			this.bestBeforeTextView.setText(bestBefore.format("yyyy-MM-dd"));
			if (storageIngredient.getBestBeforeDate().before(
				new Date(new Date().getTime() - (new Date().getTime() % 86400000)))) {
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
