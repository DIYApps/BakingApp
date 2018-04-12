package com.example.ndp.bakingapp.ui.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class IngredientsWidgetService extends RemoteViewsService {


     static final String LOG_TAG = "_BAK_WidgetService";
    private static final String RECIPE_ID_KEY = "recipe_id_key";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String recipeId =  intent.getData().getSchemeSpecificPart();
        Log.d(LOG_TAG , "onGetViewFactory recipe id "+ recipeId );
        return new IngredientWidgetViewFactory(this.getApplicationContext() ,
                recipeId);
    }
}
