package com.example.ndp.bakingapp.data;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.local.LocalRecipeRepository;
import com.example.ndp.bakingapp.data.remote.NetworkRecipeRepository;

public class RecipeRepositoryFactory {

    private RecipeRepositoryFactory() {
    }

    public static RecipeRepository getRecipeRepository(){
        PreferenceHelper preferenceHelper = new PreferenceHelper();
        boolean isRepositorySynced = preferenceHelper.readIsRepositorySyncedPref();
        if(isRepositorySynced){
            return new LocalRecipeRepository();
        }
        else{
            return new NetworkRecipeRepository();
        }
    }
}
