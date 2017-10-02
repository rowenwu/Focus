package com.pk.example;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CreateProfileActivity extends ListActivity {
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private AppAdapter listadaptor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        packageManager = getPackageManager();

        new LoadApplications().execute();
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        getListView().setItemChecked(position, true);

//        ApplicationInfo app = applist.get(position);
//        try {
//            Intent intent = packageManager
//                    .getLaunchIntentForPackage(app.packageName);
//
//            if (null != intent) {
//                startActivity(intent);
//            }
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(CreateProfileActivity.this, e.getMessage(),
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Toast.makeText(CreateProfileActivity.this, e.getMessage(),
//                    Toast.LENGTH_LONG).show();
//        }
    }

//    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
//        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
//        for (ApplicationInfo info : list) {
//            try {
//                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
//                    applist.add(info);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return applist;
//    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
//            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            listadaptor = new AppAdapter(CreateProfileActivity.this,
                    R.layout.snippet_list_row, applist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CreateProfileActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}