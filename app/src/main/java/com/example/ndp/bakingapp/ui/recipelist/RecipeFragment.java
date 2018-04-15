package com.example.ndp.bakingapp.ui.recipelist;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.PreferenceHelper;
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


    private static final String LOG_TAG = "_BAK_MainFragment::";
    @BindView( R.id.recyclerViewRecipe)
    RecyclerView mRecyclerView;

    @BindView( R.id.pb_loading_indicator )
    ProgressBar mLoadingIndicator;

    @BindView( R.id.imageViewErrorMessage )
    ImageView mErrorMessageImageView;

    private RecipeAdapter mRecipeAdapter;
    private RecipePresenter recipePresenter;

    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private static final String RECIPE_KEY = "recipe_key";
    private ArrayList<Recipe> recipes;
    Unbinder  mUnbinder;


    public RecipeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this , view);
        mRecipeAdapter =  new RecipeAdapter(this);
        setHasOptionsMenu(true);
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
        Log.d(LOG_TAG , "onViewCreated()::called");
        if(savedInstanceState == null) {
            Log.d(LOG_TAG , "savedInstanceBundle is null");
            recipePresenter.loadRecipe(false);
        }else{
            Log.d(LOG_TAG , "savedInstanceBundle is not null");
            if(savedInstanceState.containsKey(RECIPE_LIST_KEY)){
                Log.d(LOG_TAG , "savedInstanceBundle key is found");
                recipes =  savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
                if(!ValidationUtils.isListEmptyOrNull(recipes)){
                    Log.d(LOG_TAG , "savedInstanceBundle restoring the list");
                   mRecipeAdapter.setRecipeList(recipes);
                }else{
                    recipePresenter.loadRecipe(false);
                }
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        Log.d(LOG_TAG, "position " + position);
        Intent recipeDetailsActivityLauncherIntent = new Intent(getActivity(),
                RecipeDetailsActivity.class);
        recipeDetailsActivityLauncherIntent.putExtra(RECIPE_KEY, recipes.get(position));
        startActivity(recipeDetailsActivityLauncherIntent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_KEY , recipes);
    }

    @Override
    public void onRecipeLoaded(ArrayList<Recipe> recipes) {
        Log.d(LOG_TAG , "Size of list " + recipes.size());
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageImageView.setVisibility(View.INVISIBLE);
        this.recipes = recipes;
        mRecipeAdapter.setRecipeList(recipes);
    }

    @Override
    public void onLoadingFailed() {
        Log.e(LOG_TAG , "Loading is failed ");
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageImageView.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity() , "No recipe to load." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoInternetConnection() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageImageView.setVisibility(View.VISIBLE);
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
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG , "onPause()::called");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();
        Log.d(LOG_TAG , "onOptionsItemSelected()::"+item.getTitle());
        if(id == R.id.action_referesh){
            recipePresenter.loadRecipe(true);
            return true;
        }
        return false;
    }
}
