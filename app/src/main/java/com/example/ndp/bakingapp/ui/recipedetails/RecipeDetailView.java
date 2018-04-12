package com.example.ndp.bakingapp.ui.recipedetails;

import com.example.ndp.bakingapp.data.models.Recipe;

public interface RecipeDetailView {

    void showRecipeDetails(Recipe recipe);
    void showErrorInLoadingRecipe();
}
