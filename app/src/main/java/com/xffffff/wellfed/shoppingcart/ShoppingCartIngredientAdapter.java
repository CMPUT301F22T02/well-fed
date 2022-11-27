package com.xffffff.wellfed.shoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.DBAdapter;
import com.xffffff.wellfed.ingredient.StorageIngredient;

import java.util.Locale;

/**
 * This class is the adapter for the ingredients in the shopping cart list
 * <p>
 * It is used to display the ingredients in the shopping cart list
 * </p>
 */
public class ShoppingCartIngredientAdapter
    extends DBAdapter<ShoppingCartIngredientAdapter.ViewHolder> {
    /**
     * The shopping cart db to get the ingredients from.
     */
    private ShoppingCartDB db;

    /**
     * Constructor for the adapter
     */
    public ShoppingCartIngredientAdapter(ShoppingCartDB db) {
        super(db.getQuery());
        this.db = db;
    }

    private OnCheckedListener onCheckedListener;

    public void setOnCheckedChangeListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    /**
     * onCreateViewHolder method for the ShoppingCartIngredientAdapter.
     *
     * @param parent   ViewGroup for the adapter.
     * @param viewType int for the adapter.
     * @return ViewHolder object for the adapter.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View shoppingCartIngredientView =
            inflater.inflate(R.layout.shopping_cart_ingredient, parent,
                false);

        return new ViewHolder(shoppingCartIngredientView);
    }

    /**
     * onBindViewHolder method for the ShoppingCartIngredientAdapter.
     *
     * @param holder   ViewHolder object for the adapter.
     * @param position int for the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {
        DocumentSnapshot doc = getSnapshot(position);
        ShoppingCartIngredient shoppingCartIngredient =
                db.snapshotToShoppingCartIngredient(doc);

        holder.description.setText(shoppingCartIngredient.getDescription());
        String amountText = String.format(Locale.CANADA, "%.2f", shoppingCartIngredient.getAmount());
        holder.subtext.setText(amountText + " " + shoppingCartIngredient.getUnit() + " | "
                + shoppingCartIngredient.getCategory());
        holder.checkBox.setChecked(shoppingCartIngredient.isPickedUp);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onCheckedListener == null) return;
            onCheckedListener.onCheckBoxClick(getSnapshot(holder
                    .getAdapterPosition()).getString("id"), isChecked);
        });

        holder.view.setOnClickListener(v -> {
            if (onItemClickListener == null | !holder.checkBox.isChecked()) return;
            onItemClickListener.onItemClick(shoppingCartIngredient);
        });


    }

    /**
     * getItemCount method for the ShoppingCartIngredientAdapter.
     *
     * @return int for the adapter.
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * onCheckedListener interface for the ShoppingCartIngredientAdapter.
     */
    public interface OnCheckedListener {
        void onCheckBoxClick(String id, boolean isChecked);
    }

    /**
     * onItemClickListner interface for the ShoppingCartIngredientAdapter.
     */
    public interface OnItemClickListener {
        void onItemClick(ShoppingCartIngredient shoppingCartIngredient);
    }

    /**
     * onItemClickListener object for the ShoppingCartIngredientAdapter.
     */
    private OnItemClickListener onItemClickListener;

    /**
     * setOnItemClickListener method for the ShoppingCartIngredientAdapter.
     *
     * @param onItemClickListener OnItemClickListener object for the adapter.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * ViewHolder class for the ShoppingCartIngredientAdapter.
     * It contains the TextViews for the description, amount,
     * unit and category of the ingredient.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView subtext;
        public CheckBox checkBox;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            description = itemView.findViewById(
                R.id.shopping_cart_ingredient_description);
            subtext = itemView.findViewById(
                R.id.shopping_cart_ingredient_subtext);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}