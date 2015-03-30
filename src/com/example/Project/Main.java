package com.example.Project;

import java.util.List;

import com.example.DAL.CRUD;
import com.example.DAL.GetSetters;


import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ActionBarActivity {

	private List<GetSetters> values;
	public final Context context = this;
	private CRUD datasource;
	String printCountry;
    String firstnameValidation;
    String lastnameValidation;
    String phonenumberValidation;
	ListView list;
	private ArrayAdapter<GetSetters> listAdapter;
	ListView listView;
	
	String textview; 

	private static final String PREFS_NAME = "MyPrefsFile";
	public int i;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listheader);
		
		 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		 i = settings.getInt("sortType", 0);
		 
		
		datasource = new CRUD(this);
		datasource.open();

		//Used to delete the database, cause there is no interface for the database so you are working blindly
//		context.deleteDatabase(DBConnection.DATABASE_NAME);
		values = datasource.getAllTasks();
		
//		TextView t = (TextView) findViewById(R.id.list);
//		for (int i = 0; i < values.size(); i++) {
//			textview = values.get(i).getFirstname();
//			t.append(textview +"\n");
//			System.out.println(values.get(i).getFirstname());
//		}
		
		if(i != 0){
			SortDatabase(i);
		}
		list = (ListView) findViewById(R.id.list);
		listAdapter = new ArrayAdapter<GetSetters>(context,
				android.R.layout.simple_list_item_1, values);

		list.setAdapter(listAdapter);
		registerForContextMenu(list);

	}


	// To update the list directly after update/add/edit
	 public void updateList() {
		 Intent intent = new Intent(this, Main.class);
	     this.startActivity(intent);
	     finish();
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(values.get(info.position).toString());
			menu.add(0, 0, 0, "Delete member");
			menu.add(1, 1, 1, "Update member");
			menu.add(2, 2, 2, "Call member");
			menu.add(3, 3, 3, "Send selected phonenumber");
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final GetSetters task = values.get(info.position);
		
		
		if (item.getItemId() == 0) {
			datasource.deleteTask(task);
			listAdapter.remove(task);
			updateList();
			listAdapter.notifyDataSetChanged();
			Toast.makeText(context, " Successfully deleted. ", Toast.LENGTH_LONG).show();
		}
		if (item.getItemId() == 1) {

			final LayoutInflater li = LayoutInflater.from(context);
			View promptsView = li.inflate(R.layout.prompts, null);

			final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setTitle("Update");
			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText firstname = (EditText) promptsView
					.findViewById(R.id.text_field_one);
			final EditText lastname = (EditText) promptsView
					.findViewById(R.id.text_field_two);
			final EditText phonenumber = (EditText) promptsView
					.findViewById(R.id.text_field_three);
			
			final View alertLayout = li.inflate(R.layout.alertlayout, null);

			alertLayout.findViewById(R.id.text_field_one);
			alertLayout.findViewById(R.id.text_field_two);
			alertLayout.findViewById(R.id.text_field_three);
			
			// set dialog message
			alertDialogBuilder

			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
				}
			}).setNegativeButton("Avbryt",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			// create alert dialog
			final AlertDialog alertDialog = alertDialogBuilder.create();
			//Make the keyboard popup at the start of the alertbuilder
			alertDialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			alertDialog.show();
			alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// Validation
							firstnameValidation = firstname.getText()
									.toString().trim();
							lastnameValidation = lastname.getText().toString()
									.trim();
							phonenumberValidation = phonenumber.getText().toString()
									.trim();
							final boolean match1 = firstname.getText().toString()
									.matches("[a-öA-Ö_ ]+$");
							final boolean match2 = lastname.getText().toString()
									.matches("[a-öA-Ö_ ]+$");
						//TODO
							final boolean match3 = phonenumber.getText().toString()
									.matches("[0-9]+$");

							if (!match1) {
								Toast.makeText(
										context,
										firstnameValidation
												+ " - Firstname can only contain characters",
										Toast.LENGTH_SHORT).show();
							}
							if (!match2) {
								Toast.makeText(
										context,
										lastnameValidation
												+ " - Lastname can only contain characters",
										Toast.LENGTH_SHORT).show();
							}
							if (!match3) {
								Toast.makeText(
										context,
										phonenumberValidation
												+ " - Phonenumber can only contain Numbers",
										Toast.LENGTH_SHORT).show();
							}
							if (match1) {
								if (match2) {
									if(match3){
										if(phonenumberValidation.length() >= 8){
										Toast.makeText(	context," Firstname updated to: "
												+ firstnameValidation
												+ "\n Lastname updated to: "
												+ lastnameValidation
												+ "\n Phone number updated to: "
												+ phonenumberValidation,Toast.LENGTH_LONG).show();
										datasource.updateTask(task, firstnameValidation, lastnameValidation, phonenumberValidation);
										updateList();
										listAdapter.notifyDataSetChanged();
										alertDialog.cancel();
										}else{
											Toast.makeText(getBaseContext(),
													"Phone number must be longer than 8 numbers", Toast.LENGTH_LONG)
													.show();
										}
									}									
								}
							}
						}
					});
		}
		
		if (item.getItemId() == 2) {
			
			String phoneNumber = task.getPhonenumber();
			
			
			PhoneCallListener phoneListener = new PhoneCallListener(context);
			
			TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);	
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:"+ phoneNumber));
			startActivity(callIntent);
			
		}
		if(item.getItemId() == 3) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, task.toString());
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
	}
		return true;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionbarmain, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.addNewMember) {
			Intent intent = new Intent(this, AddContact.class);
			this.startActivityForResult(intent, 1);
		}
			//Check which actionbarSelection that has been chosen
		if(id == R.id.sortFirstnameAsc || id == R.id.sortFirstnameDesc || id == R.id.sortLastnameAsc || id == R.id.sortLastnameDesc || id == R.id.settings){
		switch (id) {
		case R.id.sortFirstnameAsc:
			i=1;
			SortDatabase(i);
			break;
		case R.id.sortFirstnameDesc:
			i=2;
			SortDatabase(i);
			break;
		case R.id.sortLastnameAsc:
			i=3;
			SortDatabase(i);
			break;
		case R.id.sortLastnameDesc:
			i=4;
			SortDatabase(i);
			break;
		case R.id.settings:
			startActivity(new Intent(this, SimplePreferenceActivity.class));
			break;
		}
		//Saves preferences
		  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt("sortType", i);

	      // Commit the edits!
	      editor.commit();

		
		listAdapter = new ArrayAdapter<GetSetters>(this,
				android.R.layout.simple_list_item_1, values);
		list.setAdapter(listAdapter);
		registerForContextMenu(list);
		
	}
		return super.onOptionsItemSelected(item);
	}

	private void SortDatabase(int id) {
	//Sends the id to the corresponding sort function
		switch (id) {
		case 1:
			values = datasource.sortFirstAndLastname(1);
			break;
		case 2:
			values = datasource.sortFirstAndLastname(2);
			break;
		case 3:
			values = datasource.sortFirstAndLastname(3);
			break;
		case 4:
			values = datasource.sortFirstAndLastname(4);
			break;
		}
		
	}
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		TextView getText = (TextView) findViewById(R.id.textView1);
		TextView getText2 = (TextView) findViewById(R.id.textView2);
		TextView getText3 = (TextView) findViewById(R.id.textView3);
		View getSeparateLines = (View) findViewById(R.id.separate);
		
		
String getBg = prefs.getString("prefSetBackgroundColor", "0");
String getTextColor = prefs.getString("prefSetTextColor", "0");


	if(getBg.equals("1")){
		main.setBackgroundColor(Color.parseColor("#0BB5FF"));
	}
	else if(getBg.equals("2")){
		main.setBackgroundColor(Color.parseColor("#b20000"));	
	}
	else if(getBg.equals("3")){
		main.setBackgroundColor(Color.GREEN);
	}
	else if(getBg.equals("4")){
		main.setBackgroundColor(Color.WHITE);
	}
	if(getTextColor.equals("1")){
		getText.setTextColor(Color.BLUE);
		getText2.setTextColor(Color.BLUE);
		getText3.setTextColor(Color.BLUE);
		getSeparateLines.setBackgroundColor(Color.BLUE);
		
	}
	else if(getTextColor.equals("2")){
		getText.setTextColor(Color.RED);
		getText2.setTextColor(Color.RED);
		getText3.setTextColor(Color.RED);
		getSeparateLines.setBackgroundColor(Color.RED);

	}
	else if(getTextColor.equals("3")){
		getText.setTextColor(Color.GREEN);
		getText2.setTextColor(Color.GREEN);
		getText3.setTextColor(Color.GREEN);
		getSeparateLines.setBackgroundColor(Color.GREEN);
	}
	else if(getTextColor.equals("4")){
		getText.setTextColor(Color.BLACK);
		getText2.setTextColor(Color.BLACK);
		getText3.setTextColor(Color.BLACK);
		getSeparateLines.setBackgroundColor(Color.BLACK);
		
	}
	else if(getTextColor.equals("5")){
		getText.setTextColor(Color.WHITE);
		getText2.setTextColor(Color.WHITE);
		getText3.setTextColor(Color.WHITE);
		getSeparateLines.setBackgroundColor(Color.WHITE);
		
	}
        super.onWindowFocusChanged(hasFocus);
    }
}
