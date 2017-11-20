package com.pk.example.clientui;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Andy on 11/16/2017.
 */

public class Holidays {

    private Map<String, String> datesToHolidays;

    public Holidays() {


    }

    public String checkIfHoliday(Calendar cal) {
//        // check if weekend
//        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
//            return false;
//        }

        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int dayOfWeekInMonth = cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        //test
        if(month == 10 && dayOfMonth == 20) {
            return "Test Holiday";
        }


        // check if New Year's Day
        if (month == 0
                && dayOfMonth == 1) {
            return " New Year's Day";
        }

        // check if Christmas
        if (month == 11
                && dayOfMonth == 25) {
            return "Christmas";
        }

        // check if 4th of July
        if (month == 3
                && dayOfMonth == 4) {
            return "4th of July";
        }

        // check Thanksgiving (4th Thursday of November)
        if (month == 10
                && dayOfWeekInMonth == 4
                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return "Thanksgiving";
        }

        // check Memorial Day (last Monday of May)
        if (month == Calendar.MAY
                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                && cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
            return "Memorial Day";
        }

        // check Labor Day (1st Monday of September)
        if (month == 8
                && dayOfWeekInMonth == 1
                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "Labor Day";
        }

        // check President's Day (3rd Monday of February)
        if (month == 1
                && dayOfWeekInMonth == 3
                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "President's Day";
        }

//        // check Veterans Day (November 11)
//        if (month == Calendar.NOVEMBER
//                && dayOfMonth == 11) {
//            return true;
//        }

        // check MLK Day (3rd Monday of January)
        if (month == 0
                && dayOfWeekInMonth == 3
                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "MLK Day";
        }

        // IF NOTHING ELSE, IT'S A BUSINESS DAY
        return "";

    }
}
