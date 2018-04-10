package com.example.ndp.bakingapp.data.remote;

import android.util.Log;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.RecipeRepository;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.data.models.RecipesList;
import com.example.ndp.bakingapp.utils.DbUtils;
import com.example.ndp.bakingapp.utils.JsonUtils;
import com.example.ndp.bakingapp.utils.NetworkUtils;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NetworkRecipeRepository implements RecipeRepository {
    private static final String LOG_TAG = "_BAK_RemoteRepo";

    @Override
    public List getRecipes() {
        ArrayList<Recipe> recipeArrayList =  new ArrayList<Recipe>();
        try {
            URL recipeUrl = new URL(NetworkUtils.RECIPE_URL_STRING);

            //call the network for recipe
            String response =  NetworkUtils.getRecipeFromServer(recipeUrl);
            Log.d(LOG_TAG , "method called ____________");

            //validate the response
            if(!ValidationUtils.isStringEmptyOrNull(response)){

                //Modify response to parse jsonArray
                String responseArray = "{\"recipes\":"+response+"}";
                RecipesList recipeList = JsonUtils.parseJson(responseArray ,
                        RecipesList.class);
                if(null != recipeList){
                    recipeArrayList =  recipeList.getRecipes();
                    DbUtils dbUtils = DbUtils.getInstance();
                    HashSet<String> recipeStrings = new HashSet<>();
                    for(Recipe recipe : recipeArrayList){
                        String recipeString =  recipe.getId()+","+recipe.getName();
                        Log.d(LOG_TAG , "recipeString::"+recipeString);
                        recipeStrings.add(recipeString);
                    }
                    //store the recipe to local db
                    dbUtils.insertAllRecipeToDb(recipeArrayList);

                    //mark the  database as synced
                    PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
                    preferenceHelper.writeIsRepositorySyncedPref(true);
                    preferenceHelper.storeRecipeNamesInSharedPreference(recipeStrings);

                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return recipeArrayList;
    }
}
