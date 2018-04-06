package com.example.ndp.bakingapp.recipe.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ndp.bakingapp.R;
import com.example.ndp.bakingapp.recipe.data.adapters.BakingStepAdapter;
import com.example.ndp.bakingapp.recipe.data.adapters.IngredientsAdapter;
import com.example.ndp.bakingapp.recipe.data.models.BakingSteps;
import com.example.ndp.bakingapp.recipe.data.models.Ingredient;
import com.example.ndp.bakingapp.recipe.data.models.Recipe;
import com.example.ndp.bakingapp.recipe.provider.RecipeContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsListfragment extends Fragment
        implements BakingStepAdapter.OnItemClickedListener {


    private static final String RECIPE_KEY = "recipe_key";
    @BindView(R.id.recyclerViewIngredients)
    RecyclerView ingredientListRecyclerView;

    @BindView(R.id.recyclerViewBakingSteps)
    RecyclerView bakingStepsRecyclerView;

    private IngredientsAdapter ingredientsAdapter;
    private BakingStepAdapter bakingStepAdapter;
    private static final String LOG_TAG = "RecipeDFragement";
    private FragmentInteraction fragmentInteraction;

    public RecipeDetailsListfragment() {
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Recipe recipe =  getActivity().getIntent().getParcelableExtra(RECIPE_KEY);

        if(recipe != null){
            Log.d(LOG_TAG ,"Recipe found with "+ recipe.getName() );
            ingredientsAdapter.setIngredients(recipe.getIngredients());
            Log.d(LOG_TAG ,"Ingredient list size " + recipe.toString() );
            bakingStepAdapter.setSteps(recipe.getSteps());
        }
        else{
            Log.e(LOG_TAG , "recipe is null");
        }
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

    @Override
    public void onItemClicked(int position) {
            fragmentInteraction.onFragmentInteraction(position);
    }

    public interface FragmentInteraction{
        void onFragmentInteraction(int position);
    }
}
