package com.pk.example.clientui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pk.example.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GoogleCalendarView extends Activity {
    String name;
    Calendar start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_calendar_view);
        TextView nameTV=(TextView)findViewById(R.id.textViewName);
        TextView startTV=(TextView)findViewById(R.id.textviewStartTime);
        TextView endTV=(TextView)findViewById(R.id.textviewEndTime);


        name = String.valueOf(getIntent().getStringExtra("name"));
        long startTime = getIntent().getLongExtra("start", 0);
        long endTime = getIntent().getLongExtra("end", 0);
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.setTimeInMillis(startTime);
        end.setTimeInMillis(endTime);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");

        nameTV.setText("Event Name: " + name);
        startTV.setText("Start Time: " + formatter.format(start.getTime()));
        endTV.setText("End Time: " + formatter.format(end.getTime()));
    }

    public void createButtonClicked(View v) {
        Intent i = new Intent(this, ScheduleViewActivity.class);

        i.putExtra("name", name);
        i.putExtra("flag", "cal");
        i.putExtra("start", start.getTimeInMillis());
        i.putExtra("end", end.getTimeInMillis());
        startActivity(i);
    }
}