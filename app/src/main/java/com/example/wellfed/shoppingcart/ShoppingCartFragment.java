package com.example.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;

import java.util.ArrayList;
import java.util.Collections;

public class ShoppingCartFragment extends Fragment implements Launcher,
        PopupMenu.OnMenuItemClickListener {
    /**
     * ShoppingCart is a singleton class that stores all ShoppingCartIngredient objects.
     */
    private ShoppingCart shoppingCart;

    /**
     * Adapter for the recycler view.
     */
    private ShoppingCartIngredientAdapter shoppingCartIngredientAdapter;

    /**
     * Controller for the ingredients.
     */
    private ShoppingCartIngredientController shoppingCartIngredientController;

    /**
     * Recycler view for the ingredients.
     */
    RecyclerView recyclerView;

    int position;

    /**
     * ActivityResultLauncher is a launcher for the ShoppingCartIngredientActivity.
     * The result is a ShoppingCartIngredient.
     * The result is null if the user cancels the edit.
     */
    ActivityResultLauncher<ShoppingCartIngredient> shoppingCartIngredientLauncher =
            registerForActivityResult(new ShoppingCartIngredientContract(), result -> {
        if (result == null) {
            return;
        }
        String type = result.first;
        ShoppingCartIngredient shoppingCartIngredient = result.second;
        switch (type) {
            case "delete":
                shoppingCartIngredientController.deleteIngredient(position);
//                shoppingCartIngredientAdapter.notifyItemRangeChanged(position,
//                        shoppingCart.getIngredients().size());
                break;
            case "edit":
                shoppingCartIngredientController.updateIngredient(position, shoppingCartIngredient);
                break;
            case "launch":
                launch(position);
                break;
            default:
                throw new IllegalArgumentException();
        }
    });

    /**
     * ActivityResultLauncher for the ShoppingCartIngredientEditActivity
     * to edit an ingredient. The result is a ShoppingCartIngredient.
     * The result is null if the user cancels the add.
     */
    ActivityResultLauncher<ShoppingCartIngredient> editShoppingCartIngredientLauncher =
            registerForActivityResult(new ShoppingCartIngredientEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                ShoppingCartIngredient shoppingCartIngredient = result.second;
                switch (type) {
                    case "add":
                        shoppingCartIngredientController.addIngredient(shoppingCartIngredient);
                        break;
                    case "quit":
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
    );

    /**
     * onCreate method for the hoppingCartFragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        shoppingCart = new ShoppingCart();
        shoppingCartIngredientController = new ShoppingCartIngredientController();

        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    /**
     * onViewCreated method for the ShoppingCartFragment.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.shopping_cart_list);

        // Create mockup data
        ShoppingCartIngredient ingredient;

        ingredient = new ShoppingCartIngredient("Salt");
        ingredient.setCategory("seasoning");
        ingredient.setUnit("gram(s)");
        ingredient.setAmount(500.0d);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Juice");
        ingredient.setCategory("beverage");
        ingredient.setUnit("L(s)");
        ingredient.setAmount(1.5d);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Banana");
        ingredient.setCategory("fruit");
        ingredient.setUnit("lb(s)");
        ingredient.setAmount(1.3d);
        shoppingCart.addIngredient(ingredient);

        // Display data in recycler view
        shoppingCartIngredientAdapter = new ShoppingCartIngredientAdapter(
                shoppingCart.getIngredients(), this);
        shoppingCartIngredientController.setIngredients(shoppingCart.getIngredients());
        shoppingCartIngredientController.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

        // create listener for dropdown button
        ImageFilterButton btn = view.findViewById(R.id.shopping_cart_filter_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropDown(view);
            }
        });
    }

    /**
     * Launches the ShoppingCartIngredientActivity.
     * @param pos The position of the ingredient in the list.
     */
    @Override
    public void launch(int pos) {
        position = pos;
        shoppingCartIngredientLauncher.launch(shoppingCart.getIngredients().get(pos));
    }

    /**
     * Launches the ShoppingCartIngredientEditActivity to edit an ingredient.
     */
    @Override
    public void launch() {
        editShoppingCartIngredientLauncher.launch(null);
    }

    /**
     * getDefaultViewModelProviderFactory method for the ShoppingCartFragment.
     * @return A default ViewModelProviderFactory.
     */
//    @NonNull
//    @Override
//    public CreationExtras getDefaultViewModelCreationExtras() {
//        return super.getDefaultViewModelCreationExtras();
//    }

    // show dropdown menu when button is clicked
    public void showDropDown(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.shopping_cart_dropdown);
        popupMenu.show();
    }

    // Define behavior for each option in dropdown menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.description:
                // sort the ingredients
                Collections.sort(shoppingCart.getIngredients());

                // set adapter
                shoppingCartIngredientAdapter = new ShoppingCartIngredientAdapter(
                        shoppingCart.getIngredients(), this);
                shoppingCartIngredientController.setIngredients(shoppingCart.getIngredients());
                shoppingCartIngredientController.setAdapter(shoppingCartIngredientAdapter);
                recyclerView.setAdapter(shoppingCartIngredientAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

                Toast.makeText(getContext(), "Sort By Description", Toast.LENGTH_LONG).show();
                return true;
            case R.id.category:
                Toast.makeText(getContext(), "Category", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }
}