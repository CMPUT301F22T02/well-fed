package com.example.wellfed.mealplan;

import android.app.Activity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.Launcher;

public class MealPlanController {
	private final ActivityBase activity;
	private MealPlanAdapter adapter;
	/**
	 * The DB of stored ingredients
	 */
	private final MealPlanDB db;

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
	 * The MealPlanController constructor. Creates a new MealPlanController
	 * object.
	 *
	 * @param activity The activity that the controller is being created in.
	 */
	public MealPlanController(Activity activity) {
		this.activity = (ActivityBase) activity;
		DBConnection connection = new DBConnection(activity.getApplicationContext());
		db = new MealPlanDB(connection);
		adapter = new MealPlanAdapter(db);
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
	 * The addMealPlan method for the MealPlanController. Adds a new meal plan
	 * to the database.
	 *
	 * @param mealPlan The meal plan to be added to the database.
	 */
	public void addMealPlan(MealPlan mealPlan) {
		db.addMealPlan(mealPlan, (addMealPlan, addSuccess) -> {
			if (!addSuccess) {
				this.activity.makeSnackbar("Failed to add " + addMealPlan.getTitle());
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
	 * @param launcher The launcher to be used to launch the activity.
	 */
	public void updateMealPlan(MealPlan mealPlan, Launcher<MealPlan> launcher) {
		db.updateMealPlan(mealPlan, (updateMealPlan, updateSuccess) -> {
			if (!updateSuccess) {
				this.activity.makeSnackbar("Failed to update " + updateMealPlan.getTitle());
			} else {
				this.activity.makeSnackbar("Updated " + updateMealPlan.getTitle());
				launcher.launch(updateMealPlan);
			}
		});
	}

	/**
	 * The deleteMealPlan method for the MealPlanController. Deletes a meal
	 * plan from the database.
	 *
	 * @param mealPlan The meal plan to be deleted from the database.
	 */
	public void deleteMealPlan(MealPlan mealPlan) {
		this.db.delMealPlan(mealPlan, ((deleteMealPlan,
										deleteSuccess) -> {
			if (!deleteSuccess) {
				this.activity.makeSnackbar("Failed to delete" + deleteMealPlan.getTitle());
			} else {
				this.activity.makeSnackbar("Deleted " + deleteMealPlan.getTitle());
			}
		}));
	}
}
