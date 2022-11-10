package com.example.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;

import java.util.ArrayList;

public class ShoppingCartFragment extends Fragment implements ShoppingCartIngredientAdapter.ShoppingCartIngredientLauncher, Launcher {
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
     * ActivityResultLauncher for the ShoppingCartIngredientEditActivity to edit an ingredient.
     * The result is a ShoppingCartIngredient.
     * The result is null if the user cancels the edit.
     */
    ActivityResultLauncher<ShoppingCartIngredient> shoppingCartIngredientLauncher = registerForActivityResult(new ShoppingCartIngredientContract(), result -> {
        if (result == null) {
            return;
        }
        String type = result.first;
        ShoppingCartIngredient shoppingCartIngredient = result.second;
        switch (type) {
            case "delete":
                shoppingCartIngredientController.deleteIngredient(position);
                shoppingCartIngredientAdapter.notifyItemRangeChanged(position,
                        shoppingCart.getIngredients().size());
                break;
            case "edit":
                shoppingCartIngredientController.updateIngredient(position, shoppingCartIngredient);
                break;
            default:
                break;
        }
    });

    /**
     * ActivityResultLauncher for the ShoppingCartIngredientAddActivity
     * to add an ingredient. The result is a ShoppingCartIngredient.
     * The result is null if the user cancels the add.
     */
    ActivityResultLauncher<ShoppingCartIngredient> addShoppingCartIngredientLauncher = registerForActivityResult(
            new ShoppingCartIngredientEditContract(), result -> {
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

        ingredient = new ShoppingCartIngredient("Banana");
        ingredient.setCategory("Fruit");
        ingredient.setUnit("lb(s)");
        ingredient.setAmount(1.3f);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Salt");
        ingredient.setCategory("seasoning");
        ingredient.setUnit("gram(s)");
        ingredient.setAmount(500.0f);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Juice");
        ingredient.setCategory("Beverage");
        ingredient.setUnit("L(s)");
        ingredient.setAmount(1.5f);
        shoppingCart.addIngredient(ingredient);

        // Display data in recycler view
        shoppingCartIngredientAdapter = new ShoppingCartIngredientAdapter(
                shoppingCart.getIngredients(), this);
        shoppingCartIngredientController.setIngredients(shoppingCart.getIngredients());
        shoppingCartIngredientController.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
    }

    /**
     * Launches the ShoppingCartIngredientEditActivity to edit an ingredient.
     * @param pos The position of the ingredient in the list.
     */
    @Override
    public void launch(int pos) {
        position = pos;
        shoppingCartIngredientLauncher.launch(shoppingCart.getIngredients().get(pos));
    }

    /**
     * Launches the ShoppingCartIngredientAddActivity to add an ingredient.
     */
    @Override
    public void launch() {
        addShoppingCartIngredientLauncher.launch(null);
    }

    /**
     * getDefaultViewModelProviderFactory method for the ShoppingCartFragment.
     * @return A default ViewModelProviderFactory.
     */
    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}