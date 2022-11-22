package com.example.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;

/**
 * Adapter that manages the view and data for the ingredients
 * in the {@link Recipe}
 *
 * @version 1.0.0
 */
public class EditItemAdapter<Item>
        extends ItemAdapter<Item> {

    private OnEditListener<Item> editListener;
    private OnDeleteListener<Item> deleteListener;
    private Boolean changed = false;

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }


    public interface OnEditListener<Item> {
        void onEdit(Item item);
    }

    public interface OnDeleteListener<Item> {
        void onDelete(Item item);
    }

    public void setEditListener(OnEditListener<Item> editListener) {
        this.editListener = editListener;
    }

    public void setDeleteListener(OnDeleteListener<Item> deleteListener) {
        this.deleteListener = deleteListener;
    }

    /**
     * inflates the view
     *
     * @param parent   activity that handles the ingredients
     * @param viewType
     * @return
     */
    @NonNull @Override public ItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =
                layoutInflater.inflate(R.layout.view_holder_item_edit, parent,
                        false);
        return new ItemViewHolder(itemView);
    }

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
        new DeleteButton(
                ((ItemViewHolder) holder).deleteButton.getContext(),
                itemViewHolder.getDeleteButton(), "Delete item?",
                () -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item);
            }
        });
    }

    @Override public void setItems(List<Item> items) {
        super.setItems(items);
        placeholderItems = new ArrayList<>(items);
    }

    public Boolean hasChanges() {
        return !placeholderItems.equals(items);
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

        public TextView getLeadingTextView() {
            return leadingTextView;
        }

        public TextView getHeadlineTextView() {
            return headlineTextView;
        }

        public Button getEditButton() {
            return editButton;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

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
    }
}