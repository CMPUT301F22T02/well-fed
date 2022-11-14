package com.example.wellfed.common;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public DBAdapter(Query query) {
        query.addSnapshotListener(this);
    }

    public void changeQuery(Query query) {
        query.addSnapshotListener(this);
    }

    @Override public void onEvent(@Nullable QuerySnapshot documentSnapshots,
                                  @Nullable FirebaseFirestoreException error) {
        if (error != null || documentSnapshots == null) {
            Log.w(TAG, "onEvent:error", error);
            // TODO: call onError handler
            return;
        }

        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
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
                    snapshots.remove(oldIndex);
                    notifyItemRemoved(change.getOldIndex());
                    break;
            }
        }
        //        TODO: call onDataChanged handler
    }

    protected DocumentSnapshot getSnapshot(int index) {
        return snapshots.get(index);
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void setSnapshots(List<DocumentSnapshot> snapshots){
        this.snapshots.clear();
        this.snapshots.addAll(snapshots);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    protected void clearSnapshots(){
        this.snapshots.clear();
        notifyDataSetChanged();
    }


    protected ArrayList<DocumentSnapshot> getSnapshots(){
        return snapshots;
    }

    @Override public int getItemCount() {
        return snapshots.size();
    }
}
