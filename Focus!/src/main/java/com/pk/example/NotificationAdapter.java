package com.pk.example;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;

public class NotificationAdapter extends ArrayAdapter<PrevNotificationEntity> {
    private List<PrevNotificationEntity> notifications = null;
    private Context context;

    public NotificationAdapter(Context context, int textViewResourceId,
                               List<PrevNotificationEntity> notifications) {
        super(context, textViewResourceId, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return ((null != notifications) ? notifications.size() : 0);
    }

    @Override
    public PrevNotificationEntity getItem(int position) {
        return ((null != notifications) ? notifications.get(position) : null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.notification_list_row, null);
        }

        PrevNotificationEntity minNotification = notifications.get(position);
        if (null != minNotification) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView notificationContext = (TextView) view.findViewById(R.id.notifcation_context);
            TextView timeAndDate = (TextView) view.findViewById(R.id.time_and_date);
            // ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(minNotification.getAppName());
            notificationContext.setText(minNotification.getNotificationContext());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Or whatever format fits best your needs.
            timeAndDate.setText(sdf.format(minNotification.getDate()));
            //holder.iconview.setImageBitmap(minNotificationEntity.getAppIcon());
        }
        return view;
    }
}
