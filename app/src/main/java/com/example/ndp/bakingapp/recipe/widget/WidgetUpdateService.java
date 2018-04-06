package com.example.ndp.bakingapp.recipe.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ndp.bakingapp.recipe.data.models.Ingredient;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;

import java.util.ArrayList;

public class WidgetUpdateService extends IntentService {


    private static final String LOG_TAG = "_BAK_UpdateService";
    public static final String ACTION_DISPLAY_INGREDIENTS_IN_WIDGET = "com.example.ndp.bakingapp.recipe.widget.display_ingredients";
    private static final String INGREDIENTS_KEY = "ingredients_key";
    private static final String RECIPE_NAME_KEY = "recipe_name_key";

    public WidgetUpdateService() {
        super(LOG_TAG);
    }

    public WidgetUpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG , "onHandleIntent()::Entry");
        String action =  intent.getAction();
        if(action.equals(ACTION_DISPLAY_INGREDIENTS_IN_WIDGET)){
            ArrayList<Ingredient> ingredients = intent.getParcelableArrayListExtra(INGREDIENTS_KEY);
            String recipeName = intent.getStringExtra(RECIPE_NAME_KEY);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
            //Now update all widgets
            BakingAppWidget.onUpdateIngredientWidgets(getApplicationContext(),
                    appWidgetManager,
                    appWidgetIds,
                    ingredients,
                    recipeName );
            Log.d(LOG_TAG , "onHandleIntent()::Exit");
        }
    }

    public static void startDisplayIngredientsService(Context context,String recipeName,
                                                      ArrayList<Ingredient> ingredients) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_DISPLAY_INGREDIENTS_IN_WIDGET);
        intent.putParcelableArrayListExtra(INGREDIENTS_KEY, ingredients);
        intent.putExtra(RECIPE_NAME_KEY, recipeName);
        context.startService(intent);
    }
}
