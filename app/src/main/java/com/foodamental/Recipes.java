package com.foodamental;

import android.content.Intent;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Recipes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProductDB productDb;
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
        productDb = new ProductDB();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        EditText ingredientText = (EditText) findViewById(R.id.ingredientText);
        String s = "";
        List<ProductObject> list = productDb.getALLProduct();
        for(ProductObject p:list)
        {
            s+=p.toString()+" ";
        }
        ingredientText.setText(s);

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

    public void sendMessage(View view)
    {
        EditText ingredientText;
        String ingredient;
        ingredientText = (EditText) findViewById(R.id.ingredientText);
        ingredient = ingredientText.getText().toString();
        if((ingredient==""))
        {
            Toast.makeText(Recipes.this,"Un des champs n'a pas été rempli, Veuillez ré-essayer",Toast.LENGTH_LONG).show();
        }
        else
        {
            sendRequest(ingredient);
            Toast.makeText(Recipes.this,ingredient,Toast.LENGTH_LONG).show();
        }
    }

    public void sendRequest(String ingredient)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String[] listIngredient = ingredient.split(" ");
        String query = "";
        for(String s:listIngredient)
        {
            query+="&allowedIngredient[]="+s;
        }
        String url = "http://api.yummly.com/v1/api/recipes?_app_id=80ae101e&_app_key=85289ec3509333e07e8112b54c053726"+query;
        final TextView scan_content = (TextView) findViewById(R.id.scan_content);
        Intent intentProduct = new Intent(this, RecipeActivity.class);
        intentProduct.putExtra("url", url);
        startActivity(intentProduct);
    }

}
