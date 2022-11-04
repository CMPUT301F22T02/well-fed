package com.example.wellfed.shoppingcart;

import com.example.wellfed.recipe.RecipeIngredientDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShoppingCartDB {
    /**
     * Holds an instance of the Firebase Firestore database
     */
    private final FirebaseFirestore db;
    /**
     * Holds a collection to the Recipes stored in the FireStore db database
     */
    final CollectionReference shoppingCartIngredientsCollection;
    /**
     * Holds a collection to the Recipe Ingredients stored in the FireStore db database
     */
    final ShoppingCartDB shoppingCartIngredientDB;

    /**
     * Create a RecipeDB object
     */
    public ShoppingCartDB(){
        db = FirebaseFirestore.getInstance();
        this.shoppingCartIngredientsCollection = db.collection("Shopping Cart Ingredients");
        shoppingCartIngredientDB = new ShoppingCartDB();
    }
}
