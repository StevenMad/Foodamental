package com.foodamental;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RecipeContentActivity extends AppCompatActivity {

    String result;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_content);
        String idExtra = (String) getIntent().getSerializableExtra("id").toString();
        tv = (TextView) findViewById(R.id.recipeContent);
        Integer id = Integer.valueOf(idExtra);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/analyzedInstructions";
        new RecipeContentAsyncTask().execute(url);
    }

    public void showResult()
    {
        TextView tv = (TextView) findViewById(R.id.recipeContent);
        tv.setText(result);
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
            Toast.makeText(RecipeContentActivity.this,result,Toast.LENGTH_LONG).show();
            tv.setText(result);
        }
    }
}
