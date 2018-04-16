package com.example.ndp.bakingapp.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.ndp.bakingapp.BakingApp;
import com.example.ndp.bakingapp.data.local.provider.RecipeContract;
import com.example.ndp.bakingapp.data.models.BakingSteps;
import com.example.ndp.bakingapp.data.models.Ingredient;
import com.example.ndp.bakingapp.data.models.Recipe;

import java.util.ArrayList;

public class DbUtils {

    private static final String LOG_TAG = "_BAK_DbUtils";
    private final ContentResolver contentResolver;

    private static DbUtils sDbUtils;

    private DbUtils() {
        this.contentResolver = BakingApp.getContext().getContentResolver();
    }

    public static DbUtils getInstance(){
        if(null == sDbUtils){
            sDbUtils = new DbUtils();
        }
        return sDbUtils;
    }


    public void insertAllIngredientsToDb(Recipe recipe){
        Log.d(LOG_TAG , "inserting ingredients with recipeID "+ recipe.getId());
        ContentValues[] values = new ContentValues[recipe.getIngredients().size()];
        int position = 0;
        for(Ingredient ingredient : recipe.getIngredients()){
            ContentValues value =  new ContentValues();
            value.put(RecipeContract.IngredientEntry.INGREDIENT_NAME , ingredient.getIngredient());
            value.put(RecipeContract.IngredientEntry.MEASURE , ingredient.getMeasure());
            value.put(RecipeContract.IngredientEntry.RECIPE_ID , String.valueOf(recipe.getId()));
            value.put(RecipeContract.IngredientEntry.QUANTITY , String.valueOf
                    (ingredient.getQuantity()));
            values[position] = value;
            position++;
        }
        int count = contentResolver.bulkInsert(RecipeContract.IngredientEntry.CONTENT_URI , values);
        Log.d(LOG_TAG , "Ingredient Inserted count::"+ count);
    }

    public void insertAllStepsToDb( Recipe recipe){
        Log.d(LOG_TAG , "inserting steps with recipeID "+ recipe.getId());
        ContentValues[] values = new ContentValues[recipe.getSteps().size()];
        int position = 0;
        for(BakingSteps step : recipe.getSteps()){
            ContentValues value =  new ContentValues();
            value.put(RecipeContract.StepEntry.VIDEO_URL , step.getVideoURL());
            value.put(RecipeContract.StepEntry.THUMBNAIL_URL , step.getThumbnailURL());
            value.put(RecipeContract.StepEntry.DESCRIPTION , step.getDescription());
            value.put(RecipeContract.StepEntry.SHORT_DESCRIPTION , step.getShortDescription());
            value.put(RecipeContract.StepEntry.RECIPE_ID , String.valueOf(recipe.getId()));
            values[position] = value;
            position++;
        }
        int count = contentResolver.bulkInsert(RecipeContract.StepEntry.CONTENT_URI , values);
        Log.d(LOG_TAG , "Steps Inserted count::"+ count);
    }

    public void insertAllRecipeToDb(ArrayList<Recipe> recipes){
        deleteAllIngredientFromDb();
        deleteAllRecipeFromDb();
        deleteAllStepFromDb();
        ContentValues[] values = new ContentValues[recipes.size()];
        int position = 0;
        for(Recipe recipe : recipes){
            ContentValues value =  new ContentValues();
            value.put(RecipeContract.RecipeEntry.NAME , recipe.getName());
            value.put(RecipeContract.RecipeEntry.RECIPE_ID , recipe.getId());
            value.put(RecipeContract.RecipeEntry.SERVINGS , recipe.getServings());
            value.put(RecipeContract.RecipeEntry.IMAGE , recipe.getServings());
            values[position] = value;
            insertAllIngredientsToDb(recipe);
            insertAllStepsToDb(recipe);
            position++;
        }
        int count = contentResolver.bulkInsert(RecipeContract.RecipeEntry.CONTENT_URI , values);
        Log.d(LOG_TAG , "Recipe Inserted count::"+ count);
    }

    public void deleteAllIngredientFromDb(){
        int deletedRow = contentResolver.delete(RecipeContract.IngredientEntry.CONTENT_URI , null ,
                null);
        Log.d(LOG_TAG , "deleteAllIngredientFromDb :: count "+ deletedRow);
    }

    public void deleteAllStepFromDb(){
        int deletedRow = contentResolver.delete(RecipeContract.StepEntry.CONTENT_URI , null ,
                null);
        Log.d(LOG_TAG , "deleteAllStepFromDb :: count "+ deletedRow);
    }

    public void deleteAllRecipeFromDb(){
        int deletedRow = contentResolver.delete(RecipeContract.RecipeEntry.CONTENT_URI , null ,
                null);
        Log.d(LOG_TAG , "deleteAllRecipeFromDb :: count "+ deletedRow);
    }

    public ArrayList<BakingSteps> convertStepCursorToList(String recipeId){
        ArrayList<BakingSteps> steps = new ArrayList<>();
        Cursor cursor = getDetailsCursorFromProvider(RecipeContract.StepEntry.CONTENT_URI
                .buildUpon().appendPath(recipeId).build());
        if(cursor == null || cursor.getCount() == 0){
            Log.e(LOG_TAG , "convertStepCursorToList():: cursor is empty");
            return steps;
        }

        int stepsCount = 0;
        while(cursor.moveToNext()){
            int descriptionIndex =cursor.getColumnIndex(RecipeContract.StepEntry.DESCRIPTION);
            int shortDescriptionIndex =cursor.getColumnIndex(RecipeContract.StepEntry.SHORT_DESCRIPTION);
            int thumbnailIndex = cursor.getColumnIndex(RecipeContract.StepEntry.THUMBNAIL_URL);
            int videoUrl = cursor.getColumnIndex(RecipeContract.StepEntry.VIDEO_URL);


            //create the BakingStep object
            BakingSteps bakingSteps = new BakingSteps();
            bakingSteps.setDescription(cursor.getString(descriptionIndex));
            bakingSteps.setShortDescription(cursor.getString(shortDescriptionIndex));
            bakingSteps.setVideoURL(cursor.getString(videoUrl));
            bakingSteps.setId(++stepsCount);
            bakingSteps.setThumbnailURL(cursor.getString(thumbnailIndex));
            steps.add(bakingSteps);
        }

        Log.d(LOG_TAG, "Step count "+ stepsCount);
        cursor.close();
        return steps;
    }

    public ArrayList<Ingredient> convertIngredientCursorToList(String recipeId){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = getDetailsCursorFromProvider(RecipeContract.IngredientEntry.CONTENT_URI
                .buildUpon().appendPath(recipeId).build());
        if(cursor == null || cursor.getCount() == 0){
            Log.e(LOG_TAG , "convertIngredientCursorToList():: cursor is empty");
            return ingredients;
        }

        while(cursor.moveToNext()){
            int ingredientNameIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.INGREDIENT_NAME);
            int quantityIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.QUANTITY);
            int measureIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.MEASURE);
            String name = cursor.getString(ingredientNameIndex);
            String measure = cursor.getString(measureIndex);
            String quantity = cursor.getString(quantityIndex);

            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(name);
            ingredient.setMeasure(measure);
            ingredient.setQuantity(Float.valueOf(quantity));
            ingredients.add(ingredient);
        }

        cursor.close();
        return ingredients;
    }

    public ArrayList<Recipe> convertRecipeCursorToList(){

        ArrayList<Recipe> recipes = new ArrayList<>();
        Cursor cursor = getDetailsCursorFromProvider(RecipeContract.RecipeEntry.CONTENT_URI);
        if(cursor == null || cursor.getCount() == 0){
            Log.e(LOG_TAG , "convertRecipeCursorToList():: cursor is empty");
            return recipes;
        }

        while(cursor.moveToNext()){
            int recipeNameIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.NAME);
            int imageIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.IMAGE);
            int servingIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.SERVINGS);
            int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_ID);
            String name = cursor.getString(recipeNameIndex);
            String image = cursor.getString(imageIndex);
            int servings = cursor.getInt(servingIndex);
            int id = cursor.getInt(idIndex);
            Log.d(LOG_TAG , "fetching recipe with id = "+ id);
            //create recipe and add to the list
            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setImage(image);
            recipe.setServings(servings);
            recipe.setId(id);
            recipes.add(recipe);
        }

        cursor.close();
        return recipes;
    }

    public Recipe convertCursorToRecipe(String recipeId){
        Uri uri  = RecipeContract.RecipeEntry.CONTENT_URI.buildUpon()
                .appendPath(recipeId).build();
        Cursor cursor = getDetailsCursorFromProvider(uri);
        if(cursor == null || cursor.getCount() == 0){
            Log.e(LOG_TAG , "convertCursorToRecipe():: cursor is empty");
            return null;
        }
        cursor.moveToNext();

        int recipeNameIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.NAME);
        int imageIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.IMAGE);
        int servingIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.SERVINGS);
        int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_ID);
        String name = cursor.getString(recipeNameIndex);
        String image = cursor.getString(imageIndex);
        int servings = cursor.getInt(servingIndex);
        int id = cursor.getInt(idIndex);
        Log.d(LOG_TAG , "convertCursorToRecipe()::fetching recipe with id = "+ id);
        //create recipe and add to the list
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setImage(image);
        recipe.setServings(servings);
        recipe.setId(id);
        cursor.close();
        recipe.setIngredients(convertIngredientCursorToList(recipeId));
        recipe.setSteps(convertStepCursorToList(recipeId));
        Log.d(LOG_TAG , "convertCursorToRecipe()::fetching recipe with id = "+ id);
        return recipe;
    }

    public Cursor getDetailsCursorFromProvider(Uri uri){
        Log.d(LOG_TAG , "getDetailsCursorFromProvider():: URI ::"+uri);
        return contentResolver.query(uri , null,
                null,
                null,
                null);
    }
}
