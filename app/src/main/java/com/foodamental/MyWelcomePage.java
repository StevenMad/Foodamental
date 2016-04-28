package com.foodamental;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyWelcomePage extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "com.foodamental.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking if the connexion.json is filled or not
        try {
            FileInputStream filein = openFileInput("connexion.json");
            InputStreamReader reader = new InputStreamReader(filein);
            char[] inputReadBuffer = new char[1024];
            String s = "";
            int charRead;

            while ((charRead = reader.read(inputReadBuffer)) > 0) {
                String readString = String.copyValueOf(inputReadBuffer, 0, charRead);
                s += readString;
            }
            reader.close();
            if(s=="")
            {
            }else
            {
                Intent intent = new Intent(this,MyMainPage.class);
                startActivity(intent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }

        setContentView(R.layout.activity_my_welcome_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_welcome_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view)
    {
        EditText prenomText;
        EditText emailText;
        EditText dateText;
        EditText passwordText;
        String prenom;
        String date;
        String email;
         String password;
        prenomText = (EditText) findViewById(R.id.prenomText);
        dateText = (EditText) findViewById(R.id.dateText);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        prenom = prenomText.getText().toString();
        date = dateText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        if((prenom=="") || (date == "") || (email=="") || ("" == password))
        {
            Toast.makeText(MyWelcomePage.this,"Un des champs n'a pas été rempli, Veuillez ré-essayer",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                FileOutputStream fileout = openFileOutput("connexion.json", MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(fileout);
                writer.write("{\"prenom\" :\"" + prenom + "\" , \"date de naissance\" :\"" + date + "\"}");
                writer.close();
                Toast.makeText(MyWelcomePage.this, "Ecriture complétée", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MyWelcomePage.this, "Fichier non trouvé", Toast.LENGTH_LONG).show();
                return;
            } catch (IOException e) {
                Toast.makeText(MyWelcomePage.this, "Ecriture non réussie", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return;
            }
            Intent intent = new Intent(this, MyMainPage.class);
            startActivity(intent);
        }
    }
}
