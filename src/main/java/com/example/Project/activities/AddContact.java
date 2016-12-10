package com.example.Project.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.DAL.CRUD;
import com.example.Project.Main;
import com.example.Project.activities.activities_data.ManageContacts;
import com.example.Project.R;

import static com.example.Project.R.menu.add_information;

public class AddContact extends Activity {


    ManageContacts manageContacts;
    private Context context = this;
    private CRUD datasource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);

        datasource = new CRUD(this);
        datasource.open();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(add_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void buttonOk(View view) {

        manageContacts = new ManageContacts(context);
        final LinearLayout view_add_information = (LinearLayout) findViewById(R.id.activity_add_information);
        if(manageContacts.createOrUpdateContact(datasource, null, view_add_information)) {
            Intent intent = new Intent(this, Main.class);
            this.startActivityForResult(intent, 1);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        this.startActivityForResult(intent, 1);
        finish();
    }
}
