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

public abstract class EditRecyclerViewFragment<Item extends Serializable>
        extends Fragment implements EditItemAdapter.OnEditListener<Item>,
                                    EditItemAdapter.OnDeleteListener<Item> {
    private final ActivityResultLauncher<Intent> searchLauncher =
            registerForActivityResult(new SearchItemContract<>(),
                    this::onSearchActivityResult);
    private RecyclerView recyclerView;
    private TextView errorTextView;
    private EditItemAdapter<Item> adapter;
    private Item selectedItem;
    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new EditItemContract<>(),
                    this::onEditActivityResult);
    private String title;

    public void setAdapter(EditItemAdapter<Item> adapter) {
        this.adapter = adapter;
        this.adapter.setEditListener(this);
        this.adapter.setDeleteListener(this);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Nullable @Override
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

    public abstract Intent createOnEditIntent(Item item);

    public abstract Intent createOnSearchIntent(Item item);

    @Override public void onEdit(Item item) {
        this.selectedItem = item;
        adapter.setChanged(true);
        Intent intent = createOnEditIntent(item);
        editLauncher.launch(intent);
    }

    public void onSearch(Item item) {
        Intent intent = createOnSearchIntent(item);
        searchLauncher.launch(intent);
    }

    @Override public void onDelete(Item item) {
        int index = adapter.getItems().indexOf(item);
        adapter.getItems().remove(index);
        adapter.notifyItemRemoved(index);
        adapter.setChanged(true);
    }

    public Boolean hasChanged() {
        return adapter.getChanged();
    }

    abstract public void onSearchActivityResult(Pair<String, Item> result);

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

    public void add(Item item) {
        adapter.getItems().add(item);
        adapter.notifyItemInserted(adapter.getItemCount());
    }
}
