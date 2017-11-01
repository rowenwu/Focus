package com.pk.example;
import com.pk.example.clientui.ScheduleViewActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Stephanie on 10/30/2017.
 */

@RunWith(MockitoJUnitRunner.Silent.class)
public class ScheduleViewActivityTest {

    ScheduleViewActivity activity = Mockito.mock(ScheduleViewActivity.class);

    @Test
    public void testDaysOfWeekArray() throws Exception {
        String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

        when(activity.getDaysOfWeek()).thenReturn(days);

        assertEquals("SUNDAY", days[0]);
        assertEquals("MONDAY", days[1]);
        assertEquals("TUESDAY", days[2]);
        assertEquals("WEDNESDAY", days[3]);
        assertEquals("THURSDAY", days[4]);
        assertEquals("FRIDAY", days[5]);
        assertEquals("SATURDAY", days[6]);
    }

    @Test
    public void testShortDaysOfWeekArray() throws Exception {
        String[] days = new String[]{"S", "M", "T", "W", "Th", "F", "Sa"};

        when(activity.getShortDaysOfWeek()).thenReturn(days);

        assertEquals("S", days[0]);
        assertEquals("M", days[1]);
        assertEquals("T", days[2]);
        assertEquals("W", days[3]);
        assertEquals("Th", days[4]);
        assertEquals("F", days[5]);
        assertEquals("Sa", days[6]);
    }

}