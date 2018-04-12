package com.example.ndp.bakingapp.ui.recipedetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.task.RecipeDetailLoader;

public class RecipeDetailPresenter {

    private static final String LOG_TAG = "_BAK_DetailsPresenter";
    private static final String EXTRA_RECIPE_ID = "recipe_id_key" ;
    private static final int RECIPE_DETAILS_LOADER_ID = 100;
    private final RecipeDetailView mRecipeDetailView;
    private final Context mContext;

    public RecipeDetailPresenter(RecipeDetailView mRecipeDetailView, Context mContext) {
        this.mRecipeDetailView = mRecipeDetailView;
        this.mContext = mContext;
    }

    public void loadRecipeDetails(Recipe recipe , String recipeId){
        //check if recipe is not null then set the view with the adapter
        if(recipe != null){
            Log.d(LOG_TAG , "loadRecipeDetails() recipe already exists");
            mRecipeDetailView.showRecipeDetails(recipe);
            return;
        }

        //create a bundle to pass recipe id
        Bundle recipeIdBundle = new Bundle();
        recipeIdBundle.putString(EXTRA_RECIPE_ID , recipeId);
        ((AppCompatActivity)mContext).getSupportLoaderManager()
                .restartLoader(RECIPE_DETAILS_LOADER_ID,
                        recipeIdBundle,
                        mLoaderCallbacks);

    }

    //callback
    private final LoaderManager.LoaderCallbacks<Recipe> mLoaderCallbacks = new
            LoaderManager.LoaderCallbacks<Recipe>() {
                @Override
                public Loader<Recipe> onCreateLoader(int i, Bundle bundle) {
                    return new RecipeDetailLoader(mContext , bundle.getString(EXTRA_RECIPE_ID));
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Recipe> loader,
                                           Recipe recipe) {
                    Log.d(LOG_TAG , "onLoadFinished()");
                    if(recipe != null) {
                        mRecipeDetailView.showRecipeDetails(recipe);
                    }else {
                        mRecipeDetailView.showErrorInLoadingRecipe();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Recipe> loader) {

                }
            };

}
