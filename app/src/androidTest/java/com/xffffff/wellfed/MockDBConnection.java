package com.xffffff.wellfed;

import android.content.Context;

import com.xffffff.wellfed.common.DBConnection;

import java.util.UUID;

/**
 * This class mocks a DB connection for testing.
 * The main difference between this class and the DBConnection class is that
 * the DBConnection class requires the context to save the UUID, but this mock
 * connection class creates a new UUID (without saving it locally) for tests
 */
public class MockDBConnection extends DBConnection {
    /**
     * Connects to the Firebase Firestore database, at the given subcollection.
     * The user is given by the FID, which is a Firebase ID given to each unique installation.
     *
     */
    public MockDBConnection() {
        super(null);
    }

    /**
     * Gets the UUID of the device, to identify the user.
     * Since this is a mock class,
     *
     * @param context: the context of the application
     */
    @Override
    protected String getUUID(Context context) {
        String uuid = "test" + UUID.randomUUID().toString();
        return uuid;
    }
}
