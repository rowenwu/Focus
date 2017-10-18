package com.pk.example;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pk.example.entity.ProfileEntity;

public class ListAdapter extends ArrayAdapter<ProfileEntity> {
    private List<ProfileEntity> profiles = null;
    private Context context;
    private boolean[] checkedState = null;

    public ListAdapter(Context context, int textViewResourceId,
                      List<ProfileEntity> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.profiles = appsList;
        checkedState = new boolean[appsList.size()];
    }

    @Override
    public int getCount() {
        return ((null != profiles) ? profiles.size() : 0);
    }

    @Override
    public ProfileEntity getItem(int position) {
        return ((null != profiles) ? profiles.get(position) : null);
    }

    public boolean changeCheckedState(int position){
        return checkedState[position] = !checkedState[position];
    }

    public ArrayList<String> getSelectedApps(){
        ArrayList<String> appPackages = new ArrayList<String>();
        for(int i = 0; i < profiles.size(); i++){
            if(checkedState[i])
                appPackages.add(profiles.get(i).getName());
        }
        return appPackages;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.profile_list_row, null);
        }

        String applicationInfo = profiles.get(position).getName();
        if (null != applicationInfo) {
            TextView appName = (TextView) view.findViewById(R.id.profile_list_content);
//            TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
//            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(applicationInfo);
//            packageName.setText(applicationInfo.packageName);
        }
        return view;
    }
}