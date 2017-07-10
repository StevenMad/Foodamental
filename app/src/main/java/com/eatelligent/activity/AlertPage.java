package com.eatelligent.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eatelligent.HomeActivity;
import com.eatelligent.R;
import com.eatelligent.dao.dbimpl.FrigoDB;
import com.eatelligent.dao.model.FrigoObject;
import com.eatelligent.util.AlarmReceiver;
import com.eatelligent.util.BottomMenu;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activité pour les alertes
 */
public class AlertPage extends Activity implements View.OnClickListener {

    static Button btn;
    private static TextView dateView;
    private static TextView timeView;
    private RadioButton buttonNo;
    private RadioButton buttonAuto;
    private RadioButton buttonPerso;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private Date date;
    private RelativeLayout layoutPerso;

    private SharedPreferences sharedPreferences;

    /**
     * Fonction qui récupère la date
     */
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            int day = arg3;
            int month = arg2;
            int year = arg1;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            date = calendar.getTime();

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    /**
     * Création de l'alarme
     *
     * @param ctx
     */
    public static void createNotification(Context ctx, int type) {

        int interval = 24 * 60 * 60 * 1000;
        String[] time;
        String[] date;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intent1 = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);

        if (type == 1) {
            time = timeView.getText().toString().split(":");
            date = dateView.getText().toString().split("/");
            calendar.set(Calendar.DATE, Integer.parseInt(date[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(date[2]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

            Log.d("STATE" , "diff = " + (calendar.getTimeInMillis() - System.currentTimeMillis()));
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else if (type == 0) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 00);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);

        }



    }

    /**
     * Fonction qui récupère le date du datePicker
     *
     * @param datePicker
     * @return
     */
    public static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrigoDB frigo = new FrigoDB();
        List<FrigoObject> list = frigo.getAllProduct(7);

        setContentView(R.layout.alert_page);
        layoutPerso = (RelativeLayout) findViewById(R.id.layoutForPerso);
        buttonNo = (RadioButton) findViewById(R.id.choiceNoAlert);
        buttonAuto = (RadioButton) findViewById(R.id.choiceAlertAuto);
        buttonPerso = (RadioButton) findViewById(R.id.choiceAlertPerso);
        Button eReminderTime = (Button) findViewById(R.id.buttonTime);
        btn = (Button) findViewById(R.id.buttonAdd);
        timeView = (TextView) findViewById(R.id.textViewTime);
        btn.setOnClickListener(this);
        eReminderTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AlertPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeView.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean bool = sharedPreferences.getBoolean("buttonPerso", false);

        if (!bool)
            layoutPerso.setVisibility(RelativeLayout.GONE);
        else
            layoutPerso.setVisibility(RelativeLayout.VISIBLE);

        buttonNo.setChecked(sharedPreferences.getBoolean("buttonNo", false));
        buttonAuto.setChecked(sharedPreferences.getBoolean("buttonAuto", false));
        buttonPerso.setChecked(sharedPreferences.getBoolean("buttonPerso", false));
        // SharedPreferences sharedPrefs = getSharedPreferences("com.mobileapp.smartapplocker", MODE_PRIVATE);
     /*   mySwitch.setChecked(sharedPrefs.getBoolean("service_status", false));

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
                    createNotification(AlertPage.this);
                } else {
                    switchStatus.setText("Switch is currently OFF");
                    SharedPreferences.Editor editor = getSharedPreferences("com.mobileapp.smartapplocker", MODE_PRIVATE).edit();
                    editor.putBoolean("service_status", mySwitch.isChecked());
                    editor.commit();
                    cancelNotification();
                }

            }
        });*/
        dateView = (TextView) findViewById(R.id.textViewDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main_page, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);

    }

    /**
     * Annulation de l'alarme
     */
    public void cancelNotification() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);
    }
    //bottom menu

    public void onRadioClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "choix " + ((RadioButton) view).getText(), duration);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("buttonNo", buttonNo.isChecked());
        editor.putBoolean("buttonAuto", buttonAuto.isChecked());
        editor.putBoolean("buttonPerso", buttonPerso.isChecked());

        editor.apply();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.choiceNoAlert:
                if (checked) {
                    layoutPerso.setVisibility(RelativeLayout.GONE);
                    toast.show();
                }
                // Remove the meat
                break;
            case R.id.choiceAlertAuto:
                if (checked) {
                    layoutPerso.setVisibility(RelativeLayout.GONE);
                    cancelNotification();
                    createNotification(AlertPage.this, 0);
                    toast.show();


                }

                break;
            case R.id.choiceAlertPerso:
                if (checked) {
                    toast.show();
                    layoutPerso.setVisibility(RelativeLayout.VISIBLE);
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showFridge(View view) {
        BottomMenu.showFridge(this, view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showRecipes(View view) {
        BottomMenu.showRecipes(this, view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToHomeScreen(View view) {
        BottomMenu.goToHomeScreen(this, view);
    }

    public void goToScan(View view) {
        BottomMenu.goToScan(this, view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToSettings(View view) {
    }

    /**
     * Fonction qui remplit le text view
     *
     * @param year
     * @param month
     * @param day
     */
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * Fonction qui envoie un message
     *
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Choisis ta date", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Fonction qui retorune le datepicker
     *
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    /**
     * Fonction qui add dans bdd
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (dateView.getText().equals("") || timeView.getText().equals("")) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(AlertPage.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(AlertPage.this);
            }
            builder.setTitle("Attention")
                    .setMessage("Choisissez votre date et votre temps pour votre notification")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            cancelNotification();
            createNotification(AlertPage.this, 1);
            Toast.makeText(getApplicationContext(), "Notification ajoutée", Toast.LENGTH_SHORT)
                    .show();
        }


    }


}



