package com.foodamental.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodamental.dao.DBHelper;
import com.foodamental.dao.UserDB;
import com.foodamental.model.FoodUser;
import com.foodamental.util.AlarmReceiver;
import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.FrigoDB;
import com.foodamental.model.FrigoObject;
import com.foodamental.util.MyMenu;
import com.foodamental.dao.ProductDB;
import com.foodamental.model.ProductObject;
import com.foodamental.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class MyMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);

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
        int id=1;
        db.addProduct(new ProductObject((long) 344344, "oignon", "carrefour","brand", 1));
        db.addProduct(new ProductObject((long) 344345, "porc", "leader","brand", 1));
        db.addProduct(new ProductObject((long) 344346, "oeufs", "carrefour","brand", 1));
        db.addProduct(new ProductObject((long) 344347, "poulet", "carrefour","brand", 1));
        db.addProduct(new ProductObject((long) 344348, "tomate", "leader","brand", 1));
        frigo.addProduct(new FrigoObject((long) id++,(long) 344344,new Date()));
        frigo.addProduct(new FrigoObject((long) id++,(long) 344346,new Date()));
        frigo.addProduct(new FrigoObject((long) id++,(long) 344347,new Date()));
        frigo.addProduct(new FrigoObject((long) id++,(long) 344348,new Date()));

        /*-----------*/

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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    //used for debug
    public void sendBarCode(View view)
    {
        String barcode = "5449000000996";
        sendRequest(barcode);
    }

    public void openCamera(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if ( (scanningResult != null)||(!scanningResult.equals(""))) {

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

        final TextView scan_content = (TextView) findViewById(R.id.scan_content);
        if (codeBar != null) {
            Intent intentProduct = new Intent(this, ProductActivity.class);
            intentProduct.putExtra("codebar", codeBar);
            startActivity(intentProduct);
        }



    }
    public static Context getContext(){
        return context;
    }

}
