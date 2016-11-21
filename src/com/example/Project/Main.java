package com.example.Project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.DAL.CRUD;
import com.example.DAL.GetSetters;

import java.util.List;

import static android.content.ContentValues.TAG;

/*
    Activities
    Todo: Strip down all files
    Todo: Name change on almost everything
    Todo: Check DB connection
    Todo: Look for more dry occurances
    Res
    Todo: Name change on most xml files
    Todo: Remove everything that are not being used
    Todo: Move all styling to styles folder

 */
public class Main extends Activity implements AppCompatCallback {

    private List<GetSetters> values;
    public final Context context = this;
    private CRUD datasource = null;
    private ListView list = null;
    private CustomListAdapter listAdapter = null;
    private ManageContacts manageContacts = null;
    private static final String PREFS_NAME = "MyPrefsFile";
    AppCompatDelegate delegate;

    // Populate Method

    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        initializeToolbar(savedInstanceState);
        setupContactList();
        createFootballField();
        manageContacts = new ManageContacts(context);
    }

    private void createFootballField() {
        ArrayAdapter<GetSetters> dataAdapter = new ArrayAdapter<GetSetters>(this,
                android.R.layout.simple_spinner_item, values);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerSelectPlayer);
//TODO Fixa

        spinner.setAdapter(dataAdapter);
    }

    private void setupContactList() {
        datasource = new CRUD(this);
        datasource.open();
        //Used to delete the database, cause there is no interface for the database so you are working blindly
//         context.deleteDatabase(DBConnection.DATABASE_NAME);
        list = (ListView) findViewById(R.id.list);

        values = initializePreferences();
        datasource.close();
        listAdapter = new CustomListAdapter(context, R.layout.list_items, values);
//        View header = (View)getLayoutInflater().inflate(R.layout.list_items, null);
//        list.addHeaderView(header);

        list.setAdapter(listAdapter);
        registerForContextMenu(list);
    }

    private void initializeToolbar(Bundle savedInstanceState) {
        //let's create the delegate, passing the activity at both arguments
        delegate = AppCompatDelegate.create(this, this);
        //the installViewFactory method replaces the default widgets
        //with the AppCompat-tinted versions
        delegate.installViewFactory();
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.main);
        //Finally, let's add the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);
    }

    public List<GetSetters> initializePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        int i = settings.getInt("sortType", 0);
        Log.d(TAG, "initializePreferences: " + datasource.sortFirstAndLastname(i));
        if(datasource.sortFirstAndLastname(i) == null) {
            values = datasource.getAllTasks();
        } else {
            values = datasource.sortFirstAndLastname(i);
        }
        return values;
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
            menu.setHeaderTitle(values.get(info.position).getPhonenumber());
//            (info.position).toString()
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
            ManageContacts manageContacts = new ManageContacts(context);
            datasource.open();
            manageContacts.removeContact(datasource, task, listAdapter);
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


            // TODO: 14/11/2016 refactor
            EditText firstnameField = (EditText) promptsView.findViewById(R.id.text_field_one);
            EditText lastnameField = (EditText) promptsView.findViewById(R.id.text_field_two);
            EditText phoneField = (EditText) promptsView.findViewById(R.id.text_field_three);
            firstnameField.setText(values.get(info.position).getFirstname());
            firstnameField.selectAll();
            lastnameField.setText(values.get(info.position).getLastname());
            phoneField.setText(values.get(info.position).getPhonenumber());

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
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D6DF7E")));
            //Make the keyboard popup at the start of the alertbuilder
            try {
                alertDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                datasource.open();
                                if(manageContacts.createOrUpdateContact(datasource, task, promptsView)) {
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
        // TODO: 14/11/2016 refactor
        //Check which actionbarSelection that has been chosen
        if(id == R.id.ASCfirstname || id == R.id.DESCfirstname || id == R.id.ASCLastname || id == R.id.DESCLastname) {
            sortDatabase(item);
            listAdapter = new CustomListAdapter(context, R.layout.list_items, values);
            list.setAdapter(listAdapter);
            registerForContextMenu(list);
        }
        if(id == R.id.settings)
            startActivity(new Intent(this, SimplePreferenceActivity.class));
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
        Log.d(TAG, "onWindowFocusChanged: " + getTextColor);
        if(!getBg.equals("0")) {
            main.setBackgroundColor(Color.parseColor("#" + getBg));

        }
        if(!getTextColor.equals("0")) {
            setColorOfBackground(Color.parseColor("#" + getTextColor));
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void setColorOfBackground(int color) {

//        View getSeparateLines = findViewById(R.id.separate);
//
//        //Get all layouts in Listheader.xml
//        LinearLayout myLayout = (LinearLayout) findViewById(R.id.listHeader_Layout);
//
//        for (int i = 0; i < myLayout.getChildCount(); i++) {
//            View v = myLayout.getChildAt(i);
//            if(v instanceof TextView) {
//                ((TextView) v).setTextColor(color);
//            }
//        }
//        getSeparateLines.setBackgroundColor(color);
    }

    public void addNew(View view) {
        Intent intent = new Intent(context, AddContact.class);
        this.startActivityForResult(intent, 1);
        finish();
    }

    public void footballFieldIMG(View view) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.animationfadein);
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.animationfadeout);
        final ImageView imageView = (ImageView) findViewById(R.id.footballField);
        if(imageView.getVisibility() == View.VISIBLE) {
            imageView.startAnimation(fadeOutAnimation);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(fadeInAnimation);
        }

    }

    public void selectPlayer(View view) {

    }

    //Custom toolbar
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }


}
