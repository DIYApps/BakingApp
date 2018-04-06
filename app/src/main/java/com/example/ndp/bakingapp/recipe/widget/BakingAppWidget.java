package com.example.ndp.bakingapp.recipe.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String RECIPE_ID_KEY = "ingredients_key";
    private static final String RECIPE_NAME_KEY = "recipe_name_key";
    private static final String LOG_TAG = "_BAK_BakingAppWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId , String recipeId,
                                String recipeName) {


        Log.d(LOG_TAG , "updateAppWidget()::");
        RemoteViews views = createIngredientList(context,appWidgetId, recipeId, recipeName);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId ) {

        RemoteViews views = null;
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    public static void onUpdateIngredientWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds ,String recipeId,
                                          String recipeName ) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId , recipeId , recipeName);
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
//        intent.putExtra(INGREDIENTS_KEY , "ingredients");
        intent.putExtra(RECIPE_ID_KEY , recipeId);
        rv.setRemoteAdapter(appWidgetId , R.id.widget_list_view, intent);
        rv.setEmptyView(R.id.widget_list_view, R.id.empty_widget_view);
        Log.d(LOG_TAG , "createIngredientList():: Exit");
        return rv;
    }
}

