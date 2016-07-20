package com.foodamental;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject js = (JSONObject) jsonArray.get(i);
                    JSONArray stepList = (JSONArray) js.get("steps");
                    for(int j=0;j<stepList.length();j++)
                    {
                        JSONObject json = (JSONObject) stepList.get(j);
                        listeStep.add("Step "+step+"\n"+json.get("step").toString());
                        step++;
                    }
                }
                ArrayAdapter<String> obj = new ArrayAdapter<String>(RecipeContentActivity.this,
                        android.R.layout.simple_list_item_1,
                        listeStep);
                lv.setAdapter(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
