package com.example.ndp.bakingapp.data.local.provider;

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
    public static final String RECIPE_PATH = "recipe";
    public static final String STEP_PATH = "step";


    public static class RecipeEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();
        public static final String RECIPE_ID = "id";
        public static final String TABLE_NAME = "recipe";
        public static final String NAME =  "name";
        public static final String IMAGE = "image";
        public static final String SERVINGS = "servings";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+RecipeContract.RecipeEntry.TABLE_NAME +"("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RECIPE_ID + " TEXT NOT NULL UNIQUE, "
                + NAME +" TEXT, "
                + IMAGE + " TEXT, "
                + SERVINGS + " INTEGER NOT NULL);";

    }

    public static class IngredientEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(INGREDIENT_PATH).build();

        public static final String TABLE_NAME =  "ingredient";
        public static final String QUANTITY =  "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT_NAME = "ingredient_name";
        public static final String RECIPE_ID = "recipe_id";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+RecipeContract.IngredientEntry.TABLE_NAME +"("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INGREDIENT_NAME + " TEXT NOT NULL, "
                + QUANTITY +" TEXT, "
                + MEASURE + " TEXT, "
                + RECIPE_ID + " TEXT NOT NULL);";
    }

    public static class StepEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(STEP_PATH).build();

        public static final String TABLE_NAME =  "step";
        public static final String RECIPE_ID = "recipe_id";
        public static final String  SHORT_DESCRIPTION = "shortDescription";
        public static final String  DESCRIPTION ="description";
        public static final String  VIDEO_URL  ="videoURL";
        public static final String  THUMBNAIL_URL = "thumbnailURL";

        public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+TABLE_NAME +"("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SHORT_DESCRIPTION + " TEXT NOT NULL, "
                + DESCRIPTION +" TEXT, "
                + VIDEO_URL + " TEXT, "
                + THUMBNAIL_URL + " TEXT, "
                + RECIPE_ID + " TEXT NOT NULL);";

    }
}
