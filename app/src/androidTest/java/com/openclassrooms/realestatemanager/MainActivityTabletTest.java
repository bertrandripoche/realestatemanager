package com.openclassrooms.realestatemanager;


import android.content.pm.ActivityInfo;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.openclassrooms.realestatemanager.ui.view.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTabletTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void CheckEditBtnDoesNotAppearWhenNoFlatItemSelectedAndAfterClickingAddFlat() {

        ViewInteraction addBtn = onView(withId(R.id.secondary_menu_add));
        addBtn.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction editBtn = onView(withId(R.id.secondary_menu_edit));
        editBtn.check(doesNotExist());
    }

    @Test
    public void CheckEditBtnAppearsWhenFlatItemClickedAndAfterClickingAddFlat() {

        ViewInteraction editBtn = onView(withId(R.id.secondary_menu_edit));

        ViewInteraction recyclerItem = onView(withId(R.id.fragment_flat_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        ViewInteraction addBtn = onView(
                allOf(withId(R.id.secondary_menu_add), withContentDescription("add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        addBtn.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        editBtn.check(matches(isDisplayed()));
    }

    @Test
    public void CheckEditBtnAppearsWhenFlatItemClickedAndAfterClickingEditFlat() {

        ViewInteraction editBtn = onView(withId(R.id.secondary_menu_edit));

        ViewInteraction recyclerItem = onView(withId(R.id.fragment_flat_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        editBtn.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        editBtn.check(matches(isDisplayed()));
    }

    @Test
    public void CheckEditBtnAppearsWhenFlatItemClickedAndAfterClickingAddFlatAndFlipScreen() {

        UiDevice device = UiDevice.getInstance(getInstrumentation());

        ViewInteraction editBtn = onView(withId(R.id.secondary_menu_edit));

        ViewInteraction recyclerItem = onView(withId(R.id.fragment_flat_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        ViewInteraction addBtn = onView(
                allOf(withId(R.id.secondary_menu_add), withContentDescription("add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        addBtn.perform(click());

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        editBtn.check(matches(isDisplayed()));
    }

    @Test
    public void CheckEditBtnAppearsWhenFlatItemClickedAndAfterClickingEditFlatAndFlipScreen() {

        ViewInteraction editBtn = onView(withId(R.id.secondary_menu_edit));

        ViewInteraction recyclerItem = onView(withId(R.id.fragment_flat_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        editBtn.perform(click());

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        editBtn.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
