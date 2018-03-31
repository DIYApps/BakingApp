package com.example.ndp.bakingapp.recipe.tasks;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.data.models.RecipesList;
import com.example.ndp.bakingapp.utils.JsonUtils;
import com.example.ndp.bakingapp.utils.NetworkUtils;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeLoader extends AsyncTaskLoader<ArrayList<Recipe>> {


    //create a empty list of recipe
    private ArrayList recipes =  new ArrayList<Recipe>();

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        //return cached data when available
        if(!recipes.isEmpty()){
            deliverResult(recipes);
            return;
        }

        //call to load data from network
        forceLoad();
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {
        ArrayList recipeArrayList =  new ArrayList<Recipe>();
        try {
            URL recipeUrl = new URL(NetworkUtils.RECIPE_URL_STRING);

            //call the network for recipe
            String response =  NetworkUtils.getRecipeFromServer(recipeUrl);

            //validate the response
            if(!ValidationUtils.isStringEmptyOrNull(response)){

                //Modify response to parse jsonArray
                String responseArray = "{\"recipes\":"+response+"}";
                RecipesList recipeList = JsonUtils.parseJson(responseArray ,
                        RecipesList.class);
                if(null != recipeList){
                    recipeArrayList =  recipeList.getRecipes();
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

    @Override
    public void deliverResult(ArrayList<Recipe> data) {
        super.deliverResult(data);
        recipes = data;
    }
}
