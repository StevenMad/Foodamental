package com.foodamental.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.foodamental.R;
import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.model.FrigoObject;
import com.foodamental.util.MyMenu;
import com.foodamental.util.Tweet;
import com.foodamental.util.TweetAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Activité qui gère les produits du frigo
 */
public class Courses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    private List<Tweet> tweets;
    private TweetAdapter adapter;
    private FrigoDB frigo = new FrigoDB();
    private int[] color = {Color.GREEN, Color.YELLOW, Color.RED, Color.BLACK};
    private SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

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
                AlertDialog.Builder adb = new AlertDialog.Builder(Courses.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("iddddd", tweets.get(position).getId().toString());
                        frigo.deleteProductWithId(tweets.get(position).getId());

                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });
    }

    /**
     * Fonction qui remplit les éléments qui vont remplir la liste view
     *
     * @return
     * @throws ParseException
     */
    private List<Tweet> genererTweets() throws ParseException {
        List<Tweet> tweets = new ArrayList<Tweet>();

        List<FrigoObject> produit = frigo.getAllProduct();
        for (FrigoObject prod : produit) {
            tweets.add(new Tweet(getColorByDate(prod.getDatePerempt()), prod.getName(), myFormat.format(prod.getDatePerempt()), prod.getIdFrigo()));
        }
        //tweets.add(new Tweet(Color.BLACK, "Florent", "Mon premier tweet !"));
        //tweets.add(new Tweet(Color.BLUE, "Kevin", "C'est ici que ça se passe !"));
        //tweets.add(new Tweet(Color.GREEN, "Logan", "Que c'est beau..."));
        //tweets.add(new Tweet(Color.RED, "Mathieu", "Il est quelle heure ??"));
        //tweets.add(new Tweet(Color.GRAY, "Willy", "On y est presque"));
        return tweets;
    }

    /**
     * Fonction qui gère la navigation du menu
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this, this, item);
    }

    private int getColorByDate(Date date) {
        Date dateCurrent = new Date();
        long diff = date.getTime() - dateCurrent.getTime();
        long result = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (result >= 30)
            return color[0];
        else if (result < 30 && result > 7)
            return color[1];
        else if (result < 7 && result > 0)
            return color[2];
        else
            return color[3];
    }


}