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
import com.example.wellfed.ingredient.StorageIngredientAdapter;
import com.example.wellfed.shoppingcart.IngredientEditContract;
import com.example.wellfed.ingredient.IngredientStorageController;
import com.example.wellfed.ingredient.StorageIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ShoppingCartFragment extends Fragment implements
        PopupMenu.OnMenuItemClickListener, Launcher<ShoppingCartIngredient>,
    ShoppingCartIngredientAdapter.OnItemClickListener {
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
    /**
     * Selected ingredient.
     */
    private ShoppingCartIngredient selectedIngredient;
    /**
     * Shopping cart dialog.
     */
    private ShoppingCartDialog dialog;

    /**
     * ActivityResultLauncher for the IngredientAddActivity to add an
     * ingredient.
     * The result is a StorageIngredient.
     * The result is null if the user cancels the add.
     */
    ActivityResultLauncher<ShoppingCartIngredient> addLauncher =
            registerForActivityResult(new IngredientContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                ShoppingCartIngredient ingredient = result.second;
                switch (type) {
                    case "add":
                        shoppingCartIngredientController.addIngredientToShoppingCart(ingredient);
                        break;
                    case "edit":
                        shoppingCartIngredientController.updateIngredientInShoppingCart(ingredient);
                        break;
                    case "launch":
                        this.onItemClick(selectedIngredient);
                    default:
                        throw new IllegalArgumentException();
                }
            });

    ActivityResultLauncher<ShoppingCartIngredient> editLauncher =
            registerForActivityResult(new IngredientEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                ShoppingCartIngredient ingredient = result.second;
                switch (type) {
                    case "add":
                        shoppingCartIngredientController.addIngredientToShoppingCart(ingredient);
                        break;
                    case "quit":
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });

    @Override
    public void onItemClick(ShoppingCartIngredient ingredient) {
        this.selectedIngredient = ingredient;
        addLauncher.launch(ingredient);
    }

    /**
     * onCreate method for the hoppingCartFragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        shoppingCart = new ShoppingCart();
        shoppingCartIngredientController =
            new ShoppingCartIngredientController(requireActivity());
        shoppingCartIngredientAdapter =
            shoppingCartIngredientController.getAdapter();
        shoppingCartIngredientAdapter.setOnItemClickListener(this);

        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    /**
     * onViewCreated method for the ShoppingCartFragment.
     *
     * @param view               The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.shopping_cart_list);

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setOptions(Arrays.asList(new String[]{"description", "category"}));
        sortingFragment.setListener(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_sort_container3, sortingFragment)
                .commit();

        // Create mockup data
        mockData();
        // Display data in recycler view
        setRecyclerView();

    }

    public void setRecyclerView() {
        recyclerView.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void mockData() {
        // Create mockup data
        ShoppingCartIngredient ingredient;

        ingredient = new ShoppingCartIngredient("Salt");
        ingredient.setCategory("seasoning");
        ingredient.setUnit("gram(s)");
        ingredient.setAmount(500.0d);
        ingredient.setPickedUp(false);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Juice");
        ingredient.setCategory("beverage");
        ingredient.setUnit("L(s)");
        ingredient.setAmount(1.5d);
        ingredient.setPickedUp(true);
        shoppingCart.addIngredient(ingredient);

        ingredient = new ShoppingCartIngredient("Banana");
        ingredient.setCategory("fruit");
        ingredient.setUnit("lb(s)");
        ingredient.setAmount(1.3d);
        ingredient.setPickedUp(false);
        shoppingCart.addIngredient(ingredient);
    }

//    @Override
//    public void onCompletePressed(ShoppingCartIngredient shoppingCartIngredient) {
//        // TODO: add ingredient to ingredient storage
//    }
//    @Override
//    public void onConfirm() {
//        editIngredientLauncher.launch()
//    }

    public void onClick(ShoppingCartIngredient ingredient) {
        editLauncher.launch(ingredient);
    }

    /**
     * Launches the IngredientAddActivity to add an ingredient.
     */
    @Override
    public void launch(ShoppingCartIngredient ingredient) {
        if (ingredient == null) {
            editLauncher.launch(null);
        } else {
            addLauncher.launch(ingredient);
        }
    }

    @Override
    public void onClick(String field) {

    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}