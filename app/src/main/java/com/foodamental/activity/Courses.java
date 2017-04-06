package com.foodamental.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodamental.Manifest;
import com.foodamental.R;
import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.model.FrigoObject;
import com.foodamental.util.BottomMenu;
import com.foodamental.util.MyMenu;
import com.foodamental.util.Tweet;
import com.foodamental.util.TweetAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Activité qui gère les produits du frigo
 */
public class Courses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private ListView mListView;
    private List<Tweet> tweets;
    private TweetAdapter adapter;
    private FrigoDB frigo = new FrigoDB();
    private int[] color = {R.drawable.green, R.drawable.yellow, R.drawable.red, R.drawable.black};
    private int[] categories = {R.drawable.huile,R.drawable.huile,R.drawable.huile,R.drawable.oeuf,R.drawable.steak,R.drawable.legumes};
    private SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String[] arraySpinner;
    private Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        DatabaseManager.getInstance();
        mListView = (ListView) findViewById(R.id.listviewperso);

        tweets = null;
        try {
            tweets = genererTweets();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        adapter = new TweetAdapter(Courses.this, tweets);
        mListView.setAdapter(adapter);

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

        this.arraySpinner = new String[]{
                "A-Z", "Z-A", "Date péremption"
        };
        s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapterSpinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String mselection = s.getSelectedItem().toString();
                triList(mselection);
                Toast.makeText(getApplicationContext(), "Trié par " + mselection, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
            tweets.add(new Tweet(categories[prod.getCategory()], prod.getName(), myFormat.format(prod.getDatePerempt()), prod.getIdFrigo(), getColorByDate(prod.getDatePerempt())));
        }
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

    private void triList(String param){
        if (param.equals("A-Z")) {
            adapter.sort(new Comparator<Tweet>() {
                @Override
                public int compare(Tweet lhs, Tweet rhs) {
                    return lhs.getPseudo().compareTo(rhs.getPseudo());
                }
            });
        }
        else if (param.equals("Z-A")){
            adapter.sort(new Comparator<Tweet>() {
                @Override
                public int compare(Tweet lhs, Tweet rhs) {
                    return rhs.getPseudo().compareTo(lhs.getPseudo());
                }
            });

        }
        else {
            adapter.sort(new Comparator<Tweet>() {
                @Override
                public int compare(Tweet lhs, Tweet rhs) {
                    return rhs.getText().compareTo(lhs.getText());
                }
            });
        }
    }
    /*-- buttons --*/
    /**
     * Fonction ouverture du scan
     * @param view
     */
    public void openCamera(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    /**
     * Fonction résultat scan
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if ((scanningResult != null) || (!scanningResult.equals(""))) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();

// nous récupérons le format du code barre
            String scanFormat = scanningResult.getFormatName();

            sendRequest(scanContent);


// nous affichons le résultat dans nos TextView


        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * Fonction qui envoie une nouvelle activity et le code barre
     * @param codeBar
     */
    public void sendRequest(String codeBar) {
        RequestQueue queue = Volley.newRequestQueue(this);

        final TextView scan_content = (TextView) findViewById(R.id.scan_content);
        if (codeBar != null) {
            Intent intentProduct = new Intent(this, ProductActivity.class);
            intentProduct.putExtra("codebar", codeBar);
            startActivity(intentProduct);
        }


    }

    //bottom menu

    public static Context getContext() {return context;}

    public void showFridge(View view) { }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showRecipes(View view) {BottomMenu.showRecipes(this,view);    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToHomeScreen(View view) { BottomMenu.goToHomeScreen(this,view);}

    public void goToScan(View view)
    {
        BottomMenu.goToScan(this,view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToSettings(View view)
    {
        BottomMenu.goToSettings(this,view);
    }

}