package at.moritzmusel.cluedo;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LobbyActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void lobbyCreateActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_startgame), withText("Start Game"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.btn_create_lobby), withText("CREATE LOBBY"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.btn_join_lobby), withText("JOIN LOBBY"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.btn_back_to_main), withText("BACK"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_create_lobby), withText("Create Lobby"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_send_link), withText("Send Link"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.txt_lobbyid), withText("12345"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("12345")));

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.text1), withText("player 1"),
                        withParent(allOf(withId(R.id.playerlist),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("player 1")));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_back), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton4.perform(click());
    }
    @Test
    public void lobbyJoinActivityTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_startgame), withText("Start Game"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_join_lobby), withText("Join Lobby"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.txt_enter_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        pressBack();
    }

    @Test
    public void lobbyJoinTextTest() {
        onView(withId(R.id.btn_startgame)).perform(click());
        onView(withId(R.id.btn_join_lobby)).perform(click());
        onView(withId(R.id.txt_enter_id)).perform(replaceText("12345"));
        onView(withId(R.id.txt_enter_id)).check(matches(withText("12345")));
        onView(withId(R.id.btn_lobby_join)).perform(click());
        onView(withId(R.id.txt_lobbyid)).check(matches(withText("12345")));
    }

    @Test
    public void lobbySendLinkTest() {
        onView(withId(R.id.btn_startgame)).perform(click());
        onView(withId(R.id.btn_create_lobby)).perform(click());
        onView(withId(R.id.btn_send_link)).perform(click());
        ViewActions.pressBack();
        onView(
                allOf(withId(android.R.id.text1), withText("player 1"),
                        withParent(allOf(withId(R.id.playerlist),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed())).check(matches(withText("player 1")));

    }
    @Test
    public void lobbyCreateTitleTest(){
        onView(withId(R.id.btn_startgame)).perform(click());
        onView(withId(R.id.btn_create_lobby)).perform(click());
        onView(withId(R.id.txt_create_lobby)).check(matches(withText("Create Lobby")));
    }

    @Test
    public void lobbyJoinTitleTest(){
        onView(withId(R.id.btn_startgame)).perform(click());
        onView(withId(R.id.btn_join_lobby)).perform(click());
        onView(withId(R.id.txt_enter_id)).perform(replaceText("12345"));
        onView(withId(R.id.btn_lobby_join)).perform(click());
        onView(withId(R.id.txt_create_lobby)).check(matches(withText("Lobby")));
    }

    /*
    ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btn_lobby_join), withText("Join"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.txt_lobbyid), withText("12345"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("12345")));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.btn_lobby_start), withText("WAITING..."),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.btn_back), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txt_enter_id), withText("12345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.txt_enter_id), withText("12345"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText(""));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.txt_enter_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.btn_lobby_join), withText("Join"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.btn_back_to_lobbydecision), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton10.perform(click());
     */

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
