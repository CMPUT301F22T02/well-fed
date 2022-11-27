package com.xffffff.wellfed.mealplan;

import android.app.Activity;
import android.util.Pair;

import com.google.firebase.firestore.ListenerRegistration;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.common.AdapterDataObserver;
import com.xffffff.wellfed.common.DBAdapter;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.DateUtil;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.unit.Unit;
import com.xffffff.wellfed.unit.UnitConverter;

import java.util.Date;

/**
 * The MealPlanController class controls the MealPlanActivity.
 **/
public class MealPlanController implements DBAdapter.OnDataChangedListener {
    /**
     * The MealPlanActivity that the controller controls.
     */
    private final ActivityBase activity;
    /**
     * listenerRegistration is the ListenerRegistration object for the
     * controller.
     */
    private ListenerRegistration listenerRegistration;
    /**
     * The DB of stored ingredients
     */
    private final MealPlanDB db;
    /**
     * The MealPlan adapter for the RecyclerView
     */
    private MealPlanAdapter adapter;
    /**
     * onDataChanged is the AdapterDataObserver that is called when the data
     */
    private OnDataChanged onDataChanged;
    /**
     * onAdapterDataChangedListener is the AdapterDataObserver that is called
     * when the data in the adapter changes.
     */
    private OnAdapterDataChangedListener onAdapterDataChangedListener;

    /**
     * onDataChanged is the interface that is called when the data in the
     * controller changes.
     */
    public interface OnDataChanged {
        void onDataChanged(MealPlan mealPlan);
    }

    /**
     * onAdapterDataChangedListener is the interface that is called when the
     * data in the adapter changes.
     */
    public interface OnAdapterDataChangedListener {
        void onAdapterDataChanged(MealPlan mealPlan, int position);
    }

    /**
     * setOnDataChanged sets the OnDataChanged listener.
     * @param onDataChanged the OnDataChanged listener
     */
    public void setOnDataChanged(OnDataChanged onDataChanged) {
        this.onDataChanged = onDataChanged;
    }

    /**
     * setOnAdapterDataChangedListener sets the OnAdapterDataChangedListener
     * listener.
     * @param listener the OnAdapterDataChangedListener
     * listener
     */
    public void setOnAdapterChangedListener(OnAdapterDataChangedListener listener) {
        this.onAdapterDataChangedListener = listener;
    }

    /**
     * The MealPlanController constructor. Creates a new MealPlanController
     * object.
     *
     * @param activity The activity that the controller is being created in.
     */
    public MealPlanController(Activity activity) {
        this.activity = (ActivityBase) activity;
        DBConnection connection =
                new DBConnection(activity.getApplicationContext());
        db = new MealPlanDB(connection);
        adapter = new MealPlanAdapter(db);
        adapter.setOnDataChangedListener(this);
        onDataChanged();
    }

    /**
     * startListening starts listening for changes in the database for the
     * specified meal plan.
     * @param id the id of the meal plan
     */
    public void startListening(String id) {
        if (listenerRegistration == null) {
            listenerRegistration = db.getMealPlanDocumentReference(id)
                    .addSnapshotListener((doc, err) -> {
                        db.getMealPlan(doc, (foundMealPlan, success) -> {
                            onDataChanged.onDataChanged(foundMealPlan);
                        });
                    });
        }
    }

    /**
     * stopListening stops listening for changes in the database.
     */
    public void stopListening() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    /**
     * GetAdapter method for the MealPlanController. Returns the adapter
     * for the RecyclerView of meal plans.
     *
     * @return The adapter for the RecyclerView of meal plans.
     */
    public MealPlanAdapter getAdapter() {
        return adapter;
    }

    /**
     * The setAdapter method for the ingredient search screen. Sets the
     * adapter for the RecyclerView of meal plans.
     *
     * @param adapter The adapter for the RecyclerView of meal plans.
     */
    public void setAdapter(MealPlanAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * The addMealPlan method for the MealPlanController. Adds a new meal plan
     * to the database.
     *
     * @param mealPlan The meal plan to be added to the database.
     */
    public void addMealPlan(MealPlan mealPlan) {
        db.addMealPlan(mealPlan, (addMealPlan, addSuccess) -> {
            if (!addSuccess) {
                this.activity.makeSnackbar(
                        "Failed to add " + addMealPlan.getTitle());
            } else {
                this.activity.makeSnackbar("Added " + addMealPlan.getTitle());
            }
        });
    }

    /**
     * The updateMealPlan method for the MealPlanController. Updates a meal
     * plan in the database.
     *
     * @param mealPlan The meal plan to be updated in the database.
     */
    public void updateMealPlan(MealPlan mealPlan) {
        db.updateMealPlan(mealPlan, (updateMealPlan, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar(
                        "Failed to update " + updateMealPlan.getTitle());
            } else {
                this.activity.makeSnackbar(
                        "Updated " + updateMealPlan.getTitle());
            }
        });
    }

    /**
     * The deleteMealPlan method for the MealPlanController. Deletes a meal
     * plan from the database.
     * @param mealPlan The meal plan to be deleted from the database.
     * @param surpressSnackbar A boolean indicating whether to surpress the snackbar or not.
     */
    public void deleteMealPlan(MealPlan mealPlan, boolean surpressSnackbar) {
        this.db.delMealPlan(mealPlan, ((deleteMealPlan, deleteSuccess) -> {
            if (!deleteSuccess) {
                if (!surpressSnackbar) {
                    this.activity.makeSnackbar(
                            "Failed to delete" + deleteMealPlan.getTitle());
                }
            } else {
                if (!surpressSnackbar) {
                    this.activity.makeSnackbar(
                            "Deleted " + deleteMealPlan.getTitle());
                }
            }
        }));
    }

    /**
     * The scaleRecipe method for the MealPlanController. Scales a recipe
     * to the desired number of servings.
     *
     * @param recipe           The recipe to be scaled.
     * @param mealPlanServings The number of servings to scale the recipe to.
     * @return The scaled recipe.
     */
    public Recipe scaleRecipe(Recipe recipe, int mealPlanServings) {
        Recipe scaledRecipe = new Recipe(recipe);
        UnitConverter unitConverter = new UnitConverter(this.activity);
        int recipeServings = scaledRecipe.getServings();
        double scalingFactor = (double) mealPlanServings / recipeServings;
        for (Ingredient ingredient : scaledRecipe.getIngredients()) {
            ingredient.setAmount(ingredient.getAmount() * scalingFactor);
            Pair<Double, Unit> pair =
                    unitConverter.scaleUnit(ingredient.getAmount(),
                            unitConverter.build(ingredient.getUnit()));
            ingredient.setAmount(pair.first);
            ingredient.setUnit(pair.second.getUnit());
        }
        scaledRecipe.setServings(mealPlanServings);
        return scaledRecipe;
    }

    /**
     * If data is changed
     */
    @Override public void onDataChanged() {
        if (onAdapterDataChangedListener != null) {
            Pair<MealPlan, Integer> pair = getCurrentMealPlan();
            onAdapterDataChangedListener.onAdapterDataChanged(pair.first,
                    pair.second);
        }
    }

    /**
     * Get today's meal plan
     */
    public Pair<MealPlan, Integer> getCurrentMealPlan() {
        Date today = new Date();
        DateUtil dateUtil = new DateUtil();
        int position = 0;
        for (MealPlan mealPlan : adapter.getMealPlans()) {
            Date eatDate = mealPlan.getEatDate();
            if (dateUtil.equals(eatDate, today)) {
                return new Pair<>(mealPlan, position);
            }
            position++;
        }
        return new Pair<>(null, -1);
    }
}
