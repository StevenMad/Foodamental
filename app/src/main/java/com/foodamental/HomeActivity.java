package com.foodamental;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.foodamental.util.JsonUtilTools;
import com.foodamental.util.RecipeItem;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

        //get the main_recipe
        new TodaysRecipeAsyncTask().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showFridge(View view)
    {
        BottomMenu.showFridge(this,view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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


    /*--- AsyncTasks ---*/
    private class TodaysRecipeAsyncTask extends AsyncTask<Void, Void, RecipeItem>
    {

        private String url = "https://www.wecook.fr/web-api/recipes?id=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //double rand = Math.random() * 8000;
            //url = url+(int) rand;
            url = url+4242;
        }

        @Override
        protected RecipeItem doInBackground(Void... params) {
            try{
                List<RecipeItem> liste = new ArrayList<>();
                JSONObject jsonUrlResponse = JsonUtilTools.getJSONFromRecipesRequest(url);
                if(jsonUrlResponse==null)
                    return null;
                //creation recipeItem
                JSONArray jsonArray = jsonUrlResponse.getJSONArray("result"); //recuperation du json
                JSONObject json = jsonArray.getJSONObject(0);
                RecipeItem item = new RecipeItem();
                if(json.has("picture_url"))
                {
                    URL imageUrl = new URL(json.getString("picture_url"));
                    Bitmap image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                    item.setImage(image);
                }else
                {
                    int id = getResources().getIdentifier("food_default","drawable",getPackageName());
                    Bitmap image = BitmapFactory.decodeResource(getResources(),id);
                    item.setImage(image);
                }
                item.setName(json.getString("name"));
                item.setId(json.getInt("id"));
                //ajout de l'element dans la liste
                return item;
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RecipeItem recipeItem) {
            ImageView image = (ImageView) findViewById(R.id.recipe_image);
            TextView tv = (TextView) findViewById(R.id.recipe_name);
            if(recipeItem.getName()==null)
                return;
            tv.setText(recipeItem.getName());
            image.setImageBitmap(recipeItem.getImage());
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

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
           // finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }



}
