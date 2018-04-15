package com.example.ndp.bakingapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.BundleMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.ndp.bakingapp.data.models.Recipe;
import com.example.ndp.bakingapp.ui.recipedetails.RecipeDetailsActivity;
import com.example.ndp.bakingapp.ui.recipelist.RecipeActivity;
import com.example.ndp.bakingapp.utils.DbUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityIntentTest {


    private static final String LOG_TAG = "Test_RecipeActivityIntentTest";
    @Rule
    public IntentsTestRule<RecipeActivity> recipeActivityActivityTestRule = new IntentsTestRule<>
            (RecipeActivity.class);
    private int TEST_INDEX = 0;
    private static final String RECIPE_KEY = "recipe_key";

    @Test
    public void clickRecipe_testIntentToNextActivity() {
        String recipeName = "Nutella Pie";
        onView(withId(R.id.recyclerViewRecipe)).perform(RecyclerViewActions.actionOnItemAtPosition
                (TEST_INDEX, click()));
        intended(toPackage("com.example.ndp.bakingapp"));

        Log.d(LOG_TAG , "toPackage test passed");
        intended(hasComponent(ComponentName.createRelative(
                recipeActivityActivityTestRule.getActivity()
                , RecipeDetailsActivity.class.getName())));
        Log.d(LOG_TAG , "hasComponent test passed");

        Matcher<Bundle> bundleMatcher = hasEntry(RECIPE_KEY, recipeName);
        intended(hasExtras(bundleMatcher));
        Log.d(LOG_TAG , "hasExtra test passed");
    }


    //create a matcher which matches the the recipe name with the intent extra
    public static Matcher<Bundle> hasEntry(final String key,final String value) {
        return new TypeSafeMatcher<Bundle>() {
            @Override
            protected boolean matchesSafely(Bundle item) {
                //get the recipeName from bundle.
                String recipeName = ((Recipe) item.get(RECIPE_KEY)).getName();

                return recipeName != null && recipeName.equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has bundle with: key: " +key);
                description.appendText(" value: " + value.toString());
            }
        };
    }
}
