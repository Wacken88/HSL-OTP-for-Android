package edu.usf.cutr.opentripplanner.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.widget.LinearLayout.*;

/**
 * Created by michael on 3/27/15.
 */
public class OTPAppWidgetLocationActivity extends Activity {
    private EditText location;
    private LinearLayout frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_widget);
        frame = (LinearLayout) findViewById(R.id.locationFrame);
        location = (EditText)findViewById(R.id.locationTextField);
        Intent startActivityIntent = getIntent();
        Rect positionButton = startActivityIntent.getSourceBounds();
        Log.i(OTPApp.TAG,"Position of the Intent bounds: " + positionButton.bottom + " " + positionButton.left +
        " "+ positionButton.right + " " + positionButton.top);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(positionButton.width(),positionButton.height());
        params.setMargins(positionButton.left,positionButton.top,positionButton.right,positionButton.bottom);
        location.setLayoutParams(params);
        frame.removeAllViews();
        frame.addView(location,params);
        frame.refreshDrawableState();
        setContentView(frame);
        location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (getIntent().getAction() == OTPApp.INTENT_GET_MY_LOCATION_STRING) {
                        Log.i(OTPApp.TAG, "get Editor action with context " + v.getContext().toString() + " Text " + v.getText() + " and action code " + OTPApp.INTENT_GET_MY_LOCATION_STRING);
                        OTPAppWidgetLocationActivity.sendMessage(v.getContext(), v.getText().toString(), OTPApp.INTENT_GET_MY_LOCATION_STRING);
                        finish();
                        handled = true;
                    } else if (getIntent().getAction() == OTPApp.INTENT_GET_END_LOCATION_STRING) {
                        Log.i(OTPApp.TAG, "get Editor action with context " + v.getContext().toString() + " Text " + v.getText() + " and action code " + OTPApp.INTENT_GET_END_LOCATION_STRING);
                        OTPAppWidgetLocationActivity.sendMessage(v.getContext(), v.getText().toString(), OTPApp.INTENT_GET_END_LOCATION_STRING);
                        finish();
                        handled = true;
                    } else {
                        Log.e(OTPApp.TAG, "Action is unknown for location activity");
                    }
                }
                return handled;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void sendMessage(Context context,String locationText, String action){
        Intent uiIntent;
        switch(action){
            case OTPApp.INTENT_GET_MY_LOCATION_STRING:
                uiIntent = new Intent(OTPApp.SEND_MY_LOCATION_STRING);
                uiIntent.putExtra(OTPApp.SEND_MY_LOCATION_STRING,locationText);
                context.sendBroadcast(uiIntent);
                break;
            case OTPApp.INTENT_GET_END_LOCATION_STRING:
                uiIntent = new Intent(OTPApp.SEND_END_LOCATION_STRING);
                uiIntent.putExtra(OTPApp.SEND_END_LOCATION_STRING,locationText);
                context.sendBroadcast(uiIntent);
                break;
            default:
                Log.e(OTPApp.TAG,"No action defined for the LocationActivity");
                break;
        }

    }
}
