package com.foodamental.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foodamental.R;
import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.dbimpl.OtherFrigoProductDB;
import com.foodamental.dao.dbimpl.UserDB;
import com.foodamental.dao.model.FoodUser;
import com.foodamental.dao.model.FrigoObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activité de la page d'authentification et inscris le user dans la base
 */
public class MyWelcomePage extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private EditText prenomText;
    Date date = null;

    public static String EXTRA_MESSAGE = "com.foodamental.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking if the connexion.json is filled or not
        setContentView(R.layout.activity_my_welcome_page);
        prenomText = (EditText) findViewById(R.id.prenomText);
        prenomText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //db
        /*-----DB----*/
        OtherFrigoProductDB frigo = new OtherFrigoProductDB();
        int id = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        Date d3 = null;
        Date d4 = null;
        try {
            d1 = sdf.parse("2012-12-01");
            d2 = sdf.parse("2015-12-21");
            d3 = sdf.parse("2016-12-21");
            d4 = sdf.parse("2012-12-2");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        frigo.addOtherProduct(new FrigoObject("oignon et toto", d1,1,1));
        frigo.addOtherProduct(new FrigoObject("oeufs",d2, 1,  2));
        frigo.addOtherProduct(new FrigoObject("poulet", d3, 1,  3));
        frigo.addOtherProduct(new FrigoObject("tomate", d4,1,  4));
        List<FrigoObject> list = frigo.getAllOtherProductOrderBy("EXPIRY_DATE");
    }

    /**
     * Fonction qui remplit le texte au format date FR
     * @param year
     * @param month
     * @param day
     */
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * Fonction du menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_welcome_page, menu);
        return true;
    }

    /**
     * Fonction du menu
     * @param item
     * @return
     */
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


    /**
     * Fonction qui inscrit dans la base
     * @param view
     */
    public void sendMessage(View view)
    {
        EditText prenomText;
        EditText emailText;
        EditText dateText;
        EditText passwordText;
        String prenom;
        String email;
         String password;
        prenomText = (EditText) findViewById(R.id.prenomText);
        prenom = prenomText.getText().toString();
        if((prenom.equals("")))
        {
            Toast.makeText(MyWelcomePage.this,"Un des champs n'a pas été rempli, Veuillez ré-essayer",Toast.LENGTH_LONG).show();
        }
        else {
            UserDB user = new UserDB();
            user.addUser(new FoodUser(prenom,date));
            Intent intent = new Intent(this, MyMainPage.class);
            startActivity(intent);
        }
    }


    /**
     * Fonction qui initialise le datePicker et renvoit un message
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Choisis ta date", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Fonction qui initialise le datePicker
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    /**
     * Fonction qui récupère les données du datePicker et remplit le champ text
     */
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            int day = arg3;
            int month = arg2;
            int year = arg1;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            date = calendar.getTime();

            showDate(arg1, arg2+1, arg3);
        }
    };

    /**
     * Fonction qui cache le clavier en cas d'appuie extérieur
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Fonction qui récupère les données du datePicker
     * @param datePicker
     * @return
     */
    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}
