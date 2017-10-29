import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pk.example.MainActivity;
import com.pk.example.ProfileListActivity;
import com.pk.example.R;
import com.pk.example.entity.ProfileEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CreateProfileTest {

//    @Rule
//    public ActivityTestRule<ProfileListActivity> mActivityRule =
//            new ActivityTestRule<>(ProfileListActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void shouldCreateProfileAndAddToProfileListWithOneAppTest() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("SingleProfile"), closeSoftKeyboard());

        //choose app to block
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //check if profile in list
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleProfile"))))));

        //click the profile to check if app is listed
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //check if app is listed
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("YouTube"))))));
    }

    @Test
    public void shouldCreateProfileAndAddToProfileListWithMultipleAppTest() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("MultipleProfile"), closeSoftKeyboard());

        //choose apps to block
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).perform(click());
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).perform(click());
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //check if profile in list
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("MultipleProfile"))))));

        //click the profile to check if app is listed
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).perform(click());

        //check if apps are listed
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("com.android.cts.priv.ctsshim"))))));
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("YouTube"))))));
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("Android Services Library"))))));
    }
//
//    @Test
//    public void createScheduleAndAddToScheduleListWithOneProfileTest() {
//        //go to schedule list
//        onView(withId(R.id.btnNavigation)).perform(click());
//
//        //click create schedule button
//        onView(withId(R.id.btnToast)).perform(click());
//
//        //type in schedule name
//        onView(withId(R.id.editTextProfileName)).perform(typeText("SingleSchedule"), closeSoftKeyboard());
//
//        //select date
//        onView(withId(R.id.btn_date)).perform(click());
//
//
//    }

}
