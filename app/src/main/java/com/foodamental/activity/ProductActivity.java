package com.foodamental.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.foodamental.util.DownloadImageTask;
import com.foodamental.dao.FrigoDB;
import com.foodamental.model.FrigoObject;
import com.foodamental.dao.ProductDB;
import com.foodamental.model.ProductObject;
import com.foodamental.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by YOUSSEF on 17/06/2016.
 */
public class ProductActivity extends Activity implements View.OnClickListener {

    static final String URL = "http://fr.openfoodfacts.org/api/v0/produit/";

    private ProductObject product;
    private FrigoObject frigo;
    private String codeBar;
    static Button btn;
    static Button btn2;
    private TextView dateView;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        this.codeBar  = getIntent().getSerializableExtra("codebar").toString();
        String url = this.URL;
        url += codeBar + ".json";
        sendRequest(url);
        btn = (Button) findViewById(R.id.buttonAdd);
        btn2 = (Button) findViewById(R.id.buttonCancel);
        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);

        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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
            if (status.equals("product not found")) {
                dataText.setText("result " + "pas de produit");
                btn.setEnabled(false);

            }
            else {
                String image = "";
                JSONObject produit = json.getJSONObject("product");
                String name = produit.getString("product_name");
                String genericName = produit.getString("generic_name_fr");
                String brand = produit.getString("brands");
                if (produit.has("image_url"))
                image = produit.getString("image_url");
                this.product = new ProductObject(Long.valueOf(this.codeBar), genericName,brand, image, 1);
                this.frigo = new FrigoObject(Long.valueOf(this.codeBar),  new Date());
                dataText.setText("result " + name + " " + brand+" "+genericName);
                ImageView imageView = (ImageView) findViewById(R.id.imageViewProduct);
                new DownloadImageTask(imageView).execute(image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            ProductDB dbproduct = new ProductDB();
            FrigoDB dbfrigo = new FrigoDB();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date_peremp = format.parse((String) dateView.getText());
                this.frigo.setDatePerempt(date_peremp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dbproduct.addProduct(this.product);
            dbfrigo.addProduct(this.frigo);

        }

        Intent intent = new Intent(ProductActivity.this, MyMainPage.class);
        startActivity(intent);


    }







    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Choisis ta date", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private Date date;
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


}
