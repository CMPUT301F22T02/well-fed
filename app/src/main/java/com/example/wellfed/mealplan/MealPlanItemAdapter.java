package com.example.wellfed.mealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.common.ItemAdapter;
import com.example.wellfed.recipe.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;


public abstract class MealPlanItemAdapter<Item> extends ItemAdapter<Item> {
    private OnItemClickListener<Item> listener;
    public interface OnItemClickListener<Item> {
        void onItemClick(Item item);
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
                layoutInflater.inflate(R.layout.view_holder_meal_plan_item, parent,
                        false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        Item item = items.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.getItemView().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView primaryTextView;
        private TextView secondaryTextView;
        private ImageView imageView;

        public View getItemView() {
            return itemView;
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

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            primaryTextView = itemView.findViewById(R.id.primaryTextView);
            secondaryTextView = itemView.findViewById(R.id.secondaryTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<Item> onItemClickListener) {
        this.listener = onItemClickListener;
    }
}
