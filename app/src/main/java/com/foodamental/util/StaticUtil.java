package com.foodamental.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Steven on 03/05/2016.
 */
public class StaticUtil {
    public static JSONObject getJsonFromString(String s)
    {
        JSONObject json = null;
        try {
            json = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String getFileContent(FileInputStream filein)
    {
        InputStreamReader reader = new InputStreamReader(filein);
        char[] inputReadBuffer = new char[1024];
        String s = "";
        int charRead;
        try {
            while ((charRead=reader.read(inputReadBuffer))>0)
            {
                String readString = String.copyValueOf(inputReadBuffer,0,charRead);
                s+=readString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    // convert InputStream to String
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static Integer getCategory(String[] categories)
    {
        for(int i=0;i<categories.length;i++)
        {
            if(categories[i].substring(0,2).equals("Jus"))
                return 0;
            if(categories[i].equals("Pâtes alimentaires"))
                return 1;
            if(categories[i].equals("Huiles"))
                return 2;
            if(categories[i].equals("Laits"))
                return 3;
            if(categories[i].equals("Oeufs"))
                return 3;
            if(categories[i].equals("Viandes"))
                return 3;
            if(categories[i].substring(0,6).equals("Légumes"))
                return 4;
        }
        return -1;
    }
}
