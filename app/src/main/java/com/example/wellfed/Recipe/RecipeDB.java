package com.example.wellfed.Recipe;

import com.example.wellfed.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeDB {
    // mock method that returns mock data
    // TODO will need to be replaced
    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("taco", "mexican"));
        recipes.add(new Recipe("daal", "indian"));
        recipes.add(new Recipe("sushi", "japanese"));
        return recipes;
    }

}
