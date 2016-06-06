package com.foodamental;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Created by Madhow on 30/05/2016.
 */
public class MyMenu {

    public static boolean onNavigationItemSelected(Activity act, Context context, MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accueil) {
            Intent intent = new Intent(context,MyMainPage.class);
            context.startActivity(intent);
        }
        if (id == R.id.nav_frigo) {
            Intent intent = new Intent(context,Courses.class);
            context.startActivity(intent);
        }else if (id == R.id.nav_recettes) {
            Intent intent = new Intent(context,Recipes.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_profil) {
            Intent intent = new Intent(context,Profile.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_param) {
            Intent intent = new Intent(context,Parametres.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_db) {
            Intent intent = new Intent(context,DBActivity.class);
            context.startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) act.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
