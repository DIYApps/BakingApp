package com.example.ndp.bakingapp.ui.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.task.RecipeLoader;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

public class RecipePresenter {

    private static final int RECIPE_LOADER_ID = 101;
    private static final String LOG_TAG ="_BAK_MainPresenter" ;
    private final Context mContext;
    private final RecipeView recipeView;

    //inject the context and recipeView to presenter
    public RecipePresenter(Context mContext , RecipeView recipeView ) {
        this.mContext = mContext;
        this.recipeView = recipeView;
    }

    public void loadRecipe(boolean shouldRefresh){
        Log.d(LOG_TAG , "load Recipe is called with shouldRefresh "+ shouldRefresh);
        if(shouldRefresh){
            PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
            preferenceHelper.writeIsRepositorySyncedPref(false);
        }
        recipeView.onShowProgressIndicator();
        ((AppCompatActivity)mContext).getSupportLoaderManager()
                .restartLoader(RECIPE_LOADER_ID , null , loaderCallbacks);
    }

    //create a callback
    private final LoaderManager.LoaderCallbacks<ArrayList<Recipe>> loaderCallbacks = new
            LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
                @Override
                public Loader<ArrayList<Recipe>> onCreateLoader(int i, Bundle bundle) {
                    recipeView.onShowProgressIndicator();
                    return new RecipeLoader(mContext);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader,
                                           ArrayList<Recipe> recipes) {
                    Log.d(LOG_TAG , "onLoadFinished() called");
                    recipeView.onHideProgressIndicator();
                    if(ValidationUtils.isListEmptyOrNull(recipes)){
                        recipeView.onLoadingFailed();
                        return;
                    }

                    //call view methods
                    recipeView.onRecipeLoaded(recipes);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
                    recipeView.onHideProgressIndicator();
                }
            };

}
