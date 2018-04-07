package com.example.ndp.bakingapp.ui.recipelist;

import com.example.ndp.bakingapp.data.models.Recipe;

import java.util.ArrayList;

public interface RecipeView {
    void onRecipeLoaded(ArrayList<Recipe> recipes);
    void onLoadingFailed();
    void onNoInternetConnection();
    void onShowProgressIndicator();
    void onHideProgressIndicator();
}
