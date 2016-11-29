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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.foodamental.R;
import com.foodamental.dao.dbimpl.CategoryDB;
import com.foodamental.dao.dbimpl.FrigoDB;
import com.foodamental.dao.model.FrigoObject;
import com.foodamental.dao.model.ProductObject;
import com.foodamental.util.DownloadImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private Date date;
    private Spinner s;
    private EditText edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);
        s = (Spinner) findViewById(R.id.spinner2);
        edit = (EditText) findViewById(R.id.editText2);
        CategoryDB dbcate = new CategoryDB();
        List<String> listcate = dbcate.getALLCategory();
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listcate);
        s.setAdapter(adapterSpinner);
        this.codeBar = getIntent().getSerializableExtra("codebar").toString();
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
     * @param url
     */
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

    /**
     * Fonction qui traite le json
     *
     * @param json
     * @throws IOException
     */
    public void traitementJson(JSONObject json) throws IOException {
        final TextView dataText = (TextView) findViewById(R.id.dataText);


        try {


            String status = json.getString("status_verbose");
            if (status.equals("product not found")) {
                dataText.setText("result " + "pas de produit");
                btn.setEnabled(false);

            } else {
                String image = "";
                String name= "";
                String genericName= "";
                String brand = "";
                JSONObject produit = json.getJSONObject("product");
                if (produit.has("product_name"))
                 name = produit.getString("product_name");
                if (produit.has("generic_name"))
                    genericName =  produit.getString("generic_name");
                if (produit.has("brands"))
                    brand = produit.getString("brands");
                if (produit.has("image_url"))
                    image = produit.getString("image_url");
                if (genericName.equals(""))
                    genericName = name;
                this.frigo = new FrigoObject(Long.valueOf(this.codeBar), genericName, brand, image, s.getSelectedItemPosition(), new Date(), Integer.parseInt(edit.getText().toString()));
                dataText.setText("result " + name + " " + brand + " " + genericName);
                ImageView imageView = (ImageView) findViewById(R.id.imageViewProduct);
                new DownloadImageTask(imageView).execute(image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            String quantityString = edit.getText().toString();
            FrigoDB dbfrigo = new FrigoDB();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date_peremp = format.parse((String) dateView.getText());
                this.frigo.setDatePerempt(date_peremp);
                this.frigo.setQuantity(Integer.parseInt(quantityString));
                this.frigo.setCategory(s.getSelectedItemPosition());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            dbfrigo.addProduct(this.frigo);

        }

        Intent intent = new Intent(ProductActivity.this, MyMainPage.class);
        startActivity(intent);


    }

    /**
     * test if integer
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
     * Fonction qui cache le clavier si appuie exterieur
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Fonction qui récupère le date du datePicker
     *
     * @param datePicker
     * @return
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


}
