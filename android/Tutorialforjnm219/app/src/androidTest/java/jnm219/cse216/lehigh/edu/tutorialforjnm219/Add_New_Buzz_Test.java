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
public class Add_New_Buzz_Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void add_New_Buzz_Test() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Log In"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.enterUserName), isDisplayed()));
        editText.perform(replaceText("jack"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.enterPassword), isDisplayed()));
        editText2.perform(replaceText("pass"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.buttonOk), withText("OK"), isDisplayed()));
        button.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Create a Buzz"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enterSubject), isDisplayed()));
        appCompatEditText.perform(replaceText("Subject"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enterMessage), isDisplayed()));
        appCompatEditText2.perform(replaceText("Message"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonOk), withText("OK"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.messageItemSubject), withText("Subject: Subject"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Subject: Subject")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.messageItemMessage), withText("Message: Message"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.message_list_view),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Message: Message")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.messageItemVotes), withText("Votes: 0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Votes: 0")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.messageItemVotes), withText("Votes: 0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Votes: 0")));

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
