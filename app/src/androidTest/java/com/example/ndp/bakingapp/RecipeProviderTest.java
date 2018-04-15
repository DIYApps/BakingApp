package com.example.ndp.bakingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.test.ProviderTestCase2;
import android.util.Log;
import com.example.ndp.bakingapp.data.local.provider.RecipeContract;
import com.example.ndp.bakingapp.data.local.provider.RecipeProvider;

import org.junit.Test;


public class RecipeProviderTest extends ProviderTestCase2<RecipeProvider> {

    private final static String LOG_TAG = "_bak_RecipeProviderTest::";
    private ContentResolver contentResolver;

    public RecipeProviderTest() {
        super(RecipeProvider.class, RecipeContract.AUTHORITY);
        Log.d(LOG_TAG , "RecipeProviderTest_");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(LOG_TAG , "setUp()");
        setContext(InstrumentationRegistry.getTargetContext());
        contentResolver = getMockContentResolver();
    }

    @Test
    public void testRecipeQuery() {
        Log.d(LOG_TAG , "testRecipeQuery()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeEntry.IMAGE , "some value");
        contentValues.put(RecipeContract.RecipeEntry.SERVINGS , 8);
        contentValues.put(RecipeContract.RecipeEntry.RECIPE_ID , 1);
        contentValues.put(RecipeContract.RecipeEntry.NAME , "Brownies");

        Uri uri = contentResolver.insert(RecipeContract.RecipeEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Cursor cursor = contentResolver.query(uri,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1 ,cursor.getCount());
        Log.e(LOG_TAG , "testRecipeQuery()"+cursor.getCount());
    }

    public void testInsertRecipe() {
        Log.d(LOG_TAG , "testInsertRecipe()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.RecipeEntry.IMAGE , "some value");
        contentValues.put(RecipeContract.RecipeEntry.SERVINGS , 8);
        contentValues.put(RecipeContract.RecipeEntry.RECIPE_ID , 1);
        contentValues.put(RecipeContract.RecipeEntry.NAME , "Brownies");

        Uri uri = contentResolver.insert(RecipeContract.RecipeEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Log.e(LOG_TAG , "testInsertRecipe()"+uri);
    }

    public void testInsertSteps() {
        Log.d(LOG_TAG , "testInsertSteps()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.StepEntry.RECIPE_ID , "1");
        contentValues.put(RecipeContract.StepEntry.SHORT_DESCRIPTION , "Dummy step");
        contentValues.put(RecipeContract.StepEntry.DESCRIPTION , "Dummy step which create recipe.");
        contentValues.put(RecipeContract.StepEntry.THUMBNAIL_URL , "Some image");
        contentValues.put(RecipeContract.StepEntry.VIDEO_URL , "Video image");

        Uri uri = contentResolver.insert(RecipeContract.StepEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Log.e(LOG_TAG , "testInsertSteps()"+uri);
    }

    public void testInsertIngredients() {
        Log.d(LOG_TAG , "testInsertIngredients()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.IngredientEntry.RECIPE_ID , "1");
        contentValues.put(RecipeContract.IngredientEntry.MEASURE , "tsp");
        contentValues.put(RecipeContract.IngredientEntry.QUANTITY , "2");
        contentValues.put(RecipeContract.IngredientEntry.INGREDIENT_NAME , "Sugar");

        Uri uri = contentResolver.insert(RecipeContract.IngredientEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Log.e(LOG_TAG , "testInsertIngredients()"+uri);
    }

    public void testQueryIngredients() {
        Log.d(LOG_TAG , "testQueryIngredients()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.IngredientEntry.RECIPE_ID , "1");
        contentValues.put(RecipeContract.IngredientEntry.MEASURE , "tsp");
        contentValues.put(RecipeContract.IngredientEntry.QUANTITY , "2");
        contentValues.put(RecipeContract.IngredientEntry.INGREDIENT_NAME , "Sugar");

        Uri uri = contentResolver.insert(RecipeContract.IngredientEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Cursor cursor = contentResolver.query(uri,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1 ,cursor.getCount());

        Log.e(LOG_TAG , "testQueryIngredients()"+uri);
    }

    public void testQuerySteps() {
        Log.d(LOG_TAG , "testQuerySteps()");

        assertNotNull(contentResolver);
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeContract.StepEntry.RECIPE_ID , "1");
        contentValues.put(RecipeContract.StepEntry.SHORT_DESCRIPTION , "Dummy step");
        contentValues.put(RecipeContract.StepEntry.DESCRIPTION , "Dummy step which create recipe.");
        contentValues.put(RecipeContract.StepEntry.THUMBNAIL_URL , "Some image");
        contentValues.put(RecipeContract.StepEntry.VIDEO_URL , "Video image");

        Uri uri = contentResolver.insert(RecipeContract.StepEntry.CONTENT_URI,
                contentValues);

        assertNotNull(uri);
        Cursor cursor = contentResolver.query(uri,
                null,
                null,
                null,
                null);

        assertNotNull(cursor);
        assertEquals(1 ,cursor.getCount());
        Log.e(LOG_TAG , "testQuerySteps()"+uri);
    }
}
