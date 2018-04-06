package com.example.ndp.bakingapp.recipe.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.ndp.bakingapp.recipe.data.models.Ingredient;

import java.util.ArrayList;

public class IngredientsWidgetService extends RemoteViewsService {


     static final String LOG_TAG = "_BAK_WidgetService";
     private static ArrayList<Ingredient> ingredients;

    public static void setIngredients(ArrayList<Ingredient> ingredients) {
        IngredientsWidgetService.ingredients = ingredients;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_TAG , "onGetViewFactory");
        return new IngredientWidgetViewFactory(this.getApplicationContext() , ingredients);
    }
}
