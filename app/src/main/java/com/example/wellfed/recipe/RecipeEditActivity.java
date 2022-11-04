package com.example.wellfed.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
// todo create an xml file for this class

/**
 * Activity which allows user to edit an existing recipe
 *
 * @version 1.0.0
 */
public class RecipeEditActivity extends ActivityBase {
    private RecyclerView ingredientRV;
    private RecyclerView commentsRV;
    private List<RecipeIngredient> recipeIngredients;
    private List<String> comments;
    private RecipeIngredientAdapter recipeIngredientAdapter;
    private Recipe recipe;
    private FloatingActionButton fab;
    private Uri uri;
    private String downloadUrl;
    ImageView recipeImg;


    // take picture
    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            result -> {
                if (result) {
                    uploadImg();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_edit);

        // intialize the variables
        Intent intent = getIntent();
        recipeIngredients = new ArrayList<>();
        comments = new ArrayList<>();
        recipe = (Recipe) intent.getSerializableExtra("Recipe");
        fab = findViewById(R.id.fab);


        // views
        EditText title = findViewById(R.id.recipe_title_editText);
        EditText prepTime = findViewById(R.id.recipe_prep_time_editText);
        EditText servings = findViewById(R.id.recipe_no_of_servings_editText);
        ingredientRV = findViewById(R.id.recipe_ingredient_recycleViewer);
        commentsRV = findViewById(R.id.recipe_comments_recycleViewer);
        Button addComment = findViewById(R.id.recipe_comment_add_btn);
        Button addIngredient = findViewById(R.id.ingredient_add_btn);

        recipeImg = findViewById(R.id.recipe_img);


        // activity started to add data a recipe
        if (recipe == null) {
            fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_save_24)); // fab is save button
            fab.setOnClickListener(view -> {
                recipe = new Recipe(title.getText().toString());
                recipe.setCategory("Test");
                recipe.addComments(comments);
                recipe.setServings(Integer.parseInt(servings.getText().toString()));
                recipe.setPrepTimeMinutes(Integer.parseInt(prepTime.getText().toString()));
                recipe.addIngredients(recipeIngredients);
                onSave();
            });
        } else {

        }
        fillIngredients();

        // ingredient recycle viewer and it's adapter
        recipeIngredientAdapter = new RecipeIngredientAdapter(recipeIngredients, R.layout.recipe_ingredient_edit);
        ingredientRV.setAdapter(recipeIngredientAdapter);
        ingredientRV.setLayoutManager(new LinearLayoutManager(RecipeEditActivity.this));

        CommentAdapter commentAdapter = new CommentAdapter(comments);
        commentsRV.setAdapter(commentAdapter);
        commentsRV.setLayoutManager(new LinearLayoutManager(RecipeEditActivity.this));


        uri = initTempUri();
        recipeImg.setOnClickListener(view -> {
            cameraLauncher.launch(uri);
        });

        addComment.setOnClickListener(view -> {
            View comment_dialog = LayoutInflater.from(this).inflate(R.layout.dialog_recipe_comment, null, false);
            AtomicReference<EditText> comment = new AtomicReference<>(comment_dialog.findViewById(R.id.comment_editText));
            AlertDialog dialog = new MaterialAlertDialogBuilder(RecipeEditActivity.this)
                    .setTitle("Comment")
                    .setView(R.layout.dialog_recipe_comment)
                    .setPositiveButton("Add", (d, which) -> {
                        comment.set((EditText) ((AlertDialog) d).findViewById(R.id.comment_editText));
                        comments.add(comment.get().getText().toString());
                        commentAdapter.notifyItemInserted(comments.size());
                    })
                    .setNeutralButton("Cancel", null).show();
        });
        addIngredient.setOnClickListener(view -> {
            AlertDialog dialog = new MaterialAlertDialogBuilder(RecipeEditActivity.this)
                    .setTitle("Comment")
                    .setView(R.layout.dialog_recipe_ingredient)
                    .setPositiveButton("Add", (d, which) -> {
                    })
                    .setNeutralButton("Cancel", null).show();
        });

    }

    public void fillIngredients() {
        // temp data
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setDescription("Cinnamon Sugar");
        recipeIngredient.setAmount(1.0F);
        recipeIngredient.setUnit("tbsp");
        RecipeIngredient recipeIngredient1 = new RecipeIngredient();
        recipeIngredient1.setDescription("Apple Slices");
        recipeIngredient1.setAmount(3.0F);
        recipeIngredient1.setUnit("slice");
        RecipeIngredient recipeIngredient2 = new RecipeIngredient();
        recipeIngredient2.setDescription("Dough");
        recipeIngredient2.setAmount(1.0F);
        recipeIngredient2.setUnit("cup");
        recipeIngredients.add(recipeIngredient);
        recipeIngredients.add(recipeIngredient1);
        recipeIngredients.add(recipeIngredient2);
        comments.add("nice");
    }

    public void onSave() {
        // return the new recipe via intent
        Intent intent = new Intent();
        intent.putExtra("type", "save");
        intent.putExtra("Recipe", recipe);
        setResult(RESULT_OK, intent);
        finish();
    }

    public Uri initTempUri() {
        File tempImgDir = new File(RecipeEditActivity.this.getFilesDir(),
                getString(R.string.temp_images_dir));
        tempImgDir.mkdir();

        File tempImg = new File(tempImgDir, getString(R.string.temp_image));
        Uri imageUri = FileProvider.getUriForFile(
                this,
                getString(R.string.authorities),
                tempImg);
        return imageUri;
    }

    public void uploadImg() {
        Uri file = uri;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference recipesRef = storage.getReference("recipe_imgs" + (new Date()).toString());

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null) {

            return;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = MediaStore.Images.Media.insertImage(RecipeEditActivity.this.getContentResolver(), bitmap, "temp", null);
        Uri uri = Uri.parse(path);
        UploadTask uploadTask = recipesRef.putFile(uri);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                int temp = 0;
                recipesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = uri.toString();
                    Picasso.get()
                            .load(downloadUrl)
                            .rotate(90)
                            .into(recipeImg);

                });
            }

        });
    }

}
