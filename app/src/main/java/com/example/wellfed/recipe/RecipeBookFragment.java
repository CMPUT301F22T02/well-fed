package com.example.wellfed.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.Launcher;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// todo create sample data for recipes
// todo setup recipe edit button
// todo setup recipe add button


// recipe object that has been modified
// update our controller here -> to reflect these changes

public class RecipeBookFragment extends Fragment implements Launcher {
    Button startRecipeBtn;
    ArrayList<Recipe> recipes;
    private int selected;
    private FirebaseFirestore db;

    private RecipeController recipeController;
    RecipeAdapter adapter;

    ActivityResultLauncher<Recipe> recipeLauncher =
            registerForActivityResult(new RecipeContract(), result -> {
                        if (result == null) {
                            return;
                        }
                        String type = result.first;
                        Recipe recipe = result.second;
                        switch (type) {
                            case "Delete":
                                recipeController.deleteRecipe(this.selected);
                                break;
                            default:
                                new IllegalArgumentException();
                        }
                    }
            );

    ActivityResultLauncher<Recipe> recipeEditLauncher = registerForActivityResult(
            new RecipeEditContract(), result -> {
                if (result == null){
                    return;
                }
                String type = result.first;
                Recipe recipe = result.second;
                switch (type){
                    case "save":
                        recipeController.addRecipe(recipe);
                }
                return;
            }
    );


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        recipeController = new RecipeController();
        db = FirebaseFirestore.getInstance();

        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.recipe_rv);

        adapter = new RecipeAdapter(getActivity(), recipes, this);
        recipeController.setRecipes(recipes);
        recipeController.setRecipeAdapter(adapter);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        final CollectionReference recipesCollection = db.collection("Recipes");
        recipesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: value){
                    recipes.clear();
                    String title = (String) doc.getData().get("title");
                    List<String> comments =(List<String>) doc.getData().get("comments");
                    String category = (String) doc.getData().get("category");
//                    Integer prepTime = (Integer) doc.getData().get("prep-time-minutes");
//                    Integer servings = (Integer) doc.getData().get("servings");
                    String url = (String) doc.getData().get("photograph");
//                    List<RecipeIngredient> recipeIngredients = (List<RecipeIngredient>) doc.getData().get("ingredients");
                    Recipe recipe = new Recipe(title);
                    recipe.setPhotoUrl(url);
//                    recipe.setServings(servings);
                    recipe.setCategory(category);
//                    recipe.setPrepTimeMinutes(prepTime);
                    recipe.addComments(comments);
//                    recipe.addIngredients(recipeIngredients);
                    recipes.add(recipe);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * launches activity to create new recipe
     */
    @Override
    public void launch() {
        recipeEditLauncher.launch(null);
    }

    @Override
    public void launch(int pos) {
        selected = pos;
        recipeLauncher.launch(recipes.get(pos));
    }
}