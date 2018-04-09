package com.example.ndp.bakingapp.data.local;

import android.content.ContentResolver;
import android.util.Log;

import com.example.ndp.bakingapp.BakingApp;
import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.models.BakingSteps;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.utils.DbUtils;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalRecipeRepository implements RecipeRepository {
    private final static List EMPTY_LIST = new ArrayList(0);
    private static final String LOG_TAG = "_BAK_LocalRepo";

    @Override
    public List getRecipes() {
        ContentResolver contentResolver = BakingApp.getContext().getContentResolver();
        DbUtils dbUtils = new DbUtils();
        ArrayList<Recipe> recipes = dbUtils.convertRecipeCursorToList();
        if(ValidationUtils.isListEmptyOrNull(recipes)){
            Log.d(LOG_TAG , "List is empty in db");
            return EMPTY_LIST;
        }
        for (Recipe recipe : recipes){
            Log.d(LOG_TAG , "recipe id = "+String.valueOf(recipe.getId()));
            recipe.setSteps(dbUtils.convertStepCursorToList
                    (String.valueOf(recipe.getId())));
            recipe.setIngredients(dbUtils.convertIngredientCursorToList
                    (String.valueOf(recipe.getId())));
        }
        return recipes;
    }
}
