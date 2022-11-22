package com.example.wellfed.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;

public class ShoppingCartFragment extends Fragment implements
        ShoppingCartIngredientAdapter.OnItemClickListener {

    /**
     * Adapter for the recycler view.
     */
    private ShoppingCartIngredientAdapter adapter;

    /**
     * Controller for the ingredients.
     */
    private ShoppingCartIngredientController controller;

    /**
     * Recycler view for the ingredients.
     */
    RecyclerView recyclerView;


    @Override
    public void onItemClick(ShoppingCartIngredient ingredient) {

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
        controller =
                new ShoppingCartIngredientController(requireActivity());
        controller.generateShoppingCart();
        adapter = controller.getAdapter();
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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}