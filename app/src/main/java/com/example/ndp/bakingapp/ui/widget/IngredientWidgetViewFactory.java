package com.example.ndp.bakingapp.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ndp.bakingapp.R;

import static com.example.ndp.bakingapp.data.local.provider.RecipeContract.IngredientEntry;

public class IngredientWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = "_BAK_ViewFactory" ;
    private final Context mContext;
    private final String mRecipeId;
    private Cursor mCursor;

    public IngredientWidgetViewFactory(Context context, String recipeId) {
        this.mContext = context;
        this.mRecipeId = recipeId;
        Log.d(LOG_TAG, "mRecipeId"+mRecipeId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri uri  = IngredientEntry.CONTENT_URI.buildUpon()
                .appendPath(mRecipeId).build();
        Log.d(LOG_TAG , "URI --> "+ uri);
        if(mCursor !=null){
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(uri, null , null ,
                null, null);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG , "OnDestroyed");

    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0){ return null;}
        mCursor.moveToPosition(position);

        int ingredientNameIndex = mCursor.getColumnIndex(IngredientEntry.INGREDIENT_NAME);
        int quantityIndex = mCursor.getColumnIndex(IngredientEntry.QUANTITY);
        int measureIndex = mCursor.getColumnIndex(IngredientEntry.MEASURE);
        String name = mCursor.getString(ingredientNameIndex);
        String measure = mCursor.getString(measureIndex);
        String quantity = mCursor.getString(quantityIndex);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName() ,
                R.layout.widget_ingredient_layout);

        remoteViews.setTextViewText(R.id.textView_widget_ingredient_name, name);
        String quantityString = measure + " "+quantity;
        Log.d(LOG_TAG , "getViewAt() " + name +","+quantityString);
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
