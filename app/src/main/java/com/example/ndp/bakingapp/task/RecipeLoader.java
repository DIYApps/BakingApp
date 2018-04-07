package com.example.ndp.bakingapp.task;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.RecipeRepositoryFactory;
import com.example.ndp.bakingapp.data.models.Recipe;

import java.util.ArrayList;

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {


    private static final String LOG_TAG = "RecipeLoader" ;
    //create a empty list of recipe
    private ArrayList recipes =  new ArrayList<Recipe>();

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        //return cached data when available
        if(!recipes.isEmpty()){
            deliverResult(recipes);
            return;
        }

        //call to load data from network
        forceLoad();
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {

        RecipeRepository recipeRepository = RecipeRepositoryFactory.getRecipeRepository();
        return (ArrayList<Recipe>) recipeRepository.getRecipes();
    }

    @Override
    public void deliverResult(ArrayList<Recipe> data) {
        super.deliverResult(data);
        recipes = data;
    }
}
