package com.foodamental.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.foodamental.util.AlarmReceiver;
import com.foodamental.dao.FrigoDB;
import com.foodamental.model.FrigoObject;
import com.foodamental.util.MyMenu;
import com.foodamental.R;
import com.foodamental.util.Tweet;
import com.foodamental.util.TweetAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlertPage extends Activity {

    AlarmManager alarmManager;
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_page);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

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

    public static class Courses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

        ListView mListView;
        List<Tweet> tweets;
        TweetAdapter adapter;
        FrigoDB frigo = new FrigoDB();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_courses);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            mListView = (ListView) findViewById(R.id.listviewperso);

            tweets = null;
            try {
                tweets = genererTweets();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            adapter = new TweetAdapter(Courses.this, tweets);
            mListView.setAdapter(adapter);
            List<Tweet> tweets2 = tweets;

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(Courses.this);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete " + position);
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("iddddd",tweets.get(position).getId().toString());
                            frigo.deleteProductWithId(tweets.get(position).getId());

                            adapter.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();
                        }});
                    adb.show();
                }
            });
        }

        private List<Tweet> genererTweets() throws ParseException {
            List<Tweet> tweets = new ArrayList<Tweet>();

            List<FrigoObject> produit = frigo.getAllProduct();
            for (FrigoObject prod : produit){
             tweets.add(new Tweet(Color.BLACK, prod.getName(), prod.getBrand(), prod.getIdFrigo()));
            }
            //tweets.add(new Tweet(Color.BLACK, "Florent", "Mon premier tweet !"));
            //tweets.add(new Tweet(Color.BLUE, "Kevin", "C'est ici que ça se passe !"));
            //tweets.add(new Tweet(Color.GREEN, "Logan", "Que c'est beau..."));
            //tweets.add(new Tweet(Color.RED, "Mathieu", "Il est quelle heure ??"));
            //tweets.add(new Tweet(Color.GRAY, "Willy", "On y est presque"));
            return tweets;
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            return MyMenu.onNavigationItemSelected(this,this,item);
        }



    }
}
