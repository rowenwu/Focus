package com.pk.example;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pk.example.entity.ScheduleEntity;

public class ScheduleAdapter extends ArrayAdapter<ScheduleEntity> {
    private List<ScheduleEntity> scheduleEntities = null;
    private Context context;

    public ScheduleAdapter(Context context, int textViewResourceId,
                               List<ScheduleEntity> scheduleEntities) {
        super(context, textViewResourceId, scheduleEntities);
        this.context = context;
        this.scheduleEntities = scheduleEntities;
    }

    @Override
    public int getCount() {
        return ((null != scheduleEntities) ? scheduleEntities.size() : 0);
    }

    @Override
    public ScheduleEntity getItem(int position) {
        return ((null != scheduleEntities) ? scheduleEntities.get(position) : null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.notification_list_row, null);
        }

        ScheduleEntity schedule = scheduleEntities.get(position);
        if (null != schedule) {
            TextView scheduleName = (TextView) view.findViewById(R.id.scheduleName);
//            TextView notificationContext = (TextView) view.findViewById(R.id.notifcation_context);
//            TextView timeAndDate =(TextView) view.findViewById(R.id.time_and_date);
//            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            scheduleName.setText(schedule.getName());
            //still need to set info
            //appName.setText(applicationInfo.loadLabel(packageManager));
            //packageName.setText(applicationInfo.packageName);
            //iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }
        return view;
    }
}
