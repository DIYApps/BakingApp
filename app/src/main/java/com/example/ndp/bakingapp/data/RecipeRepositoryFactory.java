package com.example.ndp.bakingapp.data;

import android.util.Log;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.local.LocalRecipeRepository;
import com.example.ndp.bakingapp.data.remote.NetworkRecipeRepository;

public class RecipeRepositoryFactory {

    private static final String LOG_TAG = "_BAK_RepoFactory";

    private RecipeRepositoryFactory() {
    }

    public static RecipeRepository getRecipeRepository(){
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
        boolean isRepositorySynced = preferenceHelper.readIsRepositorySyncedPref();
        if(isRepositorySynced){
            Log.d(LOG_TAG , "Returning Local Repository");
            return new LocalRecipeRepository();
        }
        else{
            Log.d(LOG_TAG , "Returning Local Repository");
            return new NetworkRecipeRepository();
        }
    }
}
