package com.foodamental;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodamental.activity.Courses;
import com.foodamental.activity.MyMainPage;
import com.foodamental.activity.Parametres;
import com.foodamental.activity.ProductActivity;
import com.foodamental.activity.RecipeContentActivity;
import com.foodamental.activity.Recipes;
import com.foodamental.dao.DatabaseManager;
import com.foodamental.util.BottomMenu;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeActivity extends AppCompatActivity {

    private final int MY_PERMISSION_STORAGE = 1;
    private final int MY_PERMISSION_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checkPermission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    MY_PERMISSION_CAMERA);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_STORAGE);
        }
        //get instance of the database
        DatabaseManager.getInstance();

        setContentView(R.layout.activity_home);
        LinearLayout view = (LinearLayout) findViewById(R.id.nav_view);
    }

    public void showFridge(View view)
    {
        BottomMenu.showFridge(this,view);
    }

    public void showRecipes(View view)
    {
        BottomMenu.showRecipes(this,view);
    }

    public void goToHomeScreen(View view)
    {

    }

    public void goToScan(View view)
    {
        BottomMenu.goToScan(this,view);
    }

    public void goToSettings(View view)
    {
        BottomMenu.goToSettings(this,view);
    }

    /**
     * Fonction résultat scan
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if ((scanningResult != null) || (!scanningResult.equals(""))) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();

// nous récupérons le format du code barre
            String scanFormat = scanningResult.getFormatName();

            sendRequest(this, scanContent);


// nous affichons le résultat dans nos TextView


        } else {
            //
        }

    }

    /**
     * Fonction qui envoie une nouvelle activity et le code barre
     * @param codeBar
     */
    public void sendRequest(Activity activity, String codeBar) {
        RequestQueue queue = Volley.newRequestQueue(activity);

        final TextView scan_content = (TextView) activity.findViewById(R.id.scan_content);
        if (codeBar != null) {
            Intent intentProduct = new Intent(activity, ProductActivity.class);
            intentProduct.putExtra("codebar", codeBar);
            activity.startActivity(intentProduct);
        }


    }



}
