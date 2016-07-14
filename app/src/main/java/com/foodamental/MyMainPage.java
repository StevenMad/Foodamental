package com.foodamental;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;

public class MyMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String URL = "http://fr.openfoodfacts.org/api/v0/produit/";
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
        setContentView(R.layout.activity_my_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //addContentView(textView1,this.getLayoutInflater());
        TextView textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();

        /*-----DB----*/
        UserDB userdb = new UserDB();
        FoodUser user = new FoodUser();

        user.setUsername("brioche");
        user.setEmail("toto");
        user.setPassword("mmlmllm");
        userdb.addUser(user);
        int result1 = userdb.getALLUser().size();

        Log.i("data","taille user est " + result1 + "");
        ProductDB productdb = new ProductDB();

        ProductObject product = new ProductObject();
        product.setId(233233);
        product.setName("brioche");
        product.setBrand("toto");
        product.setImage("mmlmllm");
        productdb.addProduct(product);
        int result = productdb.getALLProduct().size();
        Log.i("data","taille est " + result + "");
        // dbhelp.updateFoodUser(1);
        /*-----------*/


        //recupération du json à la création
        try {
            FileInputStream filein = openFileInput("connexion.json");
            InputStreamReader reader = new InputStreamReader(filein);
            char[] inputReadBuffer = new char[1024];
            String s = "";
            int charRead;
            while ((charRead = reader.read(inputReadBuffer)) > 0) {
                String readString = String.copyValueOf(inputReadBuffer, 0, charRead);
                s += readString;
            }
            reader.close();
            JSONObject json = new JSONObject(s);
            String value = json.getString("prenom");
            textView.setText("Bonjour " + value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (JSONException e) {
            Toast.makeText(MyMainPage.this, "text is not in json format", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String hist = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AlertPage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this, this, item);
    }

    public void openCamera(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

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

    public void sendRequest(String codeBar) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = this.URL;
        url += codeBar + ".json";
        final TextView scan_content = (TextView) findViewById(R.id.scan_content);
        Intent intentProduct = new Intent(this, ProductActivity.class);
        intentProduct.putExtra("url", url);
        startActivity(intentProduct);


    }
    public static Context getContext(){
        return context;
    }

}
