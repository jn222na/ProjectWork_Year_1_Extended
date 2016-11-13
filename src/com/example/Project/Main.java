package com.example.Project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.DAL.CRUD;
import com.example.DAL.GetSetters;

import java.util.ArrayList;
import java.util.List;

/*
    Manifest
    Todo: Needs update
    Activities
    Todo: Strip down all files
    Todo: Name change on almost everything
    Todo: Check DB connection
    Todo: Change colorpreference settings to Color palette instead of hardcoded colors
    Todo: Look for more dry occurances
    Res
    Todo: Name change on most xml files
    Todo: Remove everything that are not being used
    Todo: Move all styling to styles folder

 */
public class Main extends Activity {

    private List<GetSetters> values;
    public final Context context = this;
    private CRUD datasource = null;
    private ListView list = null;
    private ArrayAdapter<GetSetters> listAdapter = null;
    private ManageContacts addContact = null;
    private static final String PREFS_NAME = "MyPrefsFile";
    public int i;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listheader);

        datasource = new CRUD(this);
        datasource.open();
        //Used to delete the database, cause there is no interface for the database so you are working blindly
//        context.deleteDatabase(DBConnection.DATABASE_NAME);
        values = datasource.getAllTasks();
        initializePreferences();
        datasource.close();
        list = (ListView) findViewById(R.id.list);
        listAdapter = new ArrayAdapter<GetSetters>(context,
                android.R.layout.simple_list_item_1, values);

        list.setAdapter(listAdapter);
        registerForContextMenu(list);
        addContact = new ManageContacts();
    }

    public void initializePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        i = settings.getInt("sortType", 0);
        if(i != 0) {
            values = datasource.sortFirstAndLastname(i);
        }
    }

    // To update the list directly after update/add/edit
    public void updateList() {
        Intent intent = new Intent(this, Main.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(values.get(info.position).toString());
            menu.add(0, 0, 0, "Delete");
            menu.add(1, 1, 1, "Update");
            menu.add(2, 2, 2, "Call");
            menu.add(3, 3, 3, "Send phonenumber");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        final GetSetters task = values.get(info.position);

        //DELETE
        if(item.getItemId() == 0) {
            ManageContacts manageContacts = new ManageContacts();
            datasource.open();
            manageContacts.removeContact(datasource, task, listAdapter, context);
            datasource.close();
        }
        //UPDATE
        if(item.getItemId() == 1) {

            final LayoutInflater li = LayoutInflater.from(context);
            final View promptsView = li.inflate(R.layout.prompts, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            alertDialogBuilder.setTitle("Update");
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

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
            try {
                alertDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Validation
                                datasource.open();
                                if(addContact.createOrUpdateContact(datasource, task, promptsView, context)) {
                                    updateList();
                                    listAdapter.notifyDataSetChanged();
                                    alertDialog.cancel();
                                }
                                datasource.close();
                            }
                        });
            } catch (NullPointerException e) {
                Log.d("SoftInputMode Error", e.getMessage());
            }

        }

        if(item.getItemId() == 2) {
            String phoneNumber = task.getPhonenumber();
            PhoneCallListener phoneListener = new PhoneCallListener(context);

            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
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
        if(id == R.id.addNewMember) {
            Intent intent = new Intent(this, AddContact.class);
            this.startActivityForResult(intent, 1);
            finish();
        }

        //Check which actionbarSelection that has been chosen
        if(id == R.id.ASCfirstname || id == R.id.DESCfirstname || id == R.id.ASCLastname || id == R.id.DESCLastname || id == R.id.settings) {
            sortDatabase(item);
            if(id == R.id.settings)
                startActivity(new Intent(this, SimplePreferenceActivity.class));

            listAdapter = new ArrayAdapter<GetSetters>(this,
                    android.R.layout.simple_list_item_1, values);
            list.setAdapter(listAdapter);
            registerForContextMenu(list);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortDatabase(MenuItem id) {
        //Sends the id to the corresponding sort function
        datasource.open();
        values = datasource.sortFirstAndLastname(id.getItemId());
        datasource.close();

        //Saves preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sortType", id.getItemId());

        // Commit the edits!
        editor.apply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        LinearLayout main = (LinearLayout) findViewById(R.id.main);

        String getBg = prefs.getString("prefSetBackgroundColor", "0");
        String getTextColor = prefs.getString("prefSetTextColor", "0");

        main.setBackgroundColor(Color.parseColor("#" + getBg));
        setColorOfBackground(Color.parseColor("#" + getTextColor));

        super.onWindowFocusChanged(hasFocus);
    }

    public void setColorOfBackground(int color) {

        View getSeparateLines = findViewById(R.id.separate);

        //Get all layouts in Listheader.xml
        ArrayList<TextView> myEditTextList = new ArrayList<TextView>();
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.listHeader_Layout);
        for (int i = 0; i < myLayout.getChildCount(); i++) {
            if(myLayout.getChildAt(i) instanceof TextView) {
                myEditTextList.add((TextView) myLayout.getChildAt(i));
            }
        }

        for (int i = 0; i < myLayout.getChildCount(); i++) {
            View v = myLayout.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTextColor(color);
            }
        }
        getSeparateLines.setBackgroundColor(color);
    }
}
