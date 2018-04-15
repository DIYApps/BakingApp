package com.example.ndp.bakingapp.ui.widget;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViewsService;

public class IngredientsWidgetService extends RemoteViewsService {


     static final String LOG_TAG = "_BAK_WidgetService";
    private static final String RECIPE_ID_KEY = "recipe_id_key";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Uri uri = intent.getData();
        if(null != uri) {
            String recipeId =  uri.getSchemeSpecificPart();
            Log.d(LOG_TAG, "onGetViewFactory recipe id " + recipeId);
            return new IngredientWidgetViewFactory(this.getApplicationContext(),
                    recipeId);
        }
        else{
            Log.e(LOG_TAG ,"Uri is null");
            return null;
        }
    }
}
