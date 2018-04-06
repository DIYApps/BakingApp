package com.example.ndp.bakingapp.recipe.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.RecipeAdapter;
import com.example.ndp.bakingapp.recipe.data.models.Ingredient;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.presenter.MainPresenter;
import com.example.ndp.bakingapp.recipe.provider.RecipeContract;
import com.example.ndp.bakingapp.recipe.view.MainView;
import com.example.ndp.bakingapp.recipe.widget.WidgetUpdateService;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


public class MainFragment extends Fragment implements RecipeAdapter.OnItemClickedListener,
        MainView {


    private static final String LOG_TAG = "MainFragment::";
    @BindView( R.id.recyclerViewRecipe)
    RecyclerView mRecyclerView;

    @BindView( R.id.pb_loading_indicator )
    ProgressBar mLoadingIndicator;

    @BindView( R.id.tv_error_message )
    TextView mErrorMessageTextView;

    private RecipeAdapter mRecipeAdapter;
    private MainPresenter mainPresenter;

    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private static final String RECIPE_KEY = "recipe_key";
    private ArrayList<Recipe> recipes;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this , view);
        mRecipeAdapter =  new RecipeAdapter(this);
        int spanCount = 1;
        if(getContext().getResources().getBoolean(R.bool.is_tablet)){
            Log.d(LOG_TAG ,"This tablet layout" );
            spanCount = 2;
        }
        else{
            Log.d(LOG_TAG ,"This not tablet layout" );
        }

        GridLayoutManager recipeLayoutManager = new GridLayoutManager(getActivity() , spanCount);
        mRecyclerView.setLayoutManager(recipeLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mainPresenter = new MainPresenter(getActivity(), this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null) {
            Log.d(LOG_TAG , "savedInstanceBundle is null");
            mainPresenter.loadRecipe();
        }else{
            Log.d(LOG_TAG , "savedInstanceBundle is not null");
            if(savedInstanceState.containsKey(RECIPE_LIST_KEY)){
                Log.d(LOG_TAG , "savedInstanceBundle key is found");
                recipes =  savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
                if(!ValidationUtils.isListEmptyOrNull(recipes)){
                    Log.d(LOG_TAG , "savedInstanceBundle restoring the list");
                   mRecipeAdapter.setRecipeList(recipes);
                }else{
                    mainPresenter.loadRecipe();
                }
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        Log.d(LOG_TAG , "position " + position);
        Intent recipeDetailsActivityLauncherIntent = new Intent(getActivity() ,
                RecipeDetailsActivity.class);
        recipeDetailsActivityLauncherIntent.putExtra(RECIPE_KEY , recipes.get(position));
        startActivity(recipeDetailsActivityLauncherIntent);
        updateWidget(recipes.get(position));

    }
    private void updateWidget(Recipe recipe){
        insertAllIngredientsToDb(recipe);
        WidgetUpdateService.startDisplayIngredientsService(getActivity(),
                recipe.getName(),
                String.valueOf(recipe.getId()));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_KEY , recipes);
    }

    @Override
    public void onRecipeLoaded(ArrayList<Recipe> recipes) {
        Log.d(LOG_TAG , "Size of list " + recipes.size());
        this.recipes = recipes;
        mRecipeAdapter.setRecipeList(recipes);
    }

    @Override
    public void onLoadingFailed() {
        Log.e(LOG_TAG , "Loading is failed ");
        Toast.makeText(getActivity() , "Loading failed." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoInternetConnection() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(getString(R.string.no_internet_connection_message));
    }

    @Override
    public void onShowProgressIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void insertAllIngredientsToDb(Recipe recipe){
        ContentResolver contentResolver = getContext().getContentResolver();
        ContentValues[] values = new ContentValues[recipe.getIngredients().size()];
        int position = 0;
        for(Ingredient ingredient : recipe.getIngredients()){
            ContentValues value =  new ContentValues();
            value.put(RecipeContract.IngredientEntry.INGREDIENT_NAME , ingredient.getIngredient());
            value.put(RecipeContract.IngredientEntry.MEASURE , ingredient.getMeasure());
            value.put(RecipeContract.IngredientEntry.RECIPE_ID , String.valueOf(recipe.getId()));
            value.put(RecipeContract.IngredientEntry.QUANTITY , String.valueOf
                    (ingredient.getQuantity()));
            values[position] = value;
            position++;
        }
        int count = contentResolver.bulkInsert(RecipeContract.IngredientEntry.CONTENT_URI , values);
        Log.d(LOG_TAG , "Ingredient Inserted count::"+ count);
    }

    private void deleteAllRecordFromDb(){
        ContentResolver contentResolver = getContext().getContentResolver();
        int deletedRow = contentResolver.delete(RecipeContract.IngredientEntry.CONTENT_URI , null ,
                null);
        Log.d(LOG_TAG , "deleteAllRecordFromDb :: count "+ deletedRow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteAllRecordFromDb();
    }
}
