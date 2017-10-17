package com.pk.example;

import java.util.ArrayList;
import java.util.List;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ScheduleEntity;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ListItemViewHolder> {

    private LiveData<List<MinNotificationEntity>> notificationList;
    private Context context;

    public NotificationAdapter(LiveData<List<MinNotificationEntity>> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.notification_list_row, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, int position) {
        MinNotificationEntity minNotificationEntity = notificationList.getValue().get(position);
        holder.appName.setText(minNotificationEntity.getAppName());
        holder.notificationContext.setText(minNotificationEntity.getNotificationContext());
        holder.timeAndDate.setText(minNotificationEntity.getDate().toLocaleString().substring(0, 11));
        //holder.iconview.setImageBitmap(minNotificationEntity.getAppIcon());
    }

    @Override
    public int getItemCount() {
        return notificationList.getValue().size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView appName;
        private TextView notificationContext;
        private TextView timeAndDate;
        private ImageView iconview;

        ListItemViewHolder(View view) {
            super(view);
            appName = (TextView) view.findViewById(R.id.app_name);
            notificationContext = (TextView) view.findViewById(R.id.notifcation_context);
            timeAndDate = (TextView) view.findViewById(R.id.time_and_date);
            iconview = (ImageView) view.findViewById(R.id.app_icon);
        }
    }
}