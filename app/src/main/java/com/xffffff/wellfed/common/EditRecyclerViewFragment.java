package com.xffffff.wellfed.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;

import java.io.Serializable;

/**
 * The EditRecyclerViewFragment class is a fragment that displays a
 * list of items that can be edited.
 *
 * @param <Item> the type of item to edit
 */
public abstract class EditRecyclerViewFragment<Item extends Serializable>
    extends Fragment implements EditItemAdapter.OnEditListener<Item>,
    EditItemAdapter.OnDeleteListener<Item> {
    /**
     * Activity result launcher for the search activity
     */
    private final ActivityResultLauncher<Intent> searchLauncher =
        registerForActivityResult(new SearchItemContract<>(),
            this::onSearchActivityResult);
    /**
     * recycler view for the list of items
     */
    private RecyclerView recyclerView;
    /**
     * adapter for the recycler view
     */
    private EditItemAdapter<Item> adapter;
    /**
     * the onEdit listener for the fragment
     */
    private Item selectedItem;
    /**
     * editLauncher the activity result launcher for the edit activity
     */
    private final ActivityResultLauncher<Intent> editLauncher =
        registerForActivityResult(new EditItemContract<>(),
            this::onEditActivityResult);
    /**
     * title string for the fragment
     */
    private String title;

    /**
     * setAdapter sets the adapter for the recycler view
     *
     * @param adapter the adapter
     */
    public void setAdapter(EditItemAdapter<Item> adapter) {
        this.adapter = adapter;
        this.adapter.setEditListener(this);
        this.adapter.setDeleteListener(this);
    }

    /**
     * setTitle sets the title string for the fragment
     *
     * @param title the title string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * onCreateView inflates the view
     *
     * @param inflater           the inflater
     * @param container          the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_recycler_view,
            container, false);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.addButton);
        Button searchButton = view.findViewById(R.id.searchButton);

        addButton.setOnClickListener(v -> {
            onEdit(null);
        });

        searchButton.setOnClickListener(v -> {
            onSearch(null);
        });

        return view;
    }

    /**
     * createOnEditIntent creates an intent for the edit activity
     * @param item the item to edit
     * @return the intent
     */
    public abstract Intent createOnEditIntent(Item item);

    /**
     * createOnSearchIntent creates an intent for the search activity
     * @param item the item to search
     * @return the intent
     */
    public abstract Intent createOnSearchIntent(Item item);

    /**
     * onEdit is called when an item is edited
     * @param item the item to edit
     */
    @Override
    public void onEdit(Item item) {
        this.selectedItem = item;
        adapter.setChanged(true);
        Intent intent = createOnEditIntent(item);
        editLauncher.launch(intent);
    }

    /**
     * onSearch is called when an item is searched
     * @param item the item to search
     */
    public void onSearch(Item item) {
        Intent intent = createOnSearchIntent(item);
        searchLauncher.launch(intent);
    }

    /**
     * onDeleted is called when an item is deleted
     * @param item the item to delete
     */
    @Override
    public void onDelete(Item item) {
        int index = adapter.getItems().indexOf(item);
        adapter.getItems().remove(index);
        adapter.notifyItemRemoved(index);
        adapter.setChanged(true);
    }

    /**
     * hasChanged returns true if the list of items has changed
     * @return true if the list of items has changed
     */
    public Boolean hasChanged() {
        return adapter.getChanged();
    }

    /**
     * onSearchActivityResult is an abstract method that is called when the
     * search activity returns
     * @param result the result from the search activity
     */
    abstract public void onSearchActivityResult(Pair<String, Item> result);

    /**
     * onEditActivityResult is called when the edit activity returns
     * @param result the result from the edit activity
     */
    private void onEditActivityResult(Pair<String, Item> result) {
        if (result == null) {
            return;
        }
        String type = result.first;
        Item item = result.second;
        switch (type) {
            case "add":
                add(item);
                adapter.setChanged(true);
                break;
            case "edit":
                int index = adapter.getItems().indexOf(selectedItem);
                adapter.getItems().set(index, item);
                adapter.notifyItemChanged(index);
                adapter.setChanged(true);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * add adds an item to the list of items
     * @param item the item to add
     */
    public void add(Item item) {
        adapter.getItems().add(item);
        adapter.notifyItemInserted(adapter.getItemCount());
    }
}
