package com.example.wellfed.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.recipe.RecipeIngredientEditActivity;

import java.io.Serializable;

public class EditItemContract<Item extends Serializable> extends ActivityResultContract<Item,
        Pair<String, Item>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Item item) {
        Intent intent = new Intent(context, RecipeIngredientEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }

    @Override
    public Pair<String, Item> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        } else {
            String type = intent.getStringExtra("type");
            Item item = (Item) intent.getSerializableExtra("item");
            return new Pair<>(type, item);
        }
    }
}
