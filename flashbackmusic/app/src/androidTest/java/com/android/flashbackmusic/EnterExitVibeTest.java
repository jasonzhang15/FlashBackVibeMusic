package com.android.flashbackmusic;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.android.flashbackmusic.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EnterExitVibeTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void enterExitVibeTest() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.Flashback), withText("Vibe"),
childAtPosition(
allOf(withId(R.id.switch_between),
childAtPosition(
withId(R.id.switch_between_main),
0)),
2),
isDisplayed()));
        appCompatButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(60000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction viewGroup = onView(
allOf(withId(R.id.main_content),
childAtPosition(
allOf(withId(android.R.id.content),
childAtPosition(
withId(R.id.action_bar_root),
0)),
0),
isDisplayed()));
        viewGroup.check(matches(isDisplayed()));
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.Album), withText("Albums"),
childAtPosition(
allOf(withId(R.id.switch_between),
childAtPosition(
withId(R.id.switch_between_main),
0)),
1),
isDisplayed()));
        appCompatButton2.perform(click());
        
        ViewInteraction button = onView(
allOf(withId(R.id.Album),
childAtPosition(
allOf(withId(R.id.switch_between),
childAtPosition(
withId(R.id.switch_between_main),
0)),
1),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction linearLayout = onView(
allOf(withId(R.id.album_main),
childAtPosition(
allOf(withId(R.id.main_content),
childAtPosition(
withId(android.R.id.content),
0)),
4),
isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
        
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
