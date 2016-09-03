package com.foodamental.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodamental.R;
import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.dbimpl.ProductDB;
import com.foodamental.dao.dbimpl.UserDB;
import com.foodamental.dao.model.FoodUser;
import com.foodamental.dao.model.FrigoObject;
import com.foodamental.dao.model.ProductObject;
import com.foodamental.util.MyMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Activité main du démarrage
 */
public class MyMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();

        DatabaseManager.getInstance();

        UserDB dbuser = new UserDB();
        List<FoodUser> users = dbuser.getALLUser();
        if (users.size() == 0) {
            Intent intent = new Intent(this, MyWelcomePage.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_my_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //addContentView(textView1,this.getLayoutInflater());
        TextView textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();

        /*-----DB----*/
        ProductDB db = new ProductDB();
        FrigoDB frigo = new FrigoDB();
        int id = 1;
        db.addProduct(new ProductObject((long) 344344, "oignon", "carrefour", "brand", 1));
        db.addProduct(new ProductObject((long) 344345, "porc", "leader", "brand", 1));
        db.addProduct(new ProductObject((long) 344346, "oeufs", "carrefour", "brand", 1));
        db.addProduct(new ProductObject((long) 344347, "poulet", "carrefour", "brand", 1));
        db.addProduct(new ProductObject((long) 344348, "tomate", "leader", "brand", 1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        Date d3 = null;
        Date d4 = null;
        try {
            d1 = sdf.parse("2012-12-01");
            d2 = sdf.parse("2015-12-21");
            d3 = sdf.parse("2016-12-21");
            d4 = sdf.parse("2012-12-2");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        frigo.addProduct(new FrigoObject((long) id++, (long) 344344, d1));
        frigo.addProduct(new FrigoObject((long) id++, (long) 344346, d2));
        frigo.addProduct(new FrigoObject((long) id++, (long) 344347, d3));
        frigo.addProduct(new FrigoObject((long) id++, (long) 344348, d4));
        List<FrigoObject> list = frigo.getAllProductOrderBy("EXPIRY_DATE");

        /*-----------*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Fonction menu
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Fonction menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_main_page, menu);
        return true;
    }

    /**
     * Fonction menu
     * @param item
     * @return
     */
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

    /**
     * Fonction menu
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this, this, item);
    }

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

    public static Context getContext() {
        return context;
    }

}
