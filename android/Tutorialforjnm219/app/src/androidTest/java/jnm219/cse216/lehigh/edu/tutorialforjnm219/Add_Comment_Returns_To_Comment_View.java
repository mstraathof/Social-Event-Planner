package jnm219.cse216.lehigh.edu.tutorialforjnm219;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Add_Comment_Returns_To_Comment_View {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void add_Comment_Returns_To_Comment_View() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Log In"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.enterUserName), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.enterUserName), isDisplayed()));
        editText2.perform(replaceText("jack"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.enterPassword), isDisplayed()));
        editText3.perform(replaceText("Grandma831"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.enterPassword), withText("Grandma831"), isDisplayed()));
        editText4.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.buttonOk), withText("OK"), isDisplayed()));
        button.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.messageCommentButton), withText("Comments"), isDisplayed()));
        appCompatButton.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("New Comment"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enterComment), isDisplayed()));
        appCompatEditText.perform(replaceText("apple"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonOk), withText("OK"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction imageView = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

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
