package com.xffffff.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that manages the view and data for the ingredients
 * in the {@link Recipe}
 *
 * @version 1.0.0
 */
public abstract class EditItemAdapter<Item> extends ItemAdapter<Item> {
    /**
     * editListener - the listener for editing an item
     */
    protected OnEditListener<Item> editListener;
    /**
     * deleteListener - the listener for deleting an item
     */
    protected OnDeleteListener<Item> deleteListener;
    /**
     * placeholderItems - the placeholder items
     */
    private List<Item> placeholderItems;
    /**
     * changed - whether the items have changed (True) or not (False)
     */
    private Boolean changed = false;

    /**
     * getChanged - gets whether the items have changed
     *
     * @return whether the items have changed (True) or not (False)
     */
    public Boolean getChanged() {
        return changed;
    }

    /**
     * setChanged - sets whether the items have changed
     *
     * @param changed whether the items have changed (True) or not (False)
     */
    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    /**
     * setEditListener - sets the listener for editing an item
     *
     * @param editListener the listener for editing an item
     */
    public void setEditListener(OnEditListener<Item> editListener) {
        this.editListener = editListener;
    }

    /**
     * setDeleteListener - sets the listener for deleting an item
     *
     * @param deleteListener the listener for deleting an item
     */
    public void setDeleteListener(OnDeleteListener<Item> deleteListener) {
        this.deleteListener = deleteListener;
    }

    /**
     * inflates the view
     *
     * @param parent   activity that handles the ingredients
     * @param viewType the type of view
     * @return the view
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =
            layoutInflater.inflate(R.layout.view_holder_item_edit, parent,
                false);
        return new ItemViewHolder(itemView);
    }

    /**
     * onBindViewHolder - binds the view to the data
     *
     * @param holder   the view holder
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Item item = items.get(position);
        itemViewHolder.getEditButton().setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(item);
            }
        });
        new DeleteButton(((ItemViewHolder) holder).deleteButton.getContext(),
            itemViewHolder.getDeleteButton(), "Delete item?", () -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item);
            }
        });
    }

    /**
     * setItems - sets the items
     *
     * @param items the items
     */
    @Override
    public void setItems(List<Item> items) {
        super.setItems(items);
        placeholderItems = new ArrayList<>(items);
    }

    /**
     * hasChanged checks if the items have changed
     *
     * @return true if the items have changed
     */
    public Boolean hasChanges() {
        return !placeholderItems.equals(items);
    }

    /**
     * onEditListener is the interface that handles the edit button
     *
     * @param <Item> the item that is being edited
     */
    public interface OnEditListener<Item> {
        void onEdit(Item item);
    }

    /**
     * onDeleteListener is the interface that handles the delete button
     *
     * @param <Item> the item that is being deleted
     */
    public interface OnDeleteListener<Item> {
        void onDelete(Item item);
    }

    /**
     * Viewholder for the items
     *
     * @version 1.0.0
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        /**
         * text view that holds the leading text
         */
        private TextView leadingTextView;

        /**
         * text view that holds the headline text
         */
        private TextView headlineTextView;
        /**
         * edit button
         */
        private Button editButton;

        /**
         * delete button
         */
        private Button deleteButton;

        /**
         * constructor for creating the view
         *
         * @param itemView view that holds the data
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            leadingTextView = itemView.findViewById(R.id.titleTextView);
            headlineTextView = itemView.findViewById(R.id.headlineTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        /**
         * getLeadingTextView - gets the leading text view
         *
         * @return the leading text view
         */
        public TextView getLeadingTextView() {
            return leadingTextView;
        }

        /**
         * getHeadlineTextView - gets the headline text view
         *
         * @return the headline text view
         */
        public TextView getHeadlineTextView() {
            return headlineTextView;
        }

        /**
         * getEditButton - gets the edit button
         *
         * @return the edit button
         */
        public Button getEditButton() {
            return editButton;
        }

        /**
         * getDeleteButton - gets the delete button
         *
         * @return the delete button
         */
        public Button getDeleteButton() {
            return deleteButton;
        }
    }
}