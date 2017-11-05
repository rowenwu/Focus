import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.pk.example.clientui.MainActivity;
import com.pk.example.R;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProfileTest {

//    @Rule
//    public ActivityTestRule<ProfileListActivity> mActivityRule =
//            new ActivityTestRule<>(ProfileListActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> homeActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void aCheckNoProfileToDisplayTest() {
        //go to schedule list
        onView(ViewMatchers.withId(R.id.btnAllProfiles)).perform(click());

        //check no profiles
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("There are no profiles to display."))))));
    }

    @Test
    public void createAProfileAndAddToProfileListWithOneAppTest() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("SingleProfile"), closeSoftKeyboard());

        //choose app to block 44
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //check if profile in list
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleProfile"))))));

        //click the profile to check if app is listed
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //check if app is listed
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("com.android.smoketest"))))));
    }

    @Test
    public void createAProfileAndAddToProfileListWithMultipleAppTest() {
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
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("MultipleProfile"))))));

        //click the profile to check if app is listed
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //check if apps are listed
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("com.android.smoketest"))))));
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("com.android.cts.priv.ctsshim"))))));
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("YouTube"))))));
    }


    @Test
    public void turnProfileActiveAndBlockingNotificationTest() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("ActiveTestProfile"), closeSoftKeyboard());

        //choose app to block 44
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //Espresso Code
        //toggle on profile to be active
        //click the profile to check if app is listed
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.toggBtn)).perform(click());

        //go back to main activity

        Intent intent = new Intent();
        homeActivityRule.launchActivity(intent);

        //click on send notification
        onView(withId(R.id.btnCreateNotify)).perform(click());

        //UIAutomator Code
        //check if notification was blocked
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text("PK Notification")), 10000);
        UiObject2 title = device.findObject(By.text("PK Notification"));
        Assert.assertNull(title);
    }

    @Test
    public void deleteProfileAndCheckIfInProfileList() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("DeleteTestProfile"), closeSoftKeyboard());

        //choose app to block 44
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //click the profile to delete
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //click to edit mode
        onView(withId(R.id.btnEditProfile)).perform(click());

        //close keyboard
        onView(withId(R.id.editTextProfileName)).perform(closeSoftKeyboard());

        //click delete profile
        onView(withId(R.id.btnDeleteProfile)).perform(click());

        //check is profile still in list
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches(not((withText(containsString(("testDeleteProfile")))))));

    }

    @Test
    public void editProfileAppsTest() {
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("ActiveTestProfile"), closeSoftKeyboard());

        //choose app to block 44
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //click profile to be edited
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).perform(click());

        //click edit button
        onView(withId(R.id.btnEditProfile)).perform(click());

        //close keyboard
        onView(withId(R.id.editTextProfileName)).perform(closeSoftKeyboard());

        //choose app to block 3
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(4).perform(click());

        //new applicatin slected
        onView(withId(R.id.btnSaveProfile)).perform(click());

        //click profile to be edited
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).perform(click());

        //check if new app
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("Example Wallpapers"))))));

    }



}
