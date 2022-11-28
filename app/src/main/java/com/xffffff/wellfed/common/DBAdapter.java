package com.xffffff.wellfed.common;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// TODO: Cite: firestore/quickstart


/**
 * The class DBAdapter is used to display documents from Firestore
 * Citation:
 * Create dynamic lists with RecyclerView. Android Developers. (n.d.).
 * Retrieved September 26, 2022, from
 * https://developer.android.com/develop/ui/views/layout/recyclerview
 */
public abstract class DBAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH>
    implements EventListener<QuerySnapshot> {
    private static final String TAG = "DBAdapter";
    private final ArrayList<DocumentSnapshot> snapshots = new ArrayList<>();
    private Query mQuery;
    private ListenerRegistration mRegistration;
    private OnDataChangedListener onDataChangedListener;

    /**
     * The listener for data changes
     */
    public interface OnDataChangedListener {
        void onDataChanged();
    }

    /**
     * DBAdapter constructor - sets the query and registers the listener
     *
     * @param query the query
     */
    public DBAdapter(Query query) {
        this.setQuery(query);
    }

    /**
     * setsOnDataChangedListener - sets the listener for data changes
     *
     * @param onDataChangedListener the listener
     */
    public void setOnDataChangedListener(
        OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    /**
     * setQuery - sets the query and registers the listener
     *
     * @param query the query
     */
    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        snapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    /**
     * onEvent - called when the query is updated
     *
     * @param documentSnapshots the query snapshot
     * @param error             the error
     */
    @Override
    public void onEvent(@Nullable QuerySnapshot documentSnapshots,
                        @Nullable FirebaseFirestoreException error) {
        if (error != null || documentSnapshots == null) {
            Log.w(TAG, "onEvent:error", error);
            // TODO: call onError handler
            return;
        }

        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            onDataChanged(change);
        }
    }

    /**
     * onDataChanged - called when the query is updated
     *
     * @param change the document change
     */
    protected void onDataChanged(DocumentChange change) {
        int oldIndex = change.getOldIndex();
        int newIndex = change.getNewIndex();
        switch (change.getType()) {
            case ADDED:
                snapshots.add(newIndex, change.getDocument());
                notifyItemInserted(newIndex);
                break;
            case MODIFIED:
                if (oldIndex == newIndex) {
                    snapshots.set(oldIndex, change.getDocument());
                    notifyItemChanged(oldIndex);
                } else {
                    snapshots.remove(oldIndex);
                    snapshots.add(newIndex, change.getDocument());
                    notifyItemMoved(oldIndex, newIndex);
                }
                break;
            case REMOVED:
                snapshots.remove(change.getOldIndex());
                notifyItemRemoved(change.getOldIndex());
                break;
        }
        if (onDataChangedListener != null) {
            onDataChangedListener.onDataChanged();
        }
    }

    /**
     * getSnapshot - gets the snapshot at the given position
     *
     * @param index the position
     * @return the snapshot
     */
    protected DocumentSnapshot getSnapshot(int index) {
        return snapshots.get(index);
    }

    /**
     * startListening - starts listening to the query
     */
    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    /**
     * stopListening - stops listening to the query
     */
    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        snapshots.clear();
        notifyDataSetChanged();
    }

    /**
     * getSnapshots - gets the snapshots
     * @return the snapshots
     */
    protected ArrayList<DocumentSnapshot> getSnapshots() {
        return snapshots;
    }

    /**
     * getItemCount - gets the number of items
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return snapshots.size();
    }
}
