package com.foodamental.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.foodamental.HomeActivity;
import com.foodamental.R;
import com.foodamental.activity.AlertPage;
import com.foodamental.activity.Courses;
import com.foodamental.activity.Parametres;
import com.foodamental.activity.ProductActivity;
import com.foodamental.activity.Recipes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by dijib on 29/03/2017.
 */

public class BottomMenu {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showFridge(Activity activity, View view)
    {
        Intent intentProducts = new Intent(activity,Courses.class);
        ActivityOptions options = ActivityOptions.makeClipRevealAnimation(view, 0,100, view.getWidth(),view.getHeight());
        activity.startActivity(intentProducts,options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showRecipes(Activity activity, View view)
    {
        Intent intentRecipes = new Intent(activity,Recipes.class);
        ActivityOptions options = ActivityOptions.makeClipRevealAnimation(view, 0,100, view.getWidth(),view.getHeight());
        activity.startActivity(intentRecipes,options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void goToHomeScreen(Activity activity, View view)
    {
        Intent intentRecipes = new Intent(activity,HomeActivity.class);
        ActivityOptions options = ActivityOptions.makeClipRevealAnimation(view, 0,100, view.getWidth(),view.getHeight());
        intentRecipes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intentRecipes,options.toBundle());
    }

    public static void goToScan(Activity activity, View view)
    {
        openCamera(activity,view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void goToSettings(Activity activity, View view)
    {
        Intent intentSettings = new Intent(activity, AlertPage.class);
        ActivityOptions options = ActivityOptions.makeClipRevealAnimation(view, 0,100, view.getWidth(),view.getHeight());
        activity.startActivity(intentSettings,options.toBundle());
    }

    public static void openCamera(Activity activity, View view) {
        new IntentIntegrator(activity).initiateScan();
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
