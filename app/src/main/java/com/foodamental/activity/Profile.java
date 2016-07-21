package com.foodamental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.foodamental.R;
import com.foodamental.util.StaticUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        //récupération du json
        try {
            FileInputStream file = openFileInput("connexion.json");
            String s = StaticUtil.getFileContent(file);
            JSONObject obj = StaticUtil.getJsonFromString(s);
            TextView prenomTextView = (TextView) findViewById(R.id.pnmText);
            TextView dtnTextView = (TextView) findViewById(R.id.dtnText);
            prenomTextView.setText("Prenom :  " + obj.getString("prenom"));
            dtnTextView.setText("Date de naissance : " + obj.getString("date de naissance"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
