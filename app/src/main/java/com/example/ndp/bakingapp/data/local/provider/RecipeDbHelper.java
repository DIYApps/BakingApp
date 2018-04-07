package com.example.ndp.bakingapp.data.local.provider;

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
        sqLiteDatabase.execSQL(RecipeContract.RecipeEntry.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(RecipeContract.IngredientEntry.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(RecipeContract.StepEntry.CREATE_TABLE_QUERY);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
