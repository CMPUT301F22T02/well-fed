package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.wellfed.ingredient.FoodStorage;
import com.example.wellfed.ingredient.StorageIngredient;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FoodStorageTest {
    private FoodStorage foodStorage;

    @Before
    public void setUp() {
        foodStorage = new FoodStorage();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, foodStorage.getIngredients().size());
    }

    @Test
    public void testAddIngredient() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        assertEquals(1, foodStorage.getIngredients().size());
        assertEquals(ingredient, foodStorage.getIngredients().get(0));
    }

    @Test
    // Test adding an ingredient that is already in the list
    public void testAddIngredient2() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        foodStorage.addIngredient(ingredient);
        assertEquals(1, foodStorage.getIngredients().size());
        assertEquals(ingredient, foodStorage.getIngredients().get(0));
    }

    @Test
    public void testDeleteIngredient() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        foodStorage.deleteIngredient(0);
        assertEquals(0, foodStorage.getIngredients().size());
    }


    @Test
    public void testUpdateIngredient() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        StorageIngredient ingredient2 = new StorageIngredient("Test2");
        foodStorage.updateIngredient(0, ingredient2);
        assertEquals(1, foodStorage.getIngredients().size());
        assertEquals(ingredient2, foodStorage.getIngredients().get(0));
    }

    @Test
    public void testSetIngredients() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        ArrayList<StorageIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        foodStorage.setIngredients(ingredients);
        assertEquals(1, foodStorage.getIngredients().size());
        assertEquals(ingredient, foodStorage.getIngredients().get(0));
    }

    @Test
    public void testGetIngredients() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        ArrayList<StorageIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        foodStorage.setIngredients(ingredients);
        assertEquals(ingredients, foodStorage.getIngredients());
    }

    @Test
    public void testGetIngredient() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        assertEquals(ingredient, foodStorage.getIngredient(0));
    }

    @Test
    public void testGetIngredient2() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        foodStorage.addIngredient(ingredient);
        StorageIngredient ingredient2 = new StorageIngredient("Test2");
        foodStorage.addIngredient(ingredient2);
        assertEquals(ingredient2, foodStorage.getIngredient(1));
    }

}
