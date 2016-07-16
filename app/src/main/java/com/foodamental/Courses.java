package com.foodamental;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Courses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Un adapteur pour gérer les données à afficher dans le spinner
    ArrayAdapter<String> adapter;
    //Liste de données à passer dans l'adapteur afin de les voir afficher dans le spinner
    ArrayList<String> data = new ArrayList<String>();

    ListView mListView;
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

        mListView = (ListView) findViewById(R.id.listView);

        List<Tweet> tweets = new ArrayList<>();
        try {
            tweets = genererTweets();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TweetAdapter adapter = new TweetAdapter(Courses.this, tweets);
        mListView.setAdapter(adapter);
    }

    public void addProduit(View view)
    {

        //EditText edittext = (EditText) findViewById(R.id.edit_text);
        //String message = edittext.getText().toString();
        //Spinner spinner = (Spinner)findViewById(R.id.spinnerProduit);
        //adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, data);
        //spinner.setAdapter(adapter);
        //if (message != null) {
          //  adapter.add(message);
        //}
        //Toast.makeText(Courses.this, message, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this,this,item);
    }

    private List<Tweet> genererTweets() throws ParseException {
        int color[] = {Color.BLACK,Color.BLUE,Color.GREEN, Color.RED,Color.GRAY};
        int rand;
        List<Tweet> tweets = new ArrayList<Tweet>();
        FrigoDB frigo = new FrigoDB();
        List<ProductDTO> products = new ArrayList<>();
        products = frigo.getAllProduct();
        for (ProductDTO prod : products) {
            rand = randInt(0,4);
            tweets.add(new Tweet(color[rand],prod.getName() ,prod.getBrand()));

        }

        return tweets;
    }
    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
