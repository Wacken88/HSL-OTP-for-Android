package edu.usf.cutr.opentripplanner.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jules on 24/04/2015.
 */
public class NotificationService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public NotificationService() {
        super("NotificationService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String tripDescription = bundle.getString(OTPApp.TRIP_DESCRIPTION);
        String tripTime = bundle.getString(OTPApp.BUNDLE_KEY_TRIP_TIME);
        int tripHour = bundle.getInt(OTPApp.BUNDLE_KEY_TRIP_HOUR);
        int tripMinutes = bundle.getInt(OTPApp.BUNDLE_KEY_TRIP_MINUTES);

        // Calculation of the time difference; hours modulo 24 to get rid of negative remaining time
        Calendar calendar = new GregorianCalendar();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);
        if (tripHour - currentHour < 0)
            tripHour += 24;
        int minutesDiff = (tripMinutes - currentMinutes) + (tripHour - currentHour) * 60;
        String timeSummary = minutesDiff + " " + getString(R.string.step_by_step_choose_itinerary_notifications_remaining_time_begin) + tripTime + getString(R.string.step_by_step_choose_itinerary_notifications_remaining_time_end);

        // Creation and display of the notification (case of already outdated trip to be taken into consideration)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(tripDescription);
        if (minutesDiff > 0) {
            builder.setContentText(timeSummary);
        }
        else if (minutesDiff == 0) {
            builder.setContentText(getString(R.string.step_by_step_choose_itinerary_notifications_departure_now));
        }
        else {
            builder.setContentText(getString(R.string.step_by_step_choose_itinerary_notifications_departure_over));
        }
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(OTPApp.NOTIFICATION_ID, builder.build());

        // Loop for updating the notification (checks if the minute has changed every ten seconds)
        while (true) {
            try {
                Thread.sleep(10000);
            }
            catch (InterruptedException e) {
            }
            Calendar cal = new GregorianCalendar();
            int curHour = cal.get(Calendar.HOUR_OF_DAY);
            int curMinutes = cal.get(Calendar.MINUTE);
            int minDiff = (tripMinutes - curMinutes) + (tripHour - curHour) * 60;

            if (minDiff == 0) {
                String notifContent = getString(R.string.step_by_step_choose_itinerary_notifications_departure_now);
                builder.setContentText(notifContent);
                manager.notify(OTPApp.NOTIFICATION_ID, builder.build());
            }
            else if (minDiff < 0) {
                String notifContent = getString(R.string.step_by_step_choose_itinerary_notifications_departure_over);
                builder.setContentText(notifContent);
                manager.notify(OTPApp.NOTIFICATION_ID, builder.build());
                break;
            }
            else {
                String timeSumm = minDiff + " " + getString(R.string.step_by_step_choose_itinerary_notifications_remaining_time_begin) + tripTime + getString(R.string.step_by_step_choose_itinerary_notifications_remaining_time_end);
                builder.setContentText(timeSumm);
                manager.notify(OTPApp.NOTIFICATION_ID, builder.build());
            }
        }
    }
}