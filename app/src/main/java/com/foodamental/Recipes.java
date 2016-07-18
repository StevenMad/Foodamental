package com.foodamental;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Recipes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProductDB productDb;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FrigoDB fdb = new FrigoDB();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        EditText ingredientText = (EditText) findViewById(R.id.ingredientText);
        query = "";
        try {
            List<ProductDTO> listDTO = fdb.getDistinctProductList();
            for(ProductDTO product:listDTO)
            {
                query+=product.toString()+" ";
            }
        } catch (ParseException e) {
            Toast.makeText(Recipes.this,"Erreur lors de la récuperation des données",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        String[] listIngredient = query.split(" ");
        query="";
        for(String s:listIngredient)
        {
            query+=s+"%2C";
        }
        query = query.substring(0,query.length()-3);
        //String url = "http://api.yummly.com/v1/api/recipes?_app_id=80ae101e&_app_key=85289ec3509333e07e8112b54c053726"+query;
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillInIngredients=false&ingredients="+query;
        new RecipeAsyncTask().execute(url);
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
        getMenuInflater().inflate(R.menu.recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this,this,item);
    }

    private class RecipeAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            try {
                URL murl = new URL(url[0]);
                HttpURLConnection conn = (HttpURLConnection) murl.openConnection();
                conn.setRequestProperty("X-Mashape-Key","h5b30mrIKJmshDMNi7qH4pF8ux1Cp1CGYsHjsnJErhOlPsv15R");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept","application/json");
                if(conn.getResponseCode()==200)
                {
                    Intent intentProduct = new Intent(Recipes.this, RecipeActivity.class);
                    intentProduct.putExtra("response",StaticUtil.getStringFromInputStream(conn.getInputStream()));
                    startActivity(intentProduct);
                    return "reussi";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
