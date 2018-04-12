package com.example.ndp.bakingapp.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.utils.DbUtils;

public class RecipeDetailLoader extends AsyncTaskLoader<Recipe> {

    private Recipe recipe;
    private final String recipeId;

    public RecipeDetailLoader(Context context, String recipeId) {
        super(context);
        this.recipeId = recipeId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(recipe != null){
            deliverResult(recipe);
            return;
        }
        forceLoad();
    }

    @Override
    public Recipe loadInBackground() {
        DbUtils dbUtils = DbUtils.getInstance();
        return dbUtils.convertCursorToRecipe(recipeId);
    }

    @Override
    protected void onReset() {
        super.onReset();
        stopLoading();
    }

    @Override
    public void deliverResult(Recipe data) {
        super.deliverResult(data);
        recipe = data;
    }
}
