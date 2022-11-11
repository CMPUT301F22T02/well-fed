package com.example.wellfed.common;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;

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
     * The OnGetFIDListener interface is used to handle the result
     * of the getFID method.
     */
    public interface OnGetFIDListener {
        /**
         * Called when the deleteIngredient method is complete.
         *
         * @param FID        The FID that was obtained from the
         *                   database.
         * @param success    True if the operation was successful, false
         *                   otherwise
         */
        void onGetFID(String FID, Boolean success);
    }

    /**
     * Connects to the Firebase Firestore database, at the given subcollection.
     * The user is given by the FID, which is a Firebase ID given to each unique installation.
     *
     * @param subcollection The subcollection within the user to connect to
     */
    public DBConnection(String subcollection) {
        this.db = FirebaseFirestore.getInstance();
        // gets the unique ID of the installation
        getFID(
                (FID, success) -> {
                    if (!success) {
                        this.collection = null;
                    } else {
                        this.collection =
                                db.collection("users").document("user"+FID)
                                        .collection(subcollection);
                    }
                }
        );
    }

    /**
     * Gets the FID of the device, to identify the user.
     */
    private void getFID(OnGetFIDListener listener) {
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.d(TAG, "getFID:onComplete");
                        if (task.isSuccessful()) {
                            Log.d(TAG, ":isSuccessful:" + task.getResult());
                            listener.onGetFID(task.getResult(), true);
                        } else {
                            Log.d(TAG, ":isFailure: Unable to get Installation ID");
                            listener.onGetFID(null, false);
                        }
                    }
                });
    }

    /**
     * Gets the collection reference of the user's subcollection.
     *
     * @return the CollectionReference specific to a user and subcollection
     */
    public CollectionReference getCollection() {
        return this.collection;
    }
}
