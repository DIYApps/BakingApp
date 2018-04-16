package com.example.ndp.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ndp.bakingapp.R;

public class WidgetUpdateService extends IntentService {


    private static final String LOG_TAG = "_BAK_UpdateService";
    public static final String ACTION_DISPLAY_INGREDIENTS_IN_WIDGET = "com.example.ndp.bakingapp.recipe.widget.display_ingredients";
//    private static final String RECIPE_ID_KEY = "ingredients_key";
//    private static final String RECIPE_NAME_KEY = "recipe_name_key";
    private static final String APP_WIDGET_ID_KEY = "app_widget_id_key" ;

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
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = null;
            int appWidgetId = 0;
            if (intent.hasExtra(APP_WIDGET_ID_KEY)) {
                appWidgetId = intent.getIntExtra(APP_WIDGET_ID_KEY, 0);
                appWidgetIds = new int[]{appWidgetId};
            }else{

            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

            //Now update all widgets
            BakingAppWidget.updateAppWidget(getApplicationContext(),
                    appWidgetManager,
                    appWidgetId);
            Log.d(LOG_TAG , "onHandleIntent()::Exit");
        }
    }

    public static void startDisplayIngredientsService(Context context,int appWidgetId) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_DISPLAY_INGREDIENTS_IN_WIDGET);
        intent.putExtra(APP_WIDGET_ID_KEY, appWidgetId);
        context.startService(intent);
    }
}
