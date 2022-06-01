package at.moritzmusel.cluedo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BoardActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void boardSwipeRightTest(){
        BoardActivity ba = new BoardActivity();
        onView(withId(R.id.btn_startgame)).perform(click());
        onView(withId(R.id.btn_create_lobby)).perform(click());
        onView(withId(R.id.txt_create_lobby)).check(matches(withText("Create Lobby")));
        onView(withId(R.id.btn_lobby_start)).perform(click());
        //ba.startNotepad();

    }
}
