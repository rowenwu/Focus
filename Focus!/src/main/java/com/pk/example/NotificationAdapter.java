package com.pk.example;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ScheduleEntity;

public class NotificationAdapter extends ArrayAdapter<MinNotificationEntity> {
    private List<MinNotificationEntity> minNotificationEntities = null;
    private Context context;

    public NotificationAdapter(Context context, int textViewResourceId,
                               List<MinNotificationEntity> minNotificationEntities) {
        super(context, textViewResourceId, minNotificationEntities);
        this.context = context;
        this.minNotificationEntities = minNotificationEntities;
    }

    @Override
    public int getCount() {
        return ((null != minNotificationEntities) ? minNotificationEntities.size() : 0);
    }

    @Override
    public MinNotificationEntity getItem(int position) {
        return ((null != minNotificationEntities) ? minNotificationEntities.get(position) : null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.notification_list_row, null);
        }

        MinNotificationEntity minNotification = minNotificationEntities.get(position);
        if (null != minNotification) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView notificationContext = (TextView) view.findViewById(R.id.notifcation_context);
            TextView timeAndDate = (TextView) view.findViewById(R.id.time_and_date);
            // ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(minNotification.getAppName());
            notificationContext.setText(minNotification.getNotificationContext());
            timeAndDate.setText(minNotification.getDate().toLocaleString().substring(0, 11));
            //holder.iconview.setImageBitmap(minNotificationEntity.getAppIcon());
        }
        return view;
    }
}
