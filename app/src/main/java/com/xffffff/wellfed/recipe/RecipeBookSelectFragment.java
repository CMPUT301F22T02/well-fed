package com.xffffff.wellfed.recipe;

/**
 * RecipeBookSelectFragment is a version of RecipeBookFragment that allows
 * the user to select a recipe
 *
 * @version 1.0.0
 */
public class RecipeBookSelectFragment extends RecipeBookFragment {
    /**
     * Stores the recipe selected listener
     */
    private OnSelectedListener listener;

    /**
     * Sets the recipe selected listener
     *
     * @param listener recipe selected listener
     */
    public void setListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Calls the recipe selected listener
     */
    @Override
    public void launch(Recipe recipe) {
        if (listener != null) {
            RecipeController recipeController = new RecipeController(requireActivity());
            RecipeDB recipeDB = recipeController.getRecipeDB();
            recipeDB.getRecipe(recipe.getId(), (foundRecipe, success) -> {
                listener.onSelected(foundRecipe);
            });
        }
    }

    /**
     * Interface for recipe selected listener
     */
    public interface OnSelectedListener {
        void onSelected(Recipe recipe);
    }
}
