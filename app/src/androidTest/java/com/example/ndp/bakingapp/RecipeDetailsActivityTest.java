package com.example.ndp.bakingapp;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ndp.bakingapp.ui.recipedetails.RecipeDetailsActivity;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    private static final String LOG_TAG = "Test_DetailsActivityTest" ;
    private static final int TEST_STEP_POSITION_WITH_VIDEO = 2;
    private static final int TEST_STEP_POSITION_WITHOUT_VIDEO = 1;
    private static final String TEST_RECIPE_NAME = "Brownies";
    private static final String TEST_RECIPE_ID = "2";

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> recipeDetailsActivityActivityTestRule =
            new ActivityTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class ){

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra("recipe_id_key" , String.valueOf(TEST_RECIPE_ID));
                    return intent;
                }
            };


    @Before
    public void startActivityWithExtra(){

    }
    @Test
    public void testTwoPlaneActivity() {
        boolean isTwoPlane  = recipeDetailsActivityActivityTestRule.getActivity().getResources()
                .getBoolean(R.bool.is_tablet);

        Log.d(LOG_TAG , "testTwoPlaneActivity");
        if(isTwoPlane){
            onView(withId(R.id.containerRecipeDetailsListFragment)).check(matches(isDisplayed()));
            onView(withId(R.id.stepDetailsFragmentContainer)).check(matches(isDisplayed()));
        }
        else{
            onView(withId(R.id.containerRecipeDetailsListFragment)).check(matches(isDisplayed()));
        }
        Log.d(LOG_TAG , "testTwoPlaneActivity passed");

    }

    @Test
    public void  clickStepWithVideo_testPlayerDisplayed() {
        onView(withId(R.id.recyclerViewBakingSteps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(TEST_STEP_POSITION_WITH_VIDEO,
                        click()));
        onView(withId(R.id.exoplayerBankingStep)).check(matches(isDisplayed()));
    }

    @Test
    public void  clickStepWithoutVideo_testOverlayImageDisplayed() {
        onView(withId(R.id.recyclerViewBakingSteps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(TEST_STEP_POSITION_WITHOUT_VIDEO,
                        click()));
        onView(withId(R.id.imageViewVideoOverlay)).check(matches(hasDrawable()));
    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }
}
