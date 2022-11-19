package com.example.wellfed.ingredient;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;


public class IngredientStorageFragment extends Fragment implements Launcher, StorageIngredientAdapter.OnItemClickListener {
	/**
	 * FoodStorage is a singleton class that stores all the ingredients.
	 */
	private FoodStorage foodStorage;
	/**
	 * The ingredientController is the controller for the ingredient.
	 */
	private IngredientStorageController controller;
	/**
	 * The recycler view for the ingredients.
	 */
	RecyclerView recyclerView;
	/**
	 * The selected item
	 */
	private int selected;
	/**
	 * ImageFilterButton is the button that filters the ingredients by image.
	 */
	private ImageFilterButton imageFilterButton;
	/**
	 * The cross icon that clears the search bar.
	 */
	private ImageView crossIcon;

	/**
	 * ActivityResultLauncher for the IngredientEditActivity to edit an
	 * ingredient.
	 * The result is a StorageIngredient.
	 * The result is null if the user cancels the edit.
	 */
	ActivityResultLauncher<StorageIngredient> launcher = registerForActivityResult(new IngredientContract(), result -> {
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
				this.launch(this.selected);
				break;
			default:
				throw new IllegalArgumentException();
		}
	});

	/**
	 * ActivityResultLauncher for the IngredientAddActivity to add an
	 * ingredient.
	 * The result is a StorageIngredient.
	 * The result is null if the user cancels the add.
	 */
	ActivityResultLauncher<StorageIngredient> editIngredientLauncher = registerForActivityResult(new IngredientEditContract(), result -> {
		if (result == null) {
			return;
		}
		String type = result.first;
		StorageIngredient ingredient = result.second;
		switch (type) {
			case "add":
				controller.addIngredient(ingredient);
				break;
			case "quit":
				break;
			default:
				throw new IllegalArgumentException();
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
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		foodStorage = new FoodStorage();
		controller = new IngredientStorageController(requireActivity());
		controller.getAdapter().setOnItemClickListener(this);

		return inflater.inflate(R.layout.fragment_ingredient_storage, container, false);
	}

	/**
	 * onViewCreated method for the IngredientStorageFragment.
	 *
	 * @param view               The View returned by onCreateView.
	 * @param savedInstanceState If non-null, this fragment is being
	 *                           re-constructed from a previous saved state
	 *                           as given here.
	 */
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView = view.findViewById(R.id.ingredient_storage_list);
		recyclerView.setAdapter(controller.getAdapter());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// Search bar
		TextInputEditText searchBar = view.findViewById(R.id.ingredient_storage_search);
		// Clear search bar
		crossIcon = view.findViewById(R.id.clear_search);
		crossIcon.setOnClickListener(v -> searchBar.setText(""));

		// On search bar text change show "Functionality not implemented yet"
		// message
		searchBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					crossIcon.setVisibility(View.VISIBLE);
				} else {
					crossIcon.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				controller.getSearchResults(s.toString());
			}
		});

		// Link filter button to filter functionality
		imageFilterButton = view.findViewById(R.id.image_filter_button);
		imageFilterButton.setOnClickListener(this::filter);
	}

	/**
	 * Launches the IngredientEditActivity to edit an ingredient.
	 *
	 * @param pos The position of the ingredient in the list.
	 */
	@Override
	public void launch(int pos) {
		this.selected = pos;
		launcher.launch(foodStorage.getIngredients().get(pos));
	}

	/**
	 * Launches the IngredientAddActivity to add an ingredient.
	 */
	@Override
	public void launch() {
		editIngredientLauncher.launch(null);
	}

	/**
	 * getDefaultViewModelProviderFactory method for the
	 * IngredientStorageFragment.
	 *
	 * @return A default ViewModelProviderFactory.
	 */
	@NonNull
	@Override
	public CreationExtras getDefaultViewModelCreationExtras() {
		return super.getDefaultViewModelCreationExtras();
	}

	/**
	 * Launches the IngredientActivity to view an StorageIngredient.
	 *
	 * @param storageIngredient The StorageIngredient to view.
	 */
	@Override
	public void onItemClick(StorageIngredient storageIngredient) {
		launcher.launch(storageIngredient);
	}

	/**
	 * Launches a dropdown menu to filter the ingredients. That contain the
	 * options to filter the ingredient by description, category, and
	 * best-before date.
	 *
	 * @param view The view that was clicked.
	 *             The view is the filter button.
	 */
	public void filter(View view) {
		// Inflate the AlertDialog with the custom layout
		AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
		View customLayout =
			getLayoutInflater().inflate(R.layout.ingredients_dropdown, null);
		builder.setTitle("Filter");

		builder.setView(customLayout);

		MaterialTextView descriptionButton =
			customLayout.findViewById(R.id.sort_by_name);
		descriptionButton.setOnClickListener(v -> {
			controller.getSortedResults("description", true);
			StorageIngredientAdapter adapter = controller.getAdapter();
			adapter.setOnItemClickListener(IngredientStorageFragment.this);
			recyclerView.setAdapter(adapter);
		});
		MaterialTextView categoryButton =
			customLayout.findViewById(R.id.sort_by_category);
		categoryButton.setOnClickListener(v -> {
			controller.getSortedResults("category", true);
			StorageIngredientAdapter adapter = controller.getAdapter();
			adapter.setOnItemClickListener(IngredientStorageFragment.this);
			recyclerView.setAdapter(adapter);
		});
		MaterialTextView bestBeforeButton =
			customLayout.findViewById(R.id.sort_by_expiration_date);
		bestBeforeButton.setOnClickListener(v -> {
			controller.getSortedResults("best-before", true);
			StorageIngredientAdapter adapter = controller.getAdapter();
			adapter.setOnItemClickListener(IngredientStorageFragment.this);
			recyclerView.setAdapter(adapter);
		});

		MaterialTextView locationButton =
			customLayout.findViewById(R.id.sort_by_location);
		locationButton.setOnClickListener(v -> {
			controller.getSortedResults("location", true);
			StorageIngredientAdapter adapter = controller.getAdapter();
			adapter.setOnItemClickListener(IngredientStorageFragment.this);
			recyclerView.setAdapter(adapter);
		});

		// Create cancel button
		builder.setNegativeButton("Close", (dialog, which) -> {
			// Do nothing
		});

		// Create and show the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}