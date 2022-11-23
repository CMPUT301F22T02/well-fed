package com.xffffff.wellfed.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * Connects to the DB, getting the users unique FirestoreID to identify them.
 * Citation: https://firebase.google
 * .com/docs/projects/manage-installations#android
 */
public class DBConnection {
    /**
     * Log tag.
     */
    private static final String TAG = "DBConnection";
    /**
     * Holds the instance of the Firebase Firestore DB.
     */
    private FirebaseFirestore db;


    /**
     * Holds a reference to the User's collection (with specific
     * subcollection) in the Firebase DB.
     */
    private CollectionReference collection;

    /**
     * Holds the UUID for a user.
     */
    private String uuid;

    /**
     * Connects to the Firebase Firestore database, at the given subcollection.
     * The user is given by the FID, which is a Firebase ID given to each
     * unique installation.
     *
     * @param context: the context of the application
     */
    public DBConnection(Context context) {
        this.db = FirebaseFirestore.getInstance();
        // gets the unique ID of the installation
        this.uuid = getUUID(context);
        Log.d(TAG, "UUID: " + uuid);
    }

    /**
     * Gets the UUID of the device, to identify the user.
     * Creates a new UUID for the user if they do not already have one.
     *
     * @param context: the context of the application
     */
    protected String getUUID(Context context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("pref", Context.MODE_PRIVATE);

        String uuid = sharedPreferences.getString("UUID", null);

        // if uuid does not exist, create the uuid and save it locally
        if (uuid == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            uuid = UUID.randomUUID().toString();
            editor.putString("UUID", uuid);
            editor.commit();
        }

        return uuid;
    }

    /**
     * Gets the collection reference of the user's subcollection.
     *
     * @param subcollection the subcollection for a user to get
     * @return the collection that was retrieved
     */
    public CollectionReference getCollection(String subcollection) {
        return this.db.collection("users").document("user" + uuid)
                .collection(subcollection);
    }

    /**
     * Gets the DB
     *
     * @return the DB
     */
    public FirebaseFirestore getDB() {
        return this.db;
    }
}
