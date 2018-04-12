package com.example.ndp.bakingapp.ui.recipedetails;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.data.adapters.BakingStepAdapter;
import com.example.ndp.bakingapp.data.adapters.IngredientsAdapter;
import com.example.ndp.bakingapp.data.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment
        implements BakingStepAdapter.OnItemClickedListener , RecipeDetailView {


    private static final String RECIPE_KEY = "recipe_key";
    private static final String RECIPE_ID_KEY = "recipe_id_key";
    @BindView(R.id.recyclerViewIngredients)
    RecyclerView ingredientListRecyclerView;

    @BindView(R.id.recyclerViewBakingSteps)
    RecyclerView bakingStepsRecyclerView;

    private IngredientsAdapter ingredientsAdapter;
    private BakingStepAdapter bakingStepAdapter;
    private static final String LOG_TAG = "_BAK_RecipeDFragement";
    private FragmentInteraction fragmentInteraction;
    private RecipeDetailPresenter mRecipeDetailPresenter;
    private Recipe recipe;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_recipe_details_listfragment,
                container, false);
        ButterKnife.bind(this , view);
        ingredientsAdapter = new IngredientsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManagerSteps = new LinearLayoutManager(getContext());
        ingredientListRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientListRecyclerView.setAdapter(ingredientsAdapter);
        bakingStepsRecyclerView.setLayoutManager(linearLayoutManagerSteps);
        bakingStepAdapter  = new BakingStepAdapter(this);
        bakingStepsRecyclerView.setAdapter(bakingStepAdapter);
        mRecipeDetailPresenter = new RecipeDetailPresenter(this , getContext());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        loadRecipe(intent);
    }

    private void loadRecipe(Intent intent){
        Recipe recipe =  intent.getParcelableExtra(RECIPE_KEY);
        String recipeId =  intent.getStringExtra(RECIPE_ID_KEY);
        Log.d(LOG_TAG ,"Recipe found with id "+ recipeId );
        mRecipeDetailPresenter.loadRecipeDetails(recipe , recipeId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof FragmentInteraction)){
            throw new IllegalArgumentException("Activity must implement FragmentInteraction ");
        }
        fragmentInteraction = (FragmentInteraction) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteraction = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    //fragment method to handle new intent sent from widget
    public void onNewIntent(Intent intent){
        Log.d(LOG_TAG , "onNewIntent()");
        loadRecipe(intent);
    }

    @Override
    public void onItemClicked(int position) {
        fragmentInteraction.onFragmentInteraction(recipe ,position);
    }

    @Override
    public void showRecipeDetails(Recipe recipe) {
        if(recipe != null){
            this.recipe = recipe;
            Log.d(LOG_TAG ,"Recipe found with "+ recipe.getName() );
            ingredientsAdapter.setIngredients(recipe.getIngredients());
            Log.d(LOG_TAG ,"Ingredient list size " + recipe.toString() );
            bakingStepAdapter.setSteps(recipe.getSteps());
            fragmentInteraction.setTitle(recipe.getName());
        }
        else{
            showErrorInLoadingRecipe();
        }
    }

    @Override
    public void showErrorInLoadingRecipe() {
        Snackbar.make(bakingStepsRecyclerView , R.string.recipe_detail_loading_error_message ,
                Snackbar.LENGTH_LONG).setAction(R.string.action_close, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        }).show();
    }

    public interface FragmentInteraction{
        void onFragmentInteraction(Recipe recipe , int position);
        void setTitle(String recipeName);
    }
}
