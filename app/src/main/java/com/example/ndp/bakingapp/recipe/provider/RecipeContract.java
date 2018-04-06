package com.example.ndp.bakingapp.recipe.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    // create the authority
    public static final String AUTHORITY = "com.example.ndp.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "plants" directory
    public static final String INGREDIENT_PATH = "ingredient";


    public static class Recipe{

    }

    public static class IngredientEntry implements BaseColumns{

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(INGREDIENT_PATH).build();

        public static final String TABLE_NAME =  "ingredient";
        public static final String QUANTITY =  "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT_NAME = "ingredient_name";
        public static final String RECIPE_ID = "recipe_id";
    }

    public static class Step{

    }
}
