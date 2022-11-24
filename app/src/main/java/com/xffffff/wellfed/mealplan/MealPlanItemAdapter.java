package com.xffffff.wellfed.mealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.ItemAdapter;

/**
 * The MealPlanItemAdapter class binds ArrayList to RecyclerView.
 *
 * @param <Item> The type of item in the ArrayList.
 */
public abstract class MealPlanItemAdapter<Item> extends ItemAdapter<Item> {
    /**
     * Listener for the on click event of the item.
     */
    private OnItemClickListener<Item> listener;

    /**
     * inflates the view
     *
     * @param parent   activity that handles the ingredients
     * @param viewType type of view
     * @return the view
     */
    @NonNull @Override public ItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =
                layoutInflater.inflate(R.layout.view_holder_meal_plan_item,
                        parent, false);
        return new ItemViewHolder(itemView);
    }

    /**
     * onBindViewHolder method binds a MealPlan object and a MealPlanViewHolder
     * object.
     *
     * @param holder   the view holder
     * @param position the position of the item in the ArrayList
     */
    @Override public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getCardView().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    /**
     * Sets the listener for an item click in the Recyclerview
     *
     * @param onItemClickListener the listener to set
     */
    public void setOnItemClickListener(
            OnItemClickListener<Item> onItemClickListener) {
        this.listener = onItemClickListener;
    }

    /**
     * OnItemClickListener interface. The listener for an item click in the
     * RecyclerView.
     *
     * @param <Item>
     */
    public interface OnItemClickListener<Item> {
        void onItemClick(Item item);
    }

    /**
     * ItemViewHolder class holds the view for the item.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        private final TextView primaryTextView;
        private final TextView secondaryTextView;
        private final ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.materialCardView);
            primaryTextView = itemView.findViewById(R.id.primaryTextView);
            secondaryTextView = itemView.findViewById(R.id.secondaryTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public MaterialCardView getCardView() {
            return cardView;
        }

        public TextView getPrimaryTextView() {
            return primaryTextView;
        }

        public TextView getSecondaryTextView() {
            return secondaryTextView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
