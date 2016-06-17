package com.foodamental;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

//this is the class the show the content of the db
public class DBActivity extends AppCompatActivity {
    int start_id;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHelper(this);
        ArrayList<String> liste = db.displayUsers();
        Cursor rs = db.getData(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        rs.moveToFirst();
        TextView tv = (TextView) findViewById(R.id.dbText);
        //tv.setText(new Integer(rs.getColumnCount()).toString());
        String s = "";
        for(String user:liste)
        {
            s+="user : "+user+"\n";
        }
        tv.setText(s);
        //tv.setText(rs.getString(rs.getColumnIndex(db.FOODB_COLUMN_USERNAME)));
    }
}
