package com.example.ndp.bakingapp.recipe.ui;


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
import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.presenter.MainPresenter;
import com.example.ndp.bakingapp.recipe.view.MainView;
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
            spanCount = 2;
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
        Toast.makeText(getActivity() , "Loaded" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingFailed() {
        Log.e(LOG_TAG , "Loading is failed ");
        Toast.makeText(getActivity() , "Loading failed." , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowProgressIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgressIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }
}
