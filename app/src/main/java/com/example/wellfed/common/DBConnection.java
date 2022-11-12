package com.example.wellfed.common;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.UUID;

/**
 * Connects to the DB, getting the users unique FirestoreID to identify them.
 * Citation: https://firebase.google.com/docs/projects/manage-installations#android
 */
public class DBConnection {
    /**
     * Holds the TAG for logging.
     */
    private static final String TAG = "DBConnection";

    /**
     * Holds the instance of the Firebase Firestore DB.
     */
    private FirebaseFirestore db;


    /**
     * Holds a reference to the User's collection (with specific subcollection) in the Firebase DB.
     */
    private CollectionReference collection;


    /**
     * Connects to the Firebase Firestore database, at the given subcollection.
     * The user is given by the FID, which is a Firebase ID given to each unique installation.
     *
     * @param subcollection The subcollection within the user to connect to
     */
    public DBConnection(Context context, String subcollection, boolean isTest) {
        this.db = FirebaseFirestore.getInstance();
        // gets the unique ID of the installation
        String uuid = getUUID(context, isTest);
        this.collection = db.collection("users")
                .document("user" + uuid).collection(subcollection);
    }

    /**
     * Gets the UUID of the device, to identify the user.
     * Creates a new UUID for the user if they do not already have one.
     */
    private String getUUID(Context context, boolean isTest) {
        // Since a test user does not have a valid context, we must create a TEST string
        if (isTest) {
            String testID = "TEST";
            return testID;
        }

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
     * @return the CollectionReference specific to a user and subcollection
     */
    public CollectionReference getCollection() {
        return this.collection;
    }

    public FirebaseFirestore getDB() {
        return this.db;
    }
}
