package com.example.wellfed.ingredient;

import java.util.ArrayList;

public class IngredientController {
    private static IngredientController instance;
    private ArrayList<StorageIngredient> ingredients;
    private IngredientAdapter ingredientAdapter;

    public IngredientController(){
        ingredients = new ArrayList<>();
    }

    public void setIngredientAdapter(IngredientAdapter ingredientAdapter) {
        this.ingredientAdapter = ingredientAdapter;
    }

    public void deleteIngredient(StorageIngredient ingredient){
        if(ingredient != null){
            ingredients.remove(ingredient);
            ingredientAdapter.notifyItemRemoved(ingredients.indexOf(ingredient));
        }
    }
    public void deleteIngredient(int pos){
        if(pos >= 0 && pos < ingredients.size()){
            ingredients.remove(pos);
            ingredientAdapter.notifyItemRemoved(pos);
        }
    }

    public void setIngredients(ArrayList<StorageIngredient> ingredients){
        this.ingredients = ingredients;
    }

    public ArrayList<StorageIngredient> getIngredients() {
        return ingredients;
    }

    public void updateIngredient(int position, StorageIngredient ingredient){
        if(position >= 0 && position < ingredients.size()){
            ingredients.set(position, ingredient);
            ingredientAdapter.notifyItemChanged(position);
        }
    }

    public void updateIngredient(StorageIngredient ingredient){
        int position = ingredients.indexOf(ingredient);
        if(position >= 0 && position < ingredients.size()){
            ingredients.set(position, ingredient);
            ingredientAdapter.notifyItemChanged(position);
        }
    }
}
