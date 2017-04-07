package com.foodamental.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.foodamental.R;
import com.foodamental.activity.AlertPage;
import com.foodamental.activity.Courses;
import com.foodamental.activity.MyMainPage;
import com.foodamental.activity.Recipes;

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
        }

        DrawerLayout drawer = (DrawerLayout) act.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
