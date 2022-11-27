package com.xffffff.wellfed.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * The EditItemContract class is a contract that handles the
 * communication between the EditItemActivity and the
 * EditItemAdapter.
 *
 * @param <Item> the type of item to edit
 */
public class EditItemContract<Item extends Serializable>
    extends ActivityResultContract<Intent, Pair<String, Item>> {
    /**
     * createIntent creates the intent for the EditItemActivity
     *
     * @param context the context of the activity
     * @param intent  the intent to edit
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent intent) {
        return intent;
    }

    /**
     * parseResult parses the result from the EditItemActivity
     *
     * @param i the result code
     * @param intent     the intent
     * @return the result
     */
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
