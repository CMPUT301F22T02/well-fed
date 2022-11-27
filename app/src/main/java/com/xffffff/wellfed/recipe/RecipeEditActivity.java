package com.xffffff.wellfed.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xffffff.wellfed.EditActivityBase;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.RequiredDropdownTextInputLayout;
import com.xffffff.wellfed.common.RequiredNumberTextInputLayout;
import com.xffffff.wellfed.common.RequiredTextInputLayout;
import com.xffffff.wellfed.ingredient.Ingredient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
// todo create an xml file for this class


/**
 * Activity which allows user to edit an existing recipe
 *
 * @version 1.0.0
 */
public class RecipeEditActivity extends EditActivityBase implements Target {
    /**
     * The list of ingredients included in a recipe.
     */
    private List<Ingredient> recipeIngredients;

    /**
     * The recipe to view the edit screen for
     */
    private Recipe recipe;

    /**
     * The unique resource identifier for the image that is associated with a
     * recipe
     */
    private Uri uri;

    /**
     * The download URL for the image associated with a recipe
     */
    private String downloadUrl;
    /**
     * A boolean for whether a photograph upload is in progress
     */
    private boolean uploadInProgress;
    /**
     * The image of the recipe to be displayed
     */
    private ImageView recipeImg;
    /**
     * Launcher for the camera activity, to take a picture of the recipe
     */
    ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(),
                    result -> {
                        if (result) {
                            loadImage();
                        }
                    });
    /**
     * The input field for the preparation time of a recipe
     */
    private RequiredNumberTextInputLayout prepTime;
    /**
     * The input field for the servings of a recipe
     */
    private RequiredNumberTextInputLayout servings;
    /**
     * The input field for the title of a recipe
     */
    private RequiredTextInputLayout title;
    /**
     * The input field for the comments of a recipe
     */
    private RequiredTextInputLayout commentsTextInput;
    /**
     * The dropdown/autofill input field for the category of a recipe
     */
    private RequiredDropdownTextInputLayout recipeCategory;
    /**
     * The fragment for editing recipe ingredients
     */
    private EditRecipeIngredientsFragment ingredientEditFragment;

    /**
     * OnCreate method for the recipe edit activity.
     *
     * @param savedInstanceState Bundle object for the activity, to restore
     *                           to earlier state.
     */
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        // initialize the variables
        Intent intent = getIntent();
        recipeIngredients = new ArrayList<>();
        recipe = (Recipe) intent.getSerializableExtra("item");
        FloatingActionButton fab = findViewById(R.id.save_fab);


        // views
        recipeImg = findViewById(R.id.recipe_img);
        title = findViewById(R.id.recipe_title);
        prepTime = findViewById(R.id.recipe_prep_time_textView);
        servings = findViewById(R.id.recipe_no_of_servings_textView);
        commentsTextInput = findViewById(R.id.commentsTextInput);
        recipeCategory = findViewById(R.id.recipe_category);
        recipeCategory.setSimpleItems(
                new String[]{"Breakfast", "Lunch", "Dinner", "Appetizer",
                        "Dessert"});

        ingredientEditFragment = new EditRecipeIngredientsFragment();
        RecipeIngredientAdapter adapter = new RecipeIngredientAdapter();
        adapter.setItems(recipeIngredients);
        ingredientEditFragment.setAdapter(adapter);
        ingredientEditFragment.setTitle("Ingredients");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, ingredientEditFragment)
                .commit();
        uploadInProgress = false;

        // activity started to add data a recipe
        if (recipe == null) {
            fab.setImageDrawable(getDrawable(
                    R.drawable.ic_baseline_save_24)); // fab is save button
            fab.setOnClickListener(view -> {
                if (areValidFields()) {
                    recipe = new Recipe(title.getText());
                    recipe.setCategory(recipeCategory.getText());
                    recipe.setComments(commentsTextInput.getText());
                    recipe.setServings(Integer.parseInt(servings.getText()));
                    recipe.setPrepTimeMinutes(
                            Integer.parseInt(prepTime.getText()));
                    for (Ingredient ingredient : recipeIngredients) {
                        recipe.addIngredient(ingredient);
                    }
                    if (uploadInProgress) {
                        this.makeSnackbar("Please wait for photograph to " +
                                "finish uploading");
                        return;
                    }
                    recipe.setPhotograph(downloadUrl);
                    onSave();
                }
            });
        } else {
            recipeIngredients = recipe.getIngredients();
            adapter.setItems(recipeIngredients);
            adapter.notifyDataSetChanged();
            title.setPlaceholderText(recipe.getTitle());
            prepTime.setPlaceholderText(recipe.getPrepTimeMinutes().toString());
            servings.setPlaceholderText(recipe.getServings().toString());
            commentsTextInput.setPlaceholderText(recipe.getComments());
            recipeCategory.setPlaceholderText(recipe.getCategory());
            updateImageView(recipe.getPhotograph());
            fab.setOnClickListener(view -> {
                if (areValidFields()) {
                    new Intent();
                    String id = recipe.getId();
                    String photoUrl = recipe.getPhotograph();
                    recipe = new Recipe(title.getText());
                    recipe.setId(id);
                    recipe.setComments(commentsTextInput.getText());
                    recipe.setServings(servings.getInteger());
                    recipe.setCategory(recipeCategory.getText());
                    recipe.addIngredients(recipeIngredients);
                    recipe.setPrepTimeMinutes(prepTime.getInteger());
                    if (uploadInProgress) {
                        this.makeSnackbar("Please wait for photograph to " +
                                "finish uploading");
                        return;
                    }
                    recipe.setPhotograph(photoUrl);
                    intent.putExtra("type", "edit");
                    intent.putExtra("item", recipe);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        uri = initTempUri();
        recipeImg.setOnClickListener(view -> cameraLauncher.launch(uri));
    }

    /**
     * Checks whether the fields of the recipe currently being edited are
     * valid or not.
     * A valid recipe must have information in all text boxes, and at least
     * one ingredient.
     *
     * @return a boolean indicating the validity of the recipe being edited
     */
    public Boolean areValidFields() {
        if (!title.isValid()) {
            return false;
        }
        if (!prepTime.isValid()) {
            return false;
        }
        if (!servings.isValid()) {
            return false;
        }
        if (!commentsTextInput.isValid()) {
            return false;
        }
        if (!recipeCategory.isValid()) {
            return false;
        }
        if (recipeIngredients.size() <= 0) {
            this.makeSnackbar("Please add at lease one ingredient");
            return false;
        }
        return true;
    }

    /**
     * Checks whether there are unsaved changes in the edit activity
     *
     * @return true if there are unsaved changes, false otherwise
     */
    @Override public Boolean hasUnsavedChanges() {
        if (title.hasChanges()) {
            return true;
        }
        if (prepTime.hasChanges()) {
            return true;
        }
        if (servings.hasChanges()) {
            return true;
        }
        if (commentsTextInput.hasChanges()) {
            return true;
        }
        if (recipeCategory.hasChanges()) {
            return true;
        }
        return ingredientEditFragment.hasChanged();
    }

    /**
     * Handles saving a recipe, and returns it via an intent to the previous
     * activity
     */
    public void onSave() {
        // return the new recipe via intent
        Intent intent = new Intent();
        intent.putExtra("type", "add");
        intent.putExtra("item", recipe);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Initializes a temporary URI for the image associated with a recipe.
     * This is the default image associated with a recipe if one doesn't exist.
     *
     * @return the default temporary URI
     */
    public Uri initTempUri() {
        File tempImgDir = new File(RecipeEditActivity.this.getFilesDir(),
                getString(R.string.temp_images_dir));
        tempImgDir.mkdir();

        File tempImg = new File(tempImgDir, getString(R.string.temp_image));
        return FileProvider.getUriForFile(this, getString(R.string.authorities),
                tempImg);
    }

    /**
     * Uploads the image taken for a recipe to an external image hoster.
     * Also stores a reference to this image in the Firebase Firestore document
     * that is associated with the recipe being edited.
     */
    public void loadImage() {
        uploadInProgress = true;
        Picasso.get().load(uri).into(this);
    }

    /**
     * Uploads the image taken for a recipe to Firestore
     *
     * @param bitmap the image to be uploaded
     */
    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = MediaStore.Images.Media.insertImage(
                RecipeEditActivity.this.getContentResolver(), bitmap,
                UUID.randomUUID().toString(), null);
        Uri uri = Uri.parse(path);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference recipesRef =
                storage.getReference("recipe_imgs" + (new Date()));
        UploadTask uploadTask = recipesRef.putFile(uri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            uploadInProgress = false;
            this.makeSnackbar("Failed to upload image");
        }).addOnSuccessListener(taskSnapshot -> {
            recipesRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                downloadUrl = uri1.toString();
                if (recipe != null) {
                    recipe.setPhotograph(downloadUrl);
                }
                updateImageView(downloadUrl);
                uploadInProgress = false;
            });
        });
    }

    /**
     * Update image view with image
     *
     * @param url url of image to display
     */
    public void updateImageView(String url) {
        if (url == null) {
            return;
        }
        Picasso.get().load(url).into(recipeImg);
        recipeImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        recipeImg.setImageTintList(null);
    }

    /**
     * Callback for when the image has been successfully loaded by Picasso
     *
     * @param bitmap the bitmap of the image
     * @param from  the source of the image
     */
    @Override public void onBitmapLoaded(Bitmap bitmap,
                                         Picasso.LoadedFrom from) {
        uploadImage(bitmap);
    }

    /**
     * Callback for when the image has failed to load
     *
     * @param e the exception that was thrown
     */
    @Override public void onBitmapFailed(Exception e,
                                         Drawable errorDrawable) {
        uploadInProgress = false;
        RecipeEditActivity.this.makeSnackbar("Failed to " +
                "upload image");
    }

    /**
     * Callback for when the image is being prepared to be loaded
     *
     * @param placeHolderDrawable the placeholder drawable
     */
    @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
