package com.eatelligent.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.eatelligent.activity.ProductActivity;
import com.eatelligent.R;
import com.eatelligent.activity.AlertPage;
import com.eatelligent.activity.Courses;
import com.eatelligent.activity.MyMainPage;

import com.eatelligent.activity.Recipes;

/**
 * Created by Madhow on 30/05/2016.
 */
public class MyMenu {

    public static boolean onNavigationItemSelected(Activity act, Context context, MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.nav_accueil:
            {
                Intent intent = new Intent(context,MyMainPage.class);
                context.startActivity(intent);
                break;
            }
            case R.id.nav_frigo:
            {
                Intent intent = new Intent(context,Courses.class);
                context.startActivity(intent);
                break;
            }
            case R.id.nav_recettes:
            {
                Intent intent = new Intent(context, Recipes.class);
                context.startActivity(intent);
                break;
            }
            case R.id.nav_param:
            {
                Intent intent = new Intent(context, AlertPage.class);
                context.startActivity(intent);
                break;
            }
            case R.id.nav_scan:
            {
                Intent intent = new Intent(context,ProductActivity.class);
                context.startActivity(intent);
                break;
            }
        }
        return true;
    }
}
