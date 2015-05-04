package edu.usf.cutr.opentripplanner.android;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jules on 25/03/2015.
 */
public class AlarmActivity extends FragmentActivity {

    private TimePicker timePicker;
    private EditText labelAlarm;
    private Button btnOk;
    private Button btnCancel;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        timePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        labelAlarm = (EditText) findViewById(R.id.labelAlarm);
        btnOk = (Button) findViewById(R.id.btnOkAlarm);
        btnCancel = (Button) findViewById(R.id.btnCancelAlarm);
        monday = (CheckBox) findViewById(R.id.checkBoxMonday);
        tuesday = (CheckBox) findViewById(R.id.checkBoxTuesday);
        wednesday = (CheckBox) findViewById(R.id.checkBoxWednesday);
        thursday = (CheckBox) findViewById(R.id.checkBoxThursday);
        friday = (CheckBox) findViewById(R.id.checkBoxFriday);
        saturday = (CheckBox) findViewById(R.id.checkBoxSaturday);
        sunday = (CheckBox) findViewById(R.id.checkBoxSunday);

        // Get only the first part of the departure and arrival names (before the coma)
        Bundle bundle = getIntent().getExtras();
        String departure = (bundle.getString(OTPApp.BUNDLE_KEY_RESULT_TRIP_START_LOCATION).split(","))[0];
        String arrival = (bundle.getString(OTPApp.BUNDLE_KEY_RESULT_TRIP_END_LOCATION).split(","))[0];

        // Default alarm name, and better 24h view for the time picker
        timePicker.setIs24HourView(true);
        labelAlarm.setText(departure + " → " + arrival);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Determine if the alarm should be repeated, and for which days
                final ArrayList <Integer> repeat = new ArrayList <Integer>();
                if (monday.isChecked())
                    repeat.add(Calendar.MONDAY);
                if (tuesday.isChecked())
                    repeat.add(Calendar.TUESDAY);
                if (wednesday.isChecked())
                    repeat.add(Calendar.WEDNESDAY);
                if (thursday.isChecked())
                    repeat.add(Calendar.THURSDAY);
                if (friday.isChecked())
                    repeat.add(Calendar.FRIDAY);
                if (saturday.isChecked())
                    repeat.add(Calendar.SATURDAY);
                if (sunday.isChecked())
                    repeat.add(Calendar.SUNDAY);

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, timePicker.getCurrentHour());
                intent.putExtra(AlarmClock.EXTRA_MINUTES, timePicker.getCurrentMinute());
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, labelAlarm.getText().toString());
                intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                intent.putExtra(AlarmClock.EXTRA_DAYS, repeat);
                startActivity(intent);
                finish();
            }
        });
    }
}
