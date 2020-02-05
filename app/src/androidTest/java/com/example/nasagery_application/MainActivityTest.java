package com.example.nasagery_application;

import android.view.View;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.nasagery_application.view.MainActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity = null;


    @Before
    public void setUp() throws Exception {

        mainActivity = rule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mainActivity.findViewById(R.id.image_recyclerview);
        assertNotNull(view);

    }

    @Test
    public void testSearchButtonClick(){
        View view = mainActivity.findViewById(R.id.search_button);
        assertNotNull(view);
    }

    @Test
    public void ensureRecyclerView() {
        onView(withId(R.id.search_edittext)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Earth"),closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.image_recyclerview))
                .inRoot(RootMatchers.withDecorView(
                Matchers.is(rule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(19));
    }
    @After
    public void tearDown() throws Exception {

        mainActivity = null;
    }
}