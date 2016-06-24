package com.foodamental;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by YOUSSEF on 17/06/2016.
 */
public class ProductActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        String url  = getIntent().getSerializableExtra("url").toString();
        sendRequest(url);




    }
    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            traitementJson(response);
                        } catch (IOException e) {
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
                dataText.setText("result " + "pas de produit");
            else {
                JSONObject produit = json.getJSONObject("product");
                String name = produit.getString("product_name");
                String brand = produit.getString("brands");
                String image = produit.getString("image_url");
                dataText.setText("result " + name + " " + brand);
                final ImageView imageView = (ImageView) findViewById(R.id.imageViewProduct);
                new DownloadImageTask(imageView).execute(image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
