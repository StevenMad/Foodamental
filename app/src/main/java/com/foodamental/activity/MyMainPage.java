package com.foodamental.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodamental.R;
import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.model.FoodUser;
import com.foodamental.util.JsonUtilTools;
import com.foodamental.util.MyMenu;
import com.foodamental.util.RecipeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Activité main du démarrage
 */
public class MyMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private final int MY_PERMISSION_STORAGE = 1;
    private final int MY_PERMISSION_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        //check permissions
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_CAMERA);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_STORAGE);
        }

        DatabaseManager.getInstance();

        setContentView(R.layout.activity_my_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //addContentView(textView1,this.getLayoutInflater());
        TextView textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();


        /*-----------*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add items on the menu
        new TodaysRecipeAsyncTask().execute();
    }

    //check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResults )
    {

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

    public void openRecipe(View view)
    {
        System.out.println("Hello World");
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
     * for database
     * @return the context
     */
    public static Context getContext() {
        return context;
    }

    /*--- AsyncTasks ---*/
    private class TodaysRecipeAsyncTask extends AsyncTask<Void, Void, RecipeItem>
    {

        private String url = "https://www.wecook.fr/web-api/recipes?id=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //double rand = Math.random() * 8000;
            //url = url+(int) rand;
            url = url+4242;
        }

        @Override
        protected RecipeItem doInBackground(Void... params) {
            try{
                List<RecipeItem> liste = new ArrayList<>();
                JSONObject jsonUrlResponse = JsonUtilTools.getJSONFromRecipesRequest(url);
                if(jsonUrlResponse==null)
                    return null;
                //creation recipeItem
                JSONArray jsonArray = jsonUrlResponse.getJSONArray("result"); //recuperation du json
                JSONObject json = jsonArray.getJSONObject(0);
                RecipeItem item = new RecipeItem();
                if(json.has("picture_url"))
                {
                    URL imageUrl = new URL(json.getString("picture_url"));
                    Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    item.setImage(image);
                }else
                {
                    int id = getResources().getIdentifier("food_default","drawable",getPackageName());
                    Bitmap image = BitmapFactory.decodeResource(getResources(),id);
                    item.setImage(image);
                }
                item.setName(json.getString("name"));
                item.setId(json.getInt("id"));
                //ajout de l'element dans la liste
                return item;
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RecipeItem recipeItem) {
            ImageView image = (ImageView) findViewById(R.id.recipe_image);
            TextView tv = (TextView) findViewById(R.id.recipe_name);
            tv.setText(recipeItem.getName());
            image.setImageBitmap(recipeItem.getImage());
        }
    }

    public void showRecipe(View view)
    {
        Intent intentRecipeContent = new Intent(MyMainPage.this,RecipeContentActivity.class);
        intentRecipeContent.putExtra("id",4242);
        startActivity(intentRecipeContent);
    }

}
