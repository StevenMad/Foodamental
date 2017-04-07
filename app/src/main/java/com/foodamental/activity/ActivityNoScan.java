package com.foodamental.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foodamental.R;
import com.foodamental.dao.dbimpl.CategoryDB;
import com.foodamental.dao.dbimpl.OtherFrigoProductDB;
import com.foodamental.dao.model.FrigoObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by YOUSSEF on 01/12/2016.
 */

public class ActivityNoScan extends Activity implements View.OnClickListener {

    static Button btn;
    static Button btn2;
    private FrigoObject frigo;
    private TextView dateView;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private Spinner s;
    private EditText edit;
    private Date date;
    private EditText editName;
    /**
     * Fonction qui récupère la date
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

            showDate(arg1, arg2 + 1, arg3);
        }
    };

    /**
     * test if integer
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Fonction qui récupère le date du datePicker
     *
     * @param datePicker
     * @return
     */
    public static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_scan);
        s = (Spinner) findViewById(R.id.spinner2);
        edit = (EditText) findViewById(R.id.editText2);
        editName = (EditText) findViewById(R.id.editText);
        CategoryDB dbcate = new CategoryDB();
        List<String> listcate = dbcate.getALLCategory();
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listcate);
        s.setAdapter(adapterSpinner);
        btn = (Button) findViewById(R.id.buttonAdd);
        btn2 = (Button) findViewById(R.id.buttonCancel);
        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);

        dateView = (TextView) findViewById(R.id.textViewDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

    }

    /**
     * Fonction qui remplit le text view
     *
     * @param year
     * @param month
     * @param day
     */
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    /**
     * Fonction qui add dans bdd
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            //ProductDB dbproduct = new ProductDB();
            String quantityString = edit.getText().toString().trim();
            String name = editName.getText().toString().trim();
            if (quantityString.equals("") || name.equals("") || quantityString == null
                    || name == null) {
                Toast.makeText(getApplicationContext(), "Please fill in all the details ", Toast.LENGTH_SHORT).show();
                return;
            }
            this.frigo = new FrigoObject();
            OtherFrigoProductDB dbfrigo = new OtherFrigoProductDB();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date_peremp = format.parse((String) dateView.getText());
                this.frigo.setDatePerempt(date_peremp);
                this.frigo.setQuantity(Integer.parseInt(quantityString));
                this.frigo.setCategory(s.getSelectedItemPosition());
                this.frigo.setName(name);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dbfrigo.addOtherProduct(this.frigo);

        }

        //Intent intent = new Intent(ActivityNoScan.this, Courses.class);
        finish();
        //startActivity(intent);


    }

    /**
     * Fonction qui envoie un message
     *
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Choisis ta date", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Fonction qui retorune le datepicker
     *
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
     * Fonction qui cache le clavier si appuie exterieur
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
