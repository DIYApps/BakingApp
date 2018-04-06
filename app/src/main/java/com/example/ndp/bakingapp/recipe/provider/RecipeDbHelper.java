package com.example.ndp.bakingapp.recipe.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "shushme.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = "_BAK_RecipeDbHelper";

    public RecipeDbHelper(Context context) {
        super(context ,DATABASE_NAME , null , DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG , "onCreate()::");
        final String CREATE_TABLE_QUERY = "CREATE TABLE "+RecipeContract.IngredientEntry.TABLE_NAME +"("
                + RecipeContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipeContract.IngredientEntry.INGREDIENT_NAME + " TEXT NOT NULL, "
                + RecipeContract.IngredientEntry.QUANTITY +" TEXT, "
                + RecipeContract.IngredientEntry.MEASURE + " TEXT, "
                + RecipeContract.IngredientEntry.RECIPE_ID + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
