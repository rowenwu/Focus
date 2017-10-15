//package com.pk.example;
//import java.util.ArrayList;
//import java.util.List;
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import android.widget.ArrayAdapter;
//
///**
// * Created by Andy on 10/6/2017.
// */
//
//public class NotificationAdapter extends ArrayAdapter<Notification> {
//    private List<Notification> notificationList = null;
//    private Context context;
//
//    public NotificationAdapter(Context context, int textViewResourceId,
//                               List<Notification> notificationList) {
//        super(context, textViewResourceId, notificationList);
//        this.context = context;
//        this.notificationList = notificationList;
//    }
//
//    @Override
//    public int getCount() {
//        return ((null != notificationList) ? notificationList.size() : 0);
//    }
//
//    @Override
//    public Notification getItem(int position) {
//        return ((null != notificationList) ? notificationList.get(position) : null);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        if (null == view) {
//            LayoutInflater layoutInflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = layoutInflater.inflate(R.layout.notification_list_row, null);
//        }
//
//        Notification notifcation = notificationList.get(position);
//        if (null != notifcation) {
//            TextView appName = (TextView) view.findViewById(R.id.app_name);
//            TextView notificationContext = (TextView) view.findViewById(R.id.notifcation_context);
//            TextView timeAndDate =(TextView) view.findViewById(R.id.time_and_date);
//            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
//
//            //still need to set info
//            //appName.setText(applicationInfo.loadLabel(packageManager));
//            //packageName.setText(applicationInfo.packageName);
//            //iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
//        }
//        return view;
//    }
//}
