package com.example.wellfed.recipe;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wellfed.ingredient.Ingredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDB {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipesCollection;
    final CollectionReference recipeIngredientsCollection;
    final CollectionReference ingredientsCollection;

    RecipeDB(){
        recipesCollection = db.collection("Recipes");
        recipeIngredientsCollection = db.collection("RecipeIngredients");
        ingredientsCollection = db.collection("Ingredients");
    }

    public void addRecipe(Recipe recipe){

        ArrayList<DocumentReference> recipeIngredientDocuments = new ArrayList<>();

        for(Ingredient i: recipe.getIngredients()){
            //add as ingredient
            String newIngredientId = ingredientsCollection.document().getId();

            //TODO Add a check to make sure this document id isn't in recipeIngredient collection already

            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("description", i.getDescription());
            ingredientMap.put("category", i.getCategory());
            this.ingredientsCollection.
                    document(newIngredientId)
                    .set(ingredientMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Add Recipe", "Ingredient was added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Add Recipe", "Ingredient not added");
                        }
                    });

            ingredientMap.clear();
            ingredientMap.put("amount", i.getAmount());
            ingredientMap.put("unit", i.getUnit());
            ingredientMap.put("ingredient", this.ingredientsCollection.document(newIngredientId));
            this.recipeIngredientsCollection
                    .document(newIngredientId)
                    .set(ingredientMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Add Recipe", "Recipe Ingredient was added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Add Recipe", "Recipe Ingredient not added");
                        }
                    });

            recipeIngredientDocuments.add(recipeIngredientsCollection.document(newIngredientId));
        }

        Map<String, Object> recipeMap = new HashMap<>();

        String newRecipeId = recipesCollection.document().getId();

        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("category", recipe.getCategory(0));
        recipeMap.put("prep-time-minutes", recipe.getPrepTimeMinutes());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("ingredients", recipeIngredientDocuments);


        this.recipesCollection
                .document(newRecipeId)
                .set(recipeMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Add Recipe", "Recipe added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Add Recipe", "Recipe not added");
                    }
                });
    }

    public void delRecipe(Recipe recipe){

    }

    public void editRecipe(Recipe recipe){

    }
}
