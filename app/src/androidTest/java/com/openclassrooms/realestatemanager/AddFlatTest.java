package com.openclassrooms.realestatemanager;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.internal.util.Checks;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Flat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddFlatTest {

    // FOR DATA
    private RealEstateManagerDatabase database;
    private static long AGENT_ID = 1;
    private static Agent AGENT_DEMO = new Agent("Philippe", "Dubois","Philippe.Dubois@gmail.com");
    private static Flat NEW_Flat1 = new Flat( "Urbain et stylé", "blablabla", "Appartement", 480000, 67, 3 , 2, 1, 5, "rue des Petites Écuries", 75010, "Paris", 48.7, 2.35, false, true, true, false, false, 1);
    private static Flat NEW_Flat2 = new Flat( "Familial de luxe", "blablabla", "Appartement", 1560000, 138, 6 , 4, 2, 121, "rue de Rivoli", 75001, "Paris", 48.7, 2.35, false, true, true, false, false, 1);
    private static Flat NEW_Flat3 = new Flat( "Pour grande famille", "blablabla", "Duplex", 980000, 108, 4 , 2, 2, 34, "rue de Trévise", 75009, "Paris",48.7, 2.35, false, true, true, false, false, 1);

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();

        this.database.mAgentDao().createAgent(AGENT_DEMO);
        this.database.mFlatDao().insertFlat(NEW_Flat1);
        this.database.mFlatDao().insertFlat(NEW_Flat2);
        this.database.mFlatDao().insertFlat(NEW_Flat3);
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkAddButtonIsOn() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.secondary_menu_add), withContentDescription("add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_summary),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        2),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("s"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_description),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        3),
                                1)));
        appCompatEditText2.perform(scrollTo(), replaceText("d"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_surface),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        4),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_price),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        8),
                                1)));
        appCompatEditText4.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edit_city),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_address),
                                        4),
                                1)));
        appCompatEditText5.perform(scrollTo(), replaceText("c"), closeSoftKeyboard());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.btn_add_flat), withContentDescription("Add button"),
                        isDisplayed()));

        int pink = Color.parseColor("#C20044");
        imageButton.check(matches(buttonShouldHaveBackgroundColor(pink)));
    }

    @Test
    public void checkAddButtonIsOff() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.secondary_menu_add), withContentDescription("add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_summary),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        2),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("s"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_description),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        3),
                                1)));
        appCompatEditText2.perform(scrollTo(), replaceText("d"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_surface),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        4),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edit_price),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.form_flat_presentation),
                                        8),
                                1)));
        appCompatEditText4.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.btn_add_flat), withContentDescription("Add button"),
                        isDisplayed()));

        int grey = Color.parseColor("#CCCCCC");
        imageButton.check(matches(buttonShouldHaveBackgroundColor(grey)));
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

    public static Matcher<View> buttonShouldHaveBackgroundColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, FloatingActionButton>(FloatingActionButton.class) {
            @Override
            public boolean matchesSafely(FloatingActionButton btn) {
                ColorStateList colorStateList = btn.getBackgroundTintList();
                ColorStateList testList = ColorStateList.valueOf(color);

                return colorStateList == testList;
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("Color did not match "+color);
            }
        };
    }

}
