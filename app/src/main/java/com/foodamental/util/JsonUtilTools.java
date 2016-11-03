package com.foodamental.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Madhow on 02/11/2016.
 */
public class JsonUtilTools {

    public static JSONObject getJSONFromRecipesRequest(String url) throws IOException, JSONException {
        URL requestURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
        //insertion de la cle
        conn.setRequestProperty("Authorization","Bearer 0VxU5I__nxIzBlJSVGATJQ");
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept","application/json");
        JSONObject jsonResult = new JSONObject();
        if(conn.getResponseCode()==200)
        {
            String response= StaticUtil.getStringFromInputStream(conn.getInputStream());
            jsonResult = new JSONObject(response);
        }
        return jsonResult;
    }
}
