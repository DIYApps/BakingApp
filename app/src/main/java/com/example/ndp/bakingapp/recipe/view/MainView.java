package com.example.ndp.bakingapp.recipe.view;

import com.example.ndp.bakingapp.recipe.data.models.Recipe;

import java.util.ArrayList;

public interface MainView {
    void onRecipeLoaded(ArrayList<Recipe> recipes);
    void onLoadingFailed();
    void onNoInternetConnection();
    void onShowProgressIndicator();
    void onHideProgressIndicator();
}
