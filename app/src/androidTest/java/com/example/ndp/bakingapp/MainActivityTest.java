package com.example.ndp.bakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.example.ndp.bakingapp.recipe.data.RecipeAdapter;
import com.example.ndp.bakingapp.recipe.ui.MainActivity;
import com.example.ndp.bakingapp.utils.NetworkUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>
            (MainActivity.class);
//
//    @Test
//    public void testLoaderIndicatorIsShowing() {
//        onView(withId(R.id.pb_loading_indicator)).check(matches(isDisplayed()));
//    }

    @Test
    public void testNetworkConnectivity_ShowErrorMessage(){
        if(!NetworkUtils.checkConnectivity(mainActivityActivityTestRule.getActivity())){
            onView(withText(R.string.no_internet_connection_message)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testRecyclerViewItemClicked_NextActivityIsDisplayed(){
        int TEST_INDEX = 2;
        onView(withId(R.id.recyclerViewRecipe)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewRecipe)).perform(RecyclerViewActions.actionOnItemAtPosition
                (TEST_INDEX, click()));

        onView(withId(R.id.recyclerViewIngredients)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewChildElelments_WithText() {
        final String RECIPE_NAME =  "Nutella Pie";
        onView(withId(R.id.recyclerViewRecipe)).perform(RecyclerViewActions.
                scrollToHolder(withRecipeName(RECIPE_NAME)));

    }

    private static Matcher<RecipeAdapter.RecipeViewHolder> withRecipeName(final String recipeName){
        return new TypeSafeMatcher<RecipeAdapter.RecipeViewHolder>(){

            @Override
            public void describeTo(Description description) {
                description.appendText("View with recipe "+ recipeName );
            }

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder item) {
                return item.getRecipeName() != null && item.getRecipeName().equals(recipeName);
            }
        };
    }
}
