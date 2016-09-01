package com.foodamental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.foodamental.R;
import com.foodamental.dao.dbimpl.UserDB;
import com.foodamental.dao.model.FoodUser;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Activité qui gère le profil user et son affichage
 */
public class Profile extends AppCompatActivity {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        //récupération du json

        UserDB db = new UserDB();
        List<FoodUser> users = db.getALLUser();

            TextView prenomTextView = (TextView) findViewById(R.id.pnmText);
            TextView dtnTextView = (TextView) findViewById(R.id.dtnText);
            prenomTextView.setText("Prenom :  " + users.get(0).getUsername());
            dtnTextView.setText("Date de naissance : " + dateFormat.format(users.get(0).getBirthday()));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
