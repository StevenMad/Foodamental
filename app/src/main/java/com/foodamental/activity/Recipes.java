package com.foodamental.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.model.FrigoObject;
import com.foodamental.translator.AdmAccessToken;
import com.foodamental.util.MyMenu;
import com.foodamental.dao.dbimpl.ProductDB;
import com.foodamental.R;
import com.foodamental.util.RecipeItem;
import com.foodamental.util.RecipesArrayAdapter;
import com.foodamental.util.StaticUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Activité des recettes
 */
public class Recipes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnTaskComplete {

    ProductDB productDb;
    private String query;
    String ingredientsEng="";
    TextView tv;
    private TranslatorAsyncTask  translatorAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        translatorAsyncTask = new TranslatorAsyncTask();
        translatorAsyncTask.response = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tv = (TextView) findViewById(R.id.id_recipes);
        createQuery();
    }

    /**
     * Fonction qui traduit les produits du frigo
     */
    private void createQuery()
    {
        FrigoDB fdb = new FrigoDB();
        EditText ingredientText = (EditText) findViewById(R.id.ingredientText);
        query = "";
        try {
            List<FrigoObject> listDTO = fdb.getDistinctProductList();
            for(FrigoObject product:listDTO)
            {
                query+=product.toString()+" ";
            }
        } catch (ParseException e) {
            Toast.makeText(Recipes.this,"Erreur lors de la récuperation des données",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        String url = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text="+query+"&from=fr&to=en";
        translatorAsyncTask.execute(url);
        //query = query.substring(0,query.length()-3);
        //String url = "http://api.yummly.com/v1/api/recipes?_app_id=80ae101e&_app_key=85289ec3509333e07e8112b54c053726"+query;
    }

    /**
     * Fonction du menu
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Fonction du menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipes, menu);
        return true;
    }

    /**
     * Fonction du menu
     * @param item
     * @return
     */
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

    /**
     * Fonction du menu
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return MyMenu.onNavigationItemSelected(this,this,item);
    }

    /**
     *
     * @param output
     */
    @Override
    public void onTaskCompleted(String output) {
        String query = "";
        String[] ingredients = output.split(" ");
        for(int i=0;i<ingredients.length;i++)
            query+=ingredients[i]+"%2C";
        query.substring(0,query.length()-3);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=" + query;
        new RecipeAsyncTask().execute(url);
    }


    public class TranslatorAsyncTask extends AsyncTask<String, Void, String> {
        private String idClient = "Foodamental01";
        private String secret = "jKyTcW44LeQYkjFZF1O4ci1VyiVFaRWUyR62YbLOQ74=";
        private ProgressDialog dialog = new ProgressDialog(Recipes.this);
        public OnTaskComplete response = null;

        @Override
        protected void onPreExecute()
        {
            this.dialog.setMessage("Please Wait");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String traceId = UUID.randomUUID().toString();
            String formatedUrl = String.format(params[0],"bonjour");
            try {
                url = new URL(formatedUrl);
                AdmAccessToken token = AdmAccessToken.getAccessToken(idClient,secret);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + token.access_token);
                conn.setRequestProperty("X-ClientTraceId", traceId);
                if(conn.getResponseCode()==200)
                {
                    String result = StaticUtil.getStringFromInputStream(conn.getInputStream());
                    String xml = result; //Populated XML String....

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = null;

                    builder = factory.newDocumentBuilder();
                    Document document = builder.parse(new InputSource(new StringReader(xml)));
                    Element rootElement = document.getDocumentElement();
                    String value = rootElement.getTextContent();
                    return value;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return "hello";
        }

        protected void onPostExecute(String result)
        {
            if(dialog.isShowing())
                dialog.dismiss();
            response.onTaskCompleted(result);
        }
    }


    private class RecipeAsyncTask extends AsyncTask<String, Void, List<RecipeItem>> {

        @Override
        protected List<RecipeItem> doInBackground(String... url) {
            try {
                List<RecipeItem> liste = new ArrayList<>();
                URL murl = new URL(url[0]);
                HttpURLConnection conn = (HttpURLConnection) murl.openConnection();
                conn.setRequestProperty("X-Mashape-Key","h5b30mrIKJmshDMNi7qH4pF8ux1Cp1CGYsHjsnJErhOlPsv15R");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept","application/json");
                if(conn.getResponseCode()==200)
                {
                    String response= StaticUtil.getStringFromInputStream(conn.getInputStream());
                    //create recipeItem
                    JSONArray array = new JSONArray(response);
                    for(int i=0;i<array.length();i++)
                    {
                        RecipeItem item = new RecipeItem();
                        JSONObject json = new JSONObject((String) array.get(i).toString());
                        URL imageUrl = new URL(json.getString("image"));
                        Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                        item.setName(json.getString("title"));
                        item.setId(json.getInt("id"));
                        item.setImage(image);
                        item.setJson(json);
                        liste.add(item);
                    }
                    return liste;
                }else
                {
                    RecipeItem item = new RecipeItem();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(List<RecipeItem> result)
        {
            final ListView lv = (ListView) findViewById(R.id.listRecipes);
            RecipesArrayAdapter recipesArrayAdapter = new RecipesArrayAdapter(Recipes.this,
                    android.R.layout.simple_list_item_1,
                    result);
            lv.setAdapter(recipesArrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RecipeItem content = (RecipeItem) lv.getItemAtPosition(position);
                    //Toast.makeText(getApplicationContext(),content.toString(),Toast.LENGTH_LONG).show();

                    Intent intentRecipeContent = new Intent(Recipes.this,RecipeContentActivity.class);
                    intentRecipeContent.putExtra("id",content.getId());
                    startActivity(intentRecipeContent);
                }
            });
        }
    }


}
