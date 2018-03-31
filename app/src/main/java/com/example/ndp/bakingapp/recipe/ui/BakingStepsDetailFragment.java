package com.example.ndp.bakingapp.recipe.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.BakingSteps;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BakingStepsDetailFragment extends Fragment {



    private ArrayList<BakingSteps> steps;
    private int position;
    private String STEPS_POSITION_KEY = "steps_position_key";
    private String STEPS_LIST_KEY = "steps_list_key";


    public BakingStepsDetailFragment() {
        // Required empty public constructor
    }

    public void setSteps(ArrayList<BakingSteps> steps) {
        this.steps = steps;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    TextView textViewStepDescription;

    ImageButton imageButtonNextStep;

    ImageButton imageButtonPreviousStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baking_steps_detail, container,
                false);
        ButterKnife.bind(this, view);
        textViewStepDescription = view.findViewById(R.id.textViewStepDescription);
        imageButtonNextStep = view.findViewById(R.id.imageButtonNextStep);
        imageButtonPreviousStep = view.findViewById(R.id.imageButtonPreviousStep);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            steps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            position = savedInstanceState.getInt(STEPS_POSITION_KEY);
        }

        if(!ValidationUtils.isListEmptyOrNull(steps)){
            BakingSteps bakingStep =  steps.get(position);
            if(null != textViewStepDescription){
                textViewStepDescription.setText(bakingStep.getDescription());
            }
        }
        else{
            //Log the details
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(steps != null) {
            outState.putParcelableArrayList(STEPS_LIST_KEY, steps);
            outState.putInt(STEPS_POSITION_KEY, position);
        }
    }
}
