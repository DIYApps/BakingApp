package com.example.ndp.bakingapp.recipe.widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.models.Ingredient;

import java.util.ArrayList;

public class IngredientWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = "_BAK_ViewFactory" ;
    private Context mContex;
    private ArrayList<Ingredient> ingredients;

    public IngredientWidgetViewFactory(Context mContex, ArrayList<Ingredient> ingredients) {
        this.mContex = mContex;
        this.ingredients = ingredients;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG , "OnDestroyed");

    }

    @Override
    public int getCount() {
        return ingredients == null ? 0 :ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContex.getPackageName() ,
                R.layout.widget_ingredient_layout);
        Ingredient ingredient = ingredients.get(position);
        remoteViews.setTextViewText(R.id.textView_widget_ingredient_name ,
                ingredient.getIngredient());
        String quantityString = ingredient.getMeasure() + " "+ingredient.getQuantity();
        remoteViews.setTextViewText(R.id.textView_widget_ingredient_quantity , quantityString);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
