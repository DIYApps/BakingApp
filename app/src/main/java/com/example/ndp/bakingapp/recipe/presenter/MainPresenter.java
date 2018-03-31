package com.example.ndp.bakingapp.recipe.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.tasks.RecipeLoader;
import com.example.ndp.bakingapp.recipe.view.MainView;
import com.example.ndp.bakingapp.utils.NetworkUtils;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

public class MainPresenter {

    private static final int RECIPE_LOADER_ID = 101;
    private static final String LOG_TAG ="MainPresenter" ;
    private Context mContext;
    private MainView mainView;

    //inject the context and mainView to presenter
    public MainPresenter(Context mContext ,MainView mainView ) {
        this.mContext = mContext;
        this.mainView = mainView;
    }

    public void loadRecipe(){
        Log.d(LOG_TAG , "load Recipe is called");
        //check for internet connectivity
        if(NetworkUtils.checkConnectivity(mContext)){

            ((AppCompatActivity)mContext).getSupportLoaderManager()
                    .initLoader(RECIPE_LOADER_ID , null , loaderCallbacks);
        }
    }

    //create a callback
    LoaderManager.LoaderCallbacks<ArrayList<Recipe>> loaderCallbacks = new
            LoaderManager.LoaderCallbacks<ArrayList<Recipe>>() {
                @Override
                public Loader<ArrayList<Recipe>> onCreateLoader(int i, Bundle bundle) {
                    mainView.onShowProgressIndicator();
                    return new RecipeLoader(mContext);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader,
                                           ArrayList<Recipe> recipes) {
                    Log.d(LOG_TAG , "onLoadFinished() called");
                    mainView.onHideProgressIndicator();
                    if(ValidationUtils.isListEmptyOrNull(recipes)){
                        mainView.onLoadingFailed();
                        return;
                    }

                    //call view methods
                    mainView.onRecipeLoaded(recipes);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {
                    mainView.onHideProgressIndicator();
                }
            };

}
