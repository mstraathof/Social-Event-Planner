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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EnterANewBuzzTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void enterANewBuzzTest() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("BUZZ"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enterSubject), isDisplayed()));
        appCompatEditText.perform(replaceText("Hello"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enterMessage), isDisplayed()));
        appCompatEditText2.perform(replaceText("World"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.enterSubject), withText("Hello"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("Hello")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.enterMessage), withText("World"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        editText2.check(matches(withText("World")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonOk), withText("OK"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.listItemSubject), withText("Subject: Hello"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.datum_list_view),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Subject: Hello")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.listItemMessage), withText("Message: World"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.datum_list_view),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Message: World")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.listItemVotes), withText("Votes: 0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.datum_list_view),
                                        0),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Votes: 0")));

        ViewInteraction button = onView(
                allOf(withId(R.id.listLikeButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.datum_list_view),
                                        0),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

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
