import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toolbar;

import com.pk.example.MainActivity;
import com.pk.example.ProfileListActivity;
import com.pk.example.R;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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

    @Rule
    public ActivityTestRule<MainActivity> homeActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

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
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("Focus!"))))));
    }

//   // @Test
//    public void createAProfileAndAddToProfileListWithMultipleAppTest() {
//        //go to profilelist
//        onView(withId(R.id.btnAllProfiles)).perform(click());
//
//        //click create profile
//        onView(withId(R.id.btnToast)).perform(click());
//
//        //check if goes to create profile view
//        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));
//
//        //type in profile name
//        onView(withId(R.id.editTextProfileName)).perform(typeText("MultipleProfile"), closeSoftKeyboard());
//
//        //choose apps to block
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).perform(click());
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).perform(click());
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).perform(click());
//
//        //creates the profile
//        onView(withId(R.id.btnCreateProfile)).perform(click());
//
//        //check if profile in list
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("MultipleProfile"))))));
//
//        //click the profile to check if app is listed
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).perform(click());
//
//        //check if apps are listed
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("com.android.cts.priv.ctsshim"))))));
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("YouTube"))))));
//        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).onChildView(withId(R.id.app_name)).check(ViewAssertions.matches((withText(containsString(("Android Services Library"))))));
//    }
//
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
//        //set date
//        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 21));
//
//        //confirm date
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //select time
//        onView(withId(R.id.btn_time)).perform(click());
//
//        //set time
//        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(10, 0));
//
//        //confirm time
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //select duration
//        onView(withId(R.id.btn_duration)).perform(click());
//
//        //set duration
//        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));
//
//        //confirm duration
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //add profile
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());
//
//        //click create button
//        onView(withId(R.id.btnCreateProfile)).perform(click());
//
//        //check if schedule is in list
//        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleSchedule"))))));

//        //click the profile to check if app is listed
//        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).perform(click());
//
//        //check if app is listed
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleProfile"))))));
//    }

//    @Test
//    public void createScheduleAndAddToScheduleListWithMultipleProfileTest() {
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
//        //set date
//        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 21));
//
//        //confirm date
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //select time
//        onView(withId(R.id.btn_time)).perform(click());
//
//        //set time
//        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(10, 0));
//
//        //confirm time
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //select duration
//        onView(withId(R.id.btn_duration)).perform(click());
//
//        //set duration
//        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));
//
//        //confirm duration
//        onView(withId(android.R.id.button1)).perform(click());
//
//        //add profiles
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).perform(click());
//
//        //click create button
//        onView(withId(R.id.btnCreateProfile)).perform(click());
//
//        //check if schedule is in list
//        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleSchedule"))))));
//
//        //click the profile to check if app is listed
//        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).perform(click());
//
//        //check if app is listed
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleProfile"))))));
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("MultipleProfile"))))));
//
//    }


//    @Test
//    public void turnProfileActiveAndBlockingNotificationTest() {
//        //Espresso Code
//        //go to profilelist
//        onView(withId(R.id.btnAllProfiles)).perform(click());
//
//        //toggle on profile to be active
//        //click the profile to check if app is listed
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).onChildView(withId(R.id.toggBtn)).perform(click());
//
//        //go back to main activity
//        //pressBack();
//        //onView(withContentDescription("Navigate up")).perform(click());
//        //onView(isRoot()).perform(ViewActions.pressMenuKey());
////        onView(withParent((withClassName(is(Toolbar.class.getName()))))).perform(click());
//        //DrawerActions.openDrawer((R.id.my_drawer_layout));
//        //pressBack();
//        //Intent intent = new Intent();
//        //homeActivityRule.launchActivity(intent);
//
//
//
//        //UIAutomator Code
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.openNotification();
//        device.wait(Until.hasObject(By.text("Focus!")), 100000000);
//        UiObject2 title = device.findObject(By.text("Focus!"));
//        Assert.assertEquals("Focus!", title.getText());
//
//
////        //click on send notification
////        onView(withId(R.id.btnCreateNotify)).perform(click());
////
////        //UIAutomator Code
////        //check if notification was blocked
////        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
////        device.openNotification();
////        device.wait(Until.hasObject(By.text("PK Notification")), 10000);
////        UiObject2 title = device.findObject(By.text("PK Notification"));
////        Assert.assertNull(title);
//        //Assert.assertEquals("Notification Listener Example", text.getText());
//
//    }

//    @Test
//    public void deleteProfileAndCheckIfInProfileList() {
//        //go to profilelist
//        onView(withId(R.id.btnAllProfiles)).perform(click());
//
//        //click the profile to delete
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());
//
//        //click to edit mode
//        onView(withId(R.id.btnEditProfile)).perform(click());
//
//        //click delete profile
//        onView(withId(R.id.btnDeleteProfile)).perform(click());
//
//        //click the profile to delete
//        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());
//
//

    //
//    }





}
