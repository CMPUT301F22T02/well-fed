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

import java.util.Locale;

/**
 * This class is the adapter for the ingredients in the shopping cart list
 * <p>
 *     It is used to display the ingredients in the shopping cart list
 * </p>
 */
public class ShoppingCartIngredientAdapter
        extends DBAdapter<ShoppingCartIngredientAdapter.ViewHolder> {

    /**
     * Constructor for the adapter
     */
    public ShoppingCartIngredientAdapter(ShoppingCartDB db) {
        super(db.getQuery());
    }

    /**
     * onCreateViewHolder method for the ShoppingCartIngredientAdapter.
     *
     * @param parent   ViewGroup for the adapter.
     * @param viewType int for the adapter.
     * @return ViewHolder object for the adapter.
     */
    @NonNull @Override public ViewHolder onCreateViewHolder(
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
    @Override public void onBindViewHolder(@NonNull ViewHolder holder,
                                           int position) {
        DocumentSnapshot doc = getSnapshot(position);
        holder.description.setText(doc.getString("description"));
        String amount = String.format(Locale.CANADA,"%.2f", ((Double) doc.getData().get("amount")));
        holder.subtext.setText(amount + " " + doc.getString("unit") + " | " +
                doc.getString("category"));
        holder.checkBox.setChecked((Boolean) doc.getData().get("picked"));

    }

    /**
     * getItemCount method for the ShoppingCartIngredientAdapter.
     *
     * @return int for the adapter.
     */
    @Override public int getItemCount() {
        return super.getItemCount();
    }

    public interface OnItemClickListener {
        void onItemClick(ShoppingCartIngredient shoppingCartIngredient);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(
                    R.id.shopping_cart_ingredient_description);
            subtext = itemView.findViewById(
                    R.id.shopping_cart_ingredient_subtext);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}