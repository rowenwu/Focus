package com.pk.example.clientui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.pk.example.R;
import com.pk.example.servicereceiver.NLService;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity {

    public TextView txtView;
//    private NotificationReceiver nReceiver;
    private Context context;
    static final int REQUEST_CAMERA_FACEBOOK = 0;
    static final int SELECT_FILE_FACEBOOK = 1;
    ShareDialog shareDialog;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String IMAGE_TYPES = "image/*";
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        nReceiver = new NotificationReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(NLService.INSERT_NOTIFICATION);
        //        registerReceiver(nReceiver,filter);

        Twitter.initialize(this);
        TweetComposer.getInstance();

        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);

        // Check for usage stats permission, needed to detect app running in foreground
        if(!hasUsageStatsPermission())
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

        ComponentName cn = new ComponentName(getApplicationContext(), NLService.class);
        String flat = Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        if(!enabled){
            toggleService();
        }

        shareDialog = new ShareDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(nReceiver);
    }

    public void toggleService() {
        if (Build.VERSION.SDK_INT >= 18)
            gotoNotifyservice(this);
        else
            gotoAccessibility(this);
    }
    public static void gotoNotifyservice(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            try {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                context.startActivity(intent);
                Toast.makeText(context, context.getText(R.string.notification_listener_not_found_detour), Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException anfe2) {
                Toast.makeText(context, anfe2.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void gotoAccessibility(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
            Toast.makeText(context, context.getText(R.string.accessibility_toast), Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException anfe) {
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                Toast.makeText(context, context.getText(R.string.accessibility_not_found_detour), Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException anfe2) {
                Toast.makeText(context, anfe2.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("PK Notification");
            ncomp.setContentText("Notification Listener Example");
            ncomp.setTicker("Notification Listener Example");
            ncomp.setSmallIcon(R.drawable.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnNavigation){
            Intent i = new Intent(this, ScheduleListActivity.class);
            startActivity(i);
        }
//        else if(v.getId() == R.id.btnPermission){
//            toggleService(v);
//        }
        else if(v.getId() == R.id.btnAllProfiles){
            Intent i = new Intent(this, ProfileListActivity.class);
            i.putExtra("flag","create");
            startActivity(i);
        }
        else if(v.getId() == R.id.btnNotificationList){
            Intent i = new Intent(this, NotificationListActivity.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.btnWeeklyView){
            Intent i = new Intent(this, CalendarActivity.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.btnShareFacebook){
                launchPhotoShareDialog(0);
        }
        else if (v.getId() == R.id.btnShareTwitter) {
            launchPhotoShareDialog(2);
        }
    }


    public boolean hasUsageStatsPermission(){
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }


    private void launchPhotoShareDialog(final int offset) {
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            final CharSequence[] items = { "Take Photo", "Choose from Library",
                    "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Share what you've been focusing on");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_CAMERA_FACEBOOK + offset);
                        dispatchTakePictureIntent(offset);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE_FACEBOOK + offset);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                    101, galleryPermissions);
            launchPhotoShareDialog(2);
        }

    }

    private void dispatchTakePictureIntent(int offset) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.pk.example.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_FACEBOOK + offset);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE_FACEBOOK) {
                shareGalleryPhotoFacebook(data);
            }
            else if (requestCode == REQUEST_CAMERA_FACEBOOK)
                shareTakenPhotoFacebook(data);
            else {
                launchTwitterShareDialog(data.getData());
            }
        }
    }

    private void shareGalleryPhotoFacebook(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Bitmap thumbnail;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
        launchFacebookShareDialog(thumbnail);
    }

    private void shareTakenPhotoFacebook(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        launchFacebookShareDialog(thumbnail);
    }

    public void launchFacebookShareDialog(Bitmap imagePath){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("Share what you've been focusing on")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.show(content);
    }

    void launchTwitterShareDialog(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        intent.setPackage("com.twitter.android");
        startActivity(intent);
    }
//    class NotificationReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
//            Log.i("intent ","intent "+intent.getExtras().toString());
//
//
//            String temp = intent.getExtras().getString("info")+ "\n----------------------------------------------" + txtView.getText();
//            txtView.setText(temp+"");
//        }
//    }
}
