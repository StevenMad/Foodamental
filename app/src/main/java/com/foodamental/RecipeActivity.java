package com.foodamental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by Madhow on 14/07/2016.
 */
public class RecipeActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_layout);
        //String url  = getIntent().getSerializableExtra("url").toString();
        String response = getIntent().getSerializableExtra("response").toString();
        showHeader(response);
     //   sendRequest(url);
    }

    public void showHeader(String response)
    {
        try {
            JSONArray array = new JSONArray(response);
            JSONObject[] liste = new JSONObject[array.length()];
            int i=0;
            for(i = 0;i<array.length();i++)
            {
                liste[i] =(JSONObject) array.get(i);
            }
            final ListView lv =(ListView) findViewById(R.id.listRecipes);
            final ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(RecipeActivity.this,
                    android.R.layout.simple_list_item_1,
                    liste);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject content = (JSONObject) lv.getItemAtPosition(position);
                    //Toast.makeText(getApplicationContext(),content.toString(),Toast.LENGTH_LONG).show();

                    Intent intentRecipeContent = new Intent(RecipeActivity.this,RecipeContentActivity.class);
                    try {
                        intentRecipeContent.putExtra("id",content.get("id").toString());
                        startActivity(intentRecipeContent);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Content named Id not found",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void traitementJson(JSONObject json) throws IOException {
        final TextView dataText = (TextView) findViewById(R.id.dataText);
        try {


            String status = json.getString("status_verbose");
            if (status.equals("product not found"))
                dataText.setText("result " + "pas de recettes");
            else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
;