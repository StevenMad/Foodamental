package com.foodamental;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
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
        String url  = getIntent().getSerializableExtra("url").toString();
        sendRequest(url);
    }

    public void sendRequest(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView recipeResult = (TextView) findViewById(R.id.recipeResult);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jarray = (JSONArray) response.get("matches");
                            String s = "";
                            for(int i = 0;i<jarray.length();i++)
                            {
                                JSONObject obj = (JSONObject) jarray.get(i);
                                s+=obj.get("recipeName")+" : "+obj.get("sourceDisplayName")+"\n";
                            }
                            recipeResult.setText(s);
                        } catch (JSONException e) {
                            recipeResult.setText("error malformed JSON");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        queue.add(jsObjRequest);

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
