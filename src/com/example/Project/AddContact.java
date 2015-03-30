package com.example.Project;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DAL.*;

public class AddContact extends ActionBarActivity {

	private EditText firstnameView;
	private EditText lastnameView;
	private EditText numberView;
	private TextView infoText;
	private CRUD datasource;
	private Main country;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_information);
		infoText = (TextView) findViewById(R.id.infoText);
		datasource = new CRUD(this);
		datasource.open();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_information, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	public void buttonOk(View view) {
		
		// handles add button click
		firstnameView = (EditText) findViewById(R.id.text_field_one);
		lastnameView = (EditText) findViewById(R.id.text_field_two);
		numberView = (EditText) findViewById(R.id.text_field_three);
		final boolean match1 = firstnameView.getText().toString()
				.matches("[a-öA-Ö_ ]+$");
		
		final boolean match2 = lastnameView.getText().toString()
				.matches("[a-öA-Ö_ ]+$");
		final boolean match3 = numberView.getText().toString()
				.matches("[0-9]+$");
		String firstnameViewString = firstnameView.getText().toString().trim();
		String lastnameViewString = lastnameView.getText().toString().trim();
		String numberViewString = numberView.getText().toString().trim();
		

				
		if (!match1) {
			Toast.makeText(getBaseContext(),
					"Firstname contains non text characters", Toast.LENGTH_LONG)
					.show();
		}
		if (!match2) {
			Toast.makeText(getBaseContext(),
					"Lastname contains non text characters", Toast.LENGTH_LONG)
					.show();
		}
		if(numberViewString.length() == 0){
			Toast.makeText(getBaseContext(),
					"Phone number must be longer than 8 numbers", Toast.LENGTH_LONG)
					.show();
		}
		
		if (match1) {
			if (match2) {
				if(match3){
				if(numberViewString.length() >= 8){
						if (!lastnameViewString.isEmpty() && !firstnameViewString.isEmpty()) {
							datasource.createTask(firstnameViewString, lastnameViewString, numberViewString);
							Intent intent = new Intent(this, Main.class);
							this.startActivityForResult(intent, 1);
							finish();
						}
					}else{
						Toast.makeText(getBaseContext(),
								"Phone number must be longer than 8 numbers", Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		}
	}
	}
				

