package com.example.ndp.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.local.provider.RecipeDbHelper;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.example.ndp.bakingapp.ui.recipelist.RecipeActivity;
import com.example.ndp.bakingapp.utils.DbUtils;
import com.example.ndp.bakingapp.utils.ValidationUtils;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String RECIPE_ID_KEY = "ingredients_key";
    private static final String RECIPE_NAME_KEY = "recipe_name_key";
    private static final String LOG_TAG = "_BAK_BakingAppWidget";
    private static final String RECIPE_KEY = "recipe_key";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId ) {

        Log.d(LOG_TAG , "updateAppWidget()::1");
        //read the data from shared preference to display ingredients
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
        String recipeString = preferenceHelper.getAppWidgetRecipePreferences(appWidgetId);
        if(ValidationUtils.isStringEmptyOrNull(recipeString)){
            Log.e(LOG_TAG , "recipeString is null");
            return;
        }
        //split the csv value
        String string[]  = recipeString.split(",");

        RemoteViews views = createIngredientList(context,appWidgetId, string[0], string[1]);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(LOG_TAG ,"onUpdate():");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews createIngredientList(Context context , int appWidgetId ,String recipeId,
                                             String recipeName){
        Log.d(LOG_TAG , "createIngredientList():: "+ recipeName);
        Log.d(LOG_TAG , "createIngredientList():: "+ recipeId);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        rv.setTextViewText(R.id.appwidget_recipe_name, recipeName);


        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(RECIPE_ID_KEY , recipeId);
        rv.setRemoteAdapter(appWidgetId , R.id.widget_list_view, intent);
        rv.setEmptyView(R.id.widget_list_view, R.id.empty_widget_view);

        DbUtils dbUtils = DbUtils.getInstance();
        Recipe recipe = dbUtils.convertCursorToRecipe(recipeId);

        Log.d(LOG_TAG ,"updateAppWidget()::recipeName"+ recipe == null ? "Null return" :
                recipe.getName());
        Log.d(LOG_TAG , "updateAppWidget()::--Stepsize"+recipe.getSteps() == null ? "0" :
                recipe.getSteps().size()+"");
        rv.setOnClickPendingIntent(R.id.appwidget_recipe_name , createPendingIntent(context ,
                recipe));

        Log.d(LOG_TAG , "createIngredientList():: Exit");
        return rv;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG , "onDeleted():: Enter");
        //delete the stored recipe preference for the destroyed widget.
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();

        for (int appWidgetId : appWidgetIds) {
            Log.d(LOG_TAG , "onDeleted():: appWidgetId"+ appWidgetId);
            preferenceHelper.deleteAppWidgetRecipePreferences(appWidgetId);
        }
        Log.d(LOG_TAG , "onDeleted():: Exit");
    }

    private static PendingIntent createPendingIntent(Context context , Recipe recipe){

        Log.d(LOG_TAG ,"createPendingIntent:::" );
        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE_KEY , recipe);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}

