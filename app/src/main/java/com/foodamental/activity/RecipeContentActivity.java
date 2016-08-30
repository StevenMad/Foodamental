package com.foodamental.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.foodamental.R;
import com.foodamental.translator.AdmAccessToken;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RecipeContentActivity extends AppCompatActivity {

    String result;
    TextView tv;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);
        String idExtra = (String) getIntent().getSerializableExtra("id").toString();
        tv = (TextView) findViewById(R.id.recipeName);
        lv = (ListView) findViewById(R.id.recipeSteps);
        Integer id = Integer.valueOf(idExtra);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/analyzedInstructions";
        new RecipeContentAsyncTask().execute(url);
    }


    private class RecipeContentAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                URL murl = new URL(url[0]);
                HttpURLConnection conn = (HttpURLConnection) murl.openConnection();
                conn.setRequestProperty("X-Mashape-Key", "h5b30mrIKJmshDMNi7qH4pF8ux1Cp1CGYsHjsnJErhOlPsv15R");
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                result = StaticUtil.getStringFromInputStream(conn.getInputStream());
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            try {
                JSONArray jsonArray = new JSONArray(result);
                List<String> listeStep = new ArrayList<String>();
                int step=1;
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject js = (JSONObject) jsonArray.get(i);
                    JSONArray stepList = (JSONArray) js.get("steps");
                    new RecipeTranslateAsyncTask().execute(stepList.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class RecipeTranslateAsyncTask extends AsyncTask<String, Void, List<String>>
    {
        private String idClient = "Foodamental01";
        private String secret = "jKyTcW44LeQYkjFZF1O4ci1VyiVFaRWUyR62YbLOQ74=";
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
                        JSONObject jsonObj = (JSONObject) jArray.get(j);
                        String query = jsonObj.getString("step");
                        String urlString = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text=" + query + "&from=en&to=fr";
                        URL url = new URL(urlString);
                        AdmAccessToken token = AdmAccessToken.getAccessToken(idClient, secret);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Authorization", "Bearer " + token.access_token);
                        conn.setRequestProperty("X-ClientTraceId", traceId);
                        if (conn.getResponseCode() == 200) {
                            String result = StaticUtil.getStringFromInputStream(conn.getInputStream());
                            String xml = result; //Populated XML String....

                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = null;

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
        }

        protected void onPostExecute(List<String> stepString)
        {
            ArrayAdapter<String> obj = new ArrayAdapter<String>(RecipeContentActivity.this,
                    android.R.layout.simple_list_item_1,
                    stepString);
            lv.setAdapter(obj);
        }
    }
}
