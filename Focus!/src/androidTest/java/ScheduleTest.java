import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.pk.example.clientui.MainActivity;
import com.pk.example.R;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ScheduleTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Rule
    public ActivityTestRule<MainActivity> homeActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Test
    public void aCheckNoScheduleToDisplayTest() {
        //go to schedule list
        onView(ViewMatchers.withId(R.id.btnNavigation)).perform(click());

        //check no profiles
        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("There are no schedules to display."))))));
    }

    @Test
    public void createScheduleAndAddToScheduleListWithOneProfileTest() {
        //make a profile
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("testProfile"), closeSoftKeyboard());

        //choose app to block 44 - focus
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //go back
        Intent intent = new Intent();
        homeActivityRule.launchActivity(intent);

        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //type in schedule name
        onView(withId(R.id.editTextProfileName)).perform(typeText("SingleSchedule"), closeSoftKeyboard());

        //select date
        onView(withId(R.id.btn_date)).perform(click());

        //set date
        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 21));

        //confirm date
        onView(withId(android.R.id.button1)).perform(click());

        //select time
        onView(withId(R.id.btn_time)).perform(click());

        //set time
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(10, 0));

        //confirm time
        onView(withId(android.R.id.button1)).perform(click());

        //select duration
        onView(withId(R.id.btn_duration)).perform(click());

        //set duration
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));

        //confirm duration
        onView(withId(android.R.id.button1)).perform(click());

        //add profile
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //click create button
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //check if schedule is in list
        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("SingleSchedule"))))));

    }

    @Test
    public void createScheduleAndAddToScheduleListWithMultipleProfileTest() {
        //make first profile
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("firstTestProfile"), closeSoftKeyboard());

        //choose app to block 44 - focus
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //make second profile
        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("secondTestProfile"), closeSoftKeyboard());

        //choose apps to block
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(0).perform(click());
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(1).perform(click());
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(2).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //go back
        Intent intent = new Intent();
        homeActivityRule.launchActivity(intent);

        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //type in schedule name
        onView(withId(R.id.editTextProfileName)).perform(typeText("MultipleSchedule"), closeSoftKeyboard());

        //select date
        onView(withId(R.id.btn_date)).perform(click());

        //set date
        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 21));

        //confirm date
        onView(withId(android.R.id.button1)).perform(click());

        //select time
        onView(withId(R.id.btn_time)).perform(click());

        //set time
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(10, 0));

        //confirm time
        onView(withId(android.R.id.button1)).perform(click());

        //select duration
        onView(withId(R.id.btn_duration)).perform(click());

        //set duration
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));

        //confirm duration
        onView(withId(android.R.id.button1)).perform(click());

        //add profiles
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(1).perform(click());

        //click create button
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //check if schedule is in list
        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches((withText(containsString(("MultipleSchedule"))))));
    }

    @Test
    public void deleteScheduleAndCheckIfInScheduleListTest() {
        //make a profile
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("testDeleteProfile"), closeSoftKeyboard());

        //choose app to block 44 - focus
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //go back
        Intent intent = new Intent();
        homeActivityRule.launchActivity(intent);

        //create schedule
        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //type in schedule name
        onView(withId(R.id.editTextProfileName)).perform(typeText("testDeleteSchedule"), closeSoftKeyboard());

        //select date
        onView(withId(R.id.btn_date)).perform(click());

        //set date
        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 21));

        //confirm date
        onView(withId(android.R.id.button1)).perform(click());

        //select time
        onView(withId(R.id.btn_time)).perform(click());

        //set time
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(10, 0));

        //confirm time
        onView(withId(android.R.id.button1)).perform(click());

        //select duration
        onView(withId(R.id.btn_duration)).perform(click());

        //set duration
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));

        //confirm duration
        onView(withId(android.R.id.button1)).perform(click());

        //add profile
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //click create button
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //click the profile to delete
        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).perform(click());

        //close the keyboard
        onView(withId(R.id.editTextScheduleName)).perform(closeSoftKeyboard());

        //click delete profile
        onView(withId(R.id.btnDeleteSchedule)).perform(click());

        //check to see if schedule was removed
        onData(allOf(instanceOf((ScheduleEntity.class)))).atPosition(0).onChildView(withId(R.id.name)).check(ViewAssertions.matches(not(withText(containsString(("testDeleteSchedule"))))));
    }

    @Test
    public void turnScheduleActiveAndBlockingNotificationTest() {
        //Espresso Code
        //go to profilelist
        onView(withId(R.id.btnAllProfiles)).perform(click());

        //click create profile
        onView(withId(R.id.btnToast)).perform(click());

        //check if goes to create profile view
        onView(withId(R.id.textView)).check(ViewAssertions.matches((withText("Profile Name"))));

        //type in profile name
        onView(withId(R.id.editTextProfileName)).perform(typeText("ActiveTestSchedule"), closeSoftKeyboard());

        //choose app to block 44 - focus
        onData(allOf(instanceOf((ApplicationInfo.class)))).atPosition(44).perform(click());

        //creates the profile
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //create schedule
        //go back
        Intent intent = new Intent();
        homeActivityRule.launchActivity(intent);

        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //type in schedule name
        onView(withId(R.id.editTextProfileName)).perform(typeText("ActiveSchedule"), closeSoftKeyboard());

        //select date
        onView(withId(R.id.btn_date)).perform(click());

        //set date
        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).perform(PickerActions.setDate(2018, 10, 30));

        //confirm date
        onView(withId(android.R.id.button1)).perform(click());

        //select time
        onView(withId(R.id.btn_time)).perform(click());

        //set time
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(21, 34));

        //confirm time
        onView(withId(android.R.id.button1)).perform(click());

        //select duration
        onView(withId(R.id.btn_duration)).perform(click());

        //set duration
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).perform(PickerActions.setTime(1, 0));

        //confirm duration
        onView(withId(android.R.id.button1)).perform(click());

        //add profile
        onData(allOf(instanceOf((ProfileEntity.class)))).atPosition(0).perform(click());

        //click create button
        onView(withId(R.id.btnCreateProfile)).perform(click());

        //go back to main activity
        homeActivityRule.launchActivity(intent);

        //click on send notification
        onView(withId(R.id.btnCreateNotify)).perform(click());

//        //UIAutomator Code
//        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        device.openNotification();
//        device.wait(Until.hasObject(By.text("Focus!")), 100000000);
//        UiObject2 title = device.findObject(By.text("Focus!"));
//        Assert.assertEquals("Focus!", title.getText());

        //UIAutomator Code
        //check if notification was blocked
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text("PK Notification")), 10000);
        UiObject2 title = device.findObject(By.text("PK Notification"));
        Assert.assertNull(title);
    }

    @Test
    public void DateMenuPopsUpTest() {
        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //select date
        onView(withId(R.id.btn_date)).perform(click());

        //check if date menu pops up
        onView(withClassName((Matchers.equalTo(DatePicker.class.getName())))).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void TimeMenuPopsUpTest() {
        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //select time
        onView(withId(R.id.btn_time)).perform(click());

        //check if time menu pops up
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void DurationMenuPopsUpTest() {
        //go to schedule list
        onView(withId(R.id.btnNavigation)).perform(click());

        //click create schedule button
        onView(withId(R.id.btnToast)).perform(click());

        //select duration
        onView(withId(R.id.btn_duration)).perform(click());

        //check if duration menu pops up
        onView(withClassName((Matchers.equalTo(TimePicker.class.getName())))).check(ViewAssertions.matches(isDisplayed()));
    }


}
