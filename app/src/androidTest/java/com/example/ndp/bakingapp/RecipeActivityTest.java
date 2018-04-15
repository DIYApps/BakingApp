package com.example.ndp.bakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ndp.bakingapp.data.PreferenceHelper;
import com.example.ndp.bakingapp.data.adapters.RecipeAdapter;
import com.example.ndp.bakingapp.ui.recipelist.RecipeActivity;

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

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {


    @Rule
    public ActivityTestRule<RecipeActivity> mainActivityActivityTestRule = new ActivityTestRule<>
            (RecipeActivity.class);

    @Test
    public void testOptionMenuClicked_recipeListRefreshed() {
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
        preferenceHelper.writeIsRepositorySyncedPref(false);

        onView(withId(R.id.action_referesh))
                .check(matches(isDisplayed()))
                .perform(click());
        assert (!preferenceHelper.readIsRepositorySyncedPref());
    }

    /**
     * Test method to test the Checks the recyelcer view to be displayed
     * scroll to specific position and perform click.
     * Verify the next activity is displayed with id of recyclerViewIngredients
     *
     */



    @Test
    public void testRecyclerViewItemClicked_NextActivityIsDisplayed(){
        int TEST_INDEX = 2;
        onView(withId(R.id.recyclerViewRecipe)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerViewRecipe)).perform(RecyclerViewActions.actionOnItemAtPosition
                (TEST_INDEX, click()));

        onView(withId(R.id.recyclerViewIngredients)).check(matches(isDisplayed()));
    }

    /**
     * test recycler view contain a entry with  desired recipe name
     */
    @Test
    public void testRecyclerViewChildElelments_WithText() {
        final String RECIPE_NAME =  "Nutella Pie";
        onView(withId(R.id.recyclerViewRecipe)).perform(RecyclerViewActions.
                scrollToHolder(withRecipeName(RECIPE_NAME)));

    }

    /*A custom matcher which matches a ViewHolder with desired recipe name.*/
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
