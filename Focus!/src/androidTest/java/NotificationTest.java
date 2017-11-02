import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.pk.example.clientui.MainActivity;
import com.pk.example.R;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by niccolashernandez on 10/30/17.
 */

@RunWith(AndroidJUnit4.class)
public class NotificationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void createNotificationAndCheckNotificationCenterTest() {
        //Espresso Code
        onView(withId(R.id.btnCreateNotify)).perform(click());

        //UIAutomator Code
        //get device
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //open notifcation center
        device.openNotification();

        //wait till notification sent, or time out
        device.wait(Until.hasObject(By.text("PK Notification")), 10000);

        //check notification info
        UiObject2 title = device.findObject(By.text("PK Notification"));
        UiObject2 text = device.findObject(By.text("Notification Listener Example"));

        //check notifcation info
        Assert.assertEquals("PK Notification", title.getText());
        Assert.assertEquals("Notification Listener Example", text.getText());
    }
}
