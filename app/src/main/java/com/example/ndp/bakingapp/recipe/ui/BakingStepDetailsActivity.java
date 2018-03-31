package com.example.ndp.bakingapp.recipe.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.BakingSteps;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;

import java.util.ArrayList;

public class BakingStepDetailsActivity extends AppCompatActivity {

    private static final String STEP_LIST_KEY =  "step_list_key";
    private static final String STEP_POSITION_KEY = "step_position_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_step_details);
        ArrayList<BakingSteps> steps  = getIntent().getParcelableArrayListExtra(STEP_LIST_KEY);
        int position = getIntent().getIntExtra(STEP_POSITION_KEY, 0);
        if(null == savedInstanceState){
            BakingStepsDetailFragment bakingStepsDetailFragment =
                    new BakingStepsDetailFragment();
            bakingStepsDetailFragment.setSteps(steps);
            bakingStepsDetailFragment.setPosition(position);
            FragmentManager fragmentManager =  getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.root_layout ,
                    bakingStepsDetailFragment , null)
                    .commit();
        }
    }
}
