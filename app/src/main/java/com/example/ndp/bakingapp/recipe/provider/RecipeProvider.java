package com.example.ndp.bakingapp.recipe.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RecipeProvider extends ContentProvider{


    private static final String LOG_TAG = "_BAK_RecipeProvider::";
    private RecipeDbHelper mRecipeDbHelper;
    private static final int INGREDIENT =  100;
    private static final int INGREDIENT_WITH_ID =  101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.INGREDIENT_PATH, INGREDIENT);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.INGREDIENT_PATH + "/#",
                INGREDIENT_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeDbHelper = new RecipeDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();
        try{
            switch (match){
                case INGREDIENT:
                    cursor = db.query(RecipeContract.IngredientEntry.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                    break;
                case INGREDIENT_WITH_ID:
                    String recipeId =  uri.getPathSegments().get(1);
                    cursor = db.query(RecipeContract.IngredientEntry.TABLE_NAME,
                            null,
                            RecipeContract.IngredientEntry.RECIPE_ID +"=?",
                            new String[]{recipeId},
                            null,
                            null,
                            null);
                    break;
                    default:
            }
        }
        catch (Exception e){
            Log.d(LOG_TAG , "error "+ e.getLocalizedMessage());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the plants directory
        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned

        try {
            switch (match) {
                case INGREDIENT:
                    // Insert new values into the database
                    long id = db.insert(RecipeContract.IngredientEntry.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnUri = ContentUris.withAppendedId
                                (RecipeContract.IngredientEntry.CONTENT_URI, id);
                    }
                    break;
                // Default case throws an UnsupportedOperationException
                default:
            }
            // Notify the resolver if the uri has been changed, and return the newly inserted URI
            getContext().getContentResolver().notifyChange(uri, null);
        }catch (Exception e){
            Log.e(LOG_TAG , "error "+ e.getLocalizedMessage());
        }
        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(LOG_TAG , "delete() uri "+ uri.toString());
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();
        int deletedRowCount = 0;
        try{
            switch (match){
                case INGREDIENT:
                    deletedRowCount = db.delete(RecipeContract.IngredientEntry.TABLE_NAME,
                            null, null);
                    break;
                case INGREDIENT_WITH_ID:
                    String recipeId =  uri.getPathSegments().get(1);
                    deletedRowCount = db.delete(RecipeContract.IngredientEntry.TABLE_NAME,
                            RecipeContract.IngredientEntry.RECIPE_ID+"=?",
                            new String[]{recipeId});
                    break;
                default:
            }
            if(deletedRowCount > 0){
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        catch (Exception e){
            Log.d(LOG_TAG , "error "+ e.getLocalizedMessage());
        }
        Log.d(LOG_TAG , "delete() exited "+ deletedRowCount);
        return deletedRowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        Log.d(LOG_TAG , "bulkInsert() entered ");
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the plants directory
        int match = sUriMatcher.match(uri);
        int count= 0;

        try {
            switch (match) {
                case INGREDIENT:
                    // Insert new values into the database
                    for (int i = 0 ; i < values.length ;i++) {
                        long id = db.insert(RecipeContract.IngredientEntry.TABLE_NAME,
                                null,
                                values[i]);
                        if (id > 0) {
                            count++;
                        }
                    }
                    break;
                // Default case throws an UnsupportedOperationException
                default:
            }
            // Notify the resolver if the uri has been changed, and return the newly inserted URI
            getContext().getContentResolver().notifyChange(uri, null);
        }catch (Exception e){
            Log.d(LOG_TAG , "error "+ e.getLocalizedMessage());
        }
        Log.d(LOG_TAG , "bulkInsert() exited "+ count);
        return count;

    }
}
