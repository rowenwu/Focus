package com.pk.example;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class DurationPickerDialog extends TimePickerDialog {

    public DurationPickerDialog(Context context, int theme,
                                OnTimeSetListener callBack, int hour, int minute) {
        super(context, theme, callBack, hour, minute, true);
        updateTitle(hour, minute);
    }

    public DurationPickerDialog(Context context, OnTimeSetListener callBack,
                                int hour, int minute) {
        super(context, callBack, hour, minute, true);
        updateTitle(hour, minute);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hour, int minute) {
        super.onTimeChanged(view, hour, minute);
        updateTitle(hour, minute);
    }

    public void updateTitle(int hour, int minute) {
        setTitle("Duration: " + hour + ":" + formatNumber(minute));
    }

    private String formatNumber(int number) {
        String result = "";
        if (number < 10) {
            result += "0";
        }
        result += number;

        return result;
    }
}