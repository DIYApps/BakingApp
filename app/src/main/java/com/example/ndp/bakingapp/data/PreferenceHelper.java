package com.example.ndp.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ndp.bakingapp.BakingApp;

public class PreferenceHelper {

    private static final String IS_REPOSITORY_SYNCED_KEY = "is_repository_synced";
    private static final String BAKING_APP_PREF = "baking_app_pref";
    private SharedPreferences sharedPreferences;

    public PreferenceHelper() {
        Context context = BakingApp.getContext();
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


}
