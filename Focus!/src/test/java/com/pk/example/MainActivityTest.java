package com.pk.example;

import com.pk.example.clientui.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
/**
 * Created by kathe on 10/30/2017.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
//@Config(constants = BuildConfig.class,
//        manifest="some/build/path/AndroidManifest.xml",
//        shadows={ShadowFoo.class, ShadowBar.class})
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .resume()
                .visible()
                .get();
    }


    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveCorrectAppName() throws Exception {
        String hello = activity.getResources().getString(R.string.app_name);
        assertThat(hello, equalTo("Focus!"));
    }

//    @Test
//    public void schedulesButtonTest() throws Exception
//    {
//
//        Button button = (Button) activity.findViewById( R.id.btnNavigation );
//
//        button.performClick();
//        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
//        assertEquals(ScheduleListActivity.class.getCanonicalName(), intent.getComponent().getClassName());
//    }
//
//    @Test
//    public void profilesButtonTest() throws Exception
//    {
//        Button button = (Button) activity.findViewById( R.id.btnAllProfiles );
//        button.performClick();
//        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
//        assertEquals(ScheduleListActivity.class.getCanonicalName(), intent.getComponent().getClassName());
//
//    }
//
//    @Test
//    public void notificationsButtonTest() throws Exception
//    {
//        Button button = (Button) activity.findViewById( R.id.btnNotificationList );
//        button.performClick();
//        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
//        assertEquals(NotificationListActivity.class.getCanonicalName(), intent.getComponent().getClassName());
//    }




}