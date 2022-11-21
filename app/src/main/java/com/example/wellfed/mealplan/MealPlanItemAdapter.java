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


public abstract class MealPlanItemAdapter<Item> extends ItemAdapter<Item> {
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView primaryTextView;
        private TextView secondaryTextView;
        private ImageView imageView;

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
            primaryTextView = itemView.findViewById(R.id.primaryTextView);
            secondaryTextView = itemView.findViewById(R.id.secondaryTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
