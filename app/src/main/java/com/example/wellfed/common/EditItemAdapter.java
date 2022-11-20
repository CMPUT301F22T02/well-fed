package com.example.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.recipe.Recipe;

import java.util.List;

/**
 * Adapter that manages the view and data for the ingredients
 * in the {@link Recipe}
 *
 * @version 1.0.0
 */
public abstract class EditItemAdapter<Item>
        extends RecyclerView.Adapter<EditItemAdapter.ItemViewHolder> {
    /**
     * list of {@link Item}
     */
    protected List<Item> items;

    private OnEditListener<Item> editListener;
    private OnDeleteListener<Item> deleteListener;

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
     * @param items the list of items
     */
    public void setItems(List<Item> items) {
        this.items = items;
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

    /**
     * Binds the data in our list to the views
     *
     * @param holder   holds the inflated view
     * @param position gives the position in the list
     */
    @Override public void onBindViewHolder(@NonNull ItemViewHolder holder,
                                           int position) {
        holder.getEditButton().setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(items.get(position));
            }
        });
        holder.getDeleteButton().setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(items.get(position));
            }
        });
    }

    /**
     * @return count of the number of ingredients in our list
     */
    @Override public int getItemCount() {
        return this.items.size();
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
        private ImageView editButton;

        /**
         * delete button
         */
        private ImageView deleteButton;

        public TextView getLeadingTextView() {
            return leadingTextView;
        }

        public TextView getHeadlineTextView() {
            return headlineTextView;
        }

        public ImageView getEditButton() {
            return editButton;
        }

        public ImageView getDeleteButton() {
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