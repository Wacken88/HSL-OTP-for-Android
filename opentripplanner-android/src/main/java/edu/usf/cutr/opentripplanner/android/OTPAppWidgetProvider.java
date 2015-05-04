package edu.usf.cutr.opentripplanner.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.usf.cutr.opentripplanner.android.fragments.MainFragment;

/**
 * Created by michael on 3/25/15.
 */
public class OTPAppWidgetProvider extends AppWidgetProvider {
    private String startLocation = "";
    private String endLocation = "";
    private String fileNameStartLocation = "StartLocation";
    private String fileNameEndLocation = "EndLocation";
    private MainFragment mainFragment;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent myLocationIntent = new Intent(context, OTPAppWidgetLocationActivity.class);
            myLocationIntent.setAction(OTPApp.INTENT_GET_MY_LOCATION_STRING);
            PendingIntent actionPendingIntentMyLocation = PendingIntent.getActivity(context,0,myLocationIntent,0);

            Intent getEndLocationIntent = new Intent(context,OTPAppWidgetLocationActivity.class);
            getEndLocationIntent.setAction(OTPApp.INTENT_GET_END_LOCATION_STRING);
            PendingIntent actionPendingIntentEndLocation = PendingIntent.getActivity(context, 0, getEndLocationIntent, 0);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_homescreen_layout);
            views.setOnClickPendingIntent(R.id.myLocationAddress,actionPendingIntentMyLocation);
            views.setOnClickPendingIntent(R.id.endLocationAddress,actionPendingIntentEndLocation);
            views.setOnClickPendingIntent(R.id.searchButton,PendingIntent.getActivity(context, 0, createSearchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void onReceive(Context context, Intent intent){
        Log.i(OTPApp.TAG, "on received called with" + intent.getAction());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_homescreen_layout);
        if(intent.getAction().equals(OTPApp.SEND_END_LOCATION_STRING)){
            endLocation = intent.getExtras().getString(OTPApp.SEND_END_LOCATION_STRING);
            try {
                FileOutputStream fos = context.openFileOutput(fileNameEndLocation,Context.MODE_PRIVATE);
                fos.write(endLocation.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            views.setTextViewText(R.id.endLocationAddress, endLocation);
            views.setOnClickPendingIntent(R.id.searchButton, PendingIntent.getActivity(context, 0, createSearchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT));
        } else if(intent.getAction().equals(OTPApp.SEND_MY_LOCATION_STRING)){
            startLocation = intent.getExtras().getString(OTPApp.SEND_MY_LOCATION_STRING);
            try {
                FileOutputStream fos = context.openFileOutput(fileNameStartLocation,Context.MODE_PRIVATE);
                fos.write(startLocation.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            views.setTextViewText(R.id.myLocationAddress, startLocation);
            views.setOnClickPendingIntent(R.id.searchButton, PendingIntent.getActivity(context, 0, createSearchIntent(context), PendingIntent.FLAG_UPDATE_CURRENT));
        }
        else {
            super.onReceive(context, intent);
        }
        ComponentName cn = new ComponentName(context, OTPAppWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(cn, views);
    }

    private Intent createSearchIntent(Context context){
        Intent searchIntent = new Intent();
        searchIntent.setAction(OTPApp.INTENT_WIDGET_SEARCH);
        StringBuilder sb = new StringBuilder();
        try{
            FileInputStream fis = context.openFileInput(fileNameStartLocation);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startLocation = sb.toString();
        sb = new StringBuilder();
        try{
            FileInputStream fis = context.openFileInput(fileNameEndLocation);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        endLocation = sb.toString();
        Log.d(OTPApp.TAG,"Widget StartLocation: " + startLocation + " and EndLocation: " + endLocation);
        searchIntent.putExtra("StartLocation", startLocation);
        searchIntent.putExtra("EndLocation", endLocation);
        searchIntent.setType("text/plain");
        searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return searchIntent;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
