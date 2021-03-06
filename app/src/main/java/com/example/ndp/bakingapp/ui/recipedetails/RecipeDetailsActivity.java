package com.example.ndp.bakingapp.ui.recipedetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.ui.stepdetails.BakingStepDetailsActivity;
import com.example.ndp.bakingapp.ui.stepdetails.BakingStepsDetailFragment;
import com.example.ndp.bakingapp.utils.ValidationUtils;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailFragment.FragmentInteraction {

    private static final String STEP_LIST_KEY =  "step_list_key";
    private static final String STEP_POSITION_KEY = "step_position_key";
    private static final String LOG_TAG = "_BAK_RecipeActivity";
    private static final String BACKSTACK_TAG = "fragment_backstack_tag" ;
    private boolean isTwoPlane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        isTwoPlane = findViewById(R.id.stepDetailsFragmentContainer) != null;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(LOG_TAG , "onNewIntent" + intent.getStringExtra("recipe_id_key"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.containerRecipeDetailsListFragment);
        if(null != fragment && fragment instanceof RecipeDetailFragment){
            ((RecipeDetailFragment)fragment).onNewIntent(intent);
        }
        setIntent(intent);
    }

    @Override
    public void onFragmentInteraction(Recipe recipe,int position) {

        if(isTwoPlane){
            BakingStepsDetailFragment bakingStepsDetailFragment =
                    new BakingStepsDetailFragment();
            bakingStepsDetailFragment.setSteps(recipe.getSteps());
            bakingStepsDetailFragment.setPosition(position);
            FragmentManager fragmentManager =  getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.stepDetailsFragmentContainer ,
                    bakingStepsDetailFragment , null)
                    .addToBackStack(BACKSTACK_TAG)
                    .commit();

        }else{
            Log.d(LOG_TAG , "clicked");
            Intent stepsDetailsActivityLauncherIntent = new Intent(this ,
                    BakingStepDetailsActivity.class);
            stepsDetailsActivityLauncherIntent.putParcelableArrayListExtra(STEP_LIST_KEY ,
                    recipe.getSteps());
            stepsDetailsActivityLauncherIntent.putExtra(STEP_POSITION_KEY , position);
            startActivity(stepsDetailsActivityLauncherIntent);
        }
    }

    @Override
    public void setTitle(String recipeName) {
        setActionBar(recipeName);
    }

    private void setActionBar(String recipeName){
        if(!ValidationUtils.isStringEmptyOrNull(recipeName)) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setSubtitle(recipeName);
                getSupportActionBar().setTitle("Ingredients and Steps");
                getSupportActionBar().setLogo(R.drawable.exo_controls_play);
            }
        }
    }

}
