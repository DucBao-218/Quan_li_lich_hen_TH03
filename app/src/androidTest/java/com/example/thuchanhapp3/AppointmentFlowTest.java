package com.example.thuchanhapp3;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.matcher.BoundedMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AppointmentFlowTest {
    @Test public void loginAddToggleAndDelete() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), LoginActivity.class);
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.login_button)).perform(click());
            onView(withId(R.id.login_error)).check(matches(isDisplayed()));
            onView(withId(R.id.username)).perform(replaceText("admin"));
            onView(withId(R.id.password)).perform(replaceText("wrong"), closeSoftKeyboard());
            onView(withId(R.id.login_button)).perform(click());
            onView(withId(R.id.login_error)).check(matches(withText(R.string.login_invalid)));
            onView(withId(R.id.password)).perform(replaceText("123456"), closeSoftKeyboard());
            onView(withId(R.id.login_button)).perform(click());
            onView(withId(R.id.add_button)).perform(click());
            onView(withId(R.id.appointment_title)).perform(replaceText("UI test appointment"), closeSoftKeyboard());
            onView(withId(R.id.appointment_location)).perform(replaceText("Test room"), closeSoftKeyboard());
            onView(withId(R.id.save_button)).perform(scrollTo(), click());
            onData(new BoundedMatcher<Object, Appointment>(Appointment.class) {
                @Override public void describeTo(Description description) {
                    description.appendText("appointment created by UI test");
                }
                @Override protected boolean matchesSafely(Appointment appointment) {
                    return appointment.getTitle().equals("UI test appointment");
                }
            }).inAdapterView(withId(R.id.appointment_list)).perform(click());
            onView(withId(R.id.detail_location)).check(matches(withText("Test room")));
            onView(withId(R.id.detail_reminder)).perform(click());
            onView(withId(R.id.reminder_status)).check(matches(withText(R.string.reminder_off)));
            onView(withId(R.id.delete_button)).perform(scrollTo(), click());
            onView(withText(R.string.delete)).perform(click());
            onView(withId(R.id.add_button)).check(matches(isDisplayed()));
            onView(withText("UI test appointment")).check(doesNotExist());
        }
    }
}
