package com.xffffff.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.SearchInput;
import com.xffffff.wellfed.common.SortingFragment;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientEditContract;
import com.xffffff.wellfed.ingredient.StorageIngredient;

import org.junit.Ignore;

import java.util.Arrays;

public class ShoppingCartFragment extends Fragment
        implements SortingFragment.OnSortClick {

    /**
     * Recycler view for the ingredients.
     */
    RecyclerView recyclerView;
    /**
     * Adapter for the recycler view.
     */
    private ShoppingCartIngredientAdapter adapter;
    /**
     * Controller for the ingredients.
     */
    private ShoppingCartIngredientController controller;
    /**
     * Selected shopping cart ingredient.
     */
    private ShoppingCartIngredient selectedShoppingCartIngredient;


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
                StorageIngredient storageIngredient = result.second;
                switch (type) {
                    case "edit":
                        controller.addIngredientToStorage(selectedShoppingCartIngredient,
                                storageIngredient);
                        break;
                    case "quit":
                        break;
                    default:
                        break;
                }
            });


    /**
     * onCreate method for the hoppingCartFragment.
     *
     * @param inflater           The LayoutInflater object that can be used
     *                           to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that
     *                           the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being
     *                           re-constructed from a previous saved state
     *                           as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        controller = new ShoppingCartIngredientController(requireActivity());

        adapter = controller.getAdapter();
        return inflater.inflate(R.layout.fragment_shopping_cart, container,
            false);
    }

    /**
     * onViewCreated method for the ShoppingCartFragment.
     *
     * @param view               The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being
     *                           re-constructed from a previous saved state
     *                           as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.shopping_cart_list);

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setOptions(
            Arrays.asList("description", "category"),
            Arrays.asList("Description", "Category"));
        sortingFragment.setListener(this);
        requireActivity().getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_sort_container3, sortingFragment).commit();

        SearchInput searchInput = view.findViewById(R.id.search_input);
        searchInput.setOnTextChange(s -> controller.getSearchResults(s));
        adapter.setOnCheckedChangeListener((id, isChecked)->{
            controller.updateCheckedStatus(id, isChecked);
        });

        adapter.setOnItemClickListener(shoppingCartIngredient -> {
            selectedShoppingCartIngredient = shoppingCartIngredient;
            String description = selectedShoppingCartIngredient.getDescription();
            StorageIngredient storageIngredient = new StorageIngredient(description);
            storageIngredient.setUnit(shoppingCartIngredient.getUnit());
            storageIngredient.setCategory(shoppingCartIngredient.getCategory());
            storageIngredient.setAmount(shoppingCartIngredient.getAmount());
            editIngredientLauncher.launch(storageIngredient);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * onResume method for the ShoppingCartFragment.
     */
    @Override
    public void onResume() {
        super.onResume();
        controller.generateShoppingCart();
    }

    /**
     * onClick method for the sorting fragment.
     * @param field The field to sort by.
     */
    @Override
    public void onClick(String field) {
        controller.sortByField(field);
    }
}