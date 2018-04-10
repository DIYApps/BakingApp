package com.example.ndp.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ndp.bakingapp.BakingApp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provide helper methods for writing and reading values from shared
 * preference. This class is Singleton class.
 */
public class PreferenceHelper {

    private static final String IS_REPOSITORY_SYNCED_KEY = "is_repository_synced";
    private static final String BAKING_APP_PREF = "baking_app_pref";
    private static final String RECIPE_NAME_LIST_KEY = "recipe_name_list_key";
    private static final String WIDGET_RECIPE_KEY ="widget_recipe_key_" ;
    private static final String EMPTY_STRING = "";
    private SharedPreferences sharedPreferences;
    private static PreferenceHelper sPreferenceHelper;

    public static PreferenceHelper getInstance() {
        if(null == sPreferenceHelper){
            sPreferenceHelper = new PreferenceHelper();
        }
        return sPreferenceHelper;
    }


    //private construct for restrict the class to instantiate outside
    private  PreferenceHelper() {
        Context context = BakingApp.getContext();

        //get the Shared preference
        this.sharedPreferences = context.getSharedPreferences(BAKING_APP_PREF,
                Context.MODE_PRIVATE);
    }

    public void writeIsRepositorySyncedPref(boolean flag){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_REPOSITORY_SYNCED_KEY , flag);
        editor.apply();
    }

    public boolean readIsRepositorySyncedPref(){
        return sharedPreferences.getBoolean(IS_REPOSITORY_SYNCED_KEY , false);
    }

    public void storeRecipeNamesInSharedPreference(HashSet recipeNameList){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(RECIPE_NAME_LIST_KEY , recipeNameList);
        editor.apply();
    }

    public HashSet  getRecipeNamesFromSharedPreference(){
        return (HashSet) sharedPreferences.getStringSet(RECIPE_NAME_LIST_KEY,
                new HashSet<String>(0));
    }

    public void setAppWidgetRecipePreferences(int appWidgetId , String recipeString){
        String key = WIDGET_RECIPE_KEY + appWidgetId;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , recipeString);
        editor.apply();
    }

    public String getAppWidgetRecipePreferences(int appWidgetId){
        String key = WIDGET_RECIPE_KEY + appWidgetId;
        return  sharedPreferences.getString(key, EMPTY_STRING);
    }

    public void deleteAppWidgetRecipePreferences(int appWidgetId){
        String key = WIDGET_RECIPE_KEY + appWidgetId;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
