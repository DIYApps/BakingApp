package com.example.ndp.bakingapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ndp.bakingapp.recipe.ui.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> recipeDetailsActivityActivityTestRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class);

    @Test
    public void testTwoPlaneActivity() {
        boolean isTwoPlane  = recipeDetailsActivityActivityTestRule.getActivity().getResources()
                .getBoolean(R.bool.is_tablet);

        if(isTwoPlane){
            onView(withId(R.id.containerRecipeDetailsListFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.stepDetailsFragmentContainer)).check(matches(isDisplayed()));
        }
        else{
            onView(withId(R.id.containerRecipeDetailsListFragment)).check(matches(isDisplayed()));
        }

    }

}
