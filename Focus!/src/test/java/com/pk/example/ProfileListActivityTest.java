package com.pk.example;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by kathe on 10/30/2017.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ProfileListActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
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
//    public void CreateButtonClicked() throws Exception
//    {
//
//        Button button = (Button) activity.findViewById( R.id.btnToast );
//
//        button.performClick();
//        ShadowActivity shadowActivity = shadowOf(activity);
//        // 9
//        Intent startedIntent = shadowActivity.getNextStartedActivity();
//        // 10
//        ShadowIntent shadowIntent = shadowOf(startedIntent);
//        assertThat(shadowIntent.getIntentClass().getName(), equalTo(ScheduleViewActivity.class.getName()));
//
//    }




}