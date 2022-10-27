package com.example.wellfed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wellfed.mealplan.MealPlan;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealPlanFragment extends Fragment {
    private static final String ARG_MEAL_PLAN = "mealPlan";
    private TextView mealPlanTitleTextView;
    private TextView mealPlanNumberOfServingsTextView;

    private MealPlan mealPlan;

    public MealPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mealPlan Parameter 1
     * @return A new instance of fragment MealPlanFragment.
     */
    public static MealPlanFragment newInstance(MealPlan mealPlan) {
        MealPlanFragment fragment = new MealPlanFragment();
        Bundle args = new Bundle();
        args.putSerializable("mealPlan", mealPlan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealPlan = (MealPlan) getArguments().getSerializable(ARG_MEAL_PLAN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container,
                false);
        mealPlanTitleTextView = view.findViewById(R.id.mealPlanTitleTextView);
        mealPlanNumberOfServingsTextView = view.findViewById(
                R.id.mealPlanNumberOfServingsTextView
        );
        mealPlanTitleTextView.setText(mealPlan.getTitle());
        mealPlanNumberOfServingsTextView.setText(
                "Number of servings: " + mealPlan.getServings()
        );
        return view;
    }
}