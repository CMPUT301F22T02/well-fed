package com.xffffff.wellfed.storage;

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
import com.xffffff.wellfed.common.Launcher;
import com.xffffff.wellfed.common.OnItemClickListener;
import com.xffffff.wellfed.common.SearchInput;
import com.xffffff.wellfed.common.SortingFragment;
import com.xffffff.wellfed.ingredient.IngredientContract;
import com.xffffff.wellfed.ingredient.IngredientEditContract;

import java.util.Arrays;

/**
 * The IngredientStorageFragment class is a fragment that displays a list of
 * ingredients in the user's storage.
 */
public class IngredientStorageFragment extends Fragment
        implements Launcher<StorageIngredient>, OnItemClickListener<StorageIngredient>,
                   SortingFragment.OnSortClick {
    /**
     * The recycler view for the ingredients.
     */
    RecyclerView recyclerView;
    /**
     * The ingredientController is the controller for the ingredient.
     */
    private IngredientStorageController controller;
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
                if ("add".equals(type)) {
                    controller.addIngredient(ingredient);
                }
            });
    /**
     * The selected item
     */
    private StorageIngredient selectedStorageIngredient;
    /**
     * ActivityResultLauncher for the IngredientEditActivity to edit an
     * ingredient.
     * The result is a StorageIngredient.
     * The result is null if the user cancels the edit.
     */
    ActivityResultLauncher<StorageIngredient> launcher =
            registerForActivityResult(new IngredientContract(), result -> {
                if (result == null) {
                    return;
                }
                String type = result.first;
                StorageIngredient ingredient = result.second;
                switch (type) {
                    case "delete":
                        controller.deleteIngredient(ingredient);
                        break;
                    case "edit":
                        controller.updateIngredient(ingredient);
                        break;
                    case "launch":
                        this.onItemClick(selectedStorageIngredient);
                        break;
                    default:
                        break;
                }
            });

    /**
     * onCreate method for the IngredientStorageFragment.
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
    @Nullable @Override public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        controller = new IngredientStorageController(requireActivity());
        controller.getAdapter().setOnItemClickListener(this);

        return inflater.inflate(R.layout.fragment_ingredient_storage, container,
                false);
    }

    /**
     * onViewCreated method for the IngredientStorageFragment.
     *
     * @param view               The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being
     *                           re-constructed from a previous saved state
     *                           as given here.
     */
    @Override public void onViewCreated(@NonNull View view,
                                        @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ingredient_storage_list);
        recyclerView.setAdapter(controller.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SortingFragment sortingFragment = new SortingFragment();
        sortingFragment.setListener(this);
        sortingFragment.setOptions(
                Arrays.asList("description", "best-before", "category",
                        "location"),
                Arrays.asList("Description", "Best Before Date", "Category",
                        "Location")
                );
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_sort_container, sortingFragment).commit();


        SearchInput searchInput = view.findViewById(R.id.search_input);
        searchInput.setOnTextChange(s -> controller.getSearchResults(s));
    }

    /**
     * Launches the IngredientAddActivity to add an ingredient.
     */
    @Override public void launch(StorageIngredient storageIngredient) {
        if (storageIngredient == null) {
            editIngredientLauncher.launch(null);
        } else {
            launcher.launch(storageIngredient);
        }
    }

    /**
     * Launches the IngredientActivity to view an StorageIngredient.
     *
     * @param storageIngredient The StorageIngredient to view.
     */
    @Override public void onItemClick(StorageIngredient storageIngredient) {
        this.selectedStorageIngredient = storageIngredient;
        launcher.launch(storageIngredient);
    }

    /**
     * onClick method for the IngredientStorageFragment.
     *
     * @param field The field to sort by.
     */
    @Override public void onClick(String field) {
        controller.getSortedResults(field);
    }


}
