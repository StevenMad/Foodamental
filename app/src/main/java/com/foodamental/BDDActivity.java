package com.foodamental;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class BDDActivity extends Activity {

	DBAdapter myDb;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db);

		openDB();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDB();
	}


	private void openDB() {
		myDb = new DBAdapter(this);
		myDb.open();
	}

	private void closeDB() {
		myDb.close();
	}


	private void displayText(String message) {
		TextView textView = (TextView) findViewById(R.id.textDisplay);
		textView.setText(message);
	}


	public void onClick_AddRecord(View v) {
		displayText("Clicked add record!");

		long newId = myDb.insertRow(111, "coca", 3);

		// Query for the record we just added.
		// Use the ID:
		Cursor cursor = myDb.getRow(newId);
		displayRecordSet(cursor);
	}

	public void onClick_ClearAll(View v) {
		displayText("Clicked clear all!");
		myDb.deleteAll();
	}

	public void onClick_DisplayRecords(View v) {
		displayText("Clicked display record!");

		Cursor cursor = myDb.getAllRows();
		displayRecordSet(cursor);
	}

	// Display an entire recordset to the screen.
	private void displayRecordSet(Cursor cursor) {
		String message = "";
		// populate the message from the cursor

		// Reset cursor to start, checking to see if there's data:
		if (cursor.moveToFirst()) do {
			// Process the data:
			int id = cursor.getInt(DBAdapter.COL_ROWID);
			String name = cursor.getString(DBAdapter.COL_NAME);
			int productname = cursor.getInt(DBAdapter.COL_PRODUCTNAME);
			String quantity = cursor.getString(DBAdapter.COL_QUANTITY);

			// Append data to the message:
			message += "id=" + id
					+ ", name=" + name
					+ ", #=" + productname
					+ ", Quantity=" + quantity
					+ "\n";
		} while (cursor.moveToNext());

		// Close the cursor to avoid a resource leak.
		cursor.close();

		displayText(message);
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"BDD Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.foodamental/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"BDD Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.foodamental/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}
}



