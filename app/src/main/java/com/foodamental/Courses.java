package com.foodamental;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Courses extends AppCompatActivity {
    //Un adapteur pour gérer les données à afficher dans le spinner
    ArrayAdapter<String> adapter;
    //Liste de données à passer dans l'adapteur afin de les voir afficher dans le spinner
    ArrayList<String> data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
    }

    public void addProduit(View view)
    {

        EditText edittext = (EditText) findViewById(R.id.edit_text);
        String message = edittext.getText().toString();
        Spinner spinner = (Spinner)findViewById(R.id.spinnerProduit);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, data);
        spinner.setAdapter(adapter);
        if (message != null) {
            adapter.add(message);
        }
        Toast.makeText(Courses.this, message, Toast.LENGTH_LONG).show();
    }
}
