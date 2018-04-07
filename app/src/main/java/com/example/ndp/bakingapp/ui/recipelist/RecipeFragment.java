package com.example.ndp.bakingapp.ui.recipelist;


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
import com.example.ndp.bakingapp.data.adapters.RecipeAdapter;
import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.example.ndp.bakingapp.ui.widget.WidgetUpdateService;
import com.example.ndp.bakingapp.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeFragment extends Fragment implements RecipeAdapter.OnItemClickedListener,
        RecipeView {


    private static final String LOG_TAG = "MainFragment::";
    @BindView( R.id.recyclerViewRecipe)
    RecyclerView mRecyclerView;

    @BindView( R.id.pb_loading_indicator )
    ProgressBar mLoadingIndicator;

    @BindView( R.id.tv_error_message )
    TextView mErrorMessageTextView;

    private RecipeAdapter mRecipeAdapter;
    private RecipePresenter recipePresenter;

    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private static final String RECIPE_KEY = "recipe_key";
    private ArrayList<Recipe> recipes;
    Unbinder  mUnbinder;


    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this , view);
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
        recipePresenter = new RecipePresenter(getActivity(), this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null) {
            Log.d(LOG_TAG , "savedInstanceBundle is null");
            recipePresenter.loadRecipe();
        }else{
            Log.d(LOG_TAG , "savedInstanceBundle is not null");
            if(savedInstanceState.containsKey(RECIPE_LIST_KEY)){
                Log.d(LOG_TAG , "savedInstanceBundle key is found");
                recipes =  savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
                if(!ValidationUtils.isListEmptyOrNull(recipes)){
                    Log.d(LOG_TAG , "savedInstanceBundle restoring the list");
                   mRecipeAdapter.setRecipeList(recipes);
                }else{
                    recipePresenter.loadRecipe();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
