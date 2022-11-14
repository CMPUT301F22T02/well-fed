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
import com.example.wellfed.ingredient.IngredientEditContract;
import com.example.wellfed.ingredient.IngredientStorageController;
import com.example.wellfed.ingredient.StorageIngredient;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ShoppingCartFragment extends Fragment implements
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

    /**
     * todo
     */
    private ShoppingCartDialog dialog;

    int position;

    /**
     * ActivityResultLauncher for the IngredientAddActivity to add an
     * ingredient.
     * The result is a StorageIngredient.
     * The result is null if the user cancels the add.
     */
    ActivityResultLauncher<StorageIngredient> editIngredientLauncher =
            registerForActivityResult(new IngredientEditContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                StorageIngredient ingredient = result.second;
                switch (type) {
                    case "add":
//                        shoppingcartcontroller.addIngredient()
//                        inside your shoppingcartcontroller:
//                        todo
                        shoppingCartIngredientController.addIngredient(ingredient);
//                        IngredientStorageController controller = new IngredientStorageController(getActivity());
//                        controller.addIngredient(ingredient);
                        break;
                    case "quit":
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            });

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
        shoppingCartIngredientController = new ShoppingCartIngredientController(new IngredientStorageController(getActivity()));
        // dialog = new ShoppingCartDialog(getActivity(), "title", "message", "confirm", this);

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
        mockData();

        // Display data in recycler view
        setRecyclerView();

        // create listener for dropdown menu
        ImageFilterButton btn = view.findViewById(R.id.shopping_cart_filter_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropDown(view);
            }
        });
    }

    public void setRecyclerView() {
        shoppingCartIngredientAdapter = new ShoppingCartIngredientAdapter(
                shoppingCart.getIngredients(), this);
        shoppingCartIngredientController.setIngredients(shoppingCart.getIngredients());
        shoppingCartIngredientController.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setAdapter(shoppingCartIngredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
    }

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
                sortIngredients("description", shoppingCart.getIngredients());

                // set adapter
                setRecyclerView();
                Toast.makeText(getContext(), "Sort By Description", Toast.LENGTH_LONG).show();
                return true;
            case R.id.category:
                sortIngredients("category", shoppingCart.getIngredients());
                setRecyclerView();

                Toast.makeText(getContext(), "Sort By Category", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    public void sortIngredients(String option, ArrayList<ShoppingCartIngredient> ingredients) {
        Collections.sort(ingredients, new Comparator<ShoppingCartIngredient>() {
            @Override
            public int compare(ShoppingCartIngredient o1, ShoppingCartIngredient o2) {
                switch (option) {
                    case "description":
                        return compareByDescription(o1, o2);
                    case "category":
                        return compareByCategory(o1, o2);
                    default:
                        return 0;
                }
            }
        });
    }

    public int compareByDescription(ShoppingCartIngredient o1, ShoppingCartIngredient o2) {
        return o1.getDescription().compareTo(o2.getDescription());
    }

    public int compareByCategory(ShoppingCartIngredient o1, ShoppingCartIngredient o2) {
        return o1.getCategory().compareTo(o2.getCategory());
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
        StorageIngredient si = new StorageIngredient(ingredient.getDescription());
        si.setAmount(ingredient.getAmount());
        si.setCategory(ingredient.getCategory());
        si.setUnit(ingredient.getUnit());
        editIngredientLauncher.launch(si);
    }

    public void launch(ShoppingCartIngredient ingredient) {
        dialog.show();
    }
}