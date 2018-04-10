package com.example.ndp.bakingapp.task;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.RecipeRepositoryFactory;
import com.example.ndp.bakingapp.data.models.Recipe;

import java.util.ArrayList;

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {


    private static final String LOG_TAG = "_BAK_RecipeLoader" ;
    //create a empty list of recipe
    private ArrayList recipes =  new ArrayList<Recipe>();

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        boolean isRepoSynced = PreferenceHelper.getInstance().readIsRepositorySyncedPref();

        Log.d(LOG_TAG ,"onStartLoading() started with " + recipes .size() +" && "+ isRepoSynced);
        Log.d(LOG_TAG ,"onStartLoading() started with " + (!recipes .isEmpty() && isRepoSynced));
        //return cached data when available and shouldRefresh is false.
        if(!recipes.isEmpty() && isRepoSynced){
            deliverResult(recipes);
            return;
        }
        Log.d(LOG_TAG ,"onStartLoading() syncing data with server" );
        //call to load data from network
        forceLoad();
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {

        RecipeRepository recipeRepository = RecipeRepositoryFactory.getRecipeRepository();
        return (ArrayList<Recipe>) recipeRepository.getRecipes();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d(LOG_TAG , "onForceLoad()::called");
    }

    @Override
    public void deliverResult(ArrayList<Recipe> data) {
        super.deliverResult(data);
        recipes = data;
    }
}
