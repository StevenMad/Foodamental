package com.foodamental.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.foodamental.R;

public class AlertPage extends Activity {
    private Switch mySwitch;
    private TextView switchStatus;
/*    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlertPage inst;
    private TextView alarmTextView;

    public static AlertPage instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_page);
/*        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);*/
        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.switch1);
        SharedPreferences sharedPrefs = getSharedPreferences("com.mobileapp.smartapplocker", MODE_PRIVATE);
        mySwitch.setChecked(sharedPrefs.getBoolean("service_status", true));

        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("Switch is currently ON");
                    SharedPreferences.Editor editor = getSharedPreferences("com.mobileapp.smartapplocker", MODE_PRIVATE).edit();
                    editor.putBoolean("service_status", mySwitch.isChecked());
                    editor.commit();
                } else {
                    switchStatus.setText("Switch is currently OFF");
                    SharedPreferences.Editor editor = getSharedPreferences("com.mobileapp.smartapplocker", MODE_PRIVATE).edit();
                    editor.putBoolean("service_status", mySwitch.isChecked());
                    editor.commit();

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main_page, menu);
        return true;
    }
}

/*
    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            //calendar.set(Calendar.DAY_OF_YEAR, alarmTimePicker.getCurrentHour());
          //  calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
           // calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(AlertPage.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlertPage.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            setAlarmText("");
            Log.d("MyActivity", "Alarm Off");
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
*/



