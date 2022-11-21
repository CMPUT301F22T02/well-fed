package com.example.wellfed.ingredient;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wellfed.common.DBConnection;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StorageIngredientDB {
	/**
	 * Holds the tag for logging purposes
	 */
	private final static String TAG = "StorageIngredientDB";

	/**
	 * Holds the instance of the Firebase Firestore DB.
	 */
	private FirebaseFirestore db;
	/**
	 * Holds a reference to the IngredientDB
	 */
	private final IngredientDB ingredientDB;
	/**
	 * Holds a connection to the users ingredient collection in the DB.
	 */
	private DBConnection ingredientsConnection;
	/**
	 * Holds the collection for the users StoredIngredient collection in DB
	 */
	private CollectionReference collection;

	/**
	 * This interface is used to handle the result of
	 * adding StorageIngredient to the db
	 */
	public interface OnAddStorageIngredientListener {
		/**
		 * Called when addStorageIngredient returns a result
		 *
		 * @param storageIngredient the storageIngredient added to the db,
		 *                          null if the storageIngredient was not added
		 * @param success           true if the operation is successful,
		 *                          false otherwise
		 */
		void onAddStoredIngredient(StorageIngredient storageIngredient, Boolean success);
	}

	/**
	 * This interface is used to handle the result of
	 * delete StorageIngredient to the db
	 */
	public interface OnDeleteStorageIngredientListener {
		/**
		 * Called when addStorageIngredient returns a result
		 *
		 * @param storageIngredient the storageIngredient deleted from the db
		 * @param success           true if the operation is successful, false
		 *                          otherwise
		 */
		void onDeleteStorageIngredient(StorageIngredient storageIngredient, Boolean success);
	}

	/**
	 * This interface is used to handle the result of
	 * finding StorageIngredient to the db
	 */
	public interface OnGetStorageIngredientListener {
		/**
		 * Called when addStorageIngredient returns a result
		 *
		 * @param storageIngredient the storageIngredient returned by the db
		 * @param success           true if the operation is successful, false
		 *                          otherwise
		 */
		void onGetStoredIngredient(StorageIngredient storageIngredient, Boolean success);
	}

	/**
	 * This interface is used to handle the result of
	 * finding StorageIngredient to the db
	 */
	public interface OnUpdateStorageIngredientListener {
		/**
		 * Called when addStorageIngredient returns a result
		 *
		 * @param storageIngredient the storageIngredient returned by the db
		 * @param success           true if the operation is successful, false
		 *                          otherwise
		 */
		void onUpdateStorageIngredient(StorageIngredient storageIngredient, Boolean success);
	}

	/**
	 * Creates a reference to the Firebase DB.
	 */
	public StorageIngredientDB(DBConnection connection) {
		this.ingredientsConnection = connection;
		this.ingredientDB = new IngredientDB(connection);
		this.db = ingredientsConnection.getDB();
		this.collection = ingredientsConnection.getCollection("StoredIngredients");
	}

	/**
	 * Adds an ingredient in storage to the Firebase DB.
	 *
	 * @param storageIngredient the ingredient to be added
	 * @param listener         the listener to be called when the operation is
	 *                         complete
	 */
	public void addStorageIngredient(@NonNull StorageIngredient storageIngredient, OnAddStorageIngredientListener listener) {
		Log.d(TAG, "addStorageIngredient:");
		ingredientDB.getIngredient(storageIngredient, (foundIngredient, foundSuccess) -> {
			Log.d(TAG, "getIngredient:");
			if (!foundSuccess) {
				Log.d(TAG, "foundSuccess:false");
				// create a new ingredient
				ingredientDB.addIngredient(storageIngredient, (addedIngredient, addSuccess) -> {
					Log.d(TAG, "addIngredient:");
					addStorageIngredient(storageIngredient, addedIngredient, listener);
				});

			} else {
				// ingredient already exists
				Log.d(TAG, "foundSuccess:true");
				addStorageIngredient(storageIngredient, foundIngredient, listener);
			}
		});
	}

	/**
	 * todo
	 *
	 * @param storageIngredient
	 * @param ingredient
	 * @param listener
	 */
	private void addStorageIngredient(StorageIngredient storageIngredient, Ingredient ingredient, OnAddStorageIngredientListener listener) {
		Log.d(TAG, "addStorageIngredient:");
		storageIngredient.setId(ingredient.getId());
		HashMap<String, Object> storageIngredientMap = new HashMap<>();
		storageIngredientMap.put("category",
				storageIngredient.getCategory());
		storageIngredientMap.put("description", storageIngredient.getDescription());
		storageIngredientMap.put("best-before", storageIngredient.getBestBeforeDate());
		storageIngredientMap.put("location", storageIngredient.getLocation());
		storageIngredientMap.put("amount", storageIngredient.getAmount());
		storageIngredientMap.put("unit", storageIngredient.getUnit());
		storageIngredientMap.put("Ingredient", ingredientDB.getDocumentReference(ingredient));
		this.collection.add(storageIngredientMap).addOnSuccessListener(stored -> {
			Log.d(TAG, "success:");
			storageIngredient.setStorageId(stored.getId());
			ingredientDB.updateReferenceCount(ingredient, 1, (updatedIngredient, success) -> {
				Log.d(TAG, "updateReferenceCount:");
				listener.onAddStoredIngredient(storageIngredient, success);
			});
		}).addOnFailureListener(exception -> {
			Log.d(TAG, "failure:");
			listener.onAddStoredIngredient(storageIngredient, false);
		});
	}

	/**
	 * Updates a stored ingredient in the Firebase DB.
	 *
	 * @param storageIngredient the Ingredient containing the updated fields
	 * @param listener          the listener to handle the result
	 */
	public void updateStorageIngredient(StorageIngredient storageIngredient, OnUpdateStorageIngredientListener listener) {
		getStorageIngredient(storageIngredient.getStorageId(), (oldStorageIngredient, getSuccess1) -> {
			if (!getSuccess1) {
				listener.onUpdateStorageIngredient(storageIngredient, false);
				return;
			}
			ingredientDB.getIngredient(storageIngredient, (getIngredient, getSuccess) -> {
				if (getSuccess) {
					updateStorageIngredient(storageIngredient, oldStorageIngredient, getIngredient, listener);
					return;
				}
				ingredientDB.addIngredient(storageIngredient, (addedIngredient, addSuccess) -> {
					Log.d(TAG, "addIngredient:");
					if (!addSuccess) {
						listener.onUpdateStorageIngredient(storageIngredient, false);
						return;
					}
					updateStorageIngredient(storageIngredient, oldStorageIngredient, addedIngredient, listener);
				});
			});

		});
	}

	/**
	 * Updates a stored ingredient in the Firebase DB.
	 *
	 * @param storageIngredient the Ingredient containing the updated fields
	 * @param listener          the listener to handle the result
	 */
	public void updateStorageIngredient(StorageIngredient storageIngredient, StorageIngredient oldStorageIngredient, Ingredient ingredient, OnUpdateStorageIngredientListener listener) {
		WriteBatch batch = db.batch();
		DocumentReference storageIngredientRef = this.collection.document(storageIngredient.getStorageId());
		batch.update(storageIngredientRef, "category", storageIngredient.getCategory());
		batch.update(storageIngredientRef, "description", storageIngredient.getDescription());
		batch.update(storageIngredientRef, "unit", storageIngredient.getUnit());
		batch.update(storageIngredientRef, "amount", storageIngredient.getAmount());
		batch.update(storageIngredientRef, "location", storageIngredient.getLocation());
		batch.update(storageIngredientRef, "best-before", storageIngredient.getBestBeforeDate());
		batch.update(storageIngredientRef, "Ingredient", ingredientDB.getDocumentReference(ingredient));

		batch.commit().addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				Log.d(TAG, "Updated storage ingredient");
				ingredientDB.updateReferenceCount(storageIngredient, 1, (updatedIngredient2, success) -> {
					if (!success) {
						listener.onUpdateStorageIngredient(storageIngredient, false);
						return;
					}
					ingredientDB.updateReferenceCount(oldStorageIngredient, -1, (updatedIngredient3, success2) -> {
						listener.onUpdateStorageIngredient(storageIngredient, success2);
					});
				});
			} else {
				Log.d(TAG, "Failed to update storage ingredient");
				listener.onUpdateStorageIngredient(storageIngredient, false);
			}
		});
	}

	/**
	 * Removes an ingredient from storage, but keeps the Ingredient reference
	 *
	 * @param storageIngredient the storageIngredient to remove
	 */
	public void deleteStorageIngredient(StorageIngredient storageIngredient, OnDeleteStorageIngredientListener listener) {
		this.collection.document(storageIngredient.getStorageId()).delete().addOnSuccessListener(onDelete -> {
			Log.d(TAG, "DocumentSnapshot successfully deleted!");
			ingredientDB.updateReferenceCount(storageIngredient, -1, (updatedIngredient, success) -> {
				Log.d(TAG, "updateReferenceCount:");
				listener.onDeleteStorageIngredient(storageIngredient, success);
			});
		}).addOnFailureListener(failure -> {
			Log.d(TAG, "Failed to delete the storageIngredient");
			listener.onDeleteStorageIngredient(storageIngredient, false);
		});
	}

	/**
	 * Gets an ingredient from the Firebase DB
	 *
	 * @param id the ID of the ingredient to get
	 * @return The ingredient queried. If there is no result, it will return
	 * a StorageIngredient
	 * with a null description.
	 * when the onComplete listener is interrupted
	 */
	public void getStorageIngredient(String id, OnGetStorageIngredientListener listener) {
		this.collection.document(id).get().addOnSuccessListener(storedSnapshot -> {
			if (storedSnapshot.exists())  {
				Log.d(TAG, "StorageIngredient found");
				this.getStorageIngredient(storedSnapshot, listener);
			} else {
				Log.d(TAG, "Failed to get Stored Ingredient");
				listener.onGetStoredIngredient(null, false);
			}
		}).addOnFailureListener(failure -> {
			Log.d(TAG, "Failed to get Stored Ingredient");
			listener.onGetStoredIngredient(null, false);
		});
	}

	/**
	 * Gets an ingredient from the Firebase DB
	 *
	 * @param snapshot the snapshot of the StorageIngredient to get
	 * @param listener the listener to call when the ingredient is found
	 */
	public void getStorageIngredient(DocumentSnapshot snapshot, OnGetStorageIngredientListener listener) {
		StorageIngredient storageIngredient = new StorageIngredient(snapshot.getString("Description"));
		storageIngredient.setStorageId(snapshot.getId());
		storageIngredient.setCategory(snapshot.getString("category"));
		storageIngredient.setDescription(snapshot.getString("description"));
		storageIngredient.setBestBefore(snapshot.getDate("best-before"));
		storageIngredient.setLocation(snapshot.getString("location"));
		storageIngredient.setAmount(snapshot.getDouble("amount"));
		storageIngredient.setUnit(snapshot.getString("unit"));
		DocumentReference ingredientReference = (DocumentReference) snapshot.get("Ingredient");
		storageIngredient.setId(ingredientReference.getId());
		listener.onGetStoredIngredient(storageIngredient, true);
	}

	/**
	 * Gets a query for StorageIngredients in Firestore
	 *
	 * @return the query
	 */
	public Query getQuery() {
		return this.collection.orderBy("best-before",
			Query.Direction.DESCENDING);
	}

	public interface OnAllIngredients {
		void onAllIngredients(ArrayList<StorageIngredient> ingredients, boolean success);
	}

	public void getAllStorageIngredients(OnAllIngredients listener) {
		// If collection is empty, return empty query
		if (this.collection == null) {
			listener.onAllIngredients(null, false);
			return;
		}
		ArrayList<StorageIngredient> storageIngredients = new ArrayList<>();
		this.collection.get()
			.addOnSuccessListener(queryDocumentSnapshots -> {
				AtomicInteger i = new AtomicInteger(queryDocumentSnapshots.size());
				AtomicInteger found = new AtomicInteger(0);
				for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
					DocumentReference ingredientRef = (DocumentReference) snapshot.getData().get("Ingredient");
					ingredientRef.get()
						.addOnSuccessListener(ingredientSnap -> {
							String description = ingredientSnap.getString("description");
							StorageIngredient storageIngredient = new StorageIngredient(description);
							storageIngredient.setStorageId(snapshot.getId());
							storageIngredient.setCategory(ingredientSnap.getString("category"));
							storageIngredient.setAmount((Double) snapshot.getData().get("amount"));
							storageIngredient.setUnit((String) snapshot.getData().get("unit"));
							storageIngredient.setLocation((String) snapshot.getData().get("location"));
							// Get Firebase Timestamp and convert to Date
							Timestamp bestBefore = (Timestamp) snapshot.getData().get("best-before");
							assert bestBefore != null;
							storageIngredient.setBestBefore(bestBefore.toDate());

							storageIngredients.add(storageIngredient);
							found.getAndAdd(1);
							if (found.get() == i.get()) {
								listener.onAllIngredients(storageIngredients, true);
							}
						})
						.addOnFailureListener(failure -> {
							listener.onAllIngredients(null, false);
						});
				}
			});
	}

}