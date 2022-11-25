package com.xffffff.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.SearchInput;
import com.xffffff.wellfed.common.SortingFragment;

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
        controller.generateShoppingCart();
        recyclerView = view.findViewById(R.id.shopping_cart_list);

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setOptions(
                Arrays.asList("description",
                        "category"));
        sortingFragment.setListener(this);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_sort_container3, sortingFragment).commit();

        SearchInput searchInput = view.findViewById(R.id.search_input);
        searchInput.setOnTextChange(s -> controller.getSearchResults(s));
        adapter.setOnCheckedChangeListener(shoppingCartIngredient -> {
            controller.updateIngredientInShoppingCart(
                    shoppingCartIngredient
            );
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.generateShoppingCart();
    }

    @Override
    public void onClick(String field) {
        controller.sortByField(field);
    }
}