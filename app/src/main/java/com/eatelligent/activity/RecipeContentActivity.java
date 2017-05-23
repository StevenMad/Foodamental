package com.eatelligent.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eatelligent.R;
import com.eatelligent.util.JsonUtilTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public class RecipeContentActivity extends AppCompatActivity {

    String result;
    TextView tv;
    ListView lv;
    TabLayout tab;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);
        String idExtra = (String) getIntent().getSerializableExtra("id").toString();
        tv = (TextView) findViewById(R.id.recipeName);
        lv = (ListView) findViewById(R.id.recipeSteps);
        Integer id = Integer.valueOf(idExtra);

        //lien vers l'api
        //String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/analyzedInstructions";
        String url = "https://www.wecook.fr/web-api/recipes?id="+id;
        new RecipeContentAsyncTask().execute(url);
    }



    private class RecipeContentAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(RecipeContentActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait");
            this.dialog.show();
        }

        @Override
        /**
         * recuperation de la recette
         */
        protected String doInBackground(String... url) {
            try {
                JSONObject json = JsonUtilTools.getJSONFromRecipesRequest(url[0]);
                return json.toString();
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

        /**
         * afficher la recette apres doInBackground
         * @param result la recette obtenue
         */
        @Override
        protected void onPostExecute(String result)
        {
            try {
                if(dialog.isShowing())
                    dialog.dismiss();
                JSONObject json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("result"); //recuperation du json
                JSONArray steps = jsonArray.getJSONObject(0).getJSONArray("steps");
                List<String> stepStringList = new ArrayList<String>();
                for(int i=0;i<steps.length();i++)
                {
                    JSONObject js = steps.getJSONObject(i);
                    stepStringList.add("Etape " + js.getInt("order") + "\n" + js.getString("step"));
                }
                ArrayAdapter<String> obj = new ArrayAdapter<String>(RecipeContentActivity.this, android.R.layout.simple_list_item_1, stepStringList);
                //affichage du resultat dans une listView
                lv.setAdapter(obj);
                TextView tv = (TextView) findViewById(R.id.recipeName);
                tv.setText(jsonArray.getJSONObject(0).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * classe de traduction
     *//*
    private class RecipeTranslateAsyncTask extends AsyncTask<String, Void, List<String>>
    {
        private String idClient = "Foodamental01";
        private String secret = "jKyTcW44LeQYkjFZF1O4ci1VyiVFaRWUyR62YbLOQ74=";
        private ProgressDialog dialog = new ProgressDialog(RecipeContentActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait");
            this.dialog.show();
        }

        *//**
         * Traduire chaque mot de la recette
         * @param jsonArrayString
         * @return
         *//*
        @Override
        protected  List<String> doInBackground(String... jsonArrayString)
        {
            try {
                String traceId = UUID.randomUUID().toString();
                JSONArray stepList = new JSONArray(jsonArrayString);
                List<String> stepString = new ArrayList<>();
                int etape=1;
                for(int i=0;i<stepList.length();i++)
                {
                    String json = (String) stepList.get(i);
                    JSONArray jArray = new JSONArray(json);
                    for(int j=0;j<jArray.length();j++)
                    {
                        //preparation de la requete vers l'api
                        JSONObject jsonObj = (JSONObject) jArray.get(j);
                        String query = jsonObj.getString("step");
                        String urlString = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text=" + query + "&from=en&to=fr";
                        URL url = new URL(urlString);
                        //instanciation du token
                        AdmAccessToken token = AdmAccessToken.getAccessToken(idClient, secret);
                        //lancement de la connexion à l'api
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Authorization", "Bearer " + token.access_token);
                        conn.setRequestProperty("X-ClientTraceId", traceId);
                        if (conn.getResponseCode() == 200) {
                            String result = StaticUtil.getStringFromInputStream(conn.getInputStream());
                            String xml = result; //Populated XML String....

                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = null;

                            //recuperation de la traduction a partir de l'xml
                            builder = factory.newDocumentBuilder();
                            Document document = builder.parse(new InputSource(new StringReader(xml)));
                            Element rootElement = document.getDocumentElement();
                            String value = rootElement.getTextContent();
                            stepString.add("Etape " + etape + "\n" + value);
                            etape++;
                        }
                    }
                }
                return stepString;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }*/

        /**
         * action apres doInBackground
         * @param stepString la list des etapes de la recette
         */
        /*protected void onPostExecute(List<String> stepString)
        {
            //suppression du spinner

        }
    }
    */
}
